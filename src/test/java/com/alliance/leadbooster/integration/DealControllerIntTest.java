package com.alliance.leadbooster.integration;

import com.alliance.leadbooster.model.MoveDealRequest;
import com.alliance.leadbooster.model.UpdateDealRequest;
import com.alliance.leadbooster.persistence.repository.DealsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

import static com.alliance.leadbooster.model.enums.DealState.CLOSED;
import static com.alliance.leadbooster.model.enums.DealState.KYC;
import static com.alliance.leadbooster.utils.TestUtils.getFileContent;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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


    @SneakyThrows
    @Test
    public void shouldReturnAllDeals() {
        //given
        String expectedResponse = getFileContent("responses/get-all-deals-response.json");

        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/deals"))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResponse));
    }

    @SneakyThrows
    @Test
    public void shouldReturnNotNamedDealsByNotExistingProduct() {
        //given
        String expectedResponse = getFileContent("responses/get-all-deals-no-product-response.json");
        //when
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/deals?product=NotProduct"))
            //then
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResponse));
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
            .andExpect(content().string(containsString("currentState\":\"KYC\"")));
    }

    @SneakyThrows
    @Test
    public void shouldFailOnMovingCauseWrongCurrentState() {
        //given
        MoveDealRequest moveDealRequest = new MoveDealRequest(CLOSED);
        //when
        mockMvc
            .perform(MockMvcRequestBuilders
                .patch("/api/v1/deals/" + DEAL_UUID + "/move")
                .content(objectMapper.writeValueAsString(moveDealRequest))
                .contentType(MediaType.APPLICATION_JSON))
            //then
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(content().string(containsString("Can`t close deal from QUALIFICATION state")));
    }


    @SneakyThrows
    @Test
    @Transactional
    @Rollback
    public void shouldUpdateDealProduct() {
        //given
        UpdateDealRequest moveTicketRequest = new UpdateDealRequest(DEAL_UUID, "My New Name", "XYZ");
        String expectedResponse = getFileContent("responses/update-deal-successful-response.json");

        //when
        mockMvc
            .perform(MockMvcRequestBuilders
                .put("/api/v1/deals")
                .content(objectMapper.writeValueAsString(moveTicketRequest))
                .contentType(MediaType.APPLICATION_JSON))
            //then
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().json(expectedResponse));
    }

}
