package com.cs.apac.hack.signingorder.service;

import com.cs.apac.hack.signingorder.model.SignEvent;
import com.cs.apac.hack.signingorder.model.SignOrder;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

public interface SignOrderService {

    Long saveSignOrder(SignOrder signOrder) throws DataAccessException;

    Collection<SignOrder> findSignOrderByStatus(String status) throws DataAccessException;

    Collection<SignOrder> findSignOrderByStartedBy(String startedBy) throws DataAccessException;

    Collection<SignOrder> findSignOrderBySignerAndStatus(String signer, String status) throws DataAccessException;

    SignOrder findSignOrderById(long id) throws DataAccessException;

    SignOrder startSignOrder(long id) throws DataAccessException;

    SignOrder cancelSignOrder(long id) throws DataAccessException;

    SignEvent signSignOrder(SignEvent signEvent, long signOrderId, byte[] signature) throws DataAccessException;
}
