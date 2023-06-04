package com.spc.healthmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spc.healthmaster.dtos.ApiErrorDto;
import com.spc.healthmaster.dtos.RequestServerDto;
import com.spc.healthmaster.services.ssh.SshManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.spc.healthmaster.factories.ApiErrorFactory.METHOD_ARGUMENT_NOT_VALID;
import static com.spc.healthmaster.factories.ApiErrorFactory.jpaException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SshManagerControllerTest {

    private static final String DELETE_PATH = "/server/id/{id}";
    private static final Long id = 0L;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private SshManagerService sshManagerService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void givenTexIdInvalidWhenCallDeleteThenBadRequest() throws Exception {
        final ApiErrorDto apiErrorDto = METHOD_ARGUMENT_NOT_VALID.withCause("convert", "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"a\"");

        mockMvc.perform(delete(DELETE_PATH, "a"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenIdAnNotFoundInDatabaseWhenCallDeleteThenReturnServerError() throws Exception {
        final ApiErrorDto apiErrorDto = jpaException("");
        doThrow(apiErrorDto.toException()).when(sshManagerService).deleteShhManager(id);
        mockMvc.perform(delete(DELETE_PATH, id))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenIdWhenCallDeleteThenOk() throws Exception {
        doNothing().when(sshManagerService).deleteShhManager(id);
        mockMvc.perform(delete(DELETE_PATH, id))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCallGetAllServerReturnEmpty() throws Exception {
        when(sshManagerService.getListSshManager()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/server"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void whenCallGetAllServerReturnList() throws Exception {
        final List<RequestServerDto> mockSshManagers = Arrays.asList(
                new RequestServerDto("Server1", 1l, "Host1", "User1", "passwrd"),
                new RequestServerDto("Server2", 2l, "Host2", "User2", "passwrd")
        );
        when(sshManagerService.getListSshManager()).thenReturn(mockSshManagers);
        final MvcResult result =  mockMvc.perform(get("/server"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<RequestServerDto> responseDtoList = new ObjectMapper().readValue(responseBody, new TypeReference<List<RequestServerDto>>() {});

        // Realizar las aserciones sobre la lista de objetos
        assertEquals(2, responseDtoList.size());
        assertEquals("Server1", responseDtoList.get(0).getServerName());
        assertEquals("Host1", responseDtoList.get(0).getHost());
        assertEquals("User1", responseDtoList.get(0).getUser());
        assertEquals("Server2", responseDtoList.get(1).getServerName());
        assertEquals("Host2", responseDtoList.get(1).getHost());
        assertEquals("User2", responseDtoList.get(1).getUser());

        // Verificar que el método del servicio se haya llamado una vez
        verify(sshManagerService, times(1)).getListSshManager();
    }

    private ApiErrorDto stringToApiError(final String errorAsString) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(errorAsString, ApiErrorDto.class);
    }

}
