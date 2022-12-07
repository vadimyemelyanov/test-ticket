package com.alliance.leadbooster.integration;

import com.alliance.leadbooster.model.DealResponseDto;
import com.alliance.leadbooster.model.MoveDealRequest;
import com.alliance.leadbooster.model.UpdateDealRequest;
import com.alliance.leadbooster.persistence.entity.Deal;
import com.alliance.leadbooster.persistence.entity.Notes;
import com.alliance.leadbooster.persistence.entity.StateHistory;
import com.alliance.leadbooster.persistence.repository.DealsRepository;
import com.alliance.leadbooster.persistence.repository.NotesRepository;
import com.alliance.leadbooster.utils.DealMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alliance.leadbooster.model.enums.DealState.KYC;
import static com.alliance.leadbooster.model.enums.DealState.QUALIFICATION;
import static com.alliance.leadbooster.utils.TestUtils.generateDeal;
import static com.alliance.leadbooster.utils.TestUtils.generateNote;
import static com.alliance.leadbooster.utils.TestUtils.generateStateHistory;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DealControllerIntTest {
    private static final String DEAL_UUID = "f596c437-f529-4128-8707-4553ef80c2c0";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @SpyBean
    private DealsRepository dealsRepository;

    @Autowired
    private NotesRepository notesRepository;

    @AfterEach
    public void setUp() {
        dealsRepository.deleteAll();
    }


    @SneakyThrows
    @Test
    @Transactional
    @Rollback
    public void shouldReturnAllDeals() {
        //given
        StateHistory stateHistory = generateStateHistory(DEAL_UUID, KYC);
        Notes note = generateNote(DEAL_UUID);

        Deal deal = generateDeal(DEAL_UUID, QUALIFICATION, Set.of(note), Set.of(stateHistory));

        dealsRepository.save(deal);

        List<DealResponseDto> savedDeals = dealsRepository.findAllByCurrentStateIsNotClosed()
            .stream().map(DealMapper::mapToDto).collect(Collectors.toList());

        //when
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/deals"))
            //then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(content().string(objectMapper.writeValueAsString(savedDeals)));
    }

    @SneakyThrows
    @Test
    public void shouldReturnNotNamedDealsByNotExistingProduct() {
        //given
        //when
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/deals?product=NotProduct"))
            //then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(0));
    }

    @SneakyThrows
    @Test
    public void shouldRespondWithHttpStatusButNotWithStacktrace() {
        //given
        Mockito.when(dealsRepository.findAllByProduct("XYZ")).thenThrow(new RuntimeException());
        //when
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/deals?product=XYZ"))
            //then
            .andExpect(status().is5xxServerError());
    }


    @SneakyThrows
    @Test
    @Rollback
    @Transactional
    public void shouldMoveDealToNewStatus() {
        //given
        StateHistory stateHistory = generateStateHistory(DEAL_UUID, KYC);
        Notes note = generateNote(DEAL_UUID);

        Deal deal = generateDeal(DEAL_UUID, QUALIFICATION, Set.of(note), Set.of(stateHistory));

        dealsRepository.save(deal);

        MoveDealRequest moveDealRequest = new MoveDealRequest(KYC);
        //when
        mockMvc
            .perform(MockMvcRequestBuilders
                .patch("/api/v1/deals/" + DEAL_UUID + "/move")
                .content(objectMapper.writeValueAsString(moveDealRequest))
                .contentType(MediaType.APPLICATION_JSON))
            //then
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.ticket.state").value(moveDealRequest.getTargetState().toString()));

    }

    @SneakyThrows
    @Test
    @Transactional
    @Rollback
    public void shouldUpdateDealProduct() {
        //given
        StateHistory stateHistory = generateStateHistory(DEAL_UUID, KYC);
        Notes note = generateNote(DEAL_UUID);

        Deal deal = generateDeal(DEAL_UUID, QUALIFICATION, Set.of(note), Set.of(stateHistory));

        dealsRepository.save(deal);

        UpdateDealRequest moveTicketRequest = new UpdateDealRequest(DEAL_UUID, "My New Name", "XYZ");

        //when
        mockMvc
            .perform(MockMvcRequestBuilders
                .put("/api/v1/deals")
                .content(objectMapper.writeValueAsString(moveTicketRequest))
                .contentType(MediaType.APPLICATION_JSON))
            //then
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.name").value(moveTicketRequest.getName()))
            .andExpect(jsonPath("$.product").value(moveTicketRequest.getProduct()));
    }

}
