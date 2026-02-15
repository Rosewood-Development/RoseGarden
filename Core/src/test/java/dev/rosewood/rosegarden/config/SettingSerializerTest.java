package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.codec.MapSettingCodecRegistry;
import dev.rosewood.rosegarden.codec.yaml.YamlCodecType;
import dev.rosewood.rosegarden.codec.yaml.YamlCodecs;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SettingSerializerTest {

    @BeforeAll
    public static void setup() {
        Server server = mock(Server.class);
        Logger logger = mock(Logger.class);
        when(server.getBukkitVersion()).thenReturn("1.21.5-R0.1-SNAPSHOT");
        when(server.getLogger()).thenReturn(logger);

        try {
            Field serverField = Bukkit.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(null, server);
        } catch (ReflectiveOperationException e) {
            fail(e);
        }

        RosePlugin rosePlugin = mock();
        MapSettingCodecRegistry registry = new MapSettingCodecRegistry();
        when(rosePlugin.getCodecRegistry()).thenReturn(registry);

        try {
            Field instanceField = RosePlugin.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, rosePlugin);
        } catch (ReflectiveOperationException e) {
            fail(e);
        }

        YamlCodecs.registerDefaults(registry);
    }

    @Test
    public void testSettingSerializer() {
        CommentedFileConfiguration section = CommentedFileConfiguration.loadConfiguration(BufferedReader.nullReader());

        SettingSerializer<String> serializer = new SettingSerializer<>(String.class);

        serializer.write(YamlCodecType.INSTANCE, section, "key", "value", false);

        assertEquals("value", serializer.read(YamlCodecType.INSTANCE, section, "key"));
        assertEquals("value", section.getString("key"));
    }

}
