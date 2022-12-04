package com.telegram.ticket.repository;

import com.telegram.ticket.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatsRepository extends JpaRepository<Chat, String> {

    @Query(value = "select * from chats" +
        "         left join state_history sh on chats.uuid = sh.chat_uuid" +
        "         left join notes n on chats.uuid = n.chat_uuid " +
        "         where current_state != 'CLOSED' and product = ?1 or product is null", nativeQuery = true)
    List<Chat> findAllByProductAndProductIsNull(String product);

    @Query(value = "select * from chats " +
        "         left join state_history sh on chats.uuid = sh.chat_uuid" +
        "         left join notes n on chats.uuid = n.chat_uuid" +
        "         where current_state != 'CLOSED'", nativeQuery = true)
    List<Chat> findAll();

    @Query(nativeQuery = true, value = "select * from chats where telegram_chat_id = ?1 and current_state not in ('CLOSED', 'COMPLETED')")
    List<Chat> findAllByChatIdAndCurrentStateNotFinal(Long chatId);
}
