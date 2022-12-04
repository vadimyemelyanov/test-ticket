package com.telegram.ticket.service;

import com.telegram.ticket.domain.Notes;
import com.telegram.ticket.dto.AddNoteRequest;
import com.telegram.ticket.repository.NotesRepository;
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
            .chatUuid(addNoteRequest.getChatUuid().toString())
            .build());
    }
}
