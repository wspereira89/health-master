package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.request.RequestResponseServerManagerDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.server.ServerManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.spc.healthmaster.constants.SwaggerServerdResponseConstants.SERVER_ERROR_500;
import static com.spc.healthmaster.constants.SwaggerServerdResponseConstants.SERVER_ERROR_500_BD;
import static com.spc.healthmaster.factories.ApiErrorFactory.METHOD_ARGUMENT_NOT_VALID;

@RestController
@RequestMapping("/server")
public class ServerManagerController {

    private final ServerManagerService serverManagerService;

    public ServerManagerController(final ServerManagerService serverManagerService) {
        this.serverManagerService = serverManagerService;
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtiene la lista de servidores que estan registrados en la BD")
    public ResponseEntity<List<RequestResponseServerManagerDto>> findAllBySshManagerId(final @PathVariable Long sshManagerId) {
        return ResponseEntity.ok(serverManagerService.findBySshManagerId(sshManagerId));
    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Eliminar un servidor en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = {
                    @ExampleObject(name = "Internal server error", value = SERVER_ERROR_500),
                    @ExampleObject(name = "Error BD", value = SERVER_ERROR_500_BD)
            }))
    })
    public void deleteById(@Valid @PathVariable("id") Long id) throws ApiException {
        serverManagerService.delete(id);
    }

    @PostMapping()
    public void save(@Valid @RequestBody RequestResponseServerManagerDto request) throws ApiException {
        serverManagerService.save(request);
    }

    @PutMapping()
    @Operation(summary = "Actualiza el servidor en la BD")
    public void edit(@Valid @RequestBody RequestResponseServerManagerDto request)
            throws ApiException {
        if(request.getId() ==null || request.getId() ==0l) {
            throw METHOD_ARGUMENT_NOT_VALID.withCause("id", "Invalid Id").toException();
        }

        this.serverManagerService.edit(request);
    }
}
