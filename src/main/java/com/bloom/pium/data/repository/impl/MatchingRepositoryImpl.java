package com.bloom.pium.data.repository.impl;

import com.bloom.pium.data.entity.Matching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class MatchingRepositoryImpl {
    private final EntityManager entityManager;

    @Autowired
    public MatchingRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void toggleParticipation(Long matchingId, boolean participate) {
        Matching matching = entityManager.find(Matching.class, matchingId);
        if (matching != null) {
            matching.setParticipate(participate);
            entityManager.merge(matching);
        }
    }
}