package com.dw.service;

import io.lettuce.core.RedisClient;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

    private static final String ORDER_COUNT = "21";

    // 单台机器不明显，靠线程id可以区分，分布式场景需要
    public static final String RANDOMID = UUID.randomUUID().toString();


    @Resource
    private RedissonClient redissonClient;

    @Resource
    private StringRedisTemplate srt;

    // 释放锁，判断是否当前线程持有
    public void unlock(String orderType) {

        String threadId = RANDOMID+ Thread.currentThread().getId();
        String value = srt.opsForValue().get("lock:setnx:" + orderType);
        if(threadId.equals(value)){
            srt.delete("lock:setnx:" + orderType);
        }

    }

    // 查询库存数量
    public String queryCount(String orderType) {
        // 查询缓存
        String s = srt.opsForValue().get("lock_order_" + orderType);
        // 命中返回
        if (Objects.nonNull(s)) {
            return s;
        }

        try {
            //未命中，获取锁
            if (getRedissonLock(orderType)) {
                // 模拟查询数据库
                String count = getCountByType(orderType);
                Thread.sleep(200);
                //成功操作缓存
                srt.opsForValue().set("lock_order_" + orderType, count, 100L, TimeUnit.SECONDS);
                return count;
            }
            //失败重试
            Thread.sleep(50);

            return queryCount(orderType);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock(orderType);
        }


    }

    private String getCountByType(String orderType) {
        System.out.println("query db");
        return ORDER_COUNT;
    }

    // 互斥锁
    private boolean getLock(String orderType) {
        //
        long id = Thread.currentThread().getId();
        Boolean flag = srt.opsForValue().setIfAbsent("lock:setnx:" + orderType, RANDOMID+id, 100L, TimeUnit.SECONDS);
        if (flag) {
            System.out.println(RANDOMID+id + " get lock");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean getRedissonLock(String orderType) throws InterruptedException {
        RLock lock = redissonClient.getLock(orderType);
        System.out.println("redissonClient: "+ redissonClient);
        boolean b = lock.tryLock(1, -1, TimeUnit.SECONDS);
        return b;
    }

}
