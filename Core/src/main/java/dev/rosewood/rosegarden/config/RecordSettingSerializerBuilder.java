package dev.rosewood.rosegarden.config;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                return constructor.apply(value1);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                return constructor.apply(value1);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                return constructor.apply(value1, value2);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                return constructor.apply(value1, value2);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                return constructor.apply(value1, value2, value3);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                return constructor.apply(value1, value2, value3);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                return constructor.apply(value1, value2, value3, value4);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                return constructor.apply(value1, value2, value3, value4);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(primitive, settingField5.key());
                return constructor.apply(value1, value2, value3, value4, value5);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().write(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().writeWithDefault(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(section, settingField5.key());
                return constructor.apply(value1, value2, value3, value4, value5);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(primitive, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(primitive, settingField6.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().write(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().write(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().writeWithDefault(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().writeWithDefault(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(section, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(section, settingField6.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(primitive, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(primitive, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(primitive, settingField7.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().write(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().write(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().write(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().writeWithDefault(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().writeWithDefault(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().writeWithDefault(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(section, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(section, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(section, settingField7.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(primitive, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(primitive, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(primitive, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(primitive, settingField8.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().write(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().write(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().write(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().write(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().writeWithDefault(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().writeWithDefault(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().writeWithDefault(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().writeWithDefault(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(section, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(section, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(section, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(section, settingField8.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(primitive, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(primitive, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(primitive, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(primitive, settingField8.key());
                T9 value9 = settingField9.settingSerializer().read(primitive, settingField9.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().write(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().write(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().write(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().write(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
                settingField9.settingSerializer().write(section, settingField9.key(), settingField9.getter().apply(value), settingField9.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().writeWithDefault(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().writeWithDefault(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().writeWithDefault(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().writeWithDefault(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
                settingField9.settingSerializer().writeWithDefault(section, settingField9.key(), settingField9.getter().apply(value), settingField9.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(section, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(section, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(section, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(section, settingField8.key());
                T9 value9 = settingField9.settingSerializer().read(section, settingField9.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(primitive, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(primitive, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(primitive, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(primitive, settingField8.key());
                T9 value9 = settingField9.settingSerializer().read(primitive, settingField9.key());
                T10 value10 = settingField10.settingSerializer().read(primitive, settingField10.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().write(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().write(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().write(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().write(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
                settingField9.settingSerializer().write(section, settingField9.key(), settingField9.getter().apply(value), settingField9.comments());
                settingField10.settingSerializer().write(section, settingField10.key(), settingField10.getter().apply(value), settingField10.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().writeWithDefault(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().writeWithDefault(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().writeWithDefault(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().writeWithDefault(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
                settingField9.settingSerializer().writeWithDefault(section, settingField9.key(), settingField9.getter().apply(value), settingField9.comments());
                settingField10.settingSerializer().writeWithDefault(section, settingField10.key(), settingField10.getter().apply(value), settingField10.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(section, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(section, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(section, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(section, settingField8.key());
                T9 value9 = settingField9.settingSerializer().read(section, settingField9.key());
                T10 value10 = settingField10.settingSerializer().read(section, settingField10.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(primitive, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(primitive, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(primitive, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(primitive, settingField8.key());
                T9 value9 = settingField9.settingSerializer().read(primitive, settingField9.key());
                T10 value10 = settingField10.settingSerializer().read(primitive, settingField10.key());
                T11 value11 = settingField11.settingSerializer().read(primitive, settingField11.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().write(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().write(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().write(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().write(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
                settingField9.settingSerializer().write(section, settingField9.key(), settingField9.getter().apply(value), settingField9.comments());
                settingField10.settingSerializer().write(section, settingField10.key(), settingField10.getter().apply(value), settingField10.comments());
                settingField11.settingSerializer().write(section, settingField11.key(), settingField11.getter().apply(value), settingField11.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().writeWithDefault(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().writeWithDefault(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().writeWithDefault(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().writeWithDefault(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
                settingField9.settingSerializer().writeWithDefault(section, settingField9.key(), settingField9.getter().apply(value), settingField9.comments());
                settingField10.settingSerializer().writeWithDefault(section, settingField10.key(), settingField10.getter().apply(value), settingField10.comments());
                settingField11.settingSerializer().writeWithDefault(section, settingField11.key(), settingField11.getter().apply(value), settingField11.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(section, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(section, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(section, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(section, settingField8.key());
                T9 value9 = settingField9.settingSerializer().read(section, settingField9.key());
                T10 value10 = settingField10.settingSerializer().read(section, settingField10.key());
                T11 value11 = settingField11.settingSerializer().read(section, settingField11.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11);
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
                T1 value1 = settingField1.settingSerializer().read(primitive, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(primitive, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(primitive, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(primitive, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(primitive, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(primitive, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(primitive, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(primitive, settingField8.key());
                T9 value9 = settingField9.settingSerializer().read(primitive, settingField9.key());
                T10 value10 = settingField10.settingSerializer().read(primitive, settingField10.key());
                T11 value11 = settingField11.settingSerializer().read(primitive, settingField11.key());
                T12 value12 = settingField12.settingSerializer().read(primitive, settingField12.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12);
            }
        };
        this.serializer = new SettingSerializer<O>(this.type, persistentDataType) {
            @Override
            public void write(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().write(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().write(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().write(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().write(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().write(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().write(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().write(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().write(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
                settingField9.settingSerializer().write(section, settingField9.key(), settingField9.getter().apply(value), settingField9.comments());
                settingField10.settingSerializer().write(section, settingField10.key(), settingField10.getter().apply(value), settingField10.comments());
                settingField11.settingSerializer().write(section, settingField11.key(), settingField11.getter().apply(value), settingField11.comments());
                settingField12.settingSerializer().write(section, settingField12.key(), settingField12.getter().apply(value), settingField12.comments());
            }
            @Override
            public void writeWithDefault(ConfigurationSection config, String key, O value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                settingField1.settingSerializer().writeWithDefault(section, settingField1.key(), settingField1.getter().apply(value), settingField1.comments());
                settingField2.settingSerializer().writeWithDefault(section, settingField2.key(), settingField2.getter().apply(value), settingField2.comments());
                settingField3.settingSerializer().writeWithDefault(section, settingField3.key(), settingField3.getter().apply(value), settingField3.comments());
                settingField4.settingSerializer().writeWithDefault(section, settingField4.key(), settingField4.getter().apply(value), settingField4.comments());
                settingField5.settingSerializer().writeWithDefault(section, settingField5.key(), settingField5.getter().apply(value), settingField5.comments());
                settingField6.settingSerializer().writeWithDefault(section, settingField6.key(), settingField6.getter().apply(value), settingField6.comments());
                settingField7.settingSerializer().writeWithDefault(section, settingField7.key(), settingField7.getter().apply(value), settingField7.comments());
                settingField8.settingSerializer().writeWithDefault(section, settingField8.key(), settingField8.getter().apply(value), settingField8.comments());
                settingField9.settingSerializer().writeWithDefault(section, settingField9.key(), settingField9.getter().apply(value), settingField9.comments());
                settingField10.settingSerializer().writeWithDefault(section, settingField10.key(), settingField10.getter().apply(value), settingField10.comments());
                settingField11.settingSerializer().writeWithDefault(section, settingField11.key(), settingField11.getter().apply(value), settingField11.comments());
                settingField12.settingSerializer().writeWithDefault(section, settingField12.key(), settingField12.getter().apply(value), settingField12.comments());
            }
            @Override
            public O read(ConfigurationSection config, String key) {
                ConfigurationSection section = config.getConfigurationSection(key);
                T1 value1 = settingField1.settingSerializer().read(section, settingField1.key());
                T2 value2 = settingField2.settingSerializer().read(section, settingField2.key());
                T3 value3 = settingField3.settingSerializer().read(section, settingField3.key());
                T4 value4 = settingField4.settingSerializer().read(section, settingField4.key());
                T5 value5 = settingField5.settingSerializer().read(section, settingField5.key());
                T6 value6 = settingField6.settingSerializer().read(section, settingField6.key());
                T7 value7 = settingField7.settingSerializer().read(section, settingField7.key());
                T8 value8 = settingField8.settingSerializer().read(section, settingField8.key());
                T9 value9 = settingField9.settingSerializer().read(section, settingField9.key());
                T10 value10 = settingField10.settingSerializer().read(section, settingField10.key());
                T11 value11 = settingField11.settingSerializer().read(section, settingField11.key());
                T12 value12 = settingField12.settingSerializer().read(section, settingField12.key());
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12);
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
