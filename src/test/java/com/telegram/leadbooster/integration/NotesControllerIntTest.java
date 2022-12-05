package com.telegram.leadbooster.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegram.leadbooster.dto.AddNoteRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NotesControllerIntTest {
    private static final String DEAL_UUID = "f596c437-f529-4128-8707-4553ef80c2c0";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void shouldSuccessfullyAddNewNote() {
        //given
        AddNoteRequest addNoteRequest = AddNoteRequest
            .builder().dealUuid(DEAL_UUID)
            .content("He is a tester").build();
        //when
        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/notes")
                .content(objectMapper.writeValueAsString(addNoteRequest))
                .contentType(MediaType.APPLICATION_JSON))
            //then
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("He is a tester")));


    }


    @SneakyThrows
    @Test
    public void shouldReturnValidationErrorOnMissingDeal() {
        //given
        AddNoteRequest addNoteRequest = new AddNoteRequest("dasdasd", "");
        //when
        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/notes")
                .content(objectMapper.writeValueAsString(addNoteRequest))
                .contentType(MediaType.APPLICATION_JSON))
            //then
            .andExpect(status().is5xxServerError());

    }


}
