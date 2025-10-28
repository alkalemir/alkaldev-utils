package dev.alkal.time;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class SchedulerTest {

  @Test
  void schedulerTest() {

    var scheduler = Scheduler.schedule(() -> System.out.println("ali"))
        .repeatEvery(1, TimeUnit.SECONDS)
        .after(4, TimeUnit.SECONDS)
        .start();

    try {
      Thread.sleep(15000);
    } catch (InterruptedException ignore) {

    }

    scheduler.stop();
    System.out.println("stopped");
    try {
      Thread.sleep(15000);
    } catch (InterruptedException ignore) {

    }
  }
}
