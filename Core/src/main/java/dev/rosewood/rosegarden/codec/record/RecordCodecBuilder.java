package dev.rosewood.rosegarden.codec.record;

import dev.rosewood.rosegarden.codec.BaseSettingCodec;
import dev.rosewood.rosegarden.codec.CodecType;
import dev.rosewood.rosegarden.codec.SettingCodec;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RecordCodecBuilder<C, O> {

    private final CodecType<C> codecType;
    private final Class<O> recordType;

    private RecordCodecBuilder(CodecType<C> codecType, Class<O> recordType) {
        this.codecType = Objects.requireNonNull(codecType, "codecType");
        this.recordType = Objects.requireNonNull(recordType, "recordType");
    }

    public static <C, O> SettingCodec<C, O> create(CodecType<C> codecType,
                                                   Class<O> recordType,
                                                   Function<RecordCodecBuilder<C, O>, SettingCodec<C, O>> builder) {
        return builder.apply(new RecordCodecBuilder<>(codecType, recordType));
    }

    private static <C, O, T> boolean testField(C container, String baseKey, RecordField<C, O, T> field) {
        if (field.optional())
            return true;

        String fieldKey = fieldKey(baseKey, field);
        return field.settingCodec().verify(container, fieldKey);
    }

    private static <C, O, T> T readField(C container, String baseKey, RecordField<C, O, T> field) {
        String fieldKey = fieldKey(baseKey, field);
        T value = field.settingCodec().decode(container, fieldKey);
        if (value == null)
            value = field.defaultValue();
        return value;
    }

    private static <C, O, T> String fieldKey(String baseKey, RecordField<C, O, T> field) {
        String leaf = field.key();
        if (baseKey == null || baseKey.isEmpty())
            return leaf;

        return field.flatten() ? baseKey : baseKey + "." + leaf;
    }

    private static <C, O, T> void writeField(C container, String baseKey, RecordField<C, O, T> field, O value, boolean appendDefault) {
        if (value == null)
            return;

        // Don't overwrite a setting that's already valid
        String fieldKey = fieldKey(baseKey, field);
        if (field.settingCodec().verify(container, fieldKey) && Objects.equals(field.settingCodec().decode(container, fieldKey), value))
            return;

        T fieldValue = field.getter().apply(value);
        field.settingCodec().encode(container, fieldKey, fieldValue, appendDefault, field.comments());
    }

    //<editor-fold desc="group methods" defaultstate="collapsed">
    public <T1> RecordFieldGroups.Group1<C, O, T1> group(RecordField<C, O, T1> t1) {
        return new RecordFieldGroups.Group1<>(t1);
    }

    public <T1, T2> RecordFieldGroups.Group2<C, O, T1, T2> group(RecordField<C, O, T1> t1,
                                                                 RecordField<C, O, T2> t2) {
        return new RecordFieldGroups.Group2<>(t1, t2);
    }

    public <T1, T2, T3> RecordFieldGroups.Group3<C, O, T1, T2, T3> group(RecordField<C, O, T1> t1,
                                                                         RecordField<C, O, T2> t2,
                                                                         RecordField<C, O, T3> t3) {
        return new RecordFieldGroups.Group3<>(t1, t2, t3);
    }

    public <T1, T2, T3, T4> RecordFieldGroups.Group4<C, O, T1, T2, T3, T4> group(RecordField<C, O, T1> t1,
                                                                                 RecordField<C, O, T2> t2,
                                                                                 RecordField<C, O, T3> t3,
                                                                                 RecordField<C, O, T4> t4) {
        return new RecordFieldGroups.Group4<>(t1, t2, t3, t4);
    }

    public <T1, T2, T3, T4, T5> RecordFieldGroups.Group5<C, O, T1, T2, T3, T4, T5> group(RecordField<C, O, T1> t1,
                                                                                         RecordField<C, O, T2> t2,
                                                                                         RecordField<C, O, T3> t3,
                                                                                         RecordField<C, O, T4> t4,
                                                                                         RecordField<C, O, T5> t5) {
        return new RecordFieldGroups.Group5<>(t1, t2, t3, t4, t5);
    }

    public <T1, T2, T3, T4, T5, T6> RecordFieldGroups.Group6<C, O, T1, T2, T3, T4, T5, T6> group(RecordField<C, O, T1> t1,
                                                                                                 RecordField<C, O, T2> t2,
                                                                                                 RecordField<C, O, T3> t3,
                                                                                                 RecordField<C, O, T4> t4,
                                                                                                 RecordField<C, O, T5> t5,
                                                                                                 RecordField<C, O, T6> t6) {
        return new RecordFieldGroups.Group6<>(t1, t2, t3, t4, t5, t6);
    }

    public <T1, T2, T3, T4, T5, T6, T7> RecordFieldGroups.Group7<C, O, T1, T2, T3, T4, T5, T6, T7> group(RecordField<C, O, T1> t1,
                                                                                                         RecordField<C, O, T2> t2,
                                                                                                         RecordField<C, O, T3> t3,
                                                                                                         RecordField<C, O, T4> t4,
                                                                                                         RecordField<C, O, T5> t5,
                                                                                                         RecordField<C, O, T6> t6,
                                                                                                         RecordField<C, O, T7> t7) {
        return new RecordFieldGroups.Group7<>(t1, t2, t3, t4, t5, t6, t7);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8> RecordFieldGroups.Group8<C, O, T1, T2, T3, T4, T5, T6, T7, T8> group(RecordField<C, O, T1> t1,
                                                                                                                 RecordField<C, O, T2> t2,
                                                                                                                 RecordField<C, O, T3> t3,
                                                                                                                 RecordField<C, O, T4> t4,
                                                                                                                 RecordField<C, O, T5> t5,
                                                                                                                 RecordField<C, O, T6> t6,
                                                                                                                 RecordField<C, O, T7> t7,
                                                                                                                 RecordField<C, O, T8> t8) {
        return new RecordFieldGroups.Group8<>(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9> RecordFieldGroups.Group9<C, O, T1, T2, T3, T4, T5, T6, T7, T8, T9> group(RecordField<C, O, T1> t1,
                                                                                                                         RecordField<C, O, T2> t2,
                                                                                                                         RecordField<C, O, T3> t3,
                                                                                                                         RecordField<C, O, T4> t4,
                                                                                                                         RecordField<C, O, T5> t5,
                                                                                                                         RecordField<C, O, T6> t6,
                                                                                                                         RecordField<C, O, T7> t7,
                                                                                                                         RecordField<C, O, T8> t8,
                                                                                                                         RecordField<C, O, T9> t9) {
        return new RecordFieldGroups.Group9<>(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> RecordFieldGroups.Group10<C, O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> group(RecordField<C, O, T1> t1,
                                                                                                                                    RecordField<C, O, T2> t2,
                                                                                                                                    RecordField<C, O, T3> t3,
                                                                                                                                    RecordField<C, O, T4> t4,
                                                                                                                                    RecordField<C, O, T5> t5,
                                                                                                                                    RecordField<C, O, T6> t6,
                                                                                                                                    RecordField<C, O, T7> t7,
                                                                                                                                    RecordField<C, O, T8> t8,
                                                                                                                                    RecordField<C, O, T9> t9,
                                                                                                                                    RecordField<C, O, T10> t10) {
        return new RecordFieldGroups.Group10<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> RecordFieldGroups.Group11<C, O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> group(RecordField<C, O, T1> t1,
                                                                                                                                              RecordField<C, O, T2> t2,
                                                                                                                                              RecordField<C, O, T3> t3,
                                                                                                                                              RecordField<C, O, T4> t4,
                                                                                                                                              RecordField<C, O, T5> t5,
                                                                                                                                              RecordField<C, O, T6> t6,
                                                                                                                                              RecordField<C, O, T7> t7,
                                                                                                                                              RecordField<C, O, T8> t8,
                                                                                                                                              RecordField<C, O, T9> t9,
                                                                                                                                              RecordField<C, O, T10> t10,
                                                                                                                                              RecordField<C, O, T11> t11) {
        return new RecordFieldGroups.Group11<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> RecordFieldGroups.Group12<C, O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> group(RecordField<C, O, T1> t1,
                                                                                                                                                        RecordField<C, O, T2> t2,
                                                                                                                                                        RecordField<C, O, T3> t3,
                                                                                                                                                        RecordField<C, O, T4> t4,
                                                                                                                                                        RecordField<C, O, T5> t5,
                                                                                                                                                        RecordField<C, O, T6> t6,
                                                                                                                                                        RecordField<C, O, T7> t7,
                                                                                                                                                        RecordField<C, O, T8> t8,
                                                                                                                                                        RecordField<C, O, T9> t9,
                                                                                                                                                        RecordField<C, O, T10> t10,
                                                                                                                                                        RecordField<C, O, T11> t11,
                                                                                                                                                        RecordField<C, O, T12> t12) {
        return new RecordFieldGroups.Group12<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    //</editor-fold>

    //<editor-fold desc="build methods" defaultstate="collapsed">
    <T1> SettingCodec<C, O> build1(Function<T1, O> constructor,
                                   RecordField<C, O, T1> settingField1) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                return constructor.apply(value1);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1);
            }
        };
    }

    <T1, T2> SettingCodec<C, O> build2(BiFunction<T1, T2, O> constructor,
                                       RecordField<C, O, T1> settingField1,
                                       RecordField<C, O, T2> settingField2) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                return constructor.apply(value1, value2);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2);
            }
        };
    }

    <T1, T2, T3> SettingCodec<C, O> build3(Functions.Function3<T1, T2, T3, O> constructor,
                                           RecordField<C, O, T1> settingField1,
                                           RecordField<C, O, T2> settingField2,
                                           RecordField<C, O, T3> settingField3) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                return constructor.apply(value1, value2, value3);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3);
            }
        };
    }

    <T1, T2, T3, T4> SettingCodec<C, O> build4(Functions.Function4<T1, T2, T3, T4, O> constructor,
                                               RecordField<C, O, T1> settingField1,
                                               RecordField<C, O, T2> settingField2,
                                               RecordField<C, O, T3> settingField3,
                                               RecordField<C, O, T4> settingField4) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                return constructor.apply(value1, value2, value3, value4);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4);
            }
        };
    }

    <T1, T2, T3, T4, T5> SettingCodec<C, O> build5(Functions.Function5<T1, T2, T3, T4, T5, O> constructor,
                                                   RecordField<C, O, T1> settingField1,
                                                   RecordField<C, O, T2> settingField2,
                                                   RecordField<C, O, T3> settingField3,
                                                   RecordField<C, O, T4> settingField4,
                                                   RecordField<C, O, T5> settingField5) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");
        Objects.requireNonNull(settingField5, "settingField5");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
                writeField(container, key, settingField5, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                T5 value5 = readField(container, key, settingField5);
                return constructor.apply(value1, value2, value3, value4, value5);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4)
                        && testField(container, key, settingField5);
            }
        };
    }

    <T1, T2, T3, T4, T5, T6> SettingCodec<C, O> build6(Functions.Function6<T1, T2, T3, T4, T5, T6, O> constructor,
                                                       RecordField<C, O, T1> settingField1,
                                                       RecordField<C, O, T2> settingField2,
                                                       RecordField<C, O, T3> settingField3,
                                                       RecordField<C, O, T4> settingField4,
                                                       RecordField<C, O, T5> settingField5,
                                                       RecordField<C, O, T6> settingField6) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");
        Objects.requireNonNull(settingField5, "settingField5");
        Objects.requireNonNull(settingField6, "settingField6");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
                writeField(container, key, settingField5, value, appendDefault);
                writeField(container, key, settingField6, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                T5 value5 = readField(container, key, settingField5);
                T6 value6 = readField(container, key, settingField6);
                return constructor.apply(value1, value2, value3, value4, value5, value6);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4)
                        && testField(container, key, settingField5)
                        && testField(container, key, settingField6);
            }
        };
    }

    <T1, T2, T3, T4, T5, T6, T7> SettingCodec<C, O> build7(Functions.Function7<T1, T2, T3, T4, T5, T6, T7, O> constructor,
                                                           RecordField<C, O, T1> settingField1,
                                                           RecordField<C, O, T2> settingField2,
                                                           RecordField<C, O, T3> settingField3,
                                                           RecordField<C, O, T4> settingField4,
                                                           RecordField<C, O, T5> settingField5,
                                                           RecordField<C, O, T6> settingField6,
                                                           RecordField<C, O, T7> settingField7) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");
        Objects.requireNonNull(settingField5, "settingField5");
        Objects.requireNonNull(settingField6, "settingField6");
        Objects.requireNonNull(settingField7, "settingField7");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
                writeField(container, key, settingField5, value, appendDefault);
                writeField(container, key, settingField6, value, appendDefault);
                writeField(container, key, settingField7, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                T5 value5 = readField(container, key, settingField5);
                T6 value6 = readField(container, key, settingField6);
                T7 value7 = readField(container, key, settingField7);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4)
                        && testField(container, key, settingField5)
                        && testField(container, key, settingField6)
                        && testField(container, key, settingField7);
            }
        };
    }

    <T1, T2, T3, T4, T5, T6, T7, T8> SettingCodec<C, O> build8(Functions.Function8<T1, T2, T3, T4, T5, T6, T7, T8, O> constructor,
                                                               RecordField<C, O, T1> settingField1,
                                                               RecordField<C, O, T2> settingField2,
                                                               RecordField<C, O, T3> settingField3,
                                                               RecordField<C, O, T4> settingField4,
                                                               RecordField<C, O, T5> settingField5,
                                                               RecordField<C, O, T6> settingField6,
                                                               RecordField<C, O, T7> settingField7,
                                                               RecordField<C, O, T8> settingField8) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");
        Objects.requireNonNull(settingField5, "settingField5");
        Objects.requireNonNull(settingField6, "settingField6");
        Objects.requireNonNull(settingField7, "settingField7");
        Objects.requireNonNull(settingField8, "settingField8");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
                writeField(container, key, settingField5, value, appendDefault);
                writeField(container, key, settingField6, value, appendDefault);
                writeField(container, key, settingField7, value, appendDefault);
                writeField(container, key, settingField8, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                T5 value5 = readField(container, key, settingField5);
                T6 value6 = readField(container, key, settingField6);
                T7 value7 = readField(container, key, settingField7);
                T8 value8 = readField(container, key, settingField8);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4)
                        && testField(container, key, settingField5)
                        && testField(container, key, settingField6)
                        && testField(container, key, settingField7)
                        && testField(container, key, settingField8);
            }
        };
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9> SettingCodec<C, O> build9(Functions.Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, O> constructor,
                                                                   RecordField<C, O, T1> settingField1,
                                                                   RecordField<C, O, T2> settingField2,
                                                                   RecordField<C, O, T3> settingField3,
                                                                   RecordField<C, O, T4> settingField4,
                                                                   RecordField<C, O, T5> settingField5,
                                                                   RecordField<C, O, T6> settingField6,
                                                                   RecordField<C, O, T7> settingField7,
                                                                   RecordField<C, O, T8> settingField8,
                                                                   RecordField<C, O, T9> settingField9) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");
        Objects.requireNonNull(settingField5, "settingField5");
        Objects.requireNonNull(settingField6, "settingField6");
        Objects.requireNonNull(settingField7, "settingField7");
        Objects.requireNonNull(settingField8, "settingField8");
        Objects.requireNonNull(settingField9, "settingField9");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
                writeField(container, key, settingField5, value, appendDefault);
                writeField(container, key, settingField6, value, appendDefault);
                writeField(container, key, settingField7, value, appendDefault);
                writeField(container, key, settingField8, value, appendDefault);
                writeField(container, key, settingField9, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                T5 value5 = readField(container, key, settingField5);
                T6 value6 = readField(container, key, settingField6);
                T7 value7 = readField(container, key, settingField7);
                T8 value8 = readField(container, key, settingField8);
                T9 value9 = readField(container, key, settingField9);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4)
                        && testField(container, key, settingField5)
                        && testField(container, key, settingField6)
                        && testField(container, key, settingField7)
                        && testField(container, key, settingField8)
                        && testField(container, key, settingField9);
            }
        };
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SettingCodec<C, O> build10(Functions.Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, O> constructor,
                                                                         RecordField<C, O, T1> settingField1,
                                                                         RecordField<C, O, T2> settingField2,
                                                                         RecordField<C, O, T3> settingField3,
                                                                         RecordField<C, O, T4> settingField4,
                                                                         RecordField<C, O, T5> settingField5,
                                                                         RecordField<C, O, T6> settingField6,
                                                                         RecordField<C, O, T7> settingField7,
                                                                         RecordField<C, O, T8> settingField8,
                                                                         RecordField<C, O, T9> settingField9,
                                                                         RecordField<C, O, T10> settingField10) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");
        Objects.requireNonNull(settingField5, "settingField5");
        Objects.requireNonNull(settingField6, "settingField6");
        Objects.requireNonNull(settingField7, "settingField7");
        Objects.requireNonNull(settingField8, "settingField8");
        Objects.requireNonNull(settingField9, "settingField9");
        Objects.requireNonNull(settingField10, "settingField10");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
                writeField(container, key, settingField5, value, appendDefault);
                writeField(container, key, settingField6, value, appendDefault);
                writeField(container, key, settingField7, value, appendDefault);
                writeField(container, key, settingField8, value, appendDefault);
                writeField(container, key, settingField9, value, appendDefault);
                writeField(container, key, settingField10, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                T5 value5 = readField(container, key, settingField5);
                T6 value6 = readField(container, key, settingField6);
                T7 value7 = readField(container, key, settingField7);
                T8 value8 = readField(container, key, settingField8);
                T9 value9 = readField(container, key, settingField9);
                T10 value10 = readField(container, key, settingField10);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4)
                        && testField(container, key, settingField5)
                        && testField(container, key, settingField6)
                        && testField(container, key, settingField7)
                        && testField(container, key, settingField8)
                        && testField(container, key, settingField9)
                        && testField(container, key, settingField10);
            }
        };
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SettingCodec<C, O> build11(Functions.Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, O> constructor,
                                                                              RecordField<C, O, T1> settingField1,
                                                                              RecordField<C, O, T2> settingField2,
                                                                              RecordField<C, O, T3> settingField3,
                                                                              RecordField<C, O, T4> settingField4,
                                                                              RecordField<C, O, T5> settingField5,
                                                                              RecordField<C, O, T6> settingField6,
                                                                              RecordField<C, O, T7> settingField7,
                                                                              RecordField<C, O, T8> settingField8,
                                                                              RecordField<C, O, T9> settingField9,
                                                                              RecordField<C, O, T10> settingField10,
                                                                              RecordField<C, O, T11> settingField11) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");
        Objects.requireNonNull(settingField5, "settingField5");
        Objects.requireNonNull(settingField6, "settingField6");
        Objects.requireNonNull(settingField7, "settingField7");
        Objects.requireNonNull(settingField8, "settingField8");
        Objects.requireNonNull(settingField9, "settingField9");
        Objects.requireNonNull(settingField10, "settingField10");
        Objects.requireNonNull(settingField11, "settingField11");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
                writeField(container, key, settingField5, value, appendDefault);
                writeField(container, key, settingField6, value, appendDefault);
                writeField(container, key, settingField7, value, appendDefault);
                writeField(container, key, settingField8, value, appendDefault);
                writeField(container, key, settingField9, value, appendDefault);
                writeField(container, key, settingField10, value, appendDefault);
                writeField(container, key, settingField11, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                T5 value5 = readField(container, key, settingField5);
                T6 value6 = readField(container, key, settingField6);
                T7 value7 = readField(container, key, settingField7);
                T8 value8 = readField(container, key, settingField8);
                T9 value9 = readField(container, key, settingField9);
                T10 value10 = readField(container, key, settingField10);
                T11 value11 = readField(container, key, settingField11);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4)
                        && testField(container, key, settingField5)
                        && testField(container, key, settingField6)
                        && testField(container, key, settingField7)
                        && testField(container, key, settingField8)
                        && testField(container, key, settingField9)
                        && testField(container, key, settingField10)
                        && testField(container, key, settingField11);
            }
        };
    }

    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SettingCodec<C, O> build12(Functions.Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, O> constructor,
                                                                                   RecordField<C, O, T1> settingField1,
                                                                                   RecordField<C, O, T2> settingField2,
                                                                                   RecordField<C, O, T3> settingField3,
                                                                                   RecordField<C, O, T4> settingField4,
                                                                                   RecordField<C, O, T5> settingField5,
                                                                                   RecordField<C, O, T6> settingField6,
                                                                                   RecordField<C, O, T7> settingField7,
                                                                                   RecordField<C, O, T8> settingField8,
                                                                                   RecordField<C, O, T9> settingField9,
                                                                                   RecordField<C, O, T10> settingField10,
                                                                                   RecordField<C, O, T11> settingField11,
                                                                                   RecordField<C, O, T12> settingField12) {
        Objects.requireNonNull(constructor, "constructor");
        Objects.requireNonNull(settingField1, "settingField1");
        Objects.requireNonNull(settingField2, "settingField2");
        Objects.requireNonNull(settingField3, "settingField3");
        Objects.requireNonNull(settingField4, "settingField4");
        Objects.requireNonNull(settingField5, "settingField5");
        Objects.requireNonNull(settingField6, "settingField6");
        Objects.requireNonNull(settingField7, "settingField7");
        Objects.requireNonNull(settingField8, "settingField8");
        Objects.requireNonNull(settingField9, "settingField9");
        Objects.requireNonNull(settingField10, "settingField10");
        Objects.requireNonNull(settingField11, "settingField11");
        Objects.requireNonNull(settingField12, "settingField12");

        return new BaseSettingCodec<C, O>(this.recordType) {
            @Override
            public Class<C> getContainerType() {
                return RecordCodecBuilder.this.codecType.getContainerType();
            }

            @Override
            public void encode(C container, String key, O value, boolean appendDefault, String... comments) {
                writeField(container, key, settingField1, value, appendDefault);
                writeField(container, key, settingField2, value, appendDefault);
                writeField(container, key, settingField3, value, appendDefault);
                writeField(container, key, settingField4, value, appendDefault);
                writeField(container, key, settingField5, value, appendDefault);
                writeField(container, key, settingField6, value, appendDefault);
                writeField(container, key, settingField7, value, appendDefault);
                writeField(container, key, settingField8, value, appendDefault);
                writeField(container, key, settingField9, value, appendDefault);
                writeField(container, key, settingField10, value, appendDefault);
                writeField(container, key, settingField11, value, appendDefault);
                writeField(container, key, settingField12, value, appendDefault);
            }

            @Override
            public O decode(C container, String key) {
                T1 value1 = readField(container, key, settingField1);
                T2 value2 = readField(container, key, settingField2);
                T3 value3 = readField(container, key, settingField3);
                T4 value4 = readField(container, key, settingField4);
                T5 value5 = readField(container, key, settingField5);
                T6 value6 = readField(container, key, settingField6);
                T7 value7 = readField(container, key, settingField7);
                T8 value8 = readField(container, key, settingField8);
                T9 value9 = readField(container, key, settingField9);
                T10 value10 = readField(container, key, settingField10);
                T11 value11 = readField(container, key, settingField11);
                T12 value12 = readField(container, key, settingField12);
                return constructor.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12);
            }

            @Override
            public boolean verify(C container, String key) {
                return testField(container, key, settingField1)
                        && testField(container, key, settingField2)
                        && testField(container, key, settingField3)
                        && testField(container, key, settingField4)
                        && testField(container, key, settingField5)
                        && testField(container, key, settingField6)
                        && testField(container, key, settingField7)
                        && testField(container, key, settingField8)
                        && testField(container, key, settingField9)
                        && testField(container, key, settingField10)
                        && testField(container, key, settingField11)
                        && testField(container, key, settingField12);
            }
        };
    }
    //</editor-fold>

}
