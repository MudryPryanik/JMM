package ru.intabia.jmm;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

import java.util.concurrent.atomic.AtomicInteger;

@JCStressTest
@Description("Classic test that demonstrates memory reordering")
@Outcome(id = "1, 1", expect = Expect.ACCEPTABLE)
@Outcome(id = {"0, 1", "1, 0"}, expect = Expect.ACCEPTABLE)
@Outcome(id = "0, 0", expect = Expect.ACCEPTABLE_INTERESTING)
public class AtomicTest {

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
        private final AtomicInteger x = new AtomicInteger(0);
        private final AtomicInteger y = new AtomicInteger(0);

        public int actor1() {
            x.addAndGet(1);
            return y.get();
        }

        public int actor2() {
            y.addAndGet(1);
            return x.get();
        }
    }
}