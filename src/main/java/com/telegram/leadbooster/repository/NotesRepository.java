package com.telegram.leadbooster.repository;

import com.telegram.leadbooster.domain.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes, String> {

}