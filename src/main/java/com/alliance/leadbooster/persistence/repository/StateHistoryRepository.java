package com.alliance.leadbooster.persistence.repository;

import com.alliance.leadbooster.persistence.entity.StateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateHistoryRepository extends JpaRepository<StateHistory, String> {
}