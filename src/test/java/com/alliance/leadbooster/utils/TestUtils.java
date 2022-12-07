package com.alliance.leadbooster.utils;

import com.alliance.leadbooster.model.enums.DealState;
import com.alliance.leadbooster.persistence.entity.Deal;
import com.alliance.leadbooster.persistence.entity.Notes;
import com.alliance.leadbooster.persistence.entity.StateHistory;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class TestUtils {


    public static String getFileContent(String path) {
        InputStream resource = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(path);

        String expectedResponse = null;
        try {
            expectedResponse = IOUtils.toString(resource, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return expectedResponse;
    }

    public static Deal generateDeal(String uuid, DealState state,
                                    Set<Notes> notesSet, Set<StateHistory> stateHistory) {
        return Deal.builder()
            .uuid(uuid)
            .name("hi")
            .currentState(state)
            .lastMessage("test")
            .lastMessageReceivedAt(LocalDateTime.now())
            .telegramLink("http://d")
            .authorUsername("test")
            .telegramChatId(1L)
            .product("GAD")
            .notes(notesSet)
            .stateHistory(stateHistory)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }


    public static StateHistory generateStateHistory(String dealUuid, DealState dealState) {
        return StateHistory.builder()
            .state(dealState)
            .uuid(UUID.randomUUID().toString())
            .dealUuid(dealUuid)
            .fromDate(LocalDateTime.now())
            .toDate(LocalDateTime.now())
            .build();
    }

    public static Notes generateNote(String dealUuid) {
        return Notes.builder()
            .uuid(UUID.randomUUID().toString())
            .dealUuid(dealUuid)
            .text("Hello ima note")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
