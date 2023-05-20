package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.commands.CommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/command")
public class CommandRestController {

    private final CommandService commandService;

    public CommandRestController(final CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping()
    public ResponseEntity<String> executeCommand(@Valid @RequestBody CommandRequestDto commandRequestDto) throws ApiException {
        return ResponseEntity
                .ok(this.commandService.executeCommand(commandRequestDto));
    }


}
