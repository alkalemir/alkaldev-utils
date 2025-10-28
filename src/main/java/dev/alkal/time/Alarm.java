package dev.alkal.time;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Alarm {
  private final long delay;
  private final long targetTimeInMillis;
  private final ScheduledExecutorService scheduledExecutorService;

  private Alarm(long delay) {
    if (delay < 0) {
      throw new IllegalArgumentException("Delay can not be negative");
    }
    this.delay = delay;
    targetTimeInMillis = delay + System.currentTimeMillis();
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  }

  public static Alarm of(long delay, TimeUnit unit) {
    var millis = unit != TimeUnit.MILLISECONDS ? unit.toMillis(delay) : delay;
    return new Alarm(millis);
  }

  public static Alarm of(LocalDateTime localDateTime) {
    var millis = Duration.between(LocalDateTime.now(), localDateTime).toMillis();
    return of(millis, TimeUnit.MILLISECONDS);
  }

  public static Alarm of(LocalTime localTime) {
    return of(localTime.atDate(LocalDate.now()));
  }

  public void start(Runnable runnable) {
    scheduledExecutorService.schedule(() -> {
      try {
        runnable.run();
      } finally {
        scheduledExecutorService.shutdown();
      }
    }, delay, TimeUnit.MILLISECONDS);
  }

  public long timeRemaining() {
    return Math.max(0, targetTimeInMillis - System.currentTimeMillis());
  }

}

