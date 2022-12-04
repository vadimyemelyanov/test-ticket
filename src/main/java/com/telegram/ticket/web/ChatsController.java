package com.telegram.ticket.web;

import com.telegram.ticket.domain.Chat;
import com.telegram.ticket.dto.MoveTicketRequest;
import com.telegram.ticket.dto.UpdateChatRequest;
import com.telegram.ticket.service.ChatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@Slf4j
public class ChatsController {
    private final ChatsService chatsService;

    public ChatsController(ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    @GetMapping
    public List<Chat> getAllChats(@RequestParam(required = false) String product) {
        log.info("[API] get all chats request");
        return chatsService.getAllChats(product);
    }

    @PutMapping
    public void updateChat(@RequestBody UpdateChatRequest request) {
        log.info("[API] update chat [{}]", request);
        chatsService.updateTicket(request);
    }

    @PatchMapping("/{uuid}/move")
    public void moveTicket(@PathVariable String uuid,
                           @RequestBody MoveTicketRequest moveTicketRequest) {
        log.info("[API] move ticket [{}] to  [{}]", uuid, moveTicketRequest);
        chatsService.moveTicketToState(uuid, moveTicketRequest.getTargetState());
    }

}
