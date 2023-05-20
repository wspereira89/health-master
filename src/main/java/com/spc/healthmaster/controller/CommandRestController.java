package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.commands.CommandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/command")
@Api(tags = "Example API")
public class CommandRestController {

    private final CommandService commandService;

    public CommandRestController(final CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping()
    @ApiOperation("Get user details")
    public ResponseEntity<Status> executeCommand(@Valid @RequestBody CommandRequestDto commandRequestDto) throws ApiException {
        return ResponseEntity
                .ok(this.commandService.executeCommand(commandRequestDto));
    }


}
