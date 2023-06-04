package com.spc.healthmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spc.healthmaster.dtos.ApiErrorDto;
import com.spc.healthmaster.dtos.RequestServerDto;
import com.spc.healthmaster.services.ssh.SshManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;
import static com.spc.healthmaster.util.JsonLoader.loadObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SshManagerControllerTest {

    private static final String DELETE_PATH = "/server/id/{id}";
    private static final String PATH_BASE = "/server";
    private static final Long id = 0L;
    private static final String VALID_BODY ="{\n" +
            "   \"user\":\"usprocessos\",\n" +
            "   \"host\":\"10.18.100.32\",\n" +
            "   \"password\":\"xxxxx\",\n" +
            "   \"serverName\":\"server desconocido\"\n" +
            "}";
    private static final String EDIT_VALID_BODY ="{\n" +
            "   \"id\":1,\n" +
            "   \"user\":\"usprocessos\",\n" +
            "   \"host\":\"10.18.100.32\",\n" +
            "   \"password\":\"xxxxx\",\n" +
            "   \"serverName\":\"server desconocido\"\n" +
            "}";

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

    @ParameterizedTest
    @MethodSource("badBodyAndResponseErrorCode")
    public void givenInvalidBodyWhenCallSaveThenReturnBadRequest(final String badBody, final String path) throws Exception {
        final ApiErrorDto apiErrorDto = loadObject(path, ApiErrorDto.class);
        mockMvc
                .perform(post(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenAlreadyExistServerWhenCallSaveThenReturnBadRequest() throws Exception {
        final ApiErrorDto apiErrorDto = alreadyExistServer("");
        doThrow(apiErrorDto.toException()).when(sshManagerService).save(any());
        mockMvc
                .perform(post(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenConnectionInvalidWhenCallSaveThenReturnBadGateway() throws Exception {
        final ApiErrorDto apiErrorDto = sshException("usrprcess", "1018.100.30")
                .withCause("session", "java.net.ConnectException: Connection timed out: connect");
        doThrow(apiErrorDto.toException()).when(sshManagerService).save(any());
        mockMvc
                .perform(post(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isBadGateway())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenErrorDatabaseWhenCallaSaveThenReturnInternalError() throws Exception {
        final ApiErrorDto apiErrorDto = jpaException("");
        doThrow(apiErrorDto.toException()).when(sshManagerService).save(any());
        mockMvc
                .perform(post(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void whenCallSaveThenOk() throws Exception {
        doNothing().when(sshManagerService).save(any());
        mockMvc
                .perform(post(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("badBodyAndResponseErrorCodeEdit")
    public void givenInvalidBodyWhenCallEditThenReturnBadRequest(final String badBody, final String path) throws Exception {
        final ApiErrorDto apiErrorDto = loadObject(path, ApiErrorDto.class);
        mockMvc
                .perform(put(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenNotFoundServerWhenCallEditThenReturnNotFound() throws Exception {
        final ApiErrorDto apiErrorDto = notFoundServerManager(1l);
        doThrow(apiErrorDto.toException()).when(sshManagerService).edit(any());
        mockMvc
                .perform(put(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EDIT_VALID_BODY))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenServerExitWhenCallEditThenReturnBadRequest() throws Exception {
        final ApiErrorDto apiErrorDto = alreadyExistServer("");
        doThrow(apiErrorDto.toException()).when(sshManagerService).edit(any());
        mockMvc
                .perform(put(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EDIT_VALID_BODY))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenConnectionShhInvalidWhenCallEditThenReturnBadGateway() throws Exception {
        final ApiErrorDto apiErrorDto = sshException("usrprcess", "1018.100.30")
                .withCause("session", "java.net.ConnectException: Connection timed out: connect");
        doThrow(apiErrorDto.toException()).when(sshManagerService).edit(any());
        mockMvc
                .perform(put(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EDIT_VALID_BODY))
                .andExpect(status().isBadGateway())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenErrorDatabaseWhenCallaEditThenReturnInternalError() throws Exception {
        final ApiErrorDto apiErrorDto = jpaException("");
        doThrow(apiErrorDto.toException()).when(sshManagerService).edit(any());
        mockMvc
                .perform(put(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EDIT_VALID_BODY))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void whenCallEditThenOk() throws Exception {
        doNothing().when(sshManagerService).edit(any());
        mockMvc
                .perform(put(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EDIT_VALID_BODY))
                .andExpect(status().isOk());
    }

    private ApiErrorDto stringToApiError(final String errorAsString) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(errorAsString, ApiErrorDto.class);
    }

    private static Stream<Arguments> badBodyAndResponseErrorCodeEdit() {
        return Stream.of(
                Arguments.of("", "/bad_request/deserialization_error_unknown.json"),
                Arguments.of("{}", "/bad_request/server/argument_not_valid.json"),
                Arguments.of("{\"id\":1}", "/bad_request/server/argument_not_valid.json"),
                Arguments.of("{\n \"id\":null,\n \"user\":null,\n \"host\":null,\n \"password\":null,\n \"serverName\":null\n }", "/bad_request/server/argument_not_valid.json"),
                Arguments.of("{\n" +
                        "   \"user\":\"usprocessos\",\n" +
                        "   \"host\":\"10.18.100.32\",\n" +
                        "   \"password\":\"xxxxx\",\n" +
                        "   \"serverName\":\"server desconocido\"\n" +
                        "}","/bad_request/server/argument_not_valid_id.json"),
                Arguments.of("{\n" +
                        "   \"id\":\"x\",\n" +
                        "   \"user\":\"usprocessos\",\n" +
                        "   \"host\":\"10.18.100.32\",\n" +
                        "   \"password\":\"xxxxx\",\n" +
                        "   \"serverName\":\"server desconocido\"\n" +
                        "}","/bad_request/server/deserialization_error_unknown_id.json")
        );
    }

    private static Stream<Arguments> badBodyAndResponseErrorCode() {
        return Stream.of(
                Arguments.of("", "/bad_request/deserialization_error_unknown.json"),
                Arguments.of("{}", "/bad_request/server/argument_not_valid.json"),
                Arguments.of("{\n \"user\":null,\n \"host\":null,\n \"password\":null,\n \"serverName\":null\n }", "/bad_request/server/argument_not_valid.json")
        );
    }
}
