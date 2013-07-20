package com.share.trade.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

	@RequestMapping("/hello")
	public ModelAndView helloWorld() {
		ModelAndView mvc=new ModelAndView("hello");
		String message = "Hello World, Spring 3.0!";
		mvc.addObject("message",message);
		
		return mvc;
	}
}