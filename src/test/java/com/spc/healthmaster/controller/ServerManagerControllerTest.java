package com.spc.healthmaster.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spc.healthmaster.dtos.error.ApiErrorDto;
import com.spc.healthmaster.dtos.request.RequestResponseServerManagerDto;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.services.server.ServerManagerService;
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
import static com.spc.healthmaster.util.JsonLoader.stringToApiError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServerManagerControllerTest {

    private static final String GET_DELETE_PATH = "/server/id/{id}";
    private static final String PATH_BASE = "/server";
    private static final Long id = 0L;
    private static final Long sshManagerId =1l;

    private  final ApiErrorDto FAILED_CONVERT = METHOD_ARGUMENT_NOT_VALID.withCause("convert", "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"a\"");
    private static final String VALID_BODY ="{\n" +
            "   \"typeStrategy\":\"GLASSFISH_SERVER\",\n" +
            "   \"serverManagerName\":\"10.18.100.32\",\n" +
            "   \"username\":\"xxxxx\",\n" +
            "   \"password\":\"xxxxx\",\n" +
            "   \"port\":\"xxxxx\",\n" +
            "   \"ssManagerId\":1\n" +
            "}";

    private static final String EDIT_VALID_BODY ="{\n" +
            "   \"typeStrategy\":\"GLASSFISH_SERVER\",\n" +
            "   \"serverManagerName\":\"10.18.100.32\",\n" +
            "   \"username\":\"xxxxx\",\n" +
            "   \"password\":\"xxxxx\",\n" +
            "   \"port\":\"xxxxx\",\n" +
            "   \"ssManagerId\":1,\n" +
            "   \"id\":1\n" +
            "}";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private ServerManagerService serverManagerService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void givenTexIdInvalidWhenCallDeleteThenBadRequest() throws Exception {
         mockMvc.perform(delete(GET_DELETE_PATH, "a"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(FAILED_CONVERT,  stringToApiError(response));
                });
    }

    @Test
    public void givenIdAnNotFoundInDatabaseWhenCallDeleteThenReturnServerError() throws Exception {
        final ApiErrorDto apiErrorDto = jpaException("");
        doThrow(apiErrorDto.toException()).when(serverManagerService).delete(id);
        mockMvc.perform(delete(GET_DELETE_PATH, id))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenTexIdInvalidWhenCallListThenBadRequest() throws Exception {
        mockMvc.perform(get(GET_DELETE_PATH, "a"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(FAILED_CONVERT,  stringToApiError(response));
                });
    }

    @Test
    public void whenCallGetAllServerReturnEmpty() throws Exception {
        when(serverManagerService.findServerManagerBySshManagerId(any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get(GET_DELETE_PATH, 1l))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void whenCallGetAllServerReturnList() throws Exception {
        final List<RequestResponseServerManagerDto> mockSshManagers = Arrays.asList(
                new RequestResponseServerManagerDto( 1l, TypeStrategy.GLASSFISH_APP, "Host1", "User1", "passwrd","" ,sshManagerId),
                new RequestResponseServerManagerDto( 2l,TypeStrategy.GLASSFISH_SERVER, "Host2", "User2", "passwrd", "", sshManagerId)
        );
        when(serverManagerService.findServerManagerBySshManagerId(any())).thenReturn(mockSshManagers);
        final MvcResult result =  mockMvc.perform(get(GET_DELETE_PATH, 1l))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = result.getResponse().getContentAsString();
        final List<RequestResponseServerManagerDto> responseDtoList = new ObjectMapper().readValue(responseBody, new TypeReference<List<RequestResponseServerManagerDto>>() {});

        // Realizar las aserciones sobre la lista de objetos
        assertEquals(2, responseDtoList.size());
        assertEquals(TypeStrategy.GLASSFISH_APP, responseDtoList.get(0).getTypeStrategy());
        assertEquals("Host1", responseDtoList.get(0).getServerManagerName());
        assertEquals("User1", responseDtoList.get(0).getUsername());
        assertEquals("passwrd", responseDtoList.get(0).getPassword());
        assertEquals(TypeStrategy.GLASSFISH_SERVER, responseDtoList.get(1).getTypeStrategy());
        assertEquals("Host2", responseDtoList.get(1).getServerManagerName());
        assertEquals("User2", responseDtoList.get(1).getUsername());
        assertEquals("passwrd", responseDtoList.get(1).getPassword());
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
        final ApiErrorDto apiErrorDto = alreadyExistServerManager("");
        doThrow(apiErrorDto.toException()).when(serverManagerService).save(any());
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
    public void givenSShManagerNotFoundWhenCallSaveThenReturnNotFound() throws Exception {
        final ApiErrorDto apiErrorDto = notFoundConnectionSsh(1l);
        doThrow(apiErrorDto.toException()).when(serverManagerService).save(any());
        mockMvc
                .perform(post(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    @Test
    public void givenErrorDatabaseWhenCallaSaveThenReturnInternalError() throws Exception {
        final ApiErrorDto apiErrorDto = jpaException("");
        doThrow(apiErrorDto.toException()).when(serverManagerService).save(any());
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
        doNothing().when(serverManagerService).save(any());
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
    public void givenNotFoundServerManagerWhenCallEditThenReturnNotFound() throws Exception {
        final ApiErrorDto apiErrorDto = notFoundServerManager(1l);
        doThrow(apiErrorDto.toException()).when(serverManagerService).edit(any());
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
    public void givenNotFoundSshConnectionWhenCallEditThenReturnNotFound() throws Exception {
        final ApiErrorDto apiErrorDto = notFoundConnectionSsh(1l);
        doThrow(apiErrorDto.toException()).when(serverManagerService).edit(any());
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
        final ApiErrorDto apiErrorDto = alreadyExistSshManager("");
        doThrow(apiErrorDto.toException()).when(serverManagerService).edit(any());
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
    public void givenErrorDatabaseWhenCallaEditThenReturnInternalError() throws Exception {
        final ApiErrorDto apiErrorDto = jpaException("");
        doThrow(apiErrorDto.toException()).when(serverManagerService).edit(any());
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
        doNothing().when(serverManagerService).edit(any());
        mockMvc
                .perform(put(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EDIT_VALID_BODY))
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> badBodyAndResponseErrorCodeEdit() {
        return Stream.of(
                Arguments.of("", "/bad_request/deserialization_error_unknown.json"),
                Arguments.of("{}", "/bad_request/server/argument_not_valid.json"),
                Arguments.of("{\n" +
                        "    \"typeStrategy\": null,\n" +
                        "    \"serverManagerName\":null,\n" +
                        "    \"username\":null,\n" +
                        "    \"password\":null,\n" +
                        "    \"port\":null,\n" +
                        "    \"ssManagerId\":null\n" +
                        "}", "/bad_request/server/argument_not_valid.json"),
                Arguments.of("{\n" +
                        "   \"id\":\"x\",\n" +
                        "    \"typeStrategy\": null,\n" +
                        "    \"serverManagerName\":null,\n" +
                        "    \"username\":null,\n" +
                        "    \"password\":null,\n" +
                        "    \"port\":null,\n" +
                        "    \"ssManagerId\":null\n" +
                        "}", "/bad_request/server/deserialization_error_unknown_id.json")
        );
    }
    private static Stream<Arguments> badBodyAndResponseErrorCode() {
        return Stream.of(
               Arguments.of("", "/bad_request/deserialization_error_unknown.json"),
                Arguments.of("{}", "/bad_request/server/argument_not_valid.json"),
                Arguments.of("{\n" +
                        "    \"typeStrategy\": null,\n" +
                        "    \"serverManagerName\":null,\n" +
                        "    \"username\":null,\n" +
                        "    \"password\":null,\n" +
                        "    \"port\":null,\n" +
                        "    \"ssManagerId\":null\n" +
                        "}", "/bad_request/server/argument_not_valid.json")
        );
    }
}
