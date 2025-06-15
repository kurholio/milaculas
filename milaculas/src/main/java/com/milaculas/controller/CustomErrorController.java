package com.milaculas.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomErrorController implements ErrorController {

	@GetMapping("/error")
	@ResponseBody
    public String handleError() {
        return "Oops! Something went wrong.";
    }
}