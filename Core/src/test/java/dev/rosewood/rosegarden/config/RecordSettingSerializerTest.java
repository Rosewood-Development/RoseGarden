package dev.rosewood.rosegarden.config;

import java.io.BufferedReader;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecordSettingSerializerTest {

    public record PlayableParticle(Particle particle,
                                   ParticleData data) {

        public static final SettingSerializer<PlayableParticle> SERIALIZER = SettingSerializers.ofRecord(PlayableParticle.class, instance -> instance.group(
                SettingField.of("particle", SettingSerializers.ofEnum(Particle.class), PlayableParticle::particle, "The particle type to spawn"),
                SettingField.ofFlattenedOptionalValue("data", ParticleData.SERIALIZER, PlayableParticle::data, null, "Extra data used to display the particle")
        ).apply(instance, PlayableParticle::new));

    }

    public interface ParticleData {
        SettingSerializer<ParticleData> SERIALIZER = SettingSerializers.ofFieldMapped(ParticleData.class, "particle", SettingSerializers.ofEnum(Particle.class), Map.ofEntries(
                Map.entry(Particle.DUST, DustOptionsData.SERIALIZER)
        ));
    }

    public record DustOptionsData(int red,
                                  int green,
                                  int blue,
                                  float size) implements ParticleData {

        public static final SettingSerializer<DustOptionsData> SERIALIZER = SettingSerializers.ofRecord(DustOptionsData.class, instance -> instance.group(
                SettingField.of("red", SettingSerializers.INTEGER, DustOptionsData::red, "The red component between 0-255"),
                SettingField.of("green", SettingSerializers.INTEGER, DustOptionsData::green, "The green component between 0-255"),
                SettingField.of("blue", SettingSerializers.INTEGER, DustOptionsData::blue, "The blue component between 0-255"),
                SettingField.of("size", SettingSerializers.FLOAT, DustOptionsData::size, "The size component between 0.01-4.0")
        ).apply(instance, DustOptionsData::new));
    }

    public static final RoseSetting<PlayableParticle> TEST_SETTING = RoseSetting.of("test-particle", PlayableParticle.SERIALIZER, () -> new PlayableParticle(Particle.DUST, new DustOptionsData(123, 255, 10, 2f)), "Test particle");

    @BeforeAll
    public static void setup() {
        Server server = mock(Server.class);
        Logger logger = mock(Logger.class);
        when(server.getBukkitVersion()).thenReturn("1.21.5-R0.1-SNAPSHOT");
        when(server.getLogger()).thenReturn(logger);
        if (Bukkit.getServer() == null)
            Bukkit.setServer(server);
    }

    @Test
    public void testFlattenedSettingField() {
        CommentedFileConfiguration section = CommentedFileConfiguration.loadConfiguration(BufferedReader.nullReader());

        ConfigurationSection subsection = section.createSection("subsection");

        TEST_SETTING.write(subsection);

        ConfigurationSection particleSection = subsection.getConfigurationSection(TEST_SETTING.getKey());
        assertNotNull(particleSection);
        assertTrue(particleSection.contains("red"));
        assertTrue(particleSection.contains("green"));
        assertTrue(particleSection.contains("blue"));
        assertTrue(particleSection.contains("size"));
    }

    @Test
    public void testWriteAndRewriteChangesValue() {
        CommentedFileConfiguration section = CommentedFileConfiguration.loadConfiguration(BufferedReader.nullReader());

        String sectionName = "test-vector";
        RoseSetting<Vector> vectorSetting = RoseSetting.of(sectionName, SettingSerializers.VECTOR, () -> new Vector(1, 2, 3));

        vectorSetting.write(section);

        Vector expected = new Vector(1, 2, 3);
        Vector actual = vectorSetting.read(section);
        assertEquals(expected, actual);

        Vector modified = new Vector(4, 5, 6);
        SettingSerializers.VECTOR.write(section, sectionName, modified);

        actual = vectorSetting.read(section);
        assertEquals(modified, actual);
    }

}
