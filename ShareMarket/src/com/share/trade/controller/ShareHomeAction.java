package com.share.trade.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.ShareHomeBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.common.ShareUtil;
import com.share.trade.database.ActivityLog;

@Controller
public class ShareHomeAction{
		
	String homePageString ="";
	
	public String getHomePageString() {
		return homePageString;
	}


	public void setHomePageString(String homePageString) {
		this.homePageString = homePageString;
	}

		ShareHomeBD homeBD=new ShareHomeBD();
	
		@RequestMapping("/showActiveShares")
		public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
			setHomePageString(homeBD.getPageFromMoneyControll());
			ModelAndView mvc=new ModelAndView("ShareHome");
			mvc.addObject("homePageString",getHomePageString() );
			//System.out.println("homePageString: "+getHomePageString().length());
			int pagelength=getHomePageString().length();
			System.out.println("PageSize : "+(pagelength*2)/1024+" KB");
			return mvc;
			
		}

		@RequestMapping("/tradehome")
		public ModelAndView tradeHome(HttpServletRequest request, HttpServletResponse response) throws Exception{
			
			ModelAndView mvc=new ModelAndView("TradeHome");
			List<ActivityLog> logs=MethodUtil.getUiLog(request.getParameter("logType"));
			
			mvc.addObject("logs",logs);
			
			return mvc;
			
		}
		
		@RequestMapping("/refreshlog")
		public ModelAndView refreshLog(HttpServletRequest request, HttpServletResponse response) throws Exception{
			
			ModelAndView mvc=new ModelAndView("LogPage");

			List<ActivityLog> logs=MethodUtil.getUiLog(request.getParameter("logType"));
			
			mvc.addObject("logs",logs);
			System.out.println("Refresh method called @ "+new Date());
			
			return mvc;
			
		}

}
