package com.dw.controller;

import com.dw.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/type")
    @ResponseBody
    public String test(@RequestParam(value = "id") String id){

        return orderService.queryCount(id);
    }
}
