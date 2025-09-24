import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Counter {
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void increment() {
        int current = getCount();

        try {
            Thread.sleep(0); // Emulate workload
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        current = current + 1;
        count = current;
    }

    public static void main(String[] args) throws Exception {
        Counter counter = new Counter();

        int threads = 16;
        int incrementsPerThread = 50_000;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int t = 0; t < threads; t++) {
            tasks.add(() -> {
                for (int i = 0; i < incrementsPerThread; i++) {
                    counter.increment();
                }
                return null;
            });
        }

        System.out.println("Starting with count: " + counter.getCount());
        pool.invokeAll(tasks);
        pool.shutdown();

        int expected = threads * incrementsPerThread;
        System.out.println("Expected final count: " + expected);
        System.out.println("Actual final count:   " + counter.getCount());
    }
}

