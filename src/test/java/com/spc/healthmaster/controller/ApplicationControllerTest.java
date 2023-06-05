package com.spc.healthmaster.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spc.healthmaster.dtos.error.ApiErrorDto;
import com.spc.healthmaster.dtos.request.ApplicationRequestResponseDto;
import com.spc.healthmaster.services.application.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationControllerTest {


    private static final String GET_DELETE_PATH = "/application/id/{id}";
    private static final String PATH_BASE = "/application";
    private static final Long id = 0L;
    private static final Long applicationId =1l;
    private static final String VALID_BODY ="{\n" +
            "   \"applicationName\": \"GEOSERVER\",\n" +
            "   \"pathFile\":\"\",\n" +
            "   \"jmxPort\" : 439,\n" +
            "    \"memory\": \"\",\n" +
            "    \"serverManagerId\" :0\n" +
            "}";

    private static final String EDIT_VALID_BODY =
            "{\n" +
                    "   \"applicationName\": \"GEOSERVER\",\n" +
                    "   \"pathFile\":\"\",\n" +
                    "   \"jmxPort\" : 439,\n" +
                    "    \"memory\": \"\",\n" +
                    "    \"serverManagerId\" :0,\n" +
                    "   \"id\":1\n" +
                    "}";

    private  final ApiErrorDto FAILED_CONVERT = METHOD_ARGUMENT_NOT_VALID.withCause("convert", "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"a\"");


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;


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
        doThrow(apiErrorDto.toException()).when(applicationService).delete(id);
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

    @ParameterizedTest
    @ValueSource(longs = { 1l, 0L })
    public void whenCallGetAllServerReturnEmpty(long id) throws Exception {
        when(applicationService.findAllApplicationByServerManagerId(any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get(GET_DELETE_PATH, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @ParameterizedTest
    @ValueSource(longs = { 1l, 0L })
    public void whenCallGetAllServerReturnList(long id) throws Exception {
        final List<ApplicationRequestResponseDto> mockSshManagers = Arrays.asList(
                new ApplicationRequestResponseDto( 1l, "application","path",0,"",id),
                new ApplicationRequestResponseDto( 2l, "application","path",0,"",id)
        );
        when(applicationService.findAllApplicationByServerManagerId(any())).thenReturn(mockSshManagers);
        final MvcResult result =  mockMvc.perform(get(GET_DELETE_PATH, id))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = result.getResponse().getContentAsString();
        final List<ApplicationRequestResponseDto> responseDtoList = new ObjectMapper().readValue(responseBody, new TypeReference<List<ApplicationRequestResponseDto>>() {});

        // Realizar las aserciones sobre la lista de objetos
        assertEquals(2, responseDtoList.size());
        assertEquals(1l, responseDtoList.get(0).getId());
        assertEquals(id, responseDtoList.get(0).getServerManagerId());
        assertEquals(2l, responseDtoList.get(1).getId());
        assertEquals(id, responseDtoList.get(1).getServerManagerId());
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
    public void givenAlreadyExistApplicationWhenCallSaveThenReturnBadRequest() throws Exception {
        final ApiErrorDto apiErrorDto = alreadyExistApplication("");
        doThrow(apiErrorDto.toException()).when(applicationService).save(any());
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
    public void givenServerNotFoundWhenCallSaveThenReturnBadRequest() throws Exception {
        final ApiErrorDto apiErrorDto = notFoundServerManager(1l);
        doThrow(apiErrorDto.toException()).when(applicationService).save(any());
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
        doThrow(apiErrorDto.toException()).when(applicationService).save(any());
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
        doNothing().when(applicationService).save(any());
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
    void whenCallEditThenReturnNotFoundApplicationApiException() throws Exception {
        final ApiErrorDto apiErrorDto = notFoundApplication(1l);
        doThrow(apiErrorDto.toException()).when(applicationService).edit(any());
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
    void whenCallEditThenReturnNotFoundServerManagerApiException() throws Exception {
        final ApiErrorDto apiErrorDto = notFoundServerManager(1l);
        doThrow(apiErrorDto.toException()).when(applicationService).edit(any());
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
    void whenCallEditThenReturnAlreadyExistApplicationApiException() throws Exception {
        final ApiErrorDto apiErrorDto = alreadyExistApplication("");
        doThrow(apiErrorDto.toException()).when(applicationService).edit(any());
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
        doThrow(apiErrorDto.toException()).when(applicationService).edit(any());
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
        doNothing().when(applicationService).edit(any());
        mockMvc
                .perform(put(PATH_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EDIT_VALID_BODY))
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> badBodyAndResponseErrorCodeEdit() {
        return Stream.of(
                Arguments.of("", "/bad_request/deserialization_error_unknown.json"),
                Arguments.of("{}", "/bad_request/application/argument_not_valid.json"),
                Arguments.of("{\n" +
                        "   \"applicationName\":null,\n" +
                        "   \"pathFile\":null,\n" +
                        "   \"jmxPort\" : null,\n" +
                        "    \"memory\": null,\n" +
                        "    \"serverManagerId\" :null\n" +
                        "}", "/bad_request/application/argument_not_valid.json"),
                Arguments.of("{\n" +
                        "   \"id\":\"x\",\n" +
                        "   \"applicationName\":null,\n" +
                        "   \"pathFile\":null,\n" +
                        "   \"jmxPort\" : null,\n" +
                        "    \"memory\": null,\n" +
                        "    \"serverManagerId\" :null\n" +
                        "}", "/bad_request/application/deserialization_error_unknown_id.json")
        );
    }

    private static Stream<Arguments> badBodyAndResponseErrorCode() {
        return Stream.of(
                Arguments.of("", "/bad_request/deserialization_error_unknown.json"),
                Arguments.of("{}", "/bad_request/application/argument_not_valid.json"),
                Arguments.of("{\n" +
                        "   \"applicationName\":null,\n" +
                        "   \"pathFile\":null,\n" +
                        "   \"jmxPort\" : null,\n" +
                        "    \"memory\": null,\n" +
                        "    \"serverManagerId\" :null\n" +
                        "}", "/bad_request/application/argument_not_valid.json")
        );
    }
}
