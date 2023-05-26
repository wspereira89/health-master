package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.ApiErrorDto;
import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.commands.CommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    /*@ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = Status.class),
            @ApiResponse(code = 400  , message = "Bad request", response = ApiErrorDto.class),
            @ApiResponse(code = 500  , message = "Bad request", response = ApiErrorDto.class),

    })*/
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
    public ResponseEntity<Status> executeCommand(@Valid @RequestBody CommandRequestDto commandRequestDto) throws ApiException {
        return ResponseEntity
                .ok(this.commandService.executeCommand(commandRequestDto));
    }


}
