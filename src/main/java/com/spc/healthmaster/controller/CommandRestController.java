package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.ApiErrorDto;
import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.dtos.ResponseDto;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.spc.healthmaster.constants.ErrorConstants.*;

@RestController
@RequestMapping("/command")
public class CommandRestController {

    private final CommandService commandService;

    public CommandRestController(final CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping()
    @Operation(summary = "Ejecutar comando sobre aplicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "Arguments not valid", value = ERROR_400_ARGUMENTS_NOT_VALID),
                    @ExampleObject(name = "Json Deserialization command action", value = ERROR_400_DESERIALIZATION_COMMAND),
                    @ExampleObject(name = "Json Deserialization Type Strategy", value = ERROR_400_DESERIALIZATION_TYPE_STRATEGY),
                    @ExampleObject(name = "Json Deserialization Unknown", value = ERROR_400_DESERIALIZATION_UNKNOWN)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(value = "{\"error\":\"internal_server_error\",\"message\":\"Internal server error\",\"status\":500,\"expected\":true,\"causes\":[]}")
            }))
    })
    public ResponseEntity<?> executeCommand(@Valid @RequestBody CommandRequestDto commandRequestDto) throws ApiException {
        final ResponseDto response = this.commandService.executeCommand(commandRequestDto);
        if (response.getDownload().isPresent()) {
            byte[] fileBytes = response.getDownload().get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", commandRequestDto.getNameFile()+".txt");
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
