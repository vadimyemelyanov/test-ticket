package com.telegram.leadbooster.service;

import com.telegram.leadbooster.domain.Notes;
import com.telegram.leadbooster.dto.AddNoteRequest;
import com.telegram.leadbooster.repository.NotesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotesService {
    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Transactional
    public void addNoteToChat(AddNoteRequest addNoteRequest) {
        notesRepository.save(Notes.builder()
            .text(addNoteRequest.getText())
            .dealUuid(addNoteRequest.getChatUuid().toString())
            .build());
    }
}
