package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.error.ApiErrorDto;
import com.spc.healthmaster.dtos.request.RequestResponseSshManagerDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.ssh.SshManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.spc.healthmaster.constants.SwaggerServerdResponseConstants.*;
import static com.spc.healthmaster.factories.ApiErrorFactory.METHOD_ARGUMENT_NOT_VALID;

@RestController
@RequestMapping("/sshConnection")
@Tag(name = "SshManagerController", description = "En este controller podras encontrar todos los metodos para listar, crear,editar y eliminar de la base datos" +
        " la configuracion ssh para conectarte a un servidor ")
public class SshManagerController {
    private final SshManagerService sshManagerService;

    public SshManagerController(final SshManagerService sshManagerService) {
        this.sshManagerService = sshManagerService;
    }

    @GetMapping()
    @Operation(summary = "Obtiene la lista de conexiones ssh que estan registrados en la BD")
    public ResponseEntity<List<RequestResponseSshManagerDto>> findAll(){
         return ResponseEntity.ok(sshManagerService.findAll());
    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Eliminar una conexion ssh en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(name = "Internal server error", value = SERVER_ERROR_500),
                    @ExampleObject(name = "Error BD", value = SERVER_ERROR_500_BD)
            }))
    })
    public void deleteById(@Valid @PathVariable("id") Long id) throws ApiException {
        sshManagerService.delete(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "Arguments not valid", value = SERVER_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID),
                    @ExampleObject(name = "already exist sshManager ", value = SERVER_ERROR_400_ALREADY_EXIST_SSHMANAGER)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(name = "Internal server error", value = SERVER_ERROR_500),
                    @ExampleObject(name = "Error BD", value = SERVER_ERROR_500_BD)
            })),
            @ApiResponse(responseCode = "502", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "ssh_connection", value = SERVER_ERROR_502_CONNECTION_SSH)
            }))
    })
    @PostMapping()
    public void save(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(value = "{\"serverName\":\"Dev - processos\",  \"host\":\"10.18.100.30\", \"user\":\"usrprocesos\", \"password\":\"pass123\"}", name = "Example 1: Dev - procces",
                                    description = "Este ejemplo te permitira Crear un servidor ")
                    }
            )
    ) RequestResponseSshManagerDto requestResponseSshManagerDto) throws ApiException {
        sshManagerService.save(requestResponseSshManagerDto);
    }

    @PutMapping()
    @Operation(summary = "Actualiza la conexion shh en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "Arguments not valid", value = SERVER_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID),
                    @ExampleObject(name = "Arguments not valid Id", value = EDIT_SERVER_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID_ID),
                    @ExampleObject(name = "Json Deserialization Unknown", value = EDIT_SERVER_ERROR_400_DESERIALIZATION_UNKNOWN),
                    @ExampleObject(name = "already exist server ", value = SERVER_ERROR_400_ALREADY_EXIST_SSHMANAGER)
            })),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "not_found_sshManager", value = EDIT_SERVER_ERROR_404_NOT_FOUND_SSHMANAGER)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(name = "Internal server error", value = SERVER_ERROR_500),
                    @ExampleObject(name = "Error BD", value = SERVER_ERROR_500_BD)
            })),
            @ApiResponse(responseCode = "502", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "ssh_connection", value = SERVER_ERROR_502_CONNECTION_SSH)
            }))
    })
    public void edit(@Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                       mediaType = MediaType.APPLICATION_JSON_VALUE,
                       examples = {
                               @ExampleObject(value = "{\"serverName\":\"Dev - processos\", \"id\":1, \"host\":\"10.18.100.30\", \"user\":\"usrprocesos\", \"password\":\"pass123\"}", name = "Example 1: Dev - procces",
                                       description = "Este ejemplo te permitira Editar un server ")
                       }
                    )
            ) RequestResponseSshManagerDto requestResponseSshManagerDto)
            throws ApiException {
        if(requestResponseSshManagerDto.getId() ==null || requestResponseSshManagerDto.getId() ==0l) {
           throw METHOD_ARGUMENT_NOT_VALID.withCause("id", "Invalid Id").toException();
        }

        this.sshManagerService.edit(requestResponseSshManagerDto);
    }

}
