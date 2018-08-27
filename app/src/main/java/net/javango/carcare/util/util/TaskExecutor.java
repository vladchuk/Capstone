package net.javango.carcare.util.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A collection of executor pools to asynchronously run various tasks.
 * Tasks of the same kind are run by dedicated executors to prevent inter-functional (ex, network, disk)
 * task starvation.
 */
public class TaskExecutor {

    private static TaskExecutor instance = new TaskExecutor();

    private Executor diskExecutor;
    private Executor mainThreadExcecutor;
    private Executor networkExecutor;

    private TaskExecutor() {
        diskExecutor = Executors.newSingleThreadExecutor();
        networkExecutor = Executors.newFixedThreadPool(3);
        mainThreadExcecutor = new MainThreadExecutor();
    }

    public static void executeDisk(Runnable runnable) {
        instance.diskExecutor.execute(runnable);
    }

    public static void executeNetwork(Runnable runnable) {
        instance.networkExecutor.execute(runnable);
    }

    public static void executeMainThread(Runnable runnable) {
        instance.mainThreadExcecutor.execute(runnable);
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
