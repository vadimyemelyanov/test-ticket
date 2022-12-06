package com.alliance.leadbooster.telegram;

import com.alliance.leadbooster.persistence.entity.Deal;
import com.alliance.leadbooster.service.DealsService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;


@Log4j2
@Component
@RequiredArgsConstructor
public class TelegramListener {

    private TelegramBot telegramBot;
    private final DealsService dealsService;

    @Value("${api.telegram.bot-token}")
    private String token;

    @Value("${api.telegram.enabled}")
    private Boolean enabled;


    @PostConstruct
    public void init() {
        if (!enabled) return;

        telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::process);

            return CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        try {
            if (update.message() != null) {
                if (update.message().groupChatCreated() != null && update.message().groupChatCreated()) {
                    sendMessage("Please set me as administrator of this chat", update.message().chat().id());
                } else if (update.message().newChatMembers() == null && update.message().leftChatMember() == null) {
                    processNewMessageFromChat(update);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Exception happened on handing message", ex);
        }
    }

    private void processNewMessageFromChat(Update update) {
        List<Deal> deals = dealsService.getDealsByChatId(update.message().chat().id());

        if (deals.isEmpty()) {
            GetChat chat = new GetChat(update.message().chat().id());
            GetChatResponse chatResponse = telegramBot.execute(chat);

            dealsService.createTicket(update.message(), chatResponse.chat().inviteLink());
        } else if (deals.size() == 1) {
            dealsService.updateDealWithMessage(deals.get(0), update.message());
        } else
            throw new IllegalStateException("cant have 2 or more in progress tickets for 1 chat");
    }

    public void sendMessage(String message, Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, message)
            .disableWebPagePreview(true);

        sendMessage(sendMessage);
    }

    private void sendMessage(BaseRequest request) {
        telegramBot.execute(request);
    }
}