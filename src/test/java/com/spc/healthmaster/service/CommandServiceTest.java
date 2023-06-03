package com.spc.healthmaster.service;

import com.spc.healthmaster.command.CommandAction;
import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.Action;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.ApplicationRepository;
import com.spc.healthmaster.repository.ServerManagerRepository;
import com.spc.healthmaster.services.commands.CommandService;
import com.spc.healthmaster.services.commands.CommandServiceImpl;
import com.spc.healthmaster.services.ssh.SshManagerComposite;
import com.spc.healthmaster.services.websoket.MessageService;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;

import java.util.*;
import java.util.stream.Stream;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CommandServiceTest {

    private CommandService commandService;
    @Mock private Map<Action, CommandAction> commandActions;
    @Mock private SshManagerComposite sshManagerComposite;
    @Mock private ServerManagerRepository serverManagerRepository;
    @Mock private List<CommandStrategy> commandStrategies;
    @Mock private MessageService messageService;
    @Mock private ApplicationRepository applicationRepository;

    @BeforeEach
    public void setup() {
        openMocks(this);
        commandService =
                new CommandServiceImpl(sshManagerComposite, serverManagerRepository, commandStrategies, commandActions, messageService, applicationRepository);
    }

    @Test
    public void givenApplicationInvalidWhenCallExecuteCommandThenWillReturnApiExceptionNotFound() {
        when(serverManagerRepository.findById(any())).thenReturn(Optional.empty());
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(mock(CommandRequestDto.class))
        );
        assertEquals("not_found_application", thrownException.getError());
    }

    @Test
    public void givenServerIdInvalidWhenCallExecuteCommandThenWillReturnApiExceptionNotFound() throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        when(serverManagerRepository.findById(any())).thenReturn(Optional.of(mock(ServerManager.class)));
        when(sshManagerComposite.getSshManagerMapById(any()))
                .thenThrow(notFoundConnectionSsh(1L).toException());
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(commandRequestDto)
        );
        assertEquals("not_found_server", thrownException.getError());
    }

    @Test
    public void givenStrategyInvalidWhenCallExecuteCommandThenWillReturnApiExceptionNotFound() throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(TypeStrategy.SPRING_BOOT_APP);
        when(serverManagerRepository.findById(any())).thenReturn(Optional.of(mock(ServerManager.class)));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(mock(SshManagerDto.class));
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(TypeStrategy.TOMCAT_SERVER);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(commandRequestDto)
        );
        assertEquals("strategy_not_found", thrownException.getError());
    }

    @ParameterizedTest
    @EnumSource(TypeStrategy.class)
    public void givenCommandInvalidWhenCallExecuteCommandThenWillReturnApiExceptionNotFound(final TypeStrategy typeStrategy) throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(typeStrategy);
        when(serverManagerRepository.findById(any())).thenReturn(Optional.of(mock(ServerManager.class)));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(mock(SshManagerDto.class));
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(typeStrategy);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(commandRequestDto)
        );
        assertEquals("command_not_found", thrownException.getError());
    }

    @ParameterizedTest
    @EnumSource(TypeStrategy.class)
    public void givenCommandStartWhenCallExecuteCommandThenWillReturnApiExceptionAlreadyInitialized(final TypeStrategy typeStrategy) throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        final ServerManager serverManager = mock(ServerManager.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(typeStrategy);
        when(serverManagerRepository.findById(any())).thenReturn(Optional.of(serverManager));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(sshManagerDto);
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(typeStrategy);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final CommandAction commandAction = mock(CommandAction.class);
        when(commandActions.get(any())).thenReturn(commandAction);
        when(commandAction.execute(any()))
                .thenThrow(alreadyInitializedException("").toException());
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(commandRequestDto)
        );
        assertEquals("already_initialized", thrownException.getError());
    }

    @ParameterizedTest
    @EnumSource(TypeStrategy.class)
    public void givenCommandStartWhenCallExecuteCommandThenWillReturnApiExceptionAlreadyStopped(final TypeStrategy typeStrategy) throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        final ServerManager serverManager = mock(ServerManager.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(typeStrategy);
        when(serverManagerRepository.findById(any())).thenReturn(Optional.of(serverManager));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(sshManagerDto);
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(typeStrategy);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final CommandAction commandAction = mock(CommandAction.class);
        when(commandActions.get(any())).thenReturn(commandAction);
        when(commandAction.execute(any()))
                .thenThrow(alreadyStoppedException("").toException());
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(commandRequestDto)
        );
        assertEquals("already_stopped", thrownException.getError());
    }

    @ParameterizedTest
    @EnumSource(TypeStrategy.class)
    public void givenConnectionFailedWhenCallExecuteCommandThenWillReturnApiException(final TypeStrategy typeStrategy)
            throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        final ServerManager serverManager = mock(ServerManager.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(typeStrategy);
        when(serverManagerRepository.findById(any())).thenReturn(Optional.of(serverManager));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(sshManagerDto);
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(typeStrategy);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final CommandAction commandAction = mock(CommandAction.class);
        when(commandActions.get(any())).thenReturn(commandAction);
        when(commandAction.execute(any()))
                .thenThrow(sshException("", "").toException());
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(commandRequestDto)
        );
        assertEquals("ssh_connection", thrownException.getError());
    }

    // Método estático que proporciona los parámetros para los casos de prueba
    public static Collection<TypeStrategy> parameters() {
        return Arrays.asList(TypeStrategy.values());
    }
}
