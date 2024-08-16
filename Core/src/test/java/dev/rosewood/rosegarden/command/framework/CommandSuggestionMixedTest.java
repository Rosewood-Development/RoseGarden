package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.command.mixed.MixedArgsCommand;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandSuggestionMixedTest {

    private RosePlugin rosePlugin;
    private CommandSender sender;

    @BeforeEach
    public void setup() {
        this.rosePlugin = mock(RosePlugin.class);
        this.sender = mock(CommandSender.class);

        when(this.rosePlugin.getName()).thenReturn("TestPlugin");
    }

    @Test
    public void testSuggestion_arg1_empty_hasPermission() {
        BaseRoseCommand command = new MixedArgsCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        when(this.sender.hasPermission(MixedArgsCommand.TEST_PERMISSION)).thenReturn(true);

        String input = "";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(new HashSet<>(Arrays.asList("on", "off", "alice", "bob")), new HashSet<>(suggestions));
    }

    @Test
    public void testSuggestion_arg1_empty_noPermission() {
        BaseRoseCommand command = new MixedArgsCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(new HashSet<>(Arrays.asList("on", "off")), new HashSet<>(suggestions));
    }

    private String[] splitInput(String input) {
        return input.split(" ", -1);
    }

}
