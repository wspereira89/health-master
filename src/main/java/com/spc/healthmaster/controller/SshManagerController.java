package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.services.ssh.SshManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/server")
public class SshManagerController {
    private final SshManagerService sshManagerService;

    public SshManagerController(final SshManagerService sshManagerService) {
        this.sshManagerService = sshManagerService;
    }

    @GetMapping()
    public ResponseEntity<List<SshManagerDto>> getAllServer(){
         return ResponseEntity.ok(sshManagerService.getListSshManager());
    }

}
