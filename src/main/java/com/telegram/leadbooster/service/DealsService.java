package com.telegram.leadbooster.service;

import com.pengrad.telegrambot.model.Message;
import com.telegram.leadbooster.domain.Deal;
import com.telegram.leadbooster.domain.StateHistory;
import com.telegram.leadbooster.dto.UpdateDealRequest;
import com.telegram.leadbooster.dto.enums.DealState;
import com.telegram.leadbooster.exceptions.EntityNotFoundException;
import com.telegram.leadbooster.repository.DealsRepository;
import com.telegram.leadbooster.repository.StateHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DealsService {
    private final DealsRepository dealsRepository;
    private final StateHistoryRepository stateHistoryRepository;

    public DealsService(DealsRepository dealsRepository, StateHistoryRepository stateHistoryRepository) {
        this.dealsRepository = dealsRepository;
        this.stateHistoryRepository = stateHistoryRepository;
    }


    @Transactional(readOnly = true)
    public List<Deal> getAllDeals(String product) {
        return getDeals(product);
    }

    @Transactional
    public Deal moveDealToState(String uuid, DealState targetState) {
        Deal ticket = dealsRepository.findById(uuid)
            .orElseThrow(EntityNotFoundException::new);

        if (targetState == DealState.CLOSED && ticket.getCurrentState() != DealState.COMPLETED) {
            throw new IllegalStateException(String.format("Can`t close deal from %s state", ticket.getCurrentState()));
        }

        stateHistoryRepository.save(StateHistory.builder()
            .state(ticket.getCurrentState())
            .dealUuid(uuid)
            .fromDate(ticket.getUpdatedAt())
            .toDate(LocalDateTime.now())
            .uuid(UUID.randomUUID().toString())
            .build());

        ticket.setCurrentState(targetState);
        ticket.setUpdatedAt(LocalDateTime.now());
        return dealsRepository.save(ticket);
    }

    @Transactional
    public Deal updateDeal(UpdateDealRequest request) {
        Deal ticket = dealsRepository.findById(request.getDealUuid())
            .orElseThrow(EntityNotFoundException::new);
        HashMap<String, String> changesMap = new HashMap<>();

        if (request.getName() != null) {
            ticket.setName(request.getName());
            changesMap.put("name", request.getName());
        }
        if (request.getProduct() != null) {
            ticket.setProduct(request.getProduct());
            changesMap.put("product", request.getProduct());
        }

        return dealsRepository.save(ticket);
    }

    @Transactional
    public void createTicket(Message message, String telegramLink, String username) {
        String messageText = extractLastMessageText(message);
        Deal ticket = Deal.builder()
            .uuid(UUID.randomUUID().toString())
            .name(message.chat().title())
            .authorUsername(username)
            .telegramChatId(message.chat().id())
            .telegramLink(telegramLink)
            .currentState(DealState.QUALIFICATION)
            .lastMessage(messageText)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .lastMessageReceivedAt(LocalDateTime.now())
            .build();
        dealsRepository.save(ticket);
        log.info("Ticket saved [{}]", ticket.getUuid());
    }

    @Transactional
    public void updateDealWithMessage(Deal ticket, Message message) {
        ticket.setLastMessageReceivedAt(LocalDateTime.now());
        String lastMessage = extractLastMessageText(message);
        ticket.setLastMessage(lastMessage);
        dealsRepository.save(ticket);
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
    public List<Deal> getDealsByChatId(Long chatId) {
        return dealsRepository.findAllByChatIdAndCurrentStateNotFinal(chatId);
    }


    private List<Deal> getDeals(String product) {
        if (product != null) {
            return dealsRepository.findAllByProduct(product);
        }
        return dealsRepository.findAllByCurrentStateIsNotClosed();
    }
}
