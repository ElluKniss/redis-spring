package com.example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTest {

    public static void main(String[] args) {

        FutureTask<Integer> futureTask = new FutureTask<Integer>(() -> {
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                if(i%10 ==0){
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getId()+"--"+ i);
                    sum += sum;
                }
            }
            return sum;
        });

        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            Integer integer = futureTask.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {

            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("out time...");
            throw new RuntimeException(e);
        }
    }
}
