package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.ssh.SshManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/id")
    public void deleteById(@PathVariable Long id) {
        sshManagerService.deleteShhManager(id);
    }

    @PostMapping()
    public void save(@RequestBody SshManagerDto sshManagerDto) throws ApiException {
        sshManagerService.saved(sshManagerDto);
    }

    @PutMapping()
    public void edit(@RequestBody SshManagerDto sshManagerDto) throws ApiException {
        this.sshManagerService.edit(sshManagerDto);
    }

}
