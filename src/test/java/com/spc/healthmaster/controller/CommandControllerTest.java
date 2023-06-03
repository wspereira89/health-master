package com.spc.healthmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spc.healthmaster.dtos.ApiErrorDto;
import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.services.commands.CommandService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;
import static com.spc.healthmaster.util.JsonLoader.loadObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommandControllerTest {


    private static final String VALID_JSON_REQUEST =  "{\"command\":\"START\",\"typeStrategy\":\"SPRING_BOOT_APP\",\"sshManagerId\":\"1\",\"serverManagerId\":\"1\"}";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private CommandService commandService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @ParameterizedTest
    @MethodSource("badBodyAndResponseErrorCode")
    public void givenInvalidBodyWhenCallExecuteCommandThenReturnBadRequest(final String badBody, final String path) throws Exception {
        final ApiErrorDto apiErrorDto = loadObject(path, ApiErrorDto.class);
        mockMvc
                .perform(post("/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiErrorDto,  stringToApiError(response));
                });
    }

    private ApiErrorDto stringToApiError(final String errorAsString) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(errorAsString, ApiErrorDto.class);
    }

    private static Stream<Arguments> badBodyAndResponseErrorCode() {
        return Stream.of(
                Arguments.of("", "/bad_request/deserialization_error_unknown.json"),
                Arguments.of("{}", "/bad_request/argument_not_valid.json"),
                Arguments.of("{\"command\":null,\"typeStrategy\":null,\"sshManagerId\":null,\"serverManagerId\":null }", "/bad_request/argument_not_valid.json"),
                Arguments.of( "{\"command\":\"\",\"typeStrategy\":\"\",\"sshManagerId\":\"\",\"serverManagerId\":\"\" }", "/bad_request/deserialization_error_command_action_enum.json"),
                Arguments.of("{\"command\":\"type\",\"typeStrategy\":\"type\",\"sshManagerId\":\"1\",\"serverManagerId\":\"1\" }", "/bad_request/deserialization_error_command_action_enum.json"),
                Arguments.of("{\"command\":\"start\",\"typeStrategy\":\"spring_boot_app\",\"sshManagerId\":\"1\",\"serverManagerId\":\"1\" }", "/bad_request/deserialization_error_command_action_enum.json"),
                Arguments.of("{\"command\":\"START\",\"typeStrategy\":\"spring_boot_app\",\"sshManagerId\":\"1\",\"serverManagerId\":\"1\" }", "/bad_request/deserialization_error_typeStrategy_enum.json")
        );
    }

    @ParameterizedTest
    @MethodSource("apiErrorAndResponseProvider")
    public void givenValidRequestWhenExecuteCommandThenReturnApiException(
            final ApiErrorDto apiErrorDto, final String errorCode, final int statusCode
    ) throws Exception {
        given(commandService.executeCommand(any())).willThrow(apiErrorDto.toException());
        mockMvc.perform(post("/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON_REQUEST))
                .andExpect(status().is(statusCode))
                .andExpect(content().json("{\"error\":\""+errorCode+"\"}"));
    }

    private static Stream<Arguments> apiErrorAndResponseProvider() {
        return Stream.of(
                Arguments.of(notFoundApplication(1L), "not_found_application", 400),
                Arguments.of(notFoundConnectionSsh(1L), "not_found_server", 400),
                Arguments.of(STRATEGY_NOT_FOUND, "strategy_not_found", 400),
                Arguments.of(COMMAND_NOT_FOUND, "command_not_found", 400),
                Arguments.of(alreadyInitializedException(""), "already_initialized", 400),
                Arguments.of(alreadyStoppedException(""), "already_stopped", 400),
                Arguments.of(sshException("", ""), "ssh_connection", 502)
        );
    }

    @Test
    public void givenValidRequestWhenExecuteCommandThenReturnOk() throws Exception {

        when(commandService.executeCommand(any(CommandRequestDto.class)))
                .thenReturn(ResponseDto.builder().status(Status.RUNNING).build());

        // Configurar RestAssured y enviar la solicitud al controlador
        mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_JSON_REQUEST))
                .andExpect(status().isOk());

        verify(commandService, times(1)).executeCommand(any(CommandRequestDto.class));
    }
}
