package com.alliance.leadbooster.service;

import com.alliance.leadbooster.model.AddNoteRequest;
import com.alliance.leadbooster.persistence.entity.Notes;
import com.alliance.leadbooster.persistence.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotesService {

    private final NotesRepository notesRepository;


    @Transactional
    public Notes addNoteToChat(AddNoteRequest addNoteRequest) {
        return notesRepository.save(Notes.builder()
            .uuid(UUID.randomUUID().toString())
            .text(addNoteRequest.getContent())
            .dealUuid(addNoteRequest.getDealUuid())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
    }
}
