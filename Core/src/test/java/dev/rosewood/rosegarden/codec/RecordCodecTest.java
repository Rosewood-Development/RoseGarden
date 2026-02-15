package dev.rosewood.rosegarden.codec;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.codec.record.RecordField;
import dev.rosewood.rosegarden.codec.yaml.YamlCodec;
import dev.rosewood.rosegarden.codec.yaml.YamlCodecs;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecordCodecTest {

    public record PlayableParticle(Particle particle,
                                   ParticleData data) {

        public static final SettingCodec<ConfigurationSection, PlayableParticle> CODEC = YamlCodecs.ofRecord(PlayableParticle.class, instance -> instance.group(
                RecordField.of("particle", particleCodec, PlayableParticle::particle, "The particle type to spawn"),
                RecordField.ofFlattenedOptionalValue("data", ParticleData.CODEC, PlayableParticle::data, null, "Extra data used to display the particle")
        ).apply(instance, PlayableParticle::new));

    }

    public interface ParticleData {
        SettingCodec<ConfigurationSection, ParticleData> CODEC = YamlCodecs.ofFieldMapped(ParticleData.class, "particle", particleCodec, Map.ofEntries(
                Map.entry(Particle.DUST, DustOptionsData.CODEC)
        ));
    }

    public record DustOptionsData(int red,
                                  int green,
                                  int blue,
                                  float size) implements ParticleData {

        public static final SettingCodec<ConfigurationSection, DustOptionsData> CODEC = YamlCodecs.ofRecord(DustOptionsData.class, instance -> instance.group(
                RecordField.of("red", YamlCodecs.INTEGER, DustOptionsData::red, "The red component between 0-255"),
                RecordField.of("green", YamlCodecs.INTEGER, DustOptionsData::green, "The green component between 0-255"),
                RecordField.of("blue", YamlCodecs.INTEGER, DustOptionsData::blue, "The blue component between 0-255"),
                RecordField.of("size", YamlCodecs.FLOAT, DustOptionsData::size, "The size component between 0.01-4.0")
        ).apply(instance, DustOptionsData::new));
    }

    private record TestRecord(int intValue, String stringValue, TestRecord2 testRecord) {
        private static final SettingCodec<ConfigurationSection, TestRecord> CODEC = YamlCodecs.ofRecord(TestRecord.class, instance -> instance.group(
                RecordField.of("int-value", YamlCodecs.INTEGER, TestRecord::intValue),
                RecordField.of("string-value", YamlCodecs.STRING, TestRecord::stringValue),
                RecordField.ofOptionalValue("test-record", TestRecord2.CODEC, TestRecord::testRecord, null)
        ).apply(instance, TestRecord::new));
    }

    private record TestRecord2(float floatValue, double doubleValue) {
        private static final SettingCodec<ConfigurationSection, TestRecord2> CODEC = YamlCodecs.ofRecord(TestRecord2.class, instance -> instance.group(
                RecordField.of("float-value", YamlCodecs.FLOAT, TestRecord2::floatValue),
                RecordField.of("double-value", YamlCodecs.DOUBLE, TestRecord2::doubleValue)
        ).apply(instance, TestRecord2::new));
    }

    private enum TestEnum {
        RECORD,
        STRING;

        public static final YamlCodec<TestEnum> CODEC = YamlCodecs.ofEnum(TestEnum.class);
    }

    private record TestRecord3(RecordCodecTest.TestEnum type, Object value) {
        private static final SettingCodec<ConfigurationSection, TestRecord3> CODEC = YamlCodecs.ofRecord(TestRecord3.class, instance -> instance.group(
                RecordField.of("type", TestEnum.CODEC, TestRecord3::type),
                RecordField.ofOptional("value", fieldMappedCodec, TestRecord3::value, () -> null)
        ).apply(instance, TestRecord3::new));
    }

    private static TestRecord testRecord;
    private static TestRecord3 testRecord3;
    private static RoseSetting<TestRecord> testSetting;
    private static RoseSetting<TestRecord3> testSetting2;
    private static RoseSetting<TestEnum> testEnumSetting;
    private static YamlCodec<Particle> particleCodec;
    private static YamlCodec<Object> fieldMappedCodec;
    private static RoseSetting<PlayableParticle> testParticleSetting;

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

        particleCodec = YamlCodecs.ofEnum(Particle.class);
        fieldMappedCodec = YamlCodecs.ofFieldMapped(Object.class, "type", TestEnum.CODEC, new HashMap<>() {{
            this.put(RecordCodecTest.TestEnum.RECORD, TestRecord.CODEC);
            this.put(RecordCodecTest.TestEnum.STRING, YamlCodecs.STRING);
        }});

        testParticleSetting = RoseSetting.of("test-particle", PlayableParticle.class, () -> new PlayableParticle(Particle.DUST, new DustOptionsData(123, 255, 10, 2f)), "Test particle");
        testRecord = new TestRecord(123, "test", new TestRecord2(234.5F, 345.6F));
        testRecord3 = new TestRecord3(TestEnum.RECORD, testRecord);
        testSetting = RoseSetting.of("test-setting", TestRecord.class, () -> testRecord, "Test comments");
        testSetting2 = RoseSetting.of("test-setting-two", TestRecord3.class, () -> testRecord3, "Test 2 comments");
        testEnumSetting = RoseSetting.ofEnum("test-enum-setting", TestEnum.class, TestEnum.RECORD);

        YamlCodecs.register(particleCodec);
        YamlCodecs.register(PlayableParticle.CODEC);
        YamlCodecs.register(ParticleData.CODEC);
        YamlCodecs.register(DustOptionsData.CODEC);
        YamlCodecs.register(TestEnum.CODEC);
        YamlCodecs.register(TestRecord.CODEC);
        YamlCodecs.register(TestRecord2.CODEC);
        YamlCodecs.register(TestRecord3.CODEC);
    }

    @Test
    public void testRecordSettingField() {
        CommentedFileConfiguration section = CommentedFileConfiguration.loadConfiguration(BufferedReader.nullReader());

        ConfigurationSection subsection = section.createSection("subsection");

        testParticleSetting.write(subsection);

        String output = section.saveToString();
        System.out.println(output);

        ConfigurationSection particleSection = subsection.getConfigurationSection(testParticleSetting.getKey());
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
        RoseSetting<Vector> vectorSetting = RoseSetting.of(sectionName, Vector.class, () -> new Vector(1, 2, 3));

        vectorSetting.write(section);

        Vector expected = new Vector(1, 2, 3);
        Vector actual = vectorSetting.read(section);
        assertEquals(expected, actual);

        Vector modified = new Vector(4, 5, 6);
        YamlCodecs.VECTOR.encode(section, sectionName, modified);

        actual = vectorSetting.read(section);
        assertEquals(modified, actual);
    }

    @Test
    public void testFlattenedSettingField() {
        YamlConfiguration section = new YamlConfiguration();

        testSetting.write(section);

        TestRecord value = testSetting.read(section);
        assertEquals(testRecord, value);
    }

    @Test
    public void testEnumNoValue_expectNull() {
        YamlConfiguration section = new YamlConfiguration();

        TestEnum value = testEnumSetting.read(section);
        assertNull(value);
    }

    @Test
    public void testRecordNoValues_expectNullRecordValues() {
        YamlConfiguration section = new YamlConfiguration();

        section.createSection("test-setting-two");

        TestRecord3 value = testSetting2.read(section);
        assertNotNull(value);
        assertNull(value.type());
        assertNull(value.value());
    }

    @Test
    public void testFieldMapped() {
        YamlConfiguration section = new YamlConfiguration();

        RoseSetting<TestEnum> typeSetting = RoseSetting.ofEnum("type", TestEnum.class, TestEnum.RECORD);

        typeSetting.write(section);
        fieldMappedCodec.encode(section, "test", testRecord);

        String output = section.saveToString();
        System.out.println(output);
    }

    @Test
    public void testRecord_serialize() {
        YamlConfiguration section = new YamlConfiguration();

        testSetting2.write(section);

        TestRecord3 value = testSetting2.read(section);
        assertNotNull(value);
        assertNotNull(value.type());
        assertNotNull(value.value());
    }

}
