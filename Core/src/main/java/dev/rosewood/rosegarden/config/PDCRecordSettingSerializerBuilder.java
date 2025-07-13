package dev.rosewood.rosegarden.config;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PDCRecordSettingSerializerBuilder<O> extends RecordSettingSerializerBuilder<O> {

    private PDCSettingSerializer<O> pdcSerializer;

    private PDCRecordSettingSerializerBuilder(Class<O> type) {
        super(type);
    }

    private static <T, O> T getPersistentValueOrDefault(PersistentDataContainer container, PDCSettingField<O, T> settingField) {
        T value = settingField.settingSerializer().read(container, settingField.key());
        if (value == null)
            value = settingField.defaultValue();
        return value;
    }

    public static <O> PDCSettingSerializer<O> createPDC(Class<O> clazz, Function<PDCRecordSettingSerializerBuilder<O>, BuiltPDC<O>> builder) {
        BuiltPDC<O> built = builder.apply(new PDCRecordSettingSerializerBuilder<>(clazz));
        PDCRecordSettingSerializerBuilder<O> instance = built.instance();
        return instance.pdcSerializer;
    }

    //<editor-fold desc="group methods" defaultstate="collapsed">
    public <T1> PDCRecordFieldGroups.Group1<O, T1> group(PDCSettingField<O, T1> t1) {
        return new PDCRecordFieldGroups.Group1<>(t1);
    }

    public <T1, T2> PDCRecordFieldGroups.Group2<O, T1, T2> group(PDCSettingField<O, T1> t1,
                                                              PDCSettingField<O, T2> t2) {
        return new PDCRecordFieldGroups.Group2<>(t1, t2);
    }

    public <T1, T2, T3> PDCRecordFieldGroups.Group3<O, T1, T2, T3> group(PDCSettingField<O, T1> t1,
                                                                      PDCSettingField<O, T2> t2,
                                                                      PDCSettingField<O, T3> t3) {
        return new PDCRecordFieldGroups.Group3<>(t1, t2, t3);
    }

    public <T1, T2, T3, T4> PDCRecordFieldGroups.Group4<O, T1, T2, T3, T4> group(PDCSettingField<O, T1> t1,
                                                                              PDCSettingField<O, T2> t2,
                                                                              PDCSettingField<O, T3> t3,
                                                                              PDCSettingField<O, T4> t4) {
        return new PDCRecordFieldGroups.Group4<>(t1, t2, t3, t4);
    }

    public <T1, T2, T3, T4, T5> PDCRecordFieldGroups.Group5<O, T1, T2, T3, T4, T5> group(PDCSettingField<O, T1> t1,
                                                                                      PDCSettingField<O, T2> t2,
                                                                                      PDCSettingField<O, T3> t3,
                                                                                      PDCSettingField<O, T4> t4,
                                                                                      PDCSettingField<O, T5> t5) {
        return new PDCRecordFieldGroups.Group5<>(t1, t2, t3, t4, t5);
    }

    public <T1, T2, T3, T4, T5, T6> PDCRecordFieldGroups.Group6<O, T1, T2, T3, T4, T5, T6> group(PDCSettingField<O, T1> t1,
                                                                                              PDCSettingField<O, T2> t2,
                                                                                              PDCSettingField<O, T3> t3,
                                                                                              PDCSettingField<O, T4> t4,
                                                                                              PDCSettingField<O, T5> t5,
                                                                                              PDCSettingField<O, T6> t6) {
        return new PDCRecordFieldGroups.Group6<>(t1, t2, t3, t4, t5, t6);
    }

    public <T1, T2, T3, T4, T5, T6, T7> PDCRecordFieldGroups.Group7<O, T1, T2, T3, T4, T5, T6, T7> group(PDCSettingField<O, T1> t1,
                                                                                                      PDCSettingField<O, T2> t2,
                                                                                                      PDCSettingField<O, T3> t3,
                                                                                                      PDCSettingField<O, T4> t4,
                                                                                                      PDCSettingField<O, T5> t5,
                                                                                                      PDCSettingField<O, T6> t6,
                                                                                                      PDCSettingField<O, T7> t7) {
        return new PDCRecordFieldGroups.Group7<>(t1, t2, t3, t4, t5, t6, t7);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8> PDCRecordFieldGroups.Group8<O, T1, T2, T3, T4, T5, T6, T7, T8> group(PDCSettingField<O, T1> t1,
                                                                                                              PDCSettingField<O, T2> t2,
                                                                                                              PDCSettingField<O, T3> t3,
                                                                                                              PDCSettingField<O, T4> t4,
                                                                                                              PDCSettingField<O, T5> t5,
                                                                                                              PDCSettingField<O, T6> t6,
                                                                                                              PDCSettingField<O, T7> t7,
                                                                                                              PDCSettingField<O, T8> t8) {
        return new PDCRecordFieldGroups.Group8<>(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9> PDCRecordFieldGroups.Group9<O, T1, T2, T3, T4, T5, T6, T7, T8, T9> group(PDCSettingField<O, T1> t1,
                                                                                                                      PDCSettingField<O, T2> t2,
                                                                                                                      PDCSettingField<O, T3> t3,
                                                                                                                      PDCSettingField<O, T4> t4,
                                                                                                                      PDCSettingField<O, T5> t5,
                                                                                                                      PDCSettingField<O, T6> t6,
                                                                                                                      PDCSettingField<O, T7> t7,
                                                                                                                      PDCSettingField<O, T8> t8,
                                                                                                                      PDCSettingField<O, T9> t9) {
        return new PDCRecordFieldGroups.Group9<>(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> PDCRecordFieldGroups.Group10<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> group(PDCSettingField<O, T1> t1,
                                                                                                                                 PDCSettingField<O, T2> t2,
                                                                                                                                 PDCSettingField<O, T3> t3,
                                                                                                                                 PDCSettingField<O, T4> t4,
                                                                                                                                 PDCSettingField<O, T5> t5,
                                                                                                                                 PDCSettingField<O, T6> t6,
                                                                                                                                 PDCSettingField<O, T7> t7,
                                                                                                                                 PDCSettingField<O, T8> t8,
                                                                                                                                 PDCSettingField<O, T9> t9,
                                                                                                                                 PDCSettingField<O, T10> t10) {
        return new PDCRecordFieldGroups.Group10<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> PDCRecordFieldGroups.Group11<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> group(PDCSettingField<O, T1> t1,
                                                                                                                                           PDCSettingField<O, T2> t2,
                                                                                                                                           PDCSettingField<O, T3> t3,
                                                                                                                                           PDCSettingField<O, T4> t4,
                                                                                                                                           PDCSettingField<O, T5> t5,
                                                                                                                                           PDCSettingField<O, T6> t6,
                                                                                                                                           PDCSettingField<O, T7> t7,
                                                                                                                                           PDCSettingField<O, T8> t8,
                                                                                                                                           PDCSettingField<O, T9> t9,
                                                                                                                                           PDCSettingField<O, T10> t10,
                                                                                                                                           PDCSettingField<O, T11> t11) {
        return new PDCRecordFieldGroups.Group11<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> PDCRecordFieldGroups.Group12<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> group(PDCSettingField<O, T1> t1,
                                                                                                                                                     PDCSettingField<O, T2> t2,
                                                                                                                                                     PDCSettingField<O, T3> t3,
                                                                                                                                                     PDCSettingField<O, T4> t4,
                                                                                                                                                     PDCSettingField<O, T5> t5,
                                                                                                                                                     PDCSettingField<O, T6> t6,
                                                                                                                                                     PDCSettingField<O, T7> t7,
                                                                                                                                                     PDCSettingField<O, T8> t8,
                                                                                                                                                     PDCSettingField<O, T9> t9,
                                                                                                                                                     PDCSettingField<O, T10> t10,
                                                                                                                                                     PDCSettingField<O, T11> t11,
                                                                                                                                                     PDCSettingField<O, T12> t12) {
        return new PDCRecordFieldGroups.Group12<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    //</editor-fold>

    //<editor-fold desc="build methods" defaultstate="collapsed">
    <T1> BuiltPDC<O> build1(Function<T1, O> constructor,
                            PDCSettingField<O, T1> settingField1) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build1(constructor,
                settingField1);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2> BuiltPDC<O> build2(BiFunction<T1, T2, O> constructor,
                             PDCSettingField<O, T1> settingField1,
                             PDCSettingField<O, T2> settingField2) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build2(constructor,
                settingField1,
                settingField2);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3> BuiltPDC<O> build3(Functions.Function3<T1, T2, T3, O> constructor,
                                    PDCSettingField<O, T1> settingField1,
                                    PDCSettingField<O, T2> settingField2,
                                    PDCSettingField<O, T3> settingField3) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build3(constructor,
                settingField1,
                settingField2,
                settingField3);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4> BuiltPDC<O> build4(Functions.Function4<T1, T2, T3, T4, O> constructor,
                                     PDCSettingField<O, T1> settingField1,
                                     PDCSettingField<O, T2> settingField2,
                                     PDCSettingField<O, T3> settingField3,
                                     PDCSettingField<O, T4> settingField4) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build4(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4, T5> BuiltPDC<O> build5(Functions.Function5<T1, T2, T3, T4, T5, O> constructor,
                                         PDCSettingField<O, T1> settingField1,
                                         PDCSettingField<O, T2> settingField2,
                                         PDCSettingField<O, T3> settingField3,
                                         PDCSettingField<O, T4> settingField4,
                                         PDCSettingField<O, T5> settingField5) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build5(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4,
                settingField5);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4, T5, T6> BuiltPDC<O> build6(Functions.Function6<T1, T2, T3, T4, T5, T6, O> constructor,
                                             PDCSettingField<O, T1> settingField1,
                                             PDCSettingField<O, T2> settingField2,
                                             PDCSettingField<O, T3> settingField3,
                                             PDCSettingField<O, T4> settingField4,
                                             PDCSettingField<O, T5> settingField5,
                                             PDCSettingField<O, T6> settingField6) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build6(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4,
                settingField5,
                settingField6);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7> BuiltPDC<O> build7(Functions.Function7<T1, T2, T3, T4, T5, T6, T7, O> constructor,
                                                 PDCSettingField<O, T1> settingField1,
                                                 PDCSettingField<O, T2> settingField2,
                                                 PDCSettingField<O, T3> settingField3,
                                                 PDCSettingField<O, T4> settingField4,
                                                 PDCSettingField<O, T5> settingField5,
                                                 PDCSettingField<O, T6> settingField6,
                                                 PDCSettingField<O, T7> settingField7) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build7(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4,
                settingField5,
                settingField6,
                settingField7);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8> BuiltPDC<O> build8(Functions.Function8<T1, T2, T3, T4, T5, T6, T7, T8, O> constructor,
                                                     PDCSettingField<O, T1> settingField1,
                                                     PDCSettingField<O, T2> settingField2,
                                                     PDCSettingField<O, T3> settingField3,
                                                     PDCSettingField<O, T4> settingField4,
                                                     PDCSettingField<O, T5> settingField5,
                                                     PDCSettingField<O, T6> settingField6,
                                                     PDCSettingField<O, T7> settingField7,
                                                     PDCSettingField<O, T8> settingField8) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build8(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4,
                settingField5,
                settingField6,
                settingField7,
                settingField8);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9> BuiltPDC<O> build9(Functions.Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, O> constructor,
                                                         PDCSettingField<O, T1> settingField1,
                                                         PDCSettingField<O, T2> settingField2,
                                                         PDCSettingField<O, T3> settingField3,
                                                         PDCSettingField<O, T4> settingField4,
                                                         PDCSettingField<O, T5> settingField5,
                                                         PDCSettingField<O, T6> settingField6,
                                                         PDCSettingField<O, T7> settingField7,
                                                         PDCSettingField<O, T8> settingField8,
                                                         PDCSettingField<O, T9> settingField9) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build9(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4,
                settingField5,
                settingField6,
                settingField7,
                settingField8,
                settingField9);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> BuiltPDC<O> build10(Functions.Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, O> constructor,
                                                               PDCSettingField<O, T1> settingField1,
                                                               PDCSettingField<O, T2> settingField2,
                                                               PDCSettingField<O, T3> settingField3,
                                                               PDCSettingField<O, T4> settingField4,
                                                               PDCSettingField<O, T5> settingField5,
                                                               PDCSettingField<O, T6> settingField6,
                                                               PDCSettingField<O, T7> settingField7,
                                                               PDCSettingField<O, T8> settingField8,
                                                               PDCSettingField<O, T9> settingField9,
                                                               PDCSettingField<O, T10> settingField10) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build10(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4,
                settingField5,
                settingField6,
                settingField7,
                settingField8,
                settingField9,
                settingField10);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> BuiltPDC<O> build11(Functions.Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, O> constructor,
                                                                    PDCSettingField<O, T1> settingField1,
                                                                    PDCSettingField<O, T2> settingField2,
                                                                    PDCSettingField<O, T3> settingField3,
                                                                    PDCSettingField<O, T4> settingField4,
                                                                    PDCSettingField<O, T5> settingField5,
                                                                    PDCSettingField<O, T6> settingField6,
                                                                    PDCSettingField<O, T7> settingField7,
                                                                    PDCSettingField<O, T8> settingField8,
                                                                    PDCSettingField<O, T9> settingField9,
                                                                    PDCSettingField<O, T10> settingField10,
                                                                    PDCSettingField<O, T11> settingField11) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build11(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4,
                settingField5,
                settingField6,
                settingField7,
                settingField8,
                settingField9,
                settingField10,
                settingField11);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> BuiltPDC<O> build12(Functions.Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, O> constructor,
                                                                         PDCSettingField<O, T1> settingField1,
                                                                         PDCSettingField<O, T2> settingField2,
                                                                         PDCSettingField<O, T3> settingField3,
                                                                         PDCSettingField<O, T4> settingField4,
                                                                         PDCSettingField<O, T5> settingField5,
                                                                         PDCSettingField<O, T6> settingField6,
                                                                         PDCSettingField<O, T7> settingField7,
                                                                         PDCSettingField<O, T8> settingField8,
                                                                         PDCSettingField<O, T9> settingField9,
                                                                         PDCSettingField<O, T10> settingField10,
                                                                         PDCSettingField<O, T11> settingField11,
                                                                         PDCSettingField<O, T12> settingField12) {
        PersistentDataType<PersistentDataContainer, O> persistentDataType = new PersistentDataType<PersistentDataContainer, O>() {
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            @Override
            public Class<O> getComplexType() {
                return PDCRecordSettingSerializerBuilder.this.type;
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
        super.build12(constructor,
                settingField1,
                settingField2,
                settingField3,
                settingField4,
                settingField5,
                settingField6,
                settingField7,
                settingField8,
                settingField9,
                settingField10,
                settingField11,
                settingField12);
        this.pdcSerializer = new PDCDelegatingSettingSerializer<>(this.serializer, persistentDataType);
        return new BuiltPDC<>(this);
    }
    //</editor-fold>

    public static class BuiltPDC<T> {

        private final PDCRecordSettingSerializerBuilder<T> instance;

        private BuiltPDC(PDCRecordSettingSerializerBuilder<T> instance) {
            this.instance = instance;
        }

        private PDCRecordSettingSerializerBuilder<T> instance() {
            return this.instance;
        }

    }

}
