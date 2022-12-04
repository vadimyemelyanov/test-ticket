package com.telegram.ticket.service;

import com.pengrad.telegrambot.model.Message;
import com.telegram.ticket.domain.StateHistory;
import com.telegram.ticket.domain.Chat;
import com.telegram.ticket.dto.TicketState;
import com.telegram.ticket.dto.UpdateChatRequest;
import com.telegram.ticket.exceptions.EntityNotFoundException;
import com.telegram.ticket.repository.StateHistoryRepository;
import com.telegram.ticket.repository.ChatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ChatsService {
    private final ChatsRepository chatsRepository;
    private final StateHistoryRepository stateHistoryRepository;

    public ChatsService(ChatsRepository chatsRepository, StateHistoryRepository stateHistoryRepository) {
        this.chatsRepository = chatsRepository;
        this.stateHistoryRepository = stateHistoryRepository;
    }


    @Transactional(readOnly = true)
    public List<Chat> getAllChats(String product) {
        return getTickets(product);
    }

    @Transactional
    public void moveTicketToState(String uuid, TicketState targetState) {
        Chat ticket = chatsRepository.findById(uuid)
            .orElseThrow(EntityNotFoundException::new);

        stateHistoryRepository.save(StateHistory.builder()
            .state(ticket.getCurrentState())
            .chatUuid(uuid)
            .fromDate(ticket.getUpdatedAt())
            .toDate(LocalDateTime.now())
            .uuid(UUID.randomUUID().toString())
            .build());

        ticket.setCurrentState(targetState);
        ticket.setUpdatedAt(LocalDateTime.now());
        chatsRepository.save(ticket);
    }

    @Transactional
    public void updateTicket(UpdateChatRequest request) {
        Chat ticket = chatsRepository.findById(request.getTicketUuid().toString())
            .orElseThrow(EntityNotFoundException::new);

        if (request.getName() != null) {
            ticket.setName(request.getName());
        }
        if (request.getProduct() != null) {
            ticket.setProduct(request.getProduct());
        }

        chatsRepository.save(ticket);
    }

    @Transactional
    public void createTicket(Message message, String telegramLink, String username) {
        String messageText = extractLastMessageText(message);
        Chat ticket = Chat.builder()
            .uuid(UUID.randomUUID().toString())
            .name(message.chat().title())
            .authorUsername(username)
            .telegramChatId(message.chat().id())
            .telegramLink(telegramLink)
            .currentState(TicketState.QUALIFICATION)
            .lastMessage(messageText)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .lastMessageReceivedAt(LocalDateTime.now())
            .build();
        chatsRepository.save(ticket);
        log.info("Ticket saved [{}]", ticket.getUuid());
    }

    @Transactional
    public void updateTicketWithMessage(Chat ticket, Message message) {
        ticket.setLastMessageReceivedAt(LocalDateTime.now());
        ticket.setLastMessage(extractLastMessageText(message));
        log.info("Ticket updated [{}]", ticket.getUuid());
    }

    private static String extractLastMessageText(Message message) {
        var telegramLastMessageText = new StringBuilder();
        if (message.text() == null) {
            if (message.audio() != null) {
                telegramLastMessageText.append("Audio added by: ")
                    .append(message.from().username()).append("\n");
            }
            if (message.document() != null) {
                telegramLastMessageText
                    .append(message.document().mimeType())
                    .append(" Document added by: ")
                    .append(message.from().username()).append("\n");

            }
            if (message.video() != null) {
                telegramLastMessageText
                    .append("Video added by: ")
                    .append(message.from().username()).append("\n");
            }
            if (message.photo() != null) {
                telegramLastMessageText
                    .append("Photo added by: ")
                    .append(message.from().username()).append("\n");
            }
            if (message.caption() != null) {
                telegramLastMessageText.append("With Caption:")
                    .append(message.caption())
                    .append("\n");
            }
        } else {
            telegramLastMessageText
                .append("Message: ")
                .append(message.text())
                .append(" sent by ")
                .append(message.from().username()).append("\n")
                .append("\n");
        }
        return telegramLastMessageText.toString();
    }

    @Transactional(readOnly = true)
    public List<Chat> getTicketsByChatId(Long chatId) {
        return chatsRepository.findAllByChatIdAndCurrentStateNotFinal(chatId);
    }


    private List<Chat> getTickets(String product) {
        if (product != null) {
            return chatsRepository.findAllByProduct(product);
        }
        return chatsRepository.findAll();
    }
}
