package com.spc.healthmaster.service;

import com.spc.healthmaster.command.CommandAction;
import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.Actions;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.ApplicationRepository;
import com.spc.healthmaster.services.commands.CommandService;
import com.spc.healthmaster.services.commands.CommandServiceImpl;
import com.spc.healthmaster.services.ssh.SshManagerComposite;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CommandServiceTest {

    private CommandService commandService;
    @Mock private Map<Actions, CommandAction> commandActions;
    @Mock private SshManagerComposite sshManagerComposite;
    @Mock private ApplicationRepository applicationRepository;
    @Mock private List<CommandStrategy> commandStrategies;

    @BeforeEach
    public void setup() {
        openMocks(this);
        commandService =
                new CommandServiceImpl(sshManagerComposite, applicationRepository, commandStrategies, commandActions);
    }

    @Test
    public void givenApplicationInvalidWhenCallExecuteCommandThenWillReturnApiExceptionNotFound() {
        when(applicationRepository.getApplication(any())).thenReturn(Optional.empty());
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(mock(CommandRequestDto.class))
        );
        assertEquals("not_found_application", thrownException.getError());
    }

    @Test
    public void givenServerIdInvalidWhenCallExecuteCommandThenWillReturnApiExceptionNotFound() throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        when(applicationRepository.getApplication(any())).thenReturn(Optional.of(mock(Aplication.class)));
        when(sshManagerComposite.getSshManagerMapById(any()))
                .thenThrow(notFoundConnectionSsh("").toException());
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(commandRequestDto)
        );
        assertEquals("not_found_server", thrownException.getError());
    }

    @Test
    public void givenStrategyInvalidWhenCallExecuteCommandThenWillReturnApiExceptionNotFound() throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(TypeStrategy.SPRING_BOOT_APP);
        when(applicationRepository.getApplication(any())).thenReturn(Optional.of(mock(Aplication.class)));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(mock(SshManagerDto.class));
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(TypeStrategy.TOMCAT_SERVER);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final ApiException thrownException = assertThrows(
                ApiException.class , ()-> commandService.executeCommand(commandRequestDto)
        );
        assertEquals("strategy_not_found", thrownException.getError());
    }

    @Test
    public void givenCommandInvalidWhenCallExecuteCommandThenWillReturnApiExceptionNotFound() throws ApiException {
        final CommandRequestDto commandRequestDto = mock(CommandRequestDto.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(TypeStrategy.SPRING_BOOT_APP);
        when(applicationRepository.getApplication(any())).thenReturn(Optional.of(mock(Aplication.class)));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(mock(SshManagerDto.class));
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(TypeStrategy.SPRING_BOOT_APP);
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
        final Aplication aplication = mock(Aplication.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(typeStrategy);
        when(applicationRepository.getApplication(any())).thenReturn(Optional.of(aplication));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(sshManagerDto);
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(typeStrategy);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final CommandAction commandAction = mock(CommandAction.class);
        when(commandActions.get(any())).thenReturn(commandAction);
        when(commandAction.execute(eq(spring),eq(sshManagerDto),eq(aplication)))
                .thenThrow(ALREADY_INITIALIZED.toException());
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
        final Aplication aplication = mock(Aplication.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(typeStrategy);
        when(applicationRepository.getApplication(any())).thenReturn(Optional.of(aplication));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(sshManagerDto);
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(typeStrategy);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final CommandAction commandAction = mock(CommandAction.class);
        when(commandActions.get(any())).thenReturn(commandAction);
        when(commandAction.execute(eq(spring),eq(sshManagerDto),eq(aplication)))
                .thenThrow(ALREADY_STOPPED.toException());
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
        final Aplication aplication = mock(Aplication.class);
        when(commandRequestDto.getTypeStrategy()).thenReturn(typeStrategy);
        when(applicationRepository.getApplication(any())).thenReturn(Optional.of(aplication));
        when(sshManagerComposite.getSshManagerMapById(any())).thenReturn(sshManagerDto);
        final CommandStrategy spring = mock(CommandStrategy.class);
        when(spring.getType()).thenReturn(typeStrategy);
        when(commandStrategies.stream()).thenReturn(Stream.of(spring));
        final CommandAction commandAction = mock(CommandAction.class);
        when(commandActions.get(any())).thenReturn(commandAction);
        when(commandAction.execute(eq(spring),eq(sshManagerDto),eq(aplication)))
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
