package com.spc.healthmaster.services.commands;

import com.spc.healthmaster.command.CommandAction;
import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.Actions;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.ApplicationRepository;
import com.spc.healthmaster.services.ssh.SshManagerComposite;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;

@Service
public class CommandServiceImpl implements CommandService {

    private final Map<Actions, CommandAction> commandActions;
    private final SshManagerComposite sshManagerComposite;
    private final ApplicationRepository applicationRepository;
    private final List<CommandStrategy> commandStrategies;

    public CommandServiceImpl(
            final SshManagerComposite sshManagerComposite,
            final ApplicationRepository applicationRepository,
            final List<CommandStrategy> commandStrategies,
            final Map<Actions, CommandAction> commandActions
    ) {
        this.sshManagerComposite = sshManagerComposite;
        this.applicationRepository = applicationRepository;
        this.commandStrategies = commandStrategies;
        this.commandActions = commandActions;
    }

    @Override
    public String executeCommand(final CommandRequestDto commandRequestDto) throws ApiException{
        final Aplication aplication = applicationRepository
                .getApplication(commandRequestDto.getApplication())
                .orElseThrow(()-> notFoundApplication(commandRequestDto.getApplication()).toException());

        final SshManagerDto manager = this.sshManagerComposite.getSshManagerMapById(commandRequestDto.getServerId());

        final CommandStrategy command = this.commandStrategies
                .stream()
                .filter(commandStrategy-> commandRequestDto.getTypeStrategy().equals(commandStrategy.getType()))
                .findFirst()
                .orElseThrow(STRATEGY_NOT_FOUND::toException);
        final CommandAction commandAction = Optional.ofNullable(commandActions.get(commandRequestDto.getCommand()))
                .orElseThrow(COMMAND_NOT_FOUND::toException);
        return commandAction.execute( command, manager, aplication);
    }
}
