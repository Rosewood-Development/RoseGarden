package dev.rosewood.rosegarden.command.framework;

import java.util.Arrays;
import java.util.List;

public record CommandInfo(String name,
                          List<String> aliases,
                          String permission,
                          boolean playerOnly,
                          String descriptionKey) {

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {

        private final String name;
        private List<String> aliases;
        private String permission;
        private boolean playerOnly;
        private String descriptionKey;

        private Builder(String name) {
            this.name = name;
            this.aliases = List.of();
            this.permission = null;
            this.playerOnly = false;
            this.descriptionKey = null;
        }

        public Builder aliases(List<String> aliases) {
            this.aliases = aliases;
            return this;
        }

        public Builder aliases(String... aliases) {
            this.aliases = Arrays.asList(aliases);
            return this;
        }

        public Builder permission(String permission) {
            this.permission = permission;
            return this;
        }

        public Builder playerOnly(boolean playerOnly) {
            this.playerOnly = playerOnly;
            return this;
        }

        public Builder descriptionKey(String descriptionKey) {
            this.descriptionKey = descriptionKey;
            return this;
        }

        public CommandInfo build() {
            return new CommandInfo(
                    this.name,
                    this.aliases,
                    this.permission,
                    this.playerOnly,
                    this.descriptionKey
            );
        }

    }

}
