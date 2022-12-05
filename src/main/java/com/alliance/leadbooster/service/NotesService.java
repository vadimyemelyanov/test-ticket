package com.alliance.leadbooster.service;

import com.alliance.leadbooster.model.AddNoteRequest;
import com.alliance.leadbooster.persistence.entity.Notes;
import com.alliance.leadbooster.persistence.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotesService {

    private final NotesRepository notesRepository;


    @Transactional
    public void addNoteToChat(AddNoteRequest addNoteRequest) {
        notesRepository.save(Notes.builder()
            .text(addNoteRequest.getText())
            .dealUuid(addNoteRequest.getChatUuid().toString())
            .build());
    }
}
