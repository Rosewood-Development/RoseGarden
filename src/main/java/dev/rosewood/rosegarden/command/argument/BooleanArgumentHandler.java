package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.Arrays;
import java.util.List;

public class BooleanArgumentHandler extends RoseCommandArgumentHandler<Boolean> {

    public BooleanArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, Boolean.class);
    }

    @Override
    protected Boolean handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        String input = argumentParser.next();
        try {
            return Boolean.parseBoolean(input);
        } catch (Exception e) {
            throw new HandledArgumentException("argument-handler-boolean", StringPlaceholders.single("input", input));
        }
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();
        return Arrays.asList("true", "false");
    }

}
