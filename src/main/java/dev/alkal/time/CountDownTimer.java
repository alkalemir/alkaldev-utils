package dev.alkal.time;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public abstract class CountDownTimer {
  private final long interval;
  private final long totalTime;
  private final ScheduledExecutorService scheduledExecutorService;

  protected CountDownTimer(long interval, long totalTime) {
    this(interval, totalTime, TimeUnit.MILLISECONDS);
  }

  protected CountDownTimer(long interval, long totalTime, TimeUnit timeUnit) {
    if (totalTime < 0) throw new IllegalArgumentException("Total time can not be negative");

    this.interval = timeUnit != TimeUnit.MILLISECONDS ? timeUnit.toMillis(interval) : interval;
    this.totalTime = timeUnit != TimeUnit.MILLISECONDS ? timeUnit.toMillis(totalTime) : totalTime;
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  }

  protected abstract void onTick(long remainingInMillis);
  protected abstract void onFinish();

  public final void start() {
    AtomicLong passedTime = new AtomicLong();
    scheduledExecutorService.scheduleAtFixedRate(() -> {
      if (passedTime.get() <= totalTime) {
        onTick(totalTime - passedTime.get());
        passedTime.getAndAdd(interval);
      } else {
        onFinish();
        scheduledExecutorService.shutdown();
      }
    }, 0, interval, TimeUnit.MILLISECONDS);
  }

  public final void cancel() {
    scheduledExecutorService.shutdown();
    onFinish();
  }
}
