package io.swyp.luckybackend.days.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DayController {
    @GetMapping("/")
    public String mainP(){
        return "Hello!! this is LuckyDay's Main Page!!\n test version";
    }
}
