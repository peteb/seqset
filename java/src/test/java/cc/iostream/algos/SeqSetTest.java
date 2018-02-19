package cc.iostream.algos;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.core.IsEqual.equalTo;

public class SeqSetTest {
    @Test
    public void nextScenarios() {
        final SeqSet set = new SeqSet();

        // Empty container returns 0
        assertThat(set.next(), equalTo(0));

        // #next is idempotent
        set.insert(0);
        assertThat(set.next(), equalTo(1));
        assertThat(set.next(), equalTo(1));

        // A gap is used
        set.insert(2);
        assertThat(set.next(), equalTo(1));

        // A gap is filled
        set.insert(1);
        assertThat(set.next(), equalTo(3));
    }

    @Test
    public void sequentialInserts() {
        // Given
        final SeqSet set = new SeqSet();

        for (int i = 0; i < 1_000; ++i) {
            set.insert(i);
        }

        // When/then
        assertThat(set.size(), equalTo(1));
        assertThat(set.height(), equalTo(1));
        assertThat(set.next(), equalTo(1_000));
    }

    @Test
    public void gappedInserts() {
        // Given
        final SeqSet set = new SeqSet();
        for (Integer i : Arrays.asList(1, 2, 3,  5, 6,  9, 10, 11, 12,  14, 15,  18, 19)) {
            set.insert(i);
        }

        final List<Integer> gapNumbers = Arrays.asList(0, 4, 7, 8, 13, 16, 17, 20);

        // When/then
        assertThat(set.size(), equalTo(5));
        assertThat(set.take(), isIn(gapNumbers));
        assertThat(set.take(), isIn(gapNumbers));
        assertThat(set.take(), isIn(gapNumbers));
        assertThat(set.take(), isIn(gapNumbers));
        assertThat(set.take(), isIn(gapNumbers));
        assertThat(set.take(), isIn(gapNumbers));
        assertThat(set.take(), isIn(gapNumbers));
        assertThat(set.take(), isIn(gapNumbers));
        assertThat(set.size(), equalTo(1));
    }

    @Test
    public void referenceTesting() {
        // Given
        final Random random = new Random(1);
        int numIterations = 50_000;
        final Set<Integer> takenNumbers = new HashSet<>();
        for (int i = 0; i < numIterations; ++i) {
            takenNumbers.add(random.nextInt(numIterations));
        }

        final SeqSet set = new SeqSet();
        for (Integer takenNumber : takenNumbers) {
            set.insert(takenNumber);
        }

        // When
        for (int i = 0; i < numIterations; ++i) {
            int val = set.next();

            assertThat(takenNumbers.contains(val), equalTo(false));

            set.insert(val);
            takenNumbers.add(val);
        }
    }

    @Test
    public void perfInsertRandomValues() {
        // Given
        final Random random = new Random(1);
        int numIterations = 500_000;
        final SeqSet set = new SeqSet();

        for (int i = 0; i < numIterations; ++i) {
            set.insert(random.nextInt(numIterations));
        }
    }

    @Test
    public void perfInsertRandomValues2() {
        // Given
        final Random random = new Random(1);
        int numIterations = 500_000;
        final TreeSet<Integer> set = new TreeSet<>();

        for (int i = 0; i < numIterations; ++i) {
            set.add(random.nextInt(numIterations));
        }
    }

    @Test
    public void perfInsertRandomValues11() {
        // Given
        int numIterations = 500_000;
        final SeqSet set = new SeqSet();

        for (int i = 0; i < numIterations; ++i) {
            set.insert(i);
        }
    }

    @Test
    public void perfInsertRandomValues22() {
        // Given
        int numIterations = 500_000;
        final TreeSet<Integer> set = new TreeSet<>();

        for (int i = 0; i < numIterations; ++i) {
            set.add(i);
        }
    }
}
