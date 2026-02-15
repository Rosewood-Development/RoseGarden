package dev.rosewood.rosegarden.codec.record;

import dev.rosewood.rosegarden.codec.SettingCodec;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface RecordFieldGroups {

    final class Group1<C, O, T1> {
        private final RecordField<C, O, T1> t1;

        public Group1(RecordField<C, O, T1> t1) {
            this.t1 = t1;
        }

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Function<T1, O> constructor) {
            return builder.build1(constructor, this.t1);
        }
    }

    final class Group2<C, O, T1, T2> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;

        public Group2(RecordField<C, O, T1> t1,
                      RecordField<C, O, T2> t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        BiFunction<T1, T2, O> constructor) {
            return builder.build2(constructor, this.t1, this.t2);
        }
    }

    final class Group3<C, O, T1, T2, T3> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;

        public Group3(RecordField<C, O, T1> t1,
                      RecordField<C, O, T2> t2,
                      RecordField<C, O, T3> t3) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function3<T1, T2, T3, O> constructor) {
            return builder.build3(constructor, this.t1, this.t2, this.t3);
        }
    }

    final class Group4<C, O, T1, T2, T3, T4> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;

        public Group4(RecordField<C, O, T1> t1,
                      RecordField<C, O, T2> t2,
                      RecordField<C, O, T3> t3,
                      RecordField<C, O, T4> t4) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
        }

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function4<T1, T2, T3, T4, O> constructor) {
            return builder.build4(constructor, this.t1, this.t2, this.t3, this.t4);
        }
    }

    final class Group5<C, O, T1, T2, T3, T4, T5> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;
        private final RecordField<C, O, T5> t5;

        public Group5(RecordField<C, O, T1> t1,
                      RecordField<C, O, T2> t2,
                      RecordField<C, O, T3> t3,
                      RecordField<C, O, T4> t4,
                      RecordField<C, O, T5> t5) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
        }

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function5<T1, T2, T3, T4, T5, O> constructor) {
            return builder.build5(constructor, this.t1, this.t2, this.t3, this.t4, this.t5);
        }
    }

    final class Group6<C, O, T1, T2, T3, T4, T5, T6> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;
        private final RecordField<C, O, T5> t5;
        private final RecordField<C, O, T6> t6;

        public Group6(RecordField<C, O, T1> t1,
                      RecordField<C, O, T2> t2,
                      RecordField<C, O, T3> t3,
                      RecordField<C, O, T4> t4,
                      RecordField<C, O, T5> t5,
                      RecordField<C, O, T6> t6) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
        }

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function6<T1, T2, T3, T4, T5, T6, O> constructor) {
            return builder.build6(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6);
        }
    }

    final class Group7<C, O, T1, T2, T3, T4, T5, T6, T7> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;
        private final RecordField<C, O, T5> t5;
        private final RecordField<C, O, T6> t6;
        private final RecordField<C, O, T7> t7;

        public Group7(RecordField<C, O, T1> t1,
                      RecordField<C, O, T2> t2,
                      RecordField<C, O, T3> t3,
                      RecordField<C, O, T4> t4,
                      RecordField<C, O, T5> t5,
                      RecordField<C, O, T6> t6,
                      RecordField<C, O, T7> t7) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
        }

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function7<T1, T2, T3, T4, T5, T6, T7, O> constructor) {
            return builder.build7(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7);
        }
    }

    final class Group8<C, O, T1, T2, T3, T4, T5, T6, T7, T8> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;
        private final RecordField<C, O, T5> t5;
        private final RecordField<C, O, T6> t6;
        private final RecordField<C, O, T7> t7;
        private final RecordField<C, O, T8> t8;

        public Group8(RecordField<C, O, T1> t1,
                      RecordField<C, O, T2> t2,
                      RecordField<C, O, T3> t3,
                      RecordField<C, O, T4> t4,
                      RecordField<C, O, T5> t5,
                      RecordField<C, O, T6> t6,
                      RecordField<C, O, T7> t7,
                      RecordField<C, O, T8> t8) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
        }

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function8<T1, T2, T3, T4, T5, T6, T7, T8, O> constructor) {
            return builder.build8(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8);
        }
    }

    final class Group9<C, O, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;
        private final RecordField<C, O, T5> t5;
        private final RecordField<C, O, T6> t6;
        private final RecordField<C, O, T7> t7;
        private final RecordField<C, O, T8> t8;
        private final RecordField<C, O, T9> t9;

        public Group9(RecordField<C, O, T1> t1,
                      RecordField<C, O, T2> t2,
                      RecordField<C, O, T3> t3,
                      RecordField<C, O, T4> t4,
                      RecordField<C, O, T5> t5,
                      RecordField<C, O, T6> t6,
                      RecordField<C, O, T7> t7,
                      RecordField<C, O, T8> t8,
                      RecordField<C, O, T9> t9) {
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

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, O> constructor) {
            return builder.build9(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9);
        }
    }

    final class Group10<C, O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;
        private final RecordField<C, O, T5> t5;
        private final RecordField<C, O, T6> t6;
        private final RecordField<C, O, T7> t7;
        private final RecordField<C, O, T8> t8;
        private final RecordField<C, O, T9> t9;
        private final RecordField<C, O, T10> t10;

        public Group10(RecordField<C, O, T1> t1,
                       RecordField<C, O, T2> t2,
                       RecordField<C, O, T3> t3,
                       RecordField<C, O, T4> t4,
                       RecordField<C, O, T5> t5,
                       RecordField<C, O, T6> t6,
                       RecordField<C, O, T7> t7,
                       RecordField<C, O, T8> t8,
                       RecordField<C, O, T9> t9,
                       RecordField<C, O, T10> t10) {
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

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, O> constructor) {
            return builder.build10(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10);
        }
    }

    final class Group11<C, O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;
        private final RecordField<C, O, T5> t5;
        private final RecordField<C, O, T6> t6;
        private final RecordField<C, O, T7> t7;
        private final RecordField<C, O, T8> t8;
        private final RecordField<C, O, T9> t9;
        private final RecordField<C, O, T10> t10;
        private final RecordField<C, O, T11> t11;

        public Group11(RecordField<C, O, T1> t1,
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

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, O> constructor) {
            return builder.build11(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11);
        }
    }

    final class Group12<C, O, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
        private final RecordField<C, O, T1> t1;
        private final RecordField<C, O, T2> t2;
        private final RecordField<C, O, T3> t3;
        private final RecordField<C, O, T4> t4;
        private final RecordField<C, O, T5> t5;
        private final RecordField<C, O, T6> t6;
        private final RecordField<C, O, T7> t7;
        private final RecordField<C, O, T8> t8;
        private final RecordField<C, O, T9> t9;
        private final RecordField<C, O, T10> t10;
        private final RecordField<C, O, T11> t11;
        private final RecordField<C, O, T12> t12;

        public Group12(RecordField<C, O, T1> t1,
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

        public SettingCodec<C, O> apply(RecordCodecBuilder<C, O> builder,
                                        Functions.Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, O> constructor) {
            return builder.build12(constructor, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12);
        }
    }

}
