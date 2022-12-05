package com.alliance.leadbooster.web;

import com.alliance.leadbooster.model.AddNoteRequest;
import com.alliance.leadbooster.persistence.entity.Notes;
import com.alliance.leadbooster.service.NotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NotesController {

    private final NotesService notesService;


    @PostMapping
    public Notes addNote(@RequestBody AddNoteRequest addNoteRequest) {
        log.info("[API] add note [{}]", addNoteRequest);
        return notesService.addNoteToChat(addNoteRequest);
    }
}
