import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomThreadPoolTest {
    private static final Logger logger = Logger.getLogger(CustomThreadPoolTest.class.getName());

    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.ALL);

        CustomThreadPool pool = new CustomThreadPool(
            2,  // corePoolSize
            4,  // maxPoolSize
            5,  // keepAliveTime
            TimeUnit.SECONDS,  // timeUnit
            10, // queueSize
            1   // minSpareThreads
        );

        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            pool.execute(() -> {
                logger.info("Task " + taskId + " started");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                logger.info("Task " + taskId + " completed");
            });
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        pool.shutdown();
    }
} 