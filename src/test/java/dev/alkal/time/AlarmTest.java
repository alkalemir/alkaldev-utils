package dev.alkal.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class AlarmTest {

  @Test
  void shouldRunAfterDelay() throws Exception {
    long delay = 500;
    Alarm alarm = Alarm.of(delay, TimeUnit.MILLISECONDS);
    AtomicBoolean triggered = new AtomicBoolean(false);
    long start = System.currentTimeMillis();

    alarm.start(() -> {
      long duration = System.currentTimeMillis() - start;
      assertTrue(duration >= 500, "Alarm fired too early");
      triggered.set(true);
    });

    Thread.sleep(delay + 200);
    assertTrue(triggered.get(), "Alarm didn't trigger on time");
  }

  @Test
  void shouldRunAfterDelay2() throws Exception {
    Alarm alarm = Alarm.of(LocalDateTime.now().plusSeconds(6));
    AtomicBoolean triggered = new AtomicBoolean(false);

    long start = System.currentTimeMillis();

    alarm.start(() -> {
      long duration = System.currentTimeMillis() - start;
      assertTrue(duration > TimeUnit.SECONDS.toMillis(6) - 50, "Alarm fired too early");
      triggered.set(true);
    });

    Thread.sleep(TimeUnit.SECONDS.toMillis(6) + 50);
    assertTrue(triggered.get(), "Alarm didn't trigger on time");
  }

  @Test
  void timeRemainingShouldDecrease() throws Exception {
    Alarm alarm = Alarm.of(500, TimeUnit.MILLISECONDS);
    long before = alarm.timeRemaining();
    Thread.sleep(200);
    long after = alarm.timeRemaining();
    assertTrue(before > after);
  }

  @Test
  void shouldThrownOnNegativeDelay() {
    assertThrows(IllegalArgumentException.class, () -> Alarm.of(-1, TimeUnit.SECONDS));
  }
}
