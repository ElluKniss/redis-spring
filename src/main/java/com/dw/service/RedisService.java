package com.dw.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService {

    @Resource
    private RedisTemplate redisTemplate;

    public void unlock(){

    }

    public void lock(){
        // 比如去查库存，只能有一个线程能获取锁去操作数据库，有可能被别的线程释放
        long id = Thread.currentThread().getId();
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock:threadId", "ww");
        if(flag){
            System.out.println(id+" get lock");
        }
    }

    public void lock2(){
        // 比如去查库存，只能有一个线程能获取锁去操作数据库
        long id = Thread.currentThread().getId();
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock:threadId", "ww");
        if(flag){
            System.out.println(id+" get lock");
        }
    }
}
