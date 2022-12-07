package com.alliance.leadbooster.integration;

import com.alliance.leadbooster.model.AddNoteRequest;
import com.alliance.leadbooster.persistence.entity.Deal;
import com.alliance.leadbooster.persistence.entity.StateHistory;
import com.alliance.leadbooster.persistence.repository.DealsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static com.alliance.leadbooster.model.enums.DealState.KYC;
import static com.alliance.leadbooster.model.enums.DealState.QUALIFICATION;
import static com.alliance.leadbooster.utils.TestUtils.generateDeal;
import static com.alliance.leadbooster.utils.TestUtils.generateStateHistory;
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
    private ObjectMapper objectMapper;
    @Autowired
    private DealsRepository dealsRepository;

    @SneakyThrows
    @Test
    public void shouldSuccessfullyAddNewNote() {
        //given
        StateHistory stateHistory = generateStateHistory(DEAL_UUID, KYC);
        Deal deal = generateDeal(DEAL_UUID, QUALIFICATION, null, Set.of(stateHistory));

        dealsRepository.save(deal);

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
