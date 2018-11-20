package com.cs.apac.hack.signingorder;

import com.cs.apac.hack.signingorder.model.SignEvent;
import com.cs.apac.hack.signingorder.model.SignOrder;
import com.cs.apac.hack.signingorder.service.SignOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class SignOrderController {

    private static final Logger LOG = LoggerFactory.getLogger(SignOrderController.class);

    @Autowired
    private SignOrderService signOrderService;

    @PostMapping
    public Long createSignOrder(@Valid @RequestBody SignOrder signOrder) {
        long signOrderId = signOrderService.saveSignOrder(signOrder);
        LOG.info("sign order created {}", signOrder);
        return signOrderId;
    }

    @PostMapping("/signOrder/{id}/sign")
    public SignEvent signBySigner(@PathVariable("id") long signOrderId,
                                  @NotNull @RequestPart("signature") MultipartFile signature,
                                  @Valid @RequestPart("event") SignEventRequest signEventRequest) throws IOException {
        SignEvent signEvent = new SignEvent();
        signEvent.setSignedDocId(signEventRequest.getDocId());
        signEvent.setSignedTimestamp(LocalDateTime.now());
        signEvent.setSigner(signEventRequest.getSigner());

        return signOrderService.signSignOrder(signEvent, signOrderId, signature.getBytes());
    }

    @PostMapping("/signOrder/{id}/start")
    public SignOrder startSignOrder(@Positive @PathVariable("id") long signOrderId) {
        return signOrderService.startSignOrder(signOrderId);
    }

    @DeleteMapping("/{id}")
    public SignOrder cancelSignOrder(@Positive @PathVariable("id") long signOrderId) {
        return signOrderService.cancelSignOrder(signOrderId);
    }

    @GetMapping("/signOrders")
    public Collection<SignOrder> getSignOrdersByUser(@NotEmpty @RequestParam("startedBy") String userId) {
        return signOrderService.findSignOrderByStartedBy(userId);
    }

    @GetMapping(path = "/signOrders/signer/{userId}")
    public Collection<SignOrder> getPendingSignOrderByUser(@PathVariable("userId") String signer, @RequestParam("status") String status) {
        return signOrderService.findSignOrderBySignerAndStatus(signer, status);
    }

    @GetMapping(path = "/{id}")
    public SignOrder getSignOrder(@Positive @PathVariable(name = "id") long signOrderId) {
        return signOrderService.findSignOrderById(signOrderId);
    }

    static final class SignEventRequest {
        @NotEmpty
        private String signer;

        @NotEmpty
        private String docId;

        public String getSigner() {
            return signer;
        }

        public void setSigner(String signer) {
            this.signer = signer;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }
    }
}
