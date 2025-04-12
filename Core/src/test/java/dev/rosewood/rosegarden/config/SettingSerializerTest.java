package dev.rosewood.rosegarden.config;

import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SettingSerializerTest {

    private record TestRecord(int intValue, String stringValue, TestRecord2 testRecord) {
        private static final SettingSerializer<TestRecord> SERIALIZER = SettingSerializers.ofRecord(TestRecord.class, instance -> instance.group(
                SettingField.of("int-value", SettingSerializers.INTEGER, TestRecord::intValue),
                SettingField.of("string-value", SettingSerializers.STRING, TestRecord::stringValue),
                SettingField.ofOptionalValue("test-record", TestRecord2.SERIALIZER, TestRecord::testRecord, null)
        ).apply(instance, TestRecord::new));
    }

    private record TestRecord2(float floatValue, double doubleValue) {
        private static final SettingSerializer<TestRecord2> SERIALIZER = SettingSerializers.ofRecord(TestRecord2.class, instance -> instance.group(
                SettingField.of("float-value", SettingSerializers.FLOAT, TestRecord2::floatValue),
                SettingField.of("double-value", SettingSerializers.DOUBLE, TestRecord2::doubleValue)
        ).apply(instance, TestRecord2::new));
    }

    private enum TestEnum {
        RECORD,
        STRING
    }

    private record TestRecord3(TestEnum type, Object value) {
        private static final SettingSerializer<TestRecord3> SERIALIZER = SettingSerializers.ofRecord(TestRecord3.class, instance -> instance.group(
                SettingField.of("type", SettingSerializers.ofEnum(TestEnum.class), TestRecord3::type),
                SettingField.ofOptional("value", SettingSerializers.ofFieldMapped(Object.class, "type", SettingSerializers.ofEnum(TestEnum.class), new HashMap<TestEnum, SettingSerializer<?>>() {{
                    this.put(TestEnum.RECORD, TestRecord.SERIALIZER);
                    this.put(TestEnum.STRING, SettingSerializers.STRING);
                }}), TestRecord3::value, () -> null)
        ).apply(instance, TestRecord3::new));
    }

    private static final TestRecord TEST_RECORD = new TestRecord(123, "test", new TestRecord2(234.5F, 345.6F));
    private static final TestRecord3 TEST_RECORD_3 = new TestRecord3(TestEnum.RECORD, TEST_RECORD);
    private static final RoseSetting<TestRecord> TEST_SETTING = RoseSetting.of("test-setting", TestRecord.SERIALIZER, () -> TEST_RECORD, "Test comments");
    private static final RoseSetting<TestRecord3> TEST_SETTING_3 = RoseSetting.of("test-setting-three", TestRecord3.SERIALIZER, () -> TEST_RECORD_3, "Test 3 comments");
    private static final RoseSetting<TestEnum> TEST_ENUM_SETTING = RoseSetting.ofEnum("test-enum-setting", TestEnum.class, TestEnum.RECORD);

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
        YamlConfiguration section = new YamlConfiguration();

        TEST_SETTING.write(section);

        TestRecord value = TEST_SETTING.read(section);
        assertEquals(TEST_RECORD, value);
    }

    @Test
    public void testEnumNoValue_expectNull() {
        YamlConfiguration section = new YamlConfiguration();

        TestEnum value = TEST_ENUM_SETTING.read(section);
        assertNull(value);
    }

    @Test
    public void testRecordNoValues_expectNullRecordValues() {
        YamlConfiguration section = new YamlConfiguration();

        section.createSection("test-setting-three");

        TestRecord3 value = TEST_SETTING_3.read(section);
        assertNotNull(value);
        assertNull(value.type());
        assertNull(value.value());
    }

    @Test
    public void testRecord_serialize() {
        YamlConfiguration section = new YamlConfiguration();

        TEST_SETTING_3.write(section);

        TestRecord3 value = TEST_SETTING_3.read(section);
        assertNotNull(value);
        assertNotNull(value.type());
        assertNotNull(value.value());
    }

}
