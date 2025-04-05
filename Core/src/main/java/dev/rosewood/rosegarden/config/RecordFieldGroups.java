package dev.rosewood.rosegarden.config;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface RecordFieldGroups {

    final class Group1<O, T1> {
        private final SettingField<O, T1> t1;

        public Group1(SettingField<O, T1> t1) {
            this.t1 = t1;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Function<T1, O> constructor) {
            return builder.build1(constructor, this.t1);
        }
    }

    final class Group2<O, T1, T2> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        
        public Group2(SettingField<O, T1> t1,
                      SettingField<O, T2> t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             BiFunction<T1, T2, O> constructor) {
            return builder.build2(constructor, this.t1, this.t2);
        }
    }

    final class Group3<O, T1, T2, T3> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;

        public Group3(SettingField<O, T1> t1,
                      SettingField<O, T2> t2,
                      SettingField<O, T3> t3) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function3<T1, T2, T3, O> constructor) {
            return builder.build3(constructor, this.t1, this.t2, this.t3);
        }
    }

    final class Group4<O, T1, T2, T3, T4> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        
        public Group4(SettingField<O, T1> t1,
                      SettingField<O, T2> t2,
                      SettingField<O, T3> t3,
                      SettingField<O, T4> t4) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function4<T1, T2, T3, T4, O> constructor) {
            return builder.build4(constructor, this.t1, this.t2, this.t3, this.t4);
        }
    }

    final class Group5<O, T1, T2, T3, T4, T5> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        private final SettingField<O, T5> t5;

        public Group5(SettingField<O, T1> t1,
                      SettingField<O, T2> t2,
                      SettingField<O, T3> t3,
                      SettingField<O, T4> t4,
                      SettingField<O, T5> t5) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function5<T1, T2, T3, T4, T5, O> constructor) {
            return builder.build5(constructor, this.t1, this.t2, this.t3, this.t4, this.t5);
        }
    }

    final class Group6<O, T1, T2, T3, T4, T5, T6> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        private final SettingField<O, T5> t5;
        private final SettingField<O, T6> t6;

        public Group6(SettingField<O, T1> t1,
                      SettingField<O, T2> t2,
                      SettingField<O, T3> t3,
                      SettingField<O, T4> t4,
                      SettingField<O, T5> t5,
                      SettingField<O, T6> t6) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function6<T1, T2, T3, T4, T5, T6, O> constructor) {
            return builder.build6(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6);
        }
    }

    final class Group7<O, T1, T2, T3, T4, T5, T6, T7> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        private final SettingField<O, T5> t5;
        private final SettingField<O, T6> t6;
        private final SettingField<O, T7> t7;

        public Group7(SettingField<O, T1> t1,
                      SettingField<O, T2> t2,
                      SettingField<O, T3> t3,
                      SettingField<O, T4> t4,
                      SettingField<O, T5> t5,
                      SettingField<O, T6> t6,
                      SettingField<O, T7> t7) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function7<T1, T2, T3, T4, T5, T6, T7, O> constructor) {
            return builder.build7(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7);
        }
    }

    final class Group8<O, T1, T2, T3, T4, T5, T6, T7, T8> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        private final SettingField<O, T5> t5;
        private final SettingField<O, T6> t6;
        private final SettingField<O, T7> t7;
        private final SettingField<O, T8> t8;

        public Group8(SettingField<O, T1> t1,
                      SettingField<O, T2> t2,
                      SettingField<O, T3> t3,
                      SettingField<O, T4> t4,
                      SettingField<O, T5> t5,
                      SettingField<O, T6> t6,
                      SettingField<O, T7> t7,
                      SettingField<O, T8> t8) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function8<T1, T2, T3, T4, T5, T6, T7, T8, O> constructor) {
            return builder.build8(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8);
        }
    }

    final class Group9<O, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        private final SettingField<O, T5> t5;
        private final SettingField<O, T6> t6;
        private final SettingField<O, T7> t7;
        private final SettingField<O, T8> t8;
        private final SettingField<O, T9> t9;

        public Group9(SettingField<O, T1> t1,
                      SettingField<O, T2> t2,
                      SettingField<O, T3> t3,
                      SettingField<O, T4> t4,
                      SettingField<O, T5> t5,
                      SettingField<O, T6> t6,
                      SettingField<O, T7> t7,
                      SettingField<O, T8> t8,
                      SettingField<O, T9> t9) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, O> constructor) {
            return builder.build9(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9);
        }
    }

    final class Group10<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        private final SettingField<O, T5> t5;
        private final SettingField<O, T6> t6;
        private final SettingField<O, T7> t7;
        private final SettingField<O, T8> t8;
        private final SettingField<O, T9> t9;
        private final SettingField<O, T10> t10;

        public Group10(SettingField<O, T1> t1,
                       SettingField<O, T2> t2,
                       SettingField<O, T3> t3,
                       SettingField<O, T4> t4,
                       SettingField<O, T5> t5,
                       SettingField<O, T6> t6,
                       SettingField<O, T7> t7,
                       SettingField<O, T8> t8,
                       SettingField<O, T9> t9,
                       SettingField<O, T10> t10) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, O> constructor) {
            return builder.build10(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10);
        }
    }

    final class Group11<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        private final SettingField<O, T5> t5;
        private final SettingField<O, T6> t6;
        private final SettingField<O, T7> t7;
        private final SettingField<O, T8> t8;
        private final SettingField<O, T9> t9;
        private final SettingField<O, T10> t10;
        private final SettingField<O, T11> t11;

        public Group11(SettingField<O, T1> t1,
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
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, O> constructor) {
            return builder.build11(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11);
        }
    }

    final class Group12<O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
        private final SettingField<O, T1> t1;
        private final SettingField<O, T2> t2;
        private final SettingField<O, T3> t3;
        private final SettingField<O, T4> t4;
        private final SettingField<O, T5> t5;
        private final SettingField<O, T6> t6;
        private final SettingField<O, T7> t7;
        private final SettingField<O, T8> t8;
        private final SettingField<O, T9> t9;
        private final SettingField<O, T10> t10;
        private final SettingField<O, T11> t11;
        private final SettingField<O, T12> t12;

        public Group12(SettingField<O, T1> t1,
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
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
        }

        public RecordSettingSerializerBuilder.Built<O> apply(RecordSettingSerializerBuilder<O> builder,
                                                             Functions.Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, O> constructor) {
            return builder.build12(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12);
        }
    }

}
