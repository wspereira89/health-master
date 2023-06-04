package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.RequestServerDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.ssh.SshManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.spc.healthmaster.factories.ApiErrorFactory.METHOD_ARGUMENT_NOT_VALID;

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
    public void edit(@Valid @RequestBody RequestServerDto requestServerDto)
            throws ApiException {
        if(requestServerDto.getId() ==null ||requestServerDto.getId() ==0l) {
           throw METHOD_ARGUMENT_NOT_VALID.withCause("id", "Invalid Id").toException();
        }

        this.sshManagerService.edit(requestServerDto);
    }

}
