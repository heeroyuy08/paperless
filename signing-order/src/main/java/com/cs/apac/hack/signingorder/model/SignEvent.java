package com.cs.apac.hack.signingorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "sign_events")
public class SignEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String signer;

    private LocalDateTime signedTimestamp;

    @Column(name = "doc_id")
    private String signedDocId;

    @ManyToOne
    @JoinColumn(name = "sign_order_id")
    @JsonIgnore
    private SignOrder signOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public LocalDateTime getSignedTimestamp() {
        return signedTimestamp;
    }

    public void setSignedTimestamp(LocalDateTime signedTimestamp) {
        this.signedTimestamp = signedTimestamp;
    }

    public String getSignedDocId() {
        return signedDocId;
    }

    public void setSignedDocId(String signedDocId) {
        this.signedDocId = signedDocId;
    }

    public SignOrder getSignOrder() {
        return signOrder;
    }

    public void setSignOrder(SignOrder signOrder) {
        this.signOrder = signOrder;
    }
}
