package com.telegram.leadbooster.repository;

import com.telegram.leadbooster.domain.StateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateHistoryRepository extends JpaRepository<StateHistory, String> {
}