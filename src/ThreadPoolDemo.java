import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPoolDemo {
    private static final Logger logger = Logger.getLogger(ThreadPoolDemo.class.getName());
    private static final AtomicInteger completedTasks = new AtomicInteger(0);
    private static final AtomicInteger rejectedTasks = new AtomicInteger(0);

    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.ALL);

        CustomThreadPool pool = new CustomThreadPool(
            2,  // corePoolSize
            4,  // maxPoolSize
            5,  // keepAliveTime
            TimeUnit.SECONDS,
            5,  // queueSize
            1   // minSpareThreads
        );

        logger.info("Starting thread pool ...");

        logger.info("\nNormal operation with moderate load");
        submitTasks(pool, 10, 1000);
        waitForTasks(15);

        logger.info("\nHigh load with potential rejections");
        submitTasks(pool, 20, 500);
        waitForTasks(15);

        logger.info("\nBurst of tasks");
        submitTasks(pool, 30, 200);
        waitForTasks(15);

        logger.info("\nLong-running tasks");
        submitTasks(pool, 5, 5000);
        waitForTasks(10);

        logger.info("\nInitiating pool shutdown...");
        pool.shutdown();

        logger.info("\nFinal Statistics:");
        logger.info("Total completed tasks: " + completedTasks.get());
        logger.info("Total rejected tasks: " + rejectedTasks.get());
    }

    private static void submitTasks(CustomThreadPool pool, int count, int sleepTime) {
        for (int i = 0; i < count; i++) {
            final int taskId = i;
            try {
                pool.execute(() -> {
                    logger.info("Task " + taskId + " started");
                    try {
                        Thread.sleep(sleepTime);
                        completedTasks.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    logger.info("Task " + taskId + " completed");
                });
            } catch (Exception e) {
                rejectedTasks.incrementAndGet();
                logger.warning("Task " + taskId + " was rejected");
            }
        }
    }

    private static void waitForTasks(int seconds) {
        try {
            logger.info("Waiting for " + seconds + " seconds...");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 