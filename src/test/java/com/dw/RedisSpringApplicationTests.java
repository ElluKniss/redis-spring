package com.dw;

import com.dw.domain.User;
import com.dw.service.OrderService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisSpringApplicationTests {

    @Autowired
    private StringRedisTemplate srt;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private OrderService orderService;

    public ExecutorService pool = Executors.newFixedThreadPool(20);


    @Test
    void contextLoads() {
        srt.opsForValue().set("name", "21");
        System.out.println(srt.opsForValue().get("name"));

        User user = new User("dw","21");
        Gson gson = new Gson();
        String s1 = gson.toJson(user);
        srt.opsForValue().set("user",s1 );

        String user1 = srt.opsForValue().get("user");
        User user2 = gson.fromJson(user1, User.class);
        System.out.println(user2);

    }

    @Test
    void testService(){
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setLocation(new ClassPathResource("test.lua"));
        redisScript.setResultType(Long.class);
        redisTemplate.execute(redisScript, Collections.singletonList("[name"), "[daiwei");
    }

    @Test
    void testSetNX(){

        Boolean flag = srt.opsForValue().setIfAbsent("setnx", "21", 100L, TimeUnit.SECONDS);

        if(flag){
            System.out.println(Thread.currentThread().getName()+" get lock");
        }
    }


}
