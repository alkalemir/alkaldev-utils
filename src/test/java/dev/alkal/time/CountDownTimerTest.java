package dev.alkal.time;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class CountDownTimerTest {

  @Test
  void foo() {
    new CountDownTimer(1, 10, TimeUnit.SECONDS) {

      @Override
      protected void onTick(long remaining) {
        System.out.println(remaining);

        if (remaining < 5000)
          cancel();
      }

      @Override
      protected void onFinish() {
        System.out.println("bitti");
      }
    }.start();

    try {
      Thread.sleep(12000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void bar() {
    CountDownTimer cw = new CountDownTimer(3, 10, TimeUnit.SECONDS) {
      @Override
      protected void onStart() {
        System.out.println("Basladi " + LocalTime.now());
      }

      @Override
      protected void onTick(long l) {
        System.out.println("OnTick " + LocalTime.now());
      }

      @Override
      protected void onFinish() {
        System.out.println("OnFinish " + LocalTime.now());
      }
    };

    cw.start();
    try {
      Thread.sleep(20000);
    } catch (InterruptedException ignore) {}
  }
}
