package com.alliance.leadbooster.persistence.repository;

import com.alliance.leadbooster.persistence.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes, String> {

}