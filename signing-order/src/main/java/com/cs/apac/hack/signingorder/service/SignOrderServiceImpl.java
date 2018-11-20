package com.cs.apac.hack.signingorder.service;

import com.cs.apac.hack.signingorder.model.SignEvent;
import com.cs.apac.hack.signingorder.model.SignOrder;
import com.cs.apac.hack.signingorder.model.SignOrderRepository;
import com.cs.apac.hack.signingorder.model.SignOrderStatus;
import com.cs.apac.hack.signingorder.model.Signer;
import com.cs.apac.hack.signingorder.service.DocumentService.Document;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SignOrderServiceImpl implements SignOrderService {

    private final SignOrderRepository signOrderRepository;
    private final DocumentSigner documentSigner;
    private final DocumentService documentService;

    @Autowired
    public SignOrderServiceImpl(SignOrderRepository signOrderRepository, DocumentSigner documentSigner, DocumentService documentService) {
        this.signOrderRepository = signOrderRepository;
        this.documentSigner = documentSigner;
        this.documentService = documentService;
    }

    @Override
    @Transactional
    public Long saveSignOrder(SignOrder signOrder) throws DataAccessException {
        return signOrderRepository.save(signOrder).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<SignOrder> findSignOrderByStatus(String status) throws DataAccessException {
        return signOrderRepository.findSignOrderByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<SignOrder> findSignOrderByStartedBy(String startedBy) throws DataAccessException {
        return signOrderRepository.findSignOrderByStartedBy(startedBy);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<SignOrder> findSignOrderBySignerAndStatus(String signer, String status) throws DataAccessException {
        return Optional.ofNullable(signOrderRepository.findSignOrderByStatus(status))
            .orElseGet(Lists::newArrayList)
            .stream()
            .filter(signOrder -> signOrder.getSigners().stream().map(Signer::getName).anyMatch(s -> s.equalsIgnoreCase(signer)) &&
                signOrder.getSignEvents().stream().map(SignEvent::getSigner).noneMatch(s -> s.equalsIgnoreCase(signer)))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SignOrder findSignOrderById(long id) throws DataAccessException {
        return signOrderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("sign order not found"));
    }

    @Override
    @Transactional
    public SignOrder startSignOrder(long id) throws DataAccessException {
        SignOrder signOrder = findSignOrderById(id);
        signOrder.setStatus(SignOrderStatus.STARTED.toString());
        saveSignOrder(signOrder);
        return signOrder;
    }

    @Override
    @Transactional
    public SignOrder cancelSignOrder(long id) throws DataAccessException {
        SignOrder signOrder = findSignOrderById(id);
        signOrder.setStatus(SignOrderStatus.CANCELLED.toString());
        saveSignOrder(signOrder);
        return signOrder;
    }

    @Override
    @Transactional
    public SignEvent signSignOrder(SignEvent signEvent, long signOrderId, byte[] signature) throws DataAccessException {
        SignOrder signOrder = findSignOrderById(signOrderId);
        Document document = findLatestDocumentToSign(signOrder);
        Document signedDoc = documentSigner.sign(signature, signEvent.getSigner(), document.getDoc());
        signEvent.setSignedDocId(documentService.storeDocument(signedDoc.getDoc()));
        signOrder.addSignEvent(signEvent);

        List<String> signers = signOrder.getSigners().stream().map(Signer::getName).collect(Collectors.toList());
        boolean hasPendingSigners = signers.stream().allMatch(signer -> signOrder.getSignEvents().stream()
            .map(SignEvent::getSigner)
            .noneMatch(s -> s.equalsIgnoreCase(signer)));
        if (!hasPendingSigners) {
            signOrder.setStatus(SignOrderStatus.COMPLETED.toString());
        }

        saveSignOrder(signOrder);
        return signEvent;
    }


    private Document findLatestDocumentToSign(SignOrder signOrder) {
        String latestDocId = signOrder.getDocId();
        List<SignEvent> sortedSignEvents = Lists.newArrayList(signOrder.getSignEvents());
        PropertyComparator.sort(sortedSignEvents, new MutableSortDefinition("signedTimestamp", false, false));

        if (!sortedSignEvents.isEmpty()) {
            latestDocId = sortedSignEvents.get(0).getSignedDocId();
        }

        return documentService.getDocument(latestDocId);
    }
}
