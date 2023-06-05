package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.error.ApiErrorDto;
import com.spc.healthmaster.dtos.request.ApplicationRequestResponseDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.application.ApplicationService;
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

import static com.spc.healthmaster.constants.swagger.ApplicationResponseConstant.*;
import static com.spc.healthmaster.constants.swagger.GeneralConstant.*;
import static com.spc.healthmaster.constants.swagger.ServerResponseConstant.ERROR_404_NOT_FOUND_SERVER_MANAGER;
import static com.spc.healthmaster.factories.ApiErrorFactory.METHOD_ARGUMENT_NOT_VALID;

@RestController
@RequestMapping("/application")
@Tag(name = "ApplicationController ", description = "En este controller podras encontrar todos los metodos para listar, crear,editar y eliminar de la base datos" +
        " la configuracion para una aplicación")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(final ApplicationService applicationService) {
        this.applicationService = applicationService;
    }


    @GetMapping("/id/{id}")
    @Operation(summary = "Obtiene la lista de aplicaciones que estan registrados en la BD")
    public ResponseEntity<List<ApplicationRequestResponseDto>> findAllApplicationByServerManagerId(final @Valid @PathVariable("id")  Long sshManagerId) {
        return ResponseEntity.ok(applicationService.findAllApplicationByServerManagerId(sshManagerId));
    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Eliminar una aplicacion en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(name = "Internal server error", value = ERROR_500_INTERNAL_SERVER),
                    @ExampleObject(name = "Error BD", value = ERROR_500_BD)
            }))
    })
    public void deleteById(@Valid @PathVariable("id") Long id) throws ApiException {
        applicationService.delete(id);
    }

    @PostMapping()
    @Operation(summary = "Crea la configuracion de una aplicacion en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "Arguments not valid", value = RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID),
                    @ExampleObject(name = "already exist application ", value = ERROR_400_ALREADY_EXIST_APPLICATION)
            })),

            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "not_found_server", value = ERROR_404_NOT_FOUND_SERVER_MANAGER)
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
                                    "   \"applicationName\": \"GEOSERVER\",\n" +
                                    "   \"pathFile\":\"\",\n" +
                                    "   \"jmxPort\" : 439,\n" +
                                    "    \"memory\": \"\",\n" +
                                    "    \"serverManagerId\" :0\n" +
                                    "}", name = "Example 1: Applicacion Springboot",
                                    description = "Este ejemplo te permitira Crear una applicacion que corre en servidor distinto a tomcat o glafish "),
                            @ExampleObject(value = "{\n" +
                                    "   \"applicationName\": \"citascaminoes\",\n" +
                                    "   \"pathFile\":\"\",\n" +
                                    "   \"jmxPort\" : 439,\n" +
                                    "    \"memory\": \"\",\n" +
                                    "    \"serverManagerId\" :1\n" +
                                    "}", name = "Example 2: Applicacion Corre en tomcat o glafish",
                                    description = "Este ejemplo te permitira Crear una applicacion que corre en servidor tomcat o glafish, " +
                                            "para esto el  atributo serverManagerId debe ser diferente de 0")
                    }
            )
    ) ApplicationRequestResponseDto request) throws ApiException {
        applicationService.save(request);
    }

    @PutMapping()
    @Operation(summary = "Actualiza la configuracion de una aplicacion en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "Arguments not valid", value = RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID),
                    @ExampleObject(name = "Arguments not valid Id", value = EDIT_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID_ID),
                    @ExampleObject(name = "Json Deserialization Unknown", value = ERROR_400_DESERIALIZATION_UNKNOWN_ID),
                    @ExampleObject(name = "already exist application ", value = ERROR_400_ALREADY_EXIST_APPLICATION)
            })),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorDto.class), examples = {
                    @ExampleObject(name = "not_found_application", value = ERROR_404_NOT_FOUND_APPLICATION),
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
                                    "   \"id\" :1,\n" +
                                    "   \"applicationName\": \"GEOSERVER\",\n" +
                                    "   \"pathFile\":\"\",\n" +
                                    "   \"jmxPort\" : 439,\n" +
                                    "    \"memory\": \"\",\n" +
                                    "    \"serverManagerId\" :0\n" +
                                    "}", name = "Example 1: Applicacion Springboot",
                                    description = "Este ejemplo te permitira Actualizar una applicacion que corre en servidor distinto a tomcat o glafish "),
                            @ExampleObject(value = "{\n" +
                                    "   \"id\" : 2,\n" +
                                    "   \"applicationName\": \"citascaminoes\",\n" +
                                    "   \"pathFile\":\"\",\n" +
                                    "   \"jmxPort\" : 439,\n" +
                                    "    \"memory\": \"\",\n" +
                                    "    \"serverManagerId\" :1\n" +
                                    "}", name = "Example 2: Applicacion Corre en tomcat o glafish",
                                    description = "Este ejemplo te permitira Actualizar una applicacion que corre en servidor tomcat o glafish, " +
                                            "para esto el  atributo serverManagerId debe ser diferente de 0")
                    }
            )
    ) ApplicationRequestResponseDto request)
            throws ApiException {
        if(request.getId() == null || request.getId() ==0l) {
            throw METHOD_ARGUMENT_NOT_VALID.withCause("id", "Invalid Id").toException();
        }

        this.applicationService.edit(request);
    }
}
