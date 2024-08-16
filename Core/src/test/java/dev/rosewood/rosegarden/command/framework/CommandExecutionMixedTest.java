package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.command.mixed.MixedArgsCommand;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandExecutionMixedTest {

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
    public void testExecution_mixed_oneArgs_hasPermission() {
        BaseRoseCommand command = new MixedArgsCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        when(this.sender.hasPermission(MixedArgsCommand.TEST_PERMISSION)).thenReturn(true);

        String input = "on";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifyNull();
        this.verifySuccess("on");
    }

    @Test
    public void testExecution_mixed_oneArgs_noPermission() {
        BaseRoseCommand command = new MixedArgsCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "on";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifyNull();
        this.verifySuccess("on");
    }

    @Test
    public void testExecution_mixed_twoArgs_hasPermission() {
        BaseRoseCommand command = new MixedArgsCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        when(this.sender.hasPermission(MixedArgsCommand.TEST_PERMISSION)).thenReturn(true);

        String input = "bob off";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifySuccess("bob");
        this.verifySuccess("off");
    }

    @Test
    public void testExecution_mixed_twoArgs_noPermission() {
        BaseRoseCommand command = new MixedArgsCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "bob off";

        commandWrapper.execute(this.sender, command.getName(), this.splitInput(input));

        this.verifyInvalid();
    }

    private void verifySuccess(String expected) {
        verify(this.sender).sendMessage(eq(expected));
    }

    private void verifyNull() {
        this.verifyNull(1);
    }

    private void verifyNull(int amount) {
        verify(this.sender, times(amount)).sendMessage(eq("null"));
    }

    private void verifyInvalid() {
        verify(this.sender).sendMessage(eq("invalid-argument"));
    }

    private String[] splitInput(String input) {
        return StringUtils.split(input, ' ');
    }

}
