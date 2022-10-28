package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest{

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync...");
            System.out.println(Thread.currentThread().getId());
            return 1/0;
        }, executorService).handleAsync((result, throwable) -> {
            throwable.printStackTrace();
            result++;
            System.out.println(Thread.currentThread().getId());

            return result;
        }, executorService);

        Integer integer = null;
        try {
            integer = completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(integer);
    }


}
