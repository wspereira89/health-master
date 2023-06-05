package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.error.ApiErrorDto;
import com.spc.healthmaster.dtos.request.CommandRequestDto;
import com.spc.healthmaster.dtos.request.response.ResponseDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.commands.CommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.spc.healthmaster.constants.swagger.GeneralConstant.ERROR_400_DESERIALIZATION_UNKNOWN;
import static com.spc.healthmaster.constants.swagger.CommandResponseConstant.*;
import static com.spc.healthmaster.constants.swagger.CommandRequestConstant.*;

@RestController
@RequestMapping("/command")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = {
                @ExampleObject(name = "RUNNING", value = COMMAND_RESPONSE_SUCCESS_RUNNING),
                @ExampleObject(name = "STOPPED", value = COMMAND_RESPONSE_SUCCESS_STOPPED),
                @ExampleObject(name = "UNDEFINED", value = COMMAND_RESPONSE_SUCCESS_UNDEFINED),
                @ExampleObject(name = "VIEW LOG", value = COMMAND_RESPONSE_SUCCESS_LOG)
        })),
        @ApiResponse(responseCode = "204", description = "Success", content = {
                @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", format = "binary"), examples = @ExampleObject(value = "Archivo TXT descargable"))
        }),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                @ExampleObject(name = "Arguments not valid", value = COMMAND_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID),
                @ExampleObject(name = "Json Deserialization command action", value = COMMAND_ERROR_400_DESERIALIZATION_COMMAND),
                @ExampleObject(name = "Json Deserialization Type Strategy", value = COMMAND_ERROR_400_DESERIALIZATION_TYPE_STRATEGY),
                @ExampleObject(name = "Json Deserialization Unknown", value = ERROR_400_DESERIALIZATION_UNKNOWN)
        })),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                @ExampleObject(value = "{\"error\":\"internal_server_error\",\"message\":\"Internal server error\",\"status\":500,\"expected\":true,\"causes\":[]}")
        }))
})
public class CommandController {

    private final CommandService commandService;

    public CommandController(final CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping( )
    @Operation(summary = "Ejecutar comando sobre aplicación")
    public ResponseEntity<?> executeCommand(@Valid @RequestBody
                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = COMMAND_DESRIPTION_REQUEST,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(value = REQUEST_START_VALID, name = "Example 1: START VALID",
                                    description = "Este ejemplo te permitira arrancar una aplicación " ),
                            @ExampleObject(value = REQUEST_STOP_VALID, name = "Example 2: STOP VALID",
                                    description = "Este ejemplo te permitira detener una aplicación "),
                            @ExampleObject(value = REQUEST_STATUS_VALID, name = "Example 3: STATUS VALID",
                                    description = "Este ejemplo te permitira saber el estado de una aplicación "),
                            @ExampleObject(value = REQUEST_LOG_VALID, name = "Example 4: LOG VALID",
                                    description = "Este ejemplo te permitira ver la lista de archivos de logs una aplicación "),
                            @ExampleObject(value = REQUEST_DOWNLOAD_VALID, name = "Example 5: DOWNLOAD VALID",
                                    description = "Este ejemplo te permitira descargar un archivo log de una aplicación ")
                    }
            )
    ) CommandRequestDto commandRequestDto) throws ApiException {
        final ResponseDto response = this.commandService.executeCommand(commandRequestDto);
        if (response.getDownload().isPresent()) {
            byte[] fileBytes = response.getDownload().get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", commandRequestDto.getNameFile()+".txt");
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
