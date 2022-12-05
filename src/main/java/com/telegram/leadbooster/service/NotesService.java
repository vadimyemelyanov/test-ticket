package com.telegram.leadbooster.service;

import com.telegram.leadbooster.domain.Notes;
import com.telegram.leadbooster.dto.AddNoteRequest;
import com.telegram.leadbooster.repository.NotesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotesService {
    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

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
