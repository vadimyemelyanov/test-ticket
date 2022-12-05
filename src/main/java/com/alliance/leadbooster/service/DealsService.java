package com.alliance.leadbooster.service;

import com.alliance.leadbooster.exceptions.EntityNotFoundException;
import com.alliance.leadbooster.model.UpdateDealRequest;
import com.alliance.leadbooster.model.enums.DealState;
import com.alliance.leadbooster.persistence.entity.Deal;
import com.alliance.leadbooster.persistence.entity.StateHistory;
import com.alliance.leadbooster.persistence.repository.DealsRepository;
import com.alliance.leadbooster.persistence.repository.StateHistoryRepository;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.alliance.leadbooster.model.enums.DealState.QUALIFICATION;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealsService {

    private final DealsRepository dealsRepository;
    private final StateHistoryRepository stateHistoryRepository;


    public List<Deal> getAllDeals(String product) {
        return getDeals(product);
    }

    @Transactional
    public Deal moveDealToState(String uuid, DealState targetState) {
        Deal ticket = findDealRequired(uuid);

        stateHistoryRepository.save(StateHistory.builder()
            .state(ticket.getCurrentState())
            .dealUuid(uuid)
            .fromDate(ticket.getUpdatedAt())
            .uuid(UUID.randomUUID().toString())
            .build());

        ticket = ticket.toBuilder()
            .currentState(targetState)
            .build();

        return save(ticket);
    }

    public Deal updateDeal(UpdateDealRequest request) {
        Deal ticket = findDealRequired(request.getDealUuid());
        HashMap<String, String> changesMap = new HashMap<>();

        final Deal.DealBuilder dealBuilder = ticket.toBuilder();
        if (request.getName() != null) {
            dealBuilder.name(request.getName());
            changesMap.put("name", request.getName());
        }
        if (request.getProduct() != null) {
            dealBuilder.product(request.getProduct());
            changesMap.put("product", request.getProduct());
        }

        return save(dealBuilder.build());
    }

    public void createTicket(Message message, String telegramLink) {
        String messageText = extractLastMessageText(message);
        String username2 = extractUsername(message);
        Deal ticket = Deal.builder()
            .uuid(UUID.randomUUID().toString())
            .name(message.chat().title())
            .authorUsername(username2)
            .telegramChatId(message.chat().id())
            .telegramLink(telegramLink)
            .currentState(QUALIFICATION)
            .lastMessage(messageText)
            .lastMessageReceivedAt(LocalDateTime.now())
            .build();

        save(ticket);
        log.info("Ticket saved [{}]", ticket.getUuid());
    }

    public void updateDealWithMessage(Deal ticket, Message message) {
        String lastMessage = extractLastMessageText(message);
        final String username = extractUsername(message);
        ticket = ticket.toBuilder()
            .lastMessageReceivedAt(LocalDateTime.now())
            .authorUsername(username)
            .lastMessage(lastMessage)
            .build();

        save(ticket);
        log.info("Ticket updated [{}]", ticket.getUuid());
    }

    @Transactional
    public Deal save(Deal ticket) {
        return dealsRepository.save(ticket);
    }

    public List<Deal> getDealsByChatId(Long chatId) {
        return dealsRepository.findAllByChatIdAndCurrentStateNotFinal(chatId);
    }

    private String extractUsername(Message message) {
        final User user = message.from();
        return isEmpty(user.username())
               ? format("%s %s", user.firstName(), user.lastName())
               : user.username();
    }

    private String extractLastMessageText(Message message) {
        if (isNotBlank(message.text()))
            return message.text();
        if (message.audio() != null)
            return "Audio";
        if (message.document() != null)
            return "Document " + message.document()
                                        .mimeType();
        if (message.video() != null)
            return "Video";
        if (message.photo() != null)
            return "Photo";
        if (message.sticker() != null)
            return "Sticker";
        if (message.caption() != null)
            return "Caption: " + message.caption();

        return EMPTY;
    }

    private Deal findDealRequired(String uuid) {
        return dealsRepository.findById(uuid)
                              .orElseThrow(EntityNotFoundException::new);
    }


    private List<Deal> getDeals(String product) {
        return ofNullable(product).map(dealsRepository::findAllByProduct)
                                  .orElseGet(dealsRepository::findAllByCurrentStateIsNotClosed);
    }
}
