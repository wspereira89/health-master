package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.ApiErrorDto;
import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.commands.CommandService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/command")
@Api(tags = "Comandos por Aplicacion")
public class CommandRestController {

    private final CommandService commandService;

    public CommandRestController(final CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping()
    @ApiOperation("Ejecutar commando sobre aplicación")
    /*@ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = Status.class),
            @ApiResponse(code = 400  , message = "Bad request", response = ApiErrorDto.class),
            @ApiResponse(code = 500  , message = "Bad request", response = ApiErrorDto.class),

    })*/
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = Status.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorDto.class, examples = @Example(value = {
                    @ExampleProperty(mediaType = "application/json", value = "{\"error\":\"validation_error\",\"message\":\"Arguments not valid\",\"status\":400,\"expected\":true,\"causes\":[]}")
            })),
            @ApiResponse(code = 500, message = "Internal server error", response = ApiErrorDto.class, examples = @Example(value = {
                    @ExampleProperty(mediaType = "application/json", value = "{\"error\":\"internal_server_error\",\"message\":\"Internal server error\",\"status\":500,\"expected\":true,\"causes\":[]}")
            }))
    })

    public ResponseEntity<Status> executeCommand(@Valid @RequestBody CommandRequestDto commandRequestDto) throws ApiException {
        return ResponseEntity
                .ok(this.commandService.executeCommand(commandRequestDto));
    }


}
