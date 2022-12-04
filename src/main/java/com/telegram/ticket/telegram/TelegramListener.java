package com.telegram.ticket.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.GetChatAdministrators;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetChatAdministratorsResponse;
import com.pengrad.telegrambot.response.GetChatResponse;
import com.telegram.ticket.domain.Chat;
import com.telegram.ticket.service.ChatsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;


@Log4j2
@Component
public class TelegramListener {
    private TelegramBot telegramBot;
    private final ChatsService chatsService;

    @Value("${api.telegram.bot-token}")
    private String token;

    public TelegramListener(ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    @PostConstruct
    public void init() {
        telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
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
            );

            return CONFIRMED_UPDATES_ALL;
        });
    }

    private void processNewMessageFromChat(Update update) {
        List<Chat> chats = chatsService.getTicketsByChatId(update.message().chat().id());
        if (chats.isEmpty()) {

            GetChat chat = new GetChat(update.message().chat().id());
            GetChatAdministrators chatAdministrators = new GetChatAdministrators(update.message().chat().id());

            GetChatResponse chatResponse = telegramBot.execute(chat);
            GetChatAdministratorsResponse administratorsResponse = telegramBot.execute(chatAdministrators);
            Optional<ChatMember> adminOfChat = administratorsResponse.administrators().stream().filter(chatMember -> !chatMember.user().isBot()).findFirst();

            chatsService.createTicket(update.message(), chatResponse.chat().inviteLink(), adminOfChat.get().user().username());
        } else if (chats.size() == 1) {
            chatsService.updateTicketWithMessage(chats.get(0), update.message());
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