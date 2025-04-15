package dev.rosewood.rosegarden.config;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import static dev.rosewood.rosegarden.config.SettingSerializerFactories.getOrCreateSection;

public class RecordSettingSerializerBuilder<O> {

    private final Class<O> type;
    private SettingSerializer<O> serializer;

    private RecordSettingSerializerBuilder(Class<O> type) {
        this.type = type;
    }

    private static <T, O> T getValueOrDefault(ConfigurationSection section, SettingField<O, T> settingField) {
        if (section == null)
            return null;

        if (settingField.flatten()) // Lift keys into the expected section for the field serializer if flattened
            section = liftSection(section, settingField.key());

        T value = settingField.settingSerializer().read(section, settingField.key());
        if (value == null)
            value = settingField.defaultValue();
        return value;
    }

    private static ConfigurationSection liftSection(ConfigurationSection section, String key) {
        MemoryConfiguration liftedSection = new MemoryConfiguration();
        Map<String, Object> map = section.getValues(true);
        String prefix = key + ".";
        map.forEach((path, val) -> {
            liftedSection.set(prefix + path, val);
            liftedSection.set(path, val); // keep original values so they can still be read if needed by other serializers
        });
        return liftedSection;
    }

    private static <T, O> T getPersistentValueOrDefault(PersistentDataContainer container, SettingField<O, T> settingField) {
        T value = settingField.settingSerializer().read(container, settingField.key());
        if (value == null)
            value = settingField.defaultValue();
        return value;
    }

    private static <T, O> void writeConfigurationField(ConfigurationSection section, SettingField<O, T> settingField, O value, boolean writeDefaults) {
        if (value == null)
            return;

        // Don't overwrite a setting that's already valid
        ConfigurationSection checkSection = section;
        if (settingField.flatten())
            checkSection = liftSection(checkSection, settingField.key());
        if (settingField.settingSerializer().readIsValid(checkSection, settingField.key()))
            return;

        if (writeDefaults) {
            settingField.settingSerializer().writeWithDefault(section, settingField.key(), settingField.getter().apply(value), settingField.comments());
        } else {
            settingField.settingSerializer().write(section, settingField.key(), settingField.getter().apply(value), settingField.comments());
        }
        if (settingField.flatten() && section.isConfigurationSection(settingField.key())) {
            // Flatten keys down a level by deleting the section they're in and add them to their parent
            ConfigurationSection flatteningSection = section.getConfigurationSection(settingField.key());
            if (flatteningSection != null) {
                Map<String, Object> map = flatteningSection.getValues(true);
                section.set(settingField.key(), null);
                map.forEach(section::set);
            }
        }
    }

    private static <T, O> boolean testSection(ConfigurationSection section, SettingField<O, T> settingField) {
        if (settingField.optional() && settingField.defaultValue() == null)
            return true;

        if (settingField.flatten())
            section = liftSection(section, settingField.key());
        return settingField.settingSerializer().readIsValid(section, settingField.key());
    }

    public static <O> SettingSerializer<O> create(Class<O> clazz, Function<RecordSettingSerializerBuilder<O>, Built<O>> builder) {
        Built<O> built = builder.apply(new RecordSettingSerializerBuilder<>(clazz));
        RecordSettingSerializerBuilder<O> instance = built.instance();
        return instance.serializer;
    }

    //<editor-fold desc="group methods" defaultstate="collapsed">
    public <T1> RecordFieldGroups.Group1<O, T1> group(SettingField<O, T1> t1) {
        return new RecordFieldGroups.Group1<>(t1);
    }

    public <T1, T2> RecordFieldGroups.Group2<O, T1, T2> group(SettingField<O, T1> t1,
                                                              SettingField<O, T2> t2) {
        return new RecordFieldGroups.Group2<>(t1, t2);
    }

    public <T1, T2, T3> RecordFieldGroups.Group3<O, T1, T2, T3> group(SettingField<O, T1> t1,
                                                                      SettingField<O, T2> t2,
                                                                      SettingField<O, T3> t3) {
        return new RecordFieldGroups.Group3<>(t1, t2, t3);
    }

    public <T1, T2, T3, T4> RecordFieldGroups.Group4<O, T1, T2, T3, T4> group(SettingField<O, T1> t1,
                                                                              SettingField<O, T2> t2,
                                                                              SettingField<O, T3> t3,
                                                                              SettingField<O, T4> t4) {
        return new RecordFieldGroups.Group4<>(t1, t2, t3, t4);
    }

    public <T1, T2, T3, T4, T5> RecordFieldGroups.Group5<O, T1, T2, T3, T4, T5> group(SettingField<O, T1> t1,
                                                                                      SettingField<O, T2> t2,
                                                                                      SettingField<O, T3> t3,
                                                                                      SettingField<O, T4> t4,
                                                                                      SettingField<O, T5> t5) {
        return new RecordFieldGroups.Group5<>(t1, t2, t3, t4, t5);
    }

    public <T1, T2, T3, T4, T5, T6> RecordFieldGroups.Group6<O, T1, T2, T3, T4, T5, T6> group(SettingField<O, T1> t1,
                                                                                              SettingField<O, T2> t2,
                                                                                              SettingField<O, T3> t3,
                                                                                              SettingField<O, T4> t4,
                                                                                              SettingField<O, T5> t5,
                                                                                              SettingField<O, T6> t6) {
        return new RecordFieldGroups.Group6<>(t1, t2, t3, t4, t5, t6);
    }

    public <T1, T2, T3, T4, T5, T6, T7> RecordFieldGroups.Group7<O, T1, T2, T3, T4, T5, T6, T7> group(SettingField<O, T1> t1,
                                                                                                      SettingField<O, T2> t2,
                                                                                                      SettingField<O, T3> t3,
                                                                                                      SettingField<O, T4> t4,
                                                                                                      SettingField<O, T5> t5,
                                                                                                      SettingField<O, T6> t6,
                                                                                                      SettingField<O, T7> t7) {
        return new RecordFieldGroups.Group7<>(t1, t2, t3, t4, t5, t6, t7);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8> RecordFieldGroups.Group8<O, T1, T2, T3, T4, T5, T6, T7, T8> group(SettingField<O, T1> t1,
                                                                                                              SettingField<O, T2> t2,
                                                                                                              SettingField<O, T3> t3,
                                                                                                              SettingField<O, T4> t4,
                                                                                                              SettingField<O, T5> t5,
                                                                                                              SettingField<O, T6> t6,
                                                                                                              SettingField<O, T7> t7,
                                                                                                              SettingField<O, T8> t8) {
        return new RecordFieldGroups.Group8<>(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9> RecordFieldGroups.Group9<O, T1, T2, T3, T4, T5, T6, T7, T8, T9> group(SettingField<O, T1> t1,
                                                                                                                      SettingField<O, T2> t2,
                                                                                                                      SettingField<O, T3> t3,
                                                                                                                      SettingField<O, T4> t4,
                                                                                                                      SettingField<O, T5> t5,
                                                                                                                      SettingField<O, T6> t6,
                                                                                                                      SettingField<O, T7> t7,
                                                                                                                      SettingField<O, T8> t8,
                                                                                                                      SettingField<O, T9> t9) {
        return new RecordFieldGroups.Group9<>(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> RecordFieldGroups.Group10<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> group(SettingField<O, T1> t1,
                                                                                                                                 SettingField<O, T2> t2,
                                                                                                                                 SettingField<O, T3> t3,
                                                                                                                                 SettingField<O, T4> t4,
                                                                                                                                 SettingField<O, T5> t5,
                                                                                                                                 SettingField<O, T6> t6,
                                                                                                                                 SettingField<O, T7> t7,
                                                                                                                                 SettingField<O, T8> t8,
                                                                                                                                 SettingField<O, T9> t9,
                                                                                                                                 SettingField<O, T10> t10) {
        return new RecordFieldGroups.Group10<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> RecordFieldGroups.Group11<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> group(SettingField<O, T1> t1,
                                                                                                                                           SettingField<O, T2> t2,
                                                                                                                                           SettingField<O, T3> t3,
                                                                                                                                           SettingField<O, T4> t4,
                                                                                                                                           SettingField<O, T5> t5,
                                                                                                                                           SettingField<O, T6> t6,
                                                                                                                                           SettingField<O, T7> t7,
                                                                                                                                           SettingField<O, T8> t8,
                                                                                                                                           SettingField<O, T9> t9,
                                                                                                                                           SettingField<O, T10> t10,
                                                                                                                                           SettingField<O, T11> t11) {
        return new RecordFieldGroups.Group11<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> RecordFieldGroups.Group12<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> group(SettingField<O, T1> t1,
                                                                                                                                                     SettingField<O, T2> t2,
                                                                                                                                                     SettingField<O, T3> t3,
                                                                                                                                                     SettingField<O, T4> t4,
                                                                                                                                                     SettingField<O, T5> t5,
                                                                                                                                                     SettingField<O, T6> t6,
                                                                                                                                                     SettingField<O, T7> t7,
                                                                                                                                                     SettingField<O, T8> t8,
                                                                                                                                                     SettingField<O, T9> t9,
                                                                                                                                                     SettingField<O, T10> t10,
                                                                                                                                                     SettingField<O, T11> t11,
                                                                                                                                                     SettingField<O, T12> t12) {
        return new RecordFieldGroups.Group12<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    //</editor-fold>

    //<editor-fold desc="build methods" defaultstate="collapsed">
    <T1> Built<O> build1(Function<T1, O> constructor,
                         SettingField<O, T1> settingField1) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                return constructor.apply(value1);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                return constructor.apply(value1);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1);
            }
        };
        return new Built<>(this);
    }

    <T1, T2> Built<O> build2(BiFunction<T1, T2, O> constructor,
                             SettingField<O, T1> settingField1,
                             SettingField<O, T2> settingField2) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                return constructor.apply(value1, value2);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                return constructor.apply(value1, value2);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3> Built<O> build3(Functions.Function3<T1, T2, T3, O> constructor,
                                 SettingField<O, T1> settingField1,
                                 SettingField<O, T2> settingField2,
                                 SettingField<O, T3> settingField3) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                return constructor.apply(value1, value2, value3);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                return constructor.apply(value1, value2, value3);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4> Built<O> build4(Functions.Function4<T1, T2, T3, T4, O> constructor,
                                     SettingField<O, T1> settingField1,
                                     SettingField<O, T2> settingField2,
                                     SettingField<O, T3> settingField3,
                                     SettingField<O, T4> settingField4) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                return constructor.apply(value1, value2, value3, value4);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                return constructor.apply(value1, value2, value3, value4);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4, T5> Built<O> build5(Functions.Function5<T1, T2, T3, T4, T5, O> constructor,
                                         SettingField<O, T1> settingField1,
                                         SettingField<O, T2> settingField2,
                                         SettingField<O, T3> settingField3,
                                         SettingField<O, T4> settingField4,
                                         SettingField<O, T5> settingField5) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                settingField5.settingSerializer().write(container, settingField5.key(), settingField5.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                T5 value5 = getPersistentValueOrDefault(primitive, settingField5);
                return constructor.apply(value1, value2, value3, value4, value5);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
                writeConfigurationField(section, settingField5, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
                writeConfigurationField(section, settingField5, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                T5 value5 = getValueOrDefault(section, settingField5);
                return constructor.apply(value1, value2, value3, value4, value5);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4) &&
                        testSection(section, settingField5);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4, T5, T6> Built<O> build6(Functions.Function6<T1, T2, T3, T4, T5, T6, O> constructor,
                                             SettingField<O, T1> settingField1,
                                             SettingField<O, T2> settingField2,
                                             SettingField<O, T3> settingField3,
                                             SettingField<O, T4> settingField4,
                                             SettingField<O, T5> settingField5,
                                             SettingField<O, T6> settingField6) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                settingField5.settingSerializer().write(container, settingField5.key(), settingField5.getter().apply(complex));
                settingField6.settingSerializer().write(container, settingField6.key(), settingField6.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                T5 value5 = getPersistentValueOrDefault(primitive, settingField5);
                T6 value6 = getPersistentValueOrDefault(primitive, settingField6);
                return constructor.apply(value1, value2, value3, value4, value5, value6);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
                writeConfigurationField(section, settingField5, value, false);
                writeConfigurationField(section, settingField6, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
                writeConfigurationField(section, settingField5, value, true);
                writeConfigurationField(section, settingField6, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                T5 value5 = getValueOrDefault(section, settingField5);
                T6 value6 = getValueOrDefault(section, settingField6);
                return constructor.apply(value1, value2, value3, value4, value5, value6);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4) &&
                        testSection(section, settingField5) &&
                        testSection(section, settingField6);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7> Built<O> build7(Functions.Function7<T1, T2, T3, T4, T5, T6, T7, O> constructor,
                                                 SettingField<O, T1> settingField1,
                                                 SettingField<O, T2> settingField2,
                                                 SettingField<O, T3> settingField3,
                                                 SettingField<O, T4> settingField4,
                                                 SettingField<O, T5> settingField5,
                                                 SettingField<O, T6> settingField6,
                                                 SettingField<O, T7> settingField7) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                settingField5.settingSerializer().write(container, settingField5.key(), settingField5.getter().apply(complex));
                settingField6.settingSerializer().write(container, settingField6.key(), settingField6.getter().apply(complex));
                settingField7.settingSerializer().write(container, settingField7.key(), settingField7.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                T5 value5 = getPersistentValueOrDefault(primitive, settingField5);
                T6 value6 = getPersistentValueOrDefault(primitive, settingField6);
                T7 value7 = getPersistentValueOrDefault(primitive, settingField7);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
                writeConfigurationField(section, settingField5, value, false);
                writeConfigurationField(section, settingField6, value, false);
                writeConfigurationField(section, settingField7, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
                writeConfigurationField(section, settingField5, value, true);
                writeConfigurationField(section, settingField6, value, true);
                writeConfigurationField(section, settingField7, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                T5 value5 = getValueOrDefault(section, settingField5);
                T6 value6 = getValueOrDefault(section, settingField6);
                T7 value7 = getValueOrDefault(section, settingField7);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4) &&
                        testSection(section, settingField5) &&
                        testSection(section, settingField6) &&
                        testSection(section, settingField7);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8> Built<O> build8(Functions.Function8<T1, T2, T3, T4, T5, T6, T7, T8, O> constructor,
                                                     SettingField<O, T1> settingField1,
                                                     SettingField<O, T2> settingField2,
                                                     SettingField<O, T3> settingField3,
                                                     SettingField<O, T4> settingField4,
                                                     SettingField<O, T5> settingField5,
                                                     SettingField<O, T6> settingField6,
                                                     SettingField<O, T7> settingField7,
                                                     SettingField<O, T8> settingField8) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                settingField5.settingSerializer().write(container, settingField5.key(), settingField5.getter().apply(complex));
                settingField6.settingSerializer().write(container, settingField6.key(), settingField6.getter().apply(complex));
                settingField7.settingSerializer().write(container, settingField7.key(), settingField7.getter().apply(complex));
                settingField8.settingSerializer().write(container, settingField8.key(), settingField8.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                T5 value5 = getPersistentValueOrDefault(primitive, settingField5);
                T6 value6 = getPersistentValueOrDefault(primitive, settingField6);
                T7 value7 = getPersistentValueOrDefault(primitive, settingField7);
                T8 value8 = getPersistentValueOrDefault(primitive, settingField8);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
                writeConfigurationField(section, settingField5, value, false);
                writeConfigurationField(section, settingField6, value, false);
                writeConfigurationField(section, settingField7, value, false);
                writeConfigurationField(section, settingField8, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
                writeConfigurationField(section, settingField5, value, true);
                writeConfigurationField(section, settingField6, value, true);
                writeConfigurationField(section, settingField7, value, true);
                writeConfigurationField(section, settingField8, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                T5 value5 = getValueOrDefault(section, settingField5);
                T6 value6 = getValueOrDefault(section, settingField6);
                T7 value7 = getValueOrDefault(section, settingField7);
                T8 value8 = getValueOrDefault(section, settingField8);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4) &&
                        testSection(section, settingField5) &&
                        testSection(section, settingField6) &&
                        testSection(section, settingField7) &&
                        testSection(section, settingField8);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9> Built<O> build9(Functions.Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, O> constructor,
                                                         SettingField<O, T1> settingField1,
                                                         SettingField<O, T2> settingField2,
                                                         SettingField<O, T3> settingField3,
                                                         SettingField<O, T4> settingField4,
                                                         SettingField<O, T5> settingField5,
                                                         SettingField<O, T6> settingField6,
                                                         SettingField<O, T7> settingField7,
                                                         SettingField<O, T8> settingField8,
                                                         SettingField<O, T9> settingField9) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                settingField5.settingSerializer().write(container, settingField5.key(), settingField5.getter().apply(complex));
                settingField6.settingSerializer().write(container, settingField6.key(), settingField6.getter().apply(complex));
                settingField7.settingSerializer().write(container, settingField7.key(), settingField7.getter().apply(complex));
                settingField8.settingSerializer().write(container, settingField8.key(), settingField8.getter().apply(complex));
                settingField9.settingSerializer().write(container, settingField9.key(), settingField9.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                T5 value5 = getPersistentValueOrDefault(primitive, settingField5);
                T6 value6 = getPersistentValueOrDefault(primitive, settingField6);
                T7 value7 = getPersistentValueOrDefault(primitive, settingField7);
                T8 value8 = getPersistentValueOrDefault(primitive, settingField8);
                T9 value9 = getPersistentValueOrDefault(primitive, settingField9);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
                writeConfigurationField(section, settingField5, value, false);
                writeConfigurationField(section, settingField6, value, false);
                writeConfigurationField(section, settingField7, value, false);
                writeConfigurationField(section, settingField8, value, false);
                writeConfigurationField(section, settingField9, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
                writeConfigurationField(section, settingField5, value, true);
                writeConfigurationField(section, settingField6, value, true);
                writeConfigurationField(section, settingField7, value, true);
                writeConfigurationField(section, settingField8, value, true);
                writeConfigurationField(section, settingField9, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                T5 value5 = getValueOrDefault(section, settingField5);
                T6 value6 = getValueOrDefault(section, settingField6);
                T7 value7 = getValueOrDefault(section, settingField7);
                T8 value8 = getValueOrDefault(section, settingField8);
                T9 value9 = getValueOrDefault(section, settingField9);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4) &&
                        testSection(section, settingField5) &&
                        testSection(section, settingField6) &&
                        testSection(section, settingField7) &&
                        testSection(section, settingField8) &&
                        testSection(section, settingField9);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Built<O> build10(Functions.Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, O> constructor,
                                                               SettingField<O, T1> settingField1,
                                                               SettingField<O, T2> settingField2,
                                                               SettingField<O, T3> settingField3,
                                                               SettingField<O, T4> settingField4,
                                                               SettingField<O, T5> settingField5,
                                                               SettingField<O, T6> settingField6,
                                                               SettingField<O, T7> settingField7,
                                                               SettingField<O, T8> settingField8,
                                                               SettingField<O, T9> settingField9,
                                                               SettingField<O, T10> settingField10) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                settingField5.settingSerializer().write(container, settingField5.key(), settingField5.getter().apply(complex));
                settingField6.settingSerializer().write(container, settingField6.key(), settingField6.getter().apply(complex));
                settingField7.settingSerializer().write(container, settingField7.key(), settingField7.getter().apply(complex));
                settingField8.settingSerializer().write(container, settingField8.key(), settingField8.getter().apply(complex));
                settingField9.settingSerializer().write(container, settingField9.key(), settingField9.getter().apply(complex));
                settingField10.settingSerializer().write(container, settingField10.key(), settingField10.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                T5 value5 = getPersistentValueOrDefault(primitive, settingField5);
                T6 value6 = getPersistentValueOrDefault(primitive, settingField6);
                T7 value7 = getPersistentValueOrDefault(primitive, settingField7);
                T8 value8 = getPersistentValueOrDefault(primitive, settingField8);
                T9 value9 = getPersistentValueOrDefault(primitive, settingField9);
                T10 value10 = getPersistentValueOrDefault(primitive, settingField10);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
                writeConfigurationField(section, settingField5, value, false);
                writeConfigurationField(section, settingField6, value, false);
                writeConfigurationField(section, settingField7, value, false);
                writeConfigurationField(section, settingField8, value, false);
                writeConfigurationField(section, settingField9, value, false);
                writeConfigurationField(section, settingField10, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
                writeConfigurationField(section, settingField5, value, true);
                writeConfigurationField(section, settingField6, value, true);
                writeConfigurationField(section, settingField7, value, true);
                writeConfigurationField(section, settingField8, value, true);
                writeConfigurationField(section, settingField9, value, true);
                writeConfigurationField(section, settingField10, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                T5 value5 = getValueOrDefault(section, settingField5);
                T6 value6 = getValueOrDefault(section, settingField6);
                T7 value7 = getValueOrDefault(section, settingField7);
                T8 value8 = getValueOrDefault(section, settingField8);
                T9 value9 = getValueOrDefault(section, settingField9);
                T10 value10 = getValueOrDefault(section, settingField10);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4) &&
                        testSection(section, settingField5) &&
                        testSection(section, settingField6) &&
                        testSection(section, settingField7) &&
                        testSection(section, settingField8) &&
                        testSection(section, settingField9) &&
                        testSection(section, settingField10);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Built<O> build11(Functions.Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, O> constructor,
                                                                    SettingField<O, T1> settingField1,
                                                                    SettingField<O, T2> settingField2,
                                                                    SettingField<O, T3> settingField3,
                                                                    SettingField<O, T4> settingField4,
                                                                    SettingField<O, T5> settingField5,
                                                                    SettingField<O, T6> settingField6,
                                                                    SettingField<O, T7> settingField7,
                                                                    SettingField<O, T8> settingField8,
                                                                    SettingField<O, T9> settingField9,
                                                                    SettingField<O, T10> settingField10,
                                                                    SettingField<O, T11> settingField11) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                settingField5.settingSerializer().write(container, settingField5.key(), settingField5.getter().apply(complex));
                settingField6.settingSerializer().write(container, settingField6.key(), settingField6.getter().apply(complex));
                settingField7.settingSerializer().write(container, settingField7.key(), settingField7.getter().apply(complex));
                settingField8.settingSerializer().write(container, settingField8.key(), settingField8.getter().apply(complex));
                settingField9.settingSerializer().write(container, settingField9.key(), settingField9.getter().apply(complex));
                settingField10.settingSerializer().write(container, settingField10.key(), settingField10.getter().apply(complex));
                settingField11.settingSerializer().write(container, settingField11.key(), settingField11.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                T5 value5 = getPersistentValueOrDefault(primitive, settingField5);
                T6 value6 = getPersistentValueOrDefault(primitive, settingField6);
                T7 value7 = getPersistentValueOrDefault(primitive, settingField7);
                T8 value8 = getPersistentValueOrDefault(primitive, settingField8);
                T9 value9 = getPersistentValueOrDefault(primitive, settingField9);
                T10 value10 = getPersistentValueOrDefault(primitive, settingField10);
                T11 value11 = getPersistentValueOrDefault(primitive, settingField11);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
                writeConfigurationField(section, settingField5, value, false);
                writeConfigurationField(section, settingField6, value, false);
                writeConfigurationField(section, settingField7, value, false);
                writeConfigurationField(section, settingField8, value, false);
                writeConfigurationField(section, settingField9, value, false);
                writeConfigurationField(section, settingField10, value, false);
                writeConfigurationField(section, settingField11, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
                writeConfigurationField(section, settingField5, value, true);
                writeConfigurationField(section, settingField6, value, true);
                writeConfigurationField(section, settingField7, value, true);
                writeConfigurationField(section, settingField8, value, true);
                writeConfigurationField(section, settingField9, value, true);
                writeConfigurationField(section, settingField10, value, true);
                writeConfigurationField(section, settingField11, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                T5 value5 = getValueOrDefault(section, settingField5);
                T6 value6 = getValueOrDefault(section, settingField6);
                T7 value7 = getValueOrDefault(section, settingField7);
                T8 value8 = getValueOrDefault(section, settingField8);
                T9 value9 = getValueOrDefault(section, settingField9);
                T10 value10 = getValueOrDefault(section, settingField10);
                T11 value11 = getValueOrDefault(section, settingField11);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4) &&
                        testSection(section, settingField5) &&
                        testSection(section, settingField6) &&
                        testSection(section, settingField7) &&
                        testSection(section, settingField8) &&
                        testSection(section, settingField9) &&
                        testSection(section, settingField10) &&
                        testSection(section, settingField11);
            }
        };
        return new Built<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Built<O> build12(Functions.Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, O> constructor,
                                                                         SettingField<O, T1> settingField1,
                                                                         SettingField<O, T2> settingField2,
                                                                         SettingField<O, T3> settingField3,
                                                                         SettingField<O, T4> settingField4,
                                                                         SettingField<O, T5> settingField5,
                                                                         SettingField<O, T6> settingField6,
                                                                         SettingField<O, T7> settingField7,
                                                                         SettingField<O, T8> settingField8,
                                                                         SettingField<O, T9> settingField9,
                                                                         SettingField<O, T10> settingField10,
                                                                         SettingField<O, T11> settingField11,
                                                                         SettingField<O, T12> settingField12) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return RecordSettingSerializerBuilder.this.type;
            }
            @Override
            public PersistentDataContainer toPrimitive(O complex, PersistentDataAdapterContext context) {
                PersistentDataContainer container = context.newPersistentDataContainer();
                settingField1.settingSerializer().write(container, settingField1.key(), settingField1.getter().apply(complex));
                settingField2.settingSerializer().write(container, settingField2.key(), settingField2.getter().apply(complex));
                settingField3.settingSerializer().write(container, settingField3.key(), settingField3.getter().apply(complex));
                settingField4.settingSerializer().write(container, settingField4.key(), settingField4.getter().apply(complex));
                settingField5.settingSerializer().write(container, settingField5.key(), settingField5.getter().apply(complex));
                settingField6.settingSerializer().write(container, settingField6.key(), settingField6.getter().apply(complex));
                settingField7.settingSerializer().write(container, settingField7.key(), settingField7.getter().apply(complex));
                settingField8.settingSerializer().write(container, settingField8.key(), settingField8.getter().apply(complex));
                settingField9.settingSerializer().write(container, settingField9.key(), settingField9.getter().apply(complex));
                settingField10.settingSerializer().write(container, settingField10.key(), settingField10.getter().apply(complex));
                settingField11.settingSerializer().write(container, settingField11.key(), settingField11.getter().apply(complex));
                settingField12.settingSerializer().write(container, settingField12.key(), settingField12.getter().apply(complex));
                return container;
            }
            @Override
            public O fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
                T1 value1 = getPersistentValueOrDefault(primitive, settingField1);
                T2 value2 = getPersistentValueOrDefault(primitive, settingField2);
                T3 value3 = getPersistentValueOrDefault(primitive, settingField3);
                T4 value4 = getPersistentValueOrDefault(primitive, settingField4);
                T5 value5 = getPersistentValueOrDefault(primitive, settingField5);
                T6 value6 = getPersistentValueOrDefault(primitive, settingField6);
                T7 value7 = getPersistentValueOrDefault(primitive, settingField7);
                T8 value8 = getPersistentValueOrDefault(primitive, settingField8);
                T9 value9 = getPersistentValueOrDefault(primitive, settingField9);
                T10 value10 = getPersistentValueOrDefault(primitive, settingField10);
                T11 value11 = getPersistentValueOrDefault(primitive, settingField11);
                T12 value12 = getPersistentValueOrDefault(primitive, settingField12);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, false);
                writeConfigurationField(section, settingField2, value, false);
                writeConfigurationField(section, settingField3, value, false);
                writeConfigurationField(section, settingField4, value, false);
                writeConfigurationField(section, settingField5, value, false);
                writeConfigurationField(section, settingField6, value, false);
                writeConfigurationField(section, settingField7, value, false);
                writeConfigurationField(section, settingField8, value, false);
                writeConfigurationField(section, settingField9, value, false);
                writeConfigurationField(section, settingField10, value, false);
                writeConfigurationField(section, settingField11, value, false);
                writeConfigurationField(section, settingField12, value, false);
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                writeConfigurationField(section, settingField1, value, true);
                writeConfigurationField(section, settingField2, value, true);
                writeConfigurationField(section, settingField3, value, true);
                writeConfigurationField(section, settingField4, value, true);
                writeConfigurationField(section, settingField5, value, true);
                writeConfigurationField(section, settingField6, value, true);
                writeConfigurationField(section, settingField7, value, true);
                writeConfigurationField(section, settingField8, value, true);
                writeConfigurationField(section, settingField9, value, true);
                writeConfigurationField(section, settingField10, value, true);
                writeConfigurationField(section, settingField11, value, true);
                writeConfigurationField(section, settingField12, value, true);
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = getValueOrDefault(section, settingField1);
                T2 value2 = getValueOrDefault(section, settingField2);
                T3 value3 = getValueOrDefault(section, settingField3);
                T4 value4 = getValueOrDefault(section, settingField4);
                T5 value5 = getValueOrDefault(section, settingField5);
                T6 value6 = getValueOrDefault(section, settingField6);
                T7 value7 = getValueOrDefault(section, settingField7);
                T8 value8 = getValueOrDefault(section, settingField8);
                T9 value9 = getValueOrDefault(section, settingField9);
                T10 value10 = getValueOrDefault(section, settingField10);
                T11 value11 = getValueOrDefault(section, settingField11);
                T12 value12 = getValueOrDefault(section, settingField12);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12);
            }
            @Override
            public boolean readIsValid(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                return section != null &&
                        testSection(section, settingField1) &&
                        testSection(section, settingField2) &&
                        testSection(section, settingField3) &&
                        testSection(section, settingField4) &&
                        testSection(section, settingField5) &&
                        testSection(section, settingField6) &&
                        testSection(section, settingField7) &&
                        testSection(section, settingField8) &&
                        testSection(section, settingField9) &&
                        testSection(section, settingField10) &&
                        testSection(section, settingField11) &&
                        testSection(section, settingField12);
            }
        };
        return new Built<>(this);
    }
    //</editor-fold>

    public static class Built<T> {

        private final RecordSettingSerializerBuilder<T> instance;

        private Built(RecordSettingSerializerBuilder<T> instance) {
            this.instance = instance;
        }

        private RecordSettingSerializerBuilder<T> instance() {
            return this.instance;
        }

    }

}
