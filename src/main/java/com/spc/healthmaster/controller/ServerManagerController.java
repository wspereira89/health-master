package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.error.ApiErrorDto;
import com.spc.healthmaster.dtos.request.RequestResponseServerManagerDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.server.ServerManagerService;
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

import static com.spc.healthmaster.constants.swagger.GeneralConstant.*;
import static com.spc.healthmaster.constants.swagger.ServerResponseConstant.*;
import static com.spc.healthmaster.constants.swagger.SshManagerResponseConstants.*;
import static com.spc.healthmaster.factories.ApiErrorFactory.METHOD_ARGUMENT_NOT_VALID;

@RestController
@RequestMapping("/server")
@Tag(name = "ServerManagerController", description = "En este controller podras encontrar todos los metodos para listar, crear,editar y eliminar de la base datos" +
        " la configuracion para conectarte a un servidor (Tomcat opr Glassfish)")
public class ServerManagerController {

    private final ServerManagerService serverManagerService;

    public ServerManagerController(final ServerManagerService serverManagerService) {
        this.serverManagerService = serverManagerService;
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtiene la lista de servidores que estan registrados en la BD")
    public ResponseEntity<List<RequestResponseServerManagerDto>> findAllBySshManagerId(final @Valid @PathVariable("id")  Long sshManagerId) {
        return ResponseEntity.ok(serverManagerService.findServerManagerBySshManagerId(sshManagerId));
    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Eliminar un servidor en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(name = "Internal server error", value = ERROR_500_INTERNAL_SERVER),
                    @ExampleObject(name = "Error BD", value = ERROR_500_BD)
            }))
    })
    public void deleteById(@Valid @PathVariable("id") Long id) throws ApiException {
        serverManagerService.delete(id);
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "Arguments not valid", value = RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID),
                    @ExampleObject(name = "already exist server manager ", value = ERROR_400_ALREADY_EXIST_SERVER_MANAGER)
            })),

            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "not_found_sshManager", value = ERROR_404_NOT_FOUND_SSHMANAGER)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(name = "Internal server error", value = ERROR_500_INTERNAL_SERVER),
                    @ExampleObject(name = "Error BD", value = ERROR_500_BD)
            }))
    })
    public void save(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(value = "{\n" +
                                    "   \"typeStrategy\":\"GLASSFISH_SERVER\",\n" +
                                    "   \"serverManagerName\":\"Glasfish gpcdomain\",\n" +
                                    "   \"username\":\"gpcdomain4\",\n" +
                                    "   \"password\":\"xxxxx\",\n" +
                                    "   \"port\":\"4408\",\n" +
                                    "   \"ssManagerId\":1\n" +
                                    "}", name = "Example 1: Glafish gpcdomain",
                                    description = "Este ejemplo te permitira Crear la configuración un servidor ")
                    }
            )
    ) RequestResponseServerManagerDto request) throws ApiException {
        serverManagerService.save(request);
    }

    @PutMapping()
    @Operation(summary = "Actualiza la configuracion del servidor en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "Arguments not valid", value = RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID),
                    @ExampleObject(name = "Arguments not valid Id", value = EDIT_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID_ID),
                    @ExampleObject(name = "Json Deserialization Unknown", value = ERROR_400_DESERIALIZATION_UNKNOWN_ID),
                    @ExampleObject(name = "already exist server ", value = ERROR_400_ALREADY_EXIST_SSHMANAGER)
            })),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "not_found_sshManager", value = ERROR_404_NOT_FOUND_SSHMANAGER),
                    @ExampleObject(name = "not_found_server", value = ERROR_404_NOT_FOUND_SERVER_MANAGER)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(name = "Internal server error", value = ERROR_500_INTERNAL_SERVER),
                    @ExampleObject(name = "Error BD", value = ERROR_500_BD)
            }))
    })
    public void edit(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(value = "{\n" +
                                    "   \"typeStrategy\":\"GLASSFISH_SERVER\",\n" +
                                    "   \"serverManagerName\":\"Glasfish gpcdomain\",\n" +
                                    "   \"username\":\"gpcdomain4\",\n" +
                                    "   \"password\":\"xxxxx\",\n" +
                                    "   \"port\":\"4408\",\n" +
                                    "   \"ssManagerId\":1,\n" +
                                    "   \"id\":1\n" +
                                    "}", name = "Example 1: Glafish gpcdomain",
                                    description = "Este ejemplo te permitira Editar la configuración un servidor ")
                    }
            )
    )RequestResponseServerManagerDto request)
            throws ApiException {
        if(request.getId() ==null || request.getId() ==0l) {
            throw METHOD_ARGUMENT_NOT_VALID.withCause("id", "Invalid Id").toException();
        }

        this.serverManagerService.edit(request);
    }
}
