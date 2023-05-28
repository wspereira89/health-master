package com.spc.healthmaster.services.commands;

import com.google.common.collect.ImmutableMap;
import com.spc.healthmaster.command.CommandAction;
import com.spc.healthmaster.dtos.*;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.Action;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.ServerManagerRepository;
import com.spc.healthmaster.services.ssh.SshManagerComposite;
import com.spc.healthmaster.services.websoket.MessageService;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.spc.healthmaster.constants.Constants.*;
import static com.spc.healthmaster.factories.ApiErrorFactory.*;

@Service
public class CommandServiceImpl implements CommandService {

    private final Map<Action, CommandAction> commandActions;
    private final SshManagerComposite sshManagerComposite;
    private final ServerManagerRepository serverManagerRepository;
    private final List<CommandStrategy> commandStrategies;
    private final MessageService messageService;
    private final Map<String, Status>  statusMap =
            ImmutableMap.of(
                    ALREADY_INITIALIZED_STATUS, Status.RUNNING,
                    ALREADY_STOPPED_STATUS, Status.STOPPED,
                    SSH_CONNECTION_STATUS, Status.UNDEFINED
            );

    public CommandServiceImpl(
            final SshManagerComposite sshManagerComposite,
            final ServerManagerRepository serverManagerRepository,
            final List<CommandStrategy> commandStrategies,
            final Map<Action, CommandAction> commandActions,
            final MessageService messageService
    ) {
        this.sshManagerComposite = sshManagerComposite;
        this.serverManagerRepository = serverManagerRepository;
        this.commandStrategies = commandStrategies;
        this.commandActions = commandActions;
        this.messageService = messageService;

    }

    @Override
    public ResponseDto executeCommand(final CommandRequestDto commandRequestDto) throws ApiException {
        final ServerManager serverManager = serverManagerRepository.findById(commandRequestDto.getServerManagerId())
                .orElseThrow(()-> notFoundApplication(commandRequestDto.getServerManagerId()).toException());

        final SshManagerDto manager = this.sshManagerComposite.getSshManagerMapById(commandRequestDto.getSshManagerId());

        final CommandStrategy command = this.commandStrategies
                .stream()
                .filter(commandStrategy-> commandRequestDto.getTypeStrategy().equals(commandStrategy.getType()))
                .findFirst()
                .orElseThrow(STRATEGY_NOT_FOUND::toException);
        final CommandAction commandAction = Optional.ofNullable(commandActions.get(commandRequestDto.getCommand()))
                .orElseThrow(COMMAND_NOT_FOUND::toException);
        final NotificationDto.NotificationDtoBuilder notificationDto = NotificationDto
                .builder()
                .typeStrategy(commandRequestDto.getTypeStrategy())
                .action(commandRequestDto.getCommand())
                .applicationId(serverManager.getId())
                .serverName(manager.getServerName());
        try {
            final WrapperExecute wrapper = WrapperExecute.builder()
                    .pathFile(commandRequestDto.getPathFile())
                    .sshManagerDto(manager)
                    .serverManager(serverManager)
                    .commandStrategy(command)
                    .build();
             final ResponseDto response = commandAction.execute(wrapper);
             response.getStatus()
                     .ifPresent(status -> {
                        notificationDto.status(status);
                        this.messageService.sendMessageToTopic(notificationDto.build());
                     });
             return response;
        } catch (final ApiException apiException) {
            notificationDto.status(statusMap.getOrDefault(apiException.getError(), Status.UNDEFINED));
            this.messageService.sendMessageToTopic(notificationDto.build());
            throw apiException;
        }
    }


}
