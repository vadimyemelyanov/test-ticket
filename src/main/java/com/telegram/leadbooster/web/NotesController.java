package com.telegram.leadbooster.web;

import com.telegram.leadbooster.dto.AddNoteRequest;
import com.telegram.leadbooster.service.NotesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notes")
@Slf4j
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping
    public void addNote(AddNoteRequest addNoteRequest) {
        log.info("[API] add note [{}]", addNoteRequest);
        notesService.addNoteToChat(addNoteRequest);
    }
}
