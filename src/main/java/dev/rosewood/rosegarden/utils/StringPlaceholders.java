package dev.rosewood.rosegarden.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An immutable class that holds a map of placeholders and their values
 */
public final class StringPlaceholders {

    private final static StringPlaceholders EMPTY = new StringPlaceholders(Collections.emptyMap(), "%", "%");

    private final String startDelimiter, endDelimiter;
    private final Map<String, String> placeholders;

    private StringPlaceholders(Map<String, String> placeholders, String startDelimiter, String endDelimiter) {
        this.placeholders = Collections.unmodifiableMap(placeholders);
        this.startDelimiter = startDelimiter;
        this.endDelimiter = endDelimiter;
    }

    public String apply(String string) {
        for (String key : this.placeholders.keySet())
            string = string.replaceAll(Pattern.quote(this.startDelimiter + key + this.endDelimiter), Matcher.quoteReplacement(this.placeholders.get(key)));
        return string;
    }

    /**
     * @return an unmodifiable map of the placeholders
     */
    public Map<String, String> getPlaceholders() {
        return this.placeholders;
    }

    /**
     * @return the start delimiter
     */
    public String getStartDelimiter() {
        return this.startDelimiter;
    }

    /**
     * @return the end delimiter
     */
    public String getEndDelimiter() {
        return this.endDelimiter;
    }

    /**
     * @return a new StringPlaceholders builder with delimiters initially set to %
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new builder with delimiters initially set to % and a placeholder added
     *
     * @param placeholder the placeholder to add
     * @param value the value to replace the placeholder with
     * @return a new StringPlaceholders builder with delimiters initially set to % and a placeholder added
     */
    public static Builder builder(String placeholder, Object value) {
        return new Builder().addPlaceholder(placeholder, value);
    }

    /**
     * @return the empty StringPlaceholders instance
     */
    public static StringPlaceholders empty() {
        return EMPTY;
    }

    /**
     * Creates a new StringPlaceholders instance with delimiters set to % and a placeholder added
     *
     * @param placeholder the placeholder to add
     * @param value the value to replace the placeholder with
     * @return a new StringPlaceholders instance with delimiters set to % and a placeholder added
     */
    public static StringPlaceholders single(String placeholder, Object value) {
        return builder(placeholder, value).build();
    }

    public static class Builder {

        private String startDelimiter, endDelimiter;
        private final Map<String, String> placeholders;

        private Builder() {
            this.startDelimiter = "%";
            this.endDelimiter = "%";
            this.placeholders = new HashMap<>();
        }

        /**
         * Adds a placeholder
         *
         * @param placeholder The placeholder to add
         * @param value The value to replace the placeholder with
         * @return this
         */
        public Builder addPlaceholder(String placeholder, Object value) {
            this.placeholders.put(placeholder, Objects.toString(value, "null"));
            return this;
        }

        /**
         * Sets the delimiters
         *
         * @param startDelimiter The start delimiter
         * @param endDelimiter The end delimiter
         * @return this
         */
        public Builder setDelimiters(String startDelimiter, String endDelimiter) {
            this.startDelimiter = startDelimiter;
            this.endDelimiter = endDelimiter;
            return this;
        }

        /**
         * Adds all placeholders from another StringPlaceholders instance
         *
         * @param placeholders The StringPlaceholders instance to add placeholders from
         * @return this
         */
        public Builder addAll(StringPlaceholders placeholders) {
            return this.addAll(placeholders.getPlaceholders());
        }

        /**
         * Adds all placeholders from a map
         *
         * @param placeholders The map to add placeholders from
         * @return this
         */
        public Builder addAll(Map<String, String> placeholders) {
            this.placeholders.putAll(placeholders);
            return this;
        }

        /**
         * @return a new StringPlaceholders instance
         */
        public StringPlaceholders build() {
            return new StringPlaceholders(this.placeholders, this.startDelimiter, this.endDelimiter);
        }

    }

}
