package ru.intabia.jmm;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

@JCStressTest
@Description("Classic test that demonstrates memory reordering")
@Outcome(id = "1, 1", expect = Expect.ACCEPTABLE)
@Outcome(id = {"0, 1", "1, 0"}, expect = Expect.ACCEPTABLE)
@Outcome(id = "0, 0", expect = Expect.ACCEPTABLE_INTERESTING)
public class DifferentMonitorTest {

    @Actor
    public final void actor1(DataHolder dataHolder, II_Result r) {
        r.r1 = dataHolder.actor1();
    }

    @Actor
    public final void actor2(DataHolder dataHolder, II_Result r) {
        r.r2 = dataHolder.actor2();
    }

    @State
    public static class DataHolder {
        private int x;
        private int y;

        private final Object first = new Object();
        private final Object second = new Object();

        public int actor1() {
            synchronized (first) {
                x = 1;
                return y;
            }
        }

        public int actor2() {
            synchronized (second) {
                y = 1;
                return x;
            }
        }
    }
}
