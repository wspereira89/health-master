package com.spc.healthmaster.services;

import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.repository.ApplicationRepository;
import com.spc.healthmaster.ssh.SshManagerComposite;
import com.spc.healthmaster.strategy.CommandStrategy;

import java.util.List;


public class CommandService {

    private final List<CommandStrategy> commandStrategies;
    private final SshManagerComposite sshManagerComposite;
    private final ApplicationRepository applicationRepository;

    public CommandService(
            final SshManagerComposite sshManagerComposite,
            final List<CommandStrategy> commandStrategies,
            final ApplicationRepository applicationRepository
    ) {
        this.commandStrategies = commandStrategies;
        this.sshManagerComposite = sshManagerComposite;
        this.applicationRepository = applicationRepository;
    }

    public void start(final TypeStrategy strategy, final String appId) throws Exception {
        final Aplication aplication = applicationRepository
                .getApplication(appId)
                .orElseThrow(()-> new Exception(""));
        final CommandStrategy command = this.commandStrategies
                .stream()
                .filter(x-> strategy.equals(x))
                .findFirst()
                .orElseThrow(()-> new Exception(""));
        command.start(sshManagerComposite.getSshManagerMapById(aplication.getServerId()), aplication);


    }

    public void stop(final TypeStrategy strategy, final String appId) throws Exception {
        final Aplication aplication = applicationRepository
                .getApplication(appId)
                .orElseThrow(()-> new Exception(""));
        final CommandStrategy command = this.commandStrategies
                .stream()
                .filter(x-> strategy.equals(x))
                .findFirst()
                .orElseThrow(()-> new Exception(""));
        command.stop(sshManagerComposite.getSshManagerMapById(aplication.getServerId()), aplication);


    }

    public boolean getStatus(final TypeStrategy strategy, final String appId) throws Exception {
        final Aplication aplication = applicationRepository
                .getApplication(appId)
                .orElseThrow(()-> new Exception(""));
        final CommandStrategy command = this.commandStrategies
                .stream()
                .filter(x-> strategy.equals(x))
                .findFirst()
                .orElseThrow(()-> new Exception(""));
       return command.status(sshManagerComposite.getSshManagerMapById(aplication.getServerId()), aplication);
    }
}
