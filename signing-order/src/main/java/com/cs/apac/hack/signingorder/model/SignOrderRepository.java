package com.cs.apac.hack.signingorder.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SignOrderRepository extends JpaRepository<SignOrder, Long> {

    @Query("FROM SignOrder signOrder WHERE signOrder.status = :status")
    List<SignOrder> findSignOrderByStatus(@Param("status") String status);

    @Query("FROM SignOrder signOrder WHERE signOrder.startedBy = :startedBy")
    List<SignOrder> findSignOrderByStartedBy(@Param("startedBy") String startedBy);

}
