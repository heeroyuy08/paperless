package com.cs.apac.hack.signingorder.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "sign_orders")
public class SignOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty
    private String status;

    @Column(name = "started_ts")
    @NotEmpty
    private LocalDateTime startedTime;

    @Column(name = "started_by")
    @NotEmpty
    private String startedBy;

    @Column(name = "doc_id")
    @NotEmpty
    private String docId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "signOrder")
    private Set<Signer> signers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "signOrder")
    private List<SignEvent> signEvents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(LocalDateTime startedTime) {
        this.startedTime = startedTime;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Set<Signer> getSigners() {
        if (signers == null) {
            signers = Sets.newHashSet();
        }
        return signers;
    }

    public void setSigners(Set<Signer> signers) {
        this.signers = signers;
    }

    public List<SignEvent> getSignEvents() {
        if (signEvents == null) {
            signEvents = Collections.emptyList();
        }
        return signEvents;
    }

    public void setSignEvents(List<SignEvent> signEvents) {
        this.signEvents = signEvents;
    }

    public void addSignEvent(SignEvent signEvent) {
        getSignEvents().add(signEvent);
        signEvent.setSignOrder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("status", status)
            .add("startedBy", startedBy)
            .toString();
    }
}
