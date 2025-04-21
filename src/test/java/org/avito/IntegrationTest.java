package org.avito;

import com.jayway.jsonpath.JsonPath;
import org.avito.model.Role;
import org.avito.repository.ItemRepository;
import org.avito.repository.PvzRepository;
import org.avito.repository.ReceptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    private String clientToken;
    private UUID pvzId;

    @Autowired
    private PvzRepository pvzRepository;

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() throws Exception {
        itemRepository.deleteAll();
        receptionRepository.deleteAll();
        pvzRepository.deleteAll();

        clientToken = getToken(Role.ROLE_CLIENT);
        String moderatorToken = getToken(Role.ROLE_MODERATOR);
        pvzId = createPvz(moderatorToken);
    }

    @Test
    void fullReceptionFlow() throws Exception {
        UUID receptionId = startReception(clientToken, pvzId);

        for (int i = 0; i < 50; i++) {
            mockMvc.perform(post("/api/items/" + pvzId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + clientToken)
                            .param("item", "clothing"))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(post("/api/reception/close/" + pvzId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + clientToken))
                .andExpect(status().isOk());
    }

    private String getToken(Role role) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/dummyLogin")
                        .param("role", role.name()))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return JsonPath.read(json, "$.token");
    }

    private UUID createPvz(String token) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/pvz")
                        .param("city", "Москва")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return UUID.fromString(JsonPath.read(json, "$.id"));
    }

    private UUID startReception(String token, UUID pvzId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/reception/" + pvzId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return UUID.fromString(JsonPath.read(json, "$.id"));
    }
}