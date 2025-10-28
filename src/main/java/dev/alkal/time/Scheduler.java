package dev.alkal.time;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
  private final Runnable task;
  private final long delay;
  private final long interval;
  private final ScheduledExecutorService scheduledExecutorService;

  private Scheduler(Runnable task, long delay, long interval) {
    this.task = task;
    this.delay = delay;
    this.interval = interval;
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  }

  public static RepeatableScheduler schedule(Runnable task) {
    return new RepeatableSchedulerBuilder(task);
  }

  public static NonRepeatableScheduler once(Runnable task) {
    return new NonRepeatableSchedulerBuilder(task);
  }

  public void stop() {
    scheduledExecutorService.shutdown();
  }

  public interface RepeatableScheduler {
    RepeatableScheduler after(long delay, TimeUnit unit);
    RepeatableScheduler repeatEvery(long interval, TimeUnit unit);
    Scheduler start();
  }

  private static class RepeatableSchedulerBuilder implements RepeatableScheduler {
    private final Runnable task;
    private long delay;
    private long interval;

    private RepeatableSchedulerBuilder(Runnable task) {
      this.task = task;
    }

    @Override
    public RepeatableScheduler after(long delay, TimeUnit timeUnit) {
      this.delay = timeUnit != TimeUnit.MILLISECONDS ? timeUnit.toMillis(delay) : delay;
      return this;
    }

    @Override
    public RepeatableScheduler repeatEvery(long interval, TimeUnit timeUnit) {
      this.interval = timeUnit != TimeUnit.MILLISECONDS ? timeUnit.toMillis(interval) : interval;
      return this;
    }

    @Override
    public Scheduler start() {
      var scheduler = new Scheduler(task, delay, interval);
      scheduler.scheduledExecutorService.scheduleAtFixedRate(task, delay, interval, TimeUnit.MILLISECONDS);
      return scheduler;
    }


  }


  public interface NonRepeatableScheduler {
    NonRepeatableScheduler after(long delay, TimeUnit unit);
    void run();
  }

  private static class NonRepeatableSchedulerBuilder implements NonRepeatableScheduler {
    private final Runnable task;
    private long delay;

    private NonRepeatableSchedulerBuilder(Runnable task) {
      this.task = task;
    }

    @Override
    public NonRepeatableScheduler after(long delay, TimeUnit timeUnit) {
      this.delay = timeUnit != TimeUnit.MILLISECONDS ? timeUnit.toMillis(delay) : delay;
      return this;
    }

    @Override
    public void run() {
      var scheduler = new Scheduler(task, delay, -1);
      scheduler.scheduledExecutorService.schedule(() -> {
        try {
          task.run();
        } finally {
          scheduler.scheduledExecutorService.shutdown();
        }
      }, delay, TimeUnit.MILLISECONDS);
    }
  }

}
