package logmc.logmcclubs.utils;

import logmc.logmcclubs.Logmcclubs;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TaskUtils {
    public static Task createTask(String name, Consumer<Task> consumer, int interval, boolean async) {
    return (async ? Task.builder().async() : Task.builder())
            .name(name)
            .execute(consumer)
            .interval(interval, TimeUnit.MILLISECONDS)
            .submit(Logmcclubs.getInstance());
}
}
