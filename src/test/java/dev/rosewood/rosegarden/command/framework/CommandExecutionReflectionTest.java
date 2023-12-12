package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.command.reflection.DefaultedReflectionCommand;
import dev.rosewood.rosegarden.command.framework.command.reflection.MissingExecutableReflectionCommand;
import dev.rosewood.rosegarden.command.framework.command.reflection.ReflectionCommand;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandExecutionReflectionTest {

    private RosePlugin rosePlugin;
    private CommandSender sender;
    private Logger logger;

    @BeforeEach
    public void setup() {
        this.rosePlugin = mock(RosePlugin.class);
        this.sender = mock(CommandSender.class);
        this.logger = mock(Logger.class);

        AbstractLocaleManager localeManager = mock(AbstractLocaleManager.class);

        when(this.rosePlugin.getName()).thenReturn("TestPlugin");
        when(this.rosePlugin.getLogger()).thenReturn(this.logger);
        when(this.rosePlugin.getManager(AbstractLocaleManager.class)).thenReturn(localeManager);
        Answer<Object> answer = invocation -> { // Forwards messages sent through the LocaleManager straight to the player as the message key
            CommandSender sender = invocation.getArgument(0);
            String messageKey = invocation.getArgument(1);
            sender.sendMessage(messageKey);
            return null;
        };
        doAnswer(answer).when(localeManager).sendCommandMessage(any(), any(), any());
        doAnswer(answer).when(localeManager).sendCommandMessage(any(), any());
    }

    @Test
    public void testExecution_oneArgs_valid() {
        BaseRoseCommand command = new ReflectionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifySuccess("VALUE_1");
    }

    @Test
    public void testExecution_twoArgs_valid() {
        BaseRoseCommand command = new ReflectionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1 banana";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifySuccess("VALUE_1");
        this.verifySuccess("banana");
    }

    @Test
    public void testExecution_missingExecutable() {
        BaseRoseCommand command = new MissingExecutableReflectionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifyWarningLog();
    }

    @Test
    public void testExecution_defaultedExecution() {
        BaseRoseCommand command = new DefaultedReflectionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifySuccess("VALUE_1");
    }

    private void verifySuccess(String expected) {
        verify(this.sender).sendMessage(eq(expected));
    }

    private void verifyWarningLog() {
        verify(this.logger).warning(any(String.class));
    }

    private String[] splitInput(String input) {
        return StringUtils.split(input, ' ');
    }

}
