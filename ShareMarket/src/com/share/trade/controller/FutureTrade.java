package com.share.trade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class FutureTrade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@RequestMapping("futureTradingHome")
	public ModelAndView showFutureHome(HttpServletRequest req,HttpServletResponse res){
		
		
		return new ModelAndView("FutureTradingHome");
	}
	
}
