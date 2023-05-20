package com.spc.healthmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spc.healthmaster.dtos.ApiError;
import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.services.commands.CommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;
import static com.spc.healthmaster.util.JsonLoader.loadObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommandRestController.class)
public class CommandControllerTest {


    private static final String VALID_JSON_REQUEST =  "{\"command\":\"START\",\"typeStrategy\":\"SPRING_BOOT_APP\",\"serverId\":\"serverId\",\"application\":\"application\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandService commandService;

    @ParameterizedTest
    @MethodSource("badBodyAndResponseErrorCode")
    public void givenInvalidBodyWhenCallExecuteCommandThenReturnBadRequest(final String badBody, final String path) throws Exception {
        final ApiError apiError = loadObject(path, ApiError.class);
        mockMvc
                .perform(post("/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiError,  stringToApiError(response));
                });
    }

    private ApiError stringToApiError(final String errorAsString) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(errorAsString, ApiError.class);
    }

    private static Stream<Arguments> badBodyAndResponseErrorCode() {
        return Stream.of(
                Arguments.of("", "/bad_request/deserialization_error_unknown.json"),
                Arguments.of("{}", "/bad_request/argument_not_valid.json"),
                Arguments.of("{\"command\":null,\"typeStrategy\":null,\"serverId\":null,\"application\":null }", "/bad_request/argument_not_valid.json"),
                Arguments.of( "{\"command\":\"\",\"typeStrategy\":\"\",\"serverId\":\"\",\"application\":\"\" }", "/bad_request/deserialization_error_command_action_enum.json"),
                Arguments.of("{\"command\":\"type\",\"typeStrategy\":\"type\",\"serverId\":\"serverId\",\"application\":\"aplicationId\" }", "/bad_request/deserialization_error_command_action_enum.json"),
                Arguments.of("{\"command\":\"start\",\"typeStrategy\":\"spring_boot_app\",\"serverId\":\"serverId\",\"application\":\"aplicationId\" }", "/bad_request/deserialization_error_command_action_enum.json"),
                Arguments.of("{\"command\":\"START\",\"typeStrategy\":\"spring_boot_app\",\"serverId\":\"serverId\",\"application\":\"aplicationId\" }", "/bad_request/deserialization_error_typeStrategy_enum.json")
        );
    }

    @ParameterizedTest
    @MethodSource("apiErrorAndResponseProvider")
    public void givenValidRequestWhenExecuteCommandThenReturnApiException(final ApiError apiError, final String errorCode) throws Exception {
        given(commandService.executeCommand(any())).willThrow(apiError.toException());
        mockMvc.perform(post("/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\""+errorCode+"\"}"));
    }

    private static Stream<Arguments> apiErrorAndResponseProvider() {
        return Stream.of(
                Arguments.of(notFoundApplication(""), "not_found_application"),
                Arguments.of(notFoundConnectionSsh(""), "not_found_server"),
                Arguments.of(STRATEGY_NOT_FOUND, "strategy_not_found"),
                Arguments.of(COMMAND_NOT_FOUND, "command_not_found"),
                Arguments.of(ALREADY_INITIALIZED, "already_initialized"),
                Arguments.of(ALREADY_STOPPED, "already_stopped"),
                Arguments.of(sshException("", ""), "ssh_connection")
        );
    }

    @Test
    public void givenValidRequestWhenExecuteCommandThenReturnOk() throws Exception {
        // Mockear la dependencia del controlador utilizando Mockito

        // Configurar el comportamiento esperado del comandoServiceMock
        Mockito.when(commandService.executeCommand(Mockito.any(CommandRequestDto.class)))
                .thenReturn("Success");

        // Configurar RestAssured y enviar la solicitud al controlador
        mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_JSON_REQUEST))
                .andExpect(status().isOk());

        // Verificar que el método executeCommand del comandoServiceMock haya sido llamado
        Mockito.verify(commandService, Mockito.times(1))
                .executeCommand(Mockito.any(CommandRequestDto.class));
    }
}
