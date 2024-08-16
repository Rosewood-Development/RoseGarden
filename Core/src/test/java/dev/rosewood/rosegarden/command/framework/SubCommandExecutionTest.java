package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.command.sub.TestSubCommand;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubCommandExecutionTest {

    private RosePlugin rosePlugin;
    private CommandSender sender;

    @BeforeEach
    public void setup() {
        this.rosePlugin = mock(RosePlugin.class);
        this.sender = mock(CommandSender.class);

        AbstractLocaleManager localeManager = mock(AbstractLocaleManager.class);

        when(this.rosePlugin.getName()).thenReturn("TestPlugin");
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
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifySuccess("VALUE_1");
    }

    @Test
    public void testExecution_oneArgs_missing() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifyMissing();
    }

    @Test
    public void testExecution_oneArgs_invalid() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "bad";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifyInvalid();
    }

    @Test
    public void testExecution_twoArgs_valid() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1 option1";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifySuccess("VALUE_1");
        this.verifySuccess("option1");
    }

    @Test
    public void testExecution_twoArgs_missingButStillSuccessBecauseOptional() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1 ";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifySuccess("VALUE_1");
    }

    @Test
    public void testExecution_twoArgs_invalid() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1 bad";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifyInvalidSubCommand();
    }

    @Test
    public void testExecution_threeArgs_valid() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "VALUE_1 option1 arg3";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifySuccess("VALUE_1");
        this.verifySuccess("option1");
        this.verifySuccess("arg3");
    }

    private void verifySuccess(String expected) {
        verify(this.sender).sendMessage(eq(expected));
    }

    private void verifyMissing() {
        verify(this.sender).sendMessage(eq("command-usage"));
    }

    private void verifyInvalid() {
        verify(this.sender).sendMessage(eq("invalid-argument"));
    }

    private void verifyInvalidSubCommand() {
        verify(this.sender).sendMessage(eq("invalid-subcommand"));
    }

    private String[] splitInput(String input) {
        return StringUtils.split(input, ' ');
    }

}
