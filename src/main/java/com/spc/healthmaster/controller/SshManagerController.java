package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.RequestServerDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.ssh.SshManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/server")
public class SshManagerController {
    private final SshManagerService sshManagerService;

    public SshManagerController(final SshManagerService sshManagerService) {
        this.sshManagerService = sshManagerService;
    }

    @GetMapping()
    public ResponseEntity<List<RequestServerDto>> getAllServer(){
         return ResponseEntity.ok(sshManagerService.getListSshManager());
    }

    @DeleteMapping("/id/{id}")
    public void deleteById(@Valid @PathVariable("id") Long id) throws ApiException {
        sshManagerService.deleteShhManager(id);
    }

    @PostMapping()
    public void save(@Valid @RequestBody RequestServerDto requestServerDto) throws ApiException {
        sshManagerService.save(requestServerDto);
    }

    @PutMapping()
    public void edit(@RequestBody RequestServerDto requestServerDto) throws ApiException {
        this.sshManagerService.edit(requestServerDto);
    }

}
