package com.cqrs.query.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/p2p")
    public void pointToPointQueryView(){}

    @GetMapping("/subscription")
    public void subscriptionQueryView(){}

    @GetMapping("/scatter-gather")
    public void scatterGatherQueryView(){}
}
