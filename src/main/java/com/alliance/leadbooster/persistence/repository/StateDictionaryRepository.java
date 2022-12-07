package com.alliance.leadbooster.persistence.repository;

import com.alliance.leadbooster.persistence.entity.StateDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateDictionaryRepository extends JpaRepository<StateDictionary, String> {
}