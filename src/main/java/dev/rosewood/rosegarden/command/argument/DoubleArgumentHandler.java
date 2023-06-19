package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.Collections;
import java.util.List;

public class DoubleArgumentHandler extends RoseCommandArgumentHandler<Double> {

    public DoubleArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, Double.class);
    }

    @Override
    protected Double handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        String input = argumentParser.next();
        try {
            return Double.parseDouble(input);
        } catch (Exception e) {
            throw new HandledArgumentException("argument-handler-double", StringPlaceholders.of("input", input));
        }
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();
        return Collections.singletonList(argumentInfo.toString());
    }

}
