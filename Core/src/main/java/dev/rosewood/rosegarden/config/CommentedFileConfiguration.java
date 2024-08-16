package dev.rosewood.rosegarden.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.DumperOptions;

public class CommentedFileConfiguration extends CommentedConfigurationSection {

    public static final String COMMENT_KEY_PREFIX = "_COMMENT_";

    private CommentedFileConfiguration(Reader configStream, int comments) {
        super(YamlConfiguration.loadConfiguration(configStream), new AtomicInteger(comments));
    }

    public void save(File file) {
        this.save(file, false);
    }

    public void save(File file, boolean compactLines) {
        String config = this.getConfigAsString();
        this.saveConfig(config, file, compactLines);
    }

    private String getConfigAsString() {
        if (!(this.config instanceof YamlConfiguration))
            throw new UnsupportedOperationException("Cannot get config string of non-YamlConfiguration");

        YamlConfiguration yamlConfiguration = (YamlConfiguration) this.config;

        // Edit the configuration to how we want it
        try {
            Field field_yamlOptions;
            try {
                field_yamlOptions = YamlConfiguration.class.getDeclaredField("yamlDumperOptions");
            } catch (NoSuchFieldException e) { // This is used for 1.18.0 and below
                field_yamlOptions = YamlConfiguration.class.getDeclaredField("yamlOptions");
            }

            field_yamlOptions.setAccessible(true);
            DumperOptions yamlOptions = (DumperOptions) field_yamlOptions.get(yamlConfiguration);
            yamlOptions.setWidth(Integer.MAX_VALUE);
            yamlOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

            if (Stream.of(DumperOptions.class.getDeclaredMethods()).anyMatch(x -> x.getName().equals("setIndicatorIndent")))
                yamlOptions.setIndicatorIndent(2);

            if (Stream.of(DumperOptions.class.getDeclaredMethods()).anyMatch(x -> x.getName().equals("setProcessComments")))
                yamlOptions.setProcessComments(false);

            if (Stream.of(DumperOptions.class.getDeclaredMethods()).anyMatch(x -> x.getName().equals("setSplitLines")))
                yamlOptions.setSplitLines(false);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return yamlConfiguration.saveToString();
    }

    public static CommentedFileConfiguration loadConfiguration(File file) {
        if (file.isDirectory())
            throw new IllegalArgumentException("Cannot create configuration from directory");

        File parent = file.getParentFile();
        if (!parent.exists())
            parent.mkdirs();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ConfigContent content = getConfigContent(file);
        return new CommentedFileConfiguration(content.getReader(), content.getComments());
    }

    public static CommentedFileConfiguration loadConfiguration(Reader reader) {
        ConfigContent content = getConfigContent(reader);
        return new CommentedFileConfiguration(content.getReader(), content.getComments());
    }

    /**
     * Read file and make comments SnakeYAML friendly
     *
     * @param file - Path to file
     * @return - File as Input Stream
     */
    private static ConfigContent getConfigContent(File file) {
        if (!file.exists())
            return ConfigContent.empty();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
            return getConfigContent(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return ConfigContent.empty();
        }
    }

    private static ConfigContent getConfigContent(Reader reader) {
        if (reader instanceof BufferedReader)
            return getConfigContent((BufferedReader) reader);
        return getConfigContent(new BufferedReader(reader));
    }

    private static ConfigContent getConfigContent(BufferedReader reader) {
        int commentNum = 0;
        StringBuilder whole = new StringBuilder();

        try {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // Convert comments into keys
                if (currentLine.trim().startsWith("#")) {
                    String addLine = currentLine.replaceAll(Pattern.quote("'"), Matcher.quoteReplacement("''"))
                            .replaceFirst("#", COMMENT_KEY_PREFIX + commentNum++ + ": '") + "'";
                    whole.append(addLine).append("\n");
                } else {
                    whole.append(currentLine).append("\n");
                }
            }

            return new ConfigContent(new StringReader(whole.toString()), commentNum);
        } catch (IOException e) {
            e.printStackTrace();
            return ConfigContent.empty();
        }
    }

    private String prepareConfigString(String configString) {
        boolean lastLine = false;

        String[] lines = configString.split("\n");
        StringBuilder config = new StringBuilder();

        for (String line : lines) {
            if (line.trim().startsWith(COMMENT_KEY_PREFIX)) {
                int whitespaceIndex = line.indexOf(line.trim());
                String comment = line.substring(0, whitespaceIndex) + "#" + line.substring(line.indexOf(":") + 3, line.length() - 1);

                String normalComment;
                if (comment.trim().startsWith("#'")) {
                    normalComment = comment.substring(0, comment.length() - 1).replaceFirst("#'", "# ");
                } else {
                    normalComment = comment;
                }

                normalComment = normalComment.replaceAll("''", "'");

                if (!lastLine) {
                    config.append(normalComment).append("\n");
                } else {
                    config.append("\n").append(normalComment).append("\n");
                }

                lastLine = false;
            } else {
                config.append(line).append("\n");
                lastLine = true;
            }
        }

        return config.toString();
    }

    /**
     * Writes the configuration string to a file and automatically formats it
     *
     * @param configString The entire configuration string
     * @param file The file to save to
     * @param compactLines If lines should forcefully be separated by at most one newline character
     */
    private void saveConfig(String configString, File file, boolean compactLines) {
        String configuration = this.prepareConfigString(configString);

        // Apply post-processing to config string to make it pretty
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(configuration)) {
            boolean lastLineHadContent = false;
            int lastCommentSpacing = -1;
            int lastLineSpacing = -1;
            boolean forceCompact = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                boolean lineHadContent = false;
                boolean lineWasComment = false;
                int commentSpacing = -1;
                int lineSpacing = line.indexOf(line.trim());

                if (line.trim().startsWith("#")) {
                    lineWasComment = true;
                    String trimmed = line.trim().replaceFirst("#", "");
                    commentSpacing = trimmed.indexOf(trimmed.trim());
                } else if (!line.trim().isEmpty()) {
                    lineHadContent = true;
                    if (line.trim().startsWith("-"))
                        forceCompact = true;
                }

                if (!compactLines && !forceCompact && (
                        (lastLineSpacing != -1 && lineSpacing != lastLineSpacing)
                                || (commentSpacing != -1 && commentSpacing <= 3 && lastCommentSpacing > 3)
                                || (lastLineHadContent && lineHadContent)
                                || (lineWasComment && lastLineHadContent))
                        && !(lastLineHadContent && !lineWasComment)) {
                    stringBuilder.append('\n');
                }

                stringBuilder.append(line).append('\n');

                lastLineHadContent = lineHadContent;
                lastCommentSpacing = commentSpacing;
                lastLineSpacing = lineSpacing;
                forceCompact = false;
            }
        }

        // Remove all spaces from "empty" lines and replace with a single newline
        // Only allow at maximum two newlines in a row
        StringBuilder compactedBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(stringBuilder.toString())) {
            int consecutiveNewlines = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    consecutiveNewlines++;
                    if (consecutiveNewlines < 2)
                        compactedBuilder.append('\n');
                } else {
                    consecutiveNewlines = 0;
                    compactedBuilder.append(line).append('\n');
                }
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
            writer.write(compactedBuilder.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ConfigContent {
        private final Reader reader;
        private final int comments;

        private ConfigContent(Reader reader, int comments) {
            this.reader = reader;
            this.comments = comments;
        }

        public Reader getReader() {
            return this.reader;
        }

        public int getComments() {
            return this.comments;
        }

        public static ConfigContent empty() {
            return new ConfigContent(new StringReader(""), 0);
        }
    }

}
