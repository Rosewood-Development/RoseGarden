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

    /**
     * Applies the placeholders to the given string
     *
     * @param string the string to apply the placeholders to
     * @return the string with the placeholders replaced
     */
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
        return new Builder().add(placeholder, value);
    }

    /**
     * @return the empty StringPlaceholders instance
     */
    public static StringPlaceholders empty() {
        return EMPTY;
    }

    /**
     * Creates a new StringPlaceholders instance with delimiters set to % and one placeholder added
     *
     * @param placeholder the placeholder to add
     * @param value the value to replace the placeholder with
     * @return a new StringPlaceholders instance with delimiters set to % and one placeholder added
     */
    public static StringPlaceholders of(String placeholder, Object value) {
        return builder(placeholder, value).build();
    }

    /**
     * Creates a new StringPlaceholders instance with delimiters set to % and two placeholders added
     *
     * @param placeholder1 the first placeholder to add
     * @param value1 the value to replace the first placeholder with
     * @param placeholder2 the second placeholder to add
     * @param value2 the value to replace the second placeholder with
     * @return a new StringPlaceholders instance with delimiters set to % and two placeholders added
     */
    public static StringPlaceholders of(String placeholder1, Object value1,
                                        String placeholder2, Object value2) {
        return builder(placeholder1, value1)
                .add(placeholder2, value2)
                .build();
    }

    /**
     * Creates a new StringPlaceholders instance with delimiters set to % and three placeholders added
     *
     * @param placeholder1 the first placeholder to add
     * @param value1 the value to replace the first placeholder with
     * @param placeholder2 the second placeholder to add
     * @param value2 the value to replace the second placeholder with
     * @param placeholder3 the third placeholder to add
     * @param value3 the value to replace the third placeholder with
     * @return a new StringPlaceholders instance with delimiters set to % and three placeholders added
     */
    public static StringPlaceholders of(String placeholder1, Object value1,
                                        String placeholder2, Object value2,
                                        String placeholder3, Object value3) {
        return builder(placeholder1, value1)
                .add(placeholder2, value2)
                .add(placeholder3, value3)
                .build();
    }

    /**
     * Creates a new StringPlaceholders instance with delimiters set to % and four placeholders added
     *
     * @param placeholder1 the first placeholder to add
     * @param value1 the value to replace the first placeholder with
     * @param placeholder2 the second placeholder to add
     * @param value2 the value to replace the second placeholder with
     * @param placeholder3 the third placeholder to add
     * @param value3 the value to replace the third placeholder with
     * @param placeholder4 the fourth placeholder to add
     * @param value4 the value to replace the fourth placeholder with
     * @return a new StringPlaceholders instance with delimiters set to % and four placeholders added
     */
    public static StringPlaceholders of(String placeholder1, Object value1,
                                        String placeholder2, Object value2,
                                        String placeholder3, Object value3,
                                        String placeholder4, Object value4) {
        return builder(placeholder1, value1)
                .add(placeholder2, value2)
                .add(placeholder3, value3)
                .add(placeholder4, value4)
                .build();
    }

    /**
     * Creates a new StringPlaceholders instance with delimiters set to % and five placeholders added
     *
     * @param placeholder1 the first placeholder to add
     * @param value1 the value to replace the first placeholder with
     * @param placeholder2 the second placeholder to add
     * @param value2 the value to replace the second placeholder with
     * @param placeholder3 the third placeholder to add
     * @param value3 the value to replace the third placeholder with
     * @param placeholder4 the fourth placeholder to add
     * @param value4 the value to replace the fourth placeholder with
     * @param placeholder5 the fifth placeholder to add
     * @param value5 the value to replace the fifth placeholder with
     * @return a new StringPlaceholders instance with delimiters set to % and five placeholders added
     */
    public static StringPlaceholders of(String placeholder1, Object value1,
                                        String placeholder2, Object value2,
                                        String placeholder3, Object value3,
                                        String placeholder4, Object value4,
                                        String placeholder5, Object value5) {
        return builder(placeholder1, value1)
                .add(placeholder2, value2)
                .add(placeholder3, value3)
                .add(placeholder4, value4)
                .add(placeholder5, value5)
                .build();
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
        public Builder add(String placeholder, Object value) {
            this.placeholders.put(placeholder, Objects.toString(value, "null"));
            return this;
        }

        /**
         * Adds a placeholder with any % characters in the value removed
         *
         * @param placeholder The placeholder to add
         * @param value The value to replace the placeholder with
         * @return this
         */
        public Builder addSanitized(String placeholder, Object value) {
            this.placeholders.put(placeholder, Objects.toString(value, "null").replace("%", ""));
            return this;
        }

        /**
         * Sets the delimiters
         *
         * @param startDelimiter The start delimiter
         * @param endDelimiter The end delimiter
         * @return this
         */
        public Builder delimiters(String startDelimiter, String endDelimiter) {
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
