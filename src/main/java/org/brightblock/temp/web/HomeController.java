package org.brightblock.temp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/")
    public @ResponseBody String hello() {
        return "Hello World";
    }

    @RequestMapping("/secure")
    public @ResponseBody String secure() {
        return "Hello World";
    }

    @RequestMapping("/login")
    public @ResponseBody String login() {
        return "Please Login";
    }

}
