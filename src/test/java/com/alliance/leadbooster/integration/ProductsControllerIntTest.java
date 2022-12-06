package com.alliance.leadbooster.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.alliance.leadbooster.utils.TestUtils.getFileContent;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ProductsControllerIntTest {
    private static final String DEAL_UUID = "f596c437-f529-4128-8707-4553ef80c2c0";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void shouldSuccessfullyAddNewNote() {
        //given
        String expectedResponse = getFileContent("responses/get-products-response.json");
        //when
        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/products"))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResponse))
            .andDo(print());
    }
}
