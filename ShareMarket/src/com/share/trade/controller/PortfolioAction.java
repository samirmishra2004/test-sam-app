package com.share.trade.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import sun.security.action.GetBooleanAction;


import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.share.trade.bd.PortfoliyoBD;
import com.share.trade.common.ShareUtil;
import com.share.trade.vo.PotfoliyoForm;
import com.share.trade.vo.ShareBucket;
@Controller
public class PortfolioAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	PortfoliyoBD bd=new PortfoliyoBD();
	

@RequestMapping("/viewPortfoliyo")
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse responses,PotfoliyoForm formObj ) throws Exception{
		//HttpServletRequest req=getRequest();
		PotfoliyoForm form=(PotfoliyoForm)formObj;
		List<ShareBucket> shareList=getSavedShareList();
		bd.getDistinctShare();
		System.out.println("PORTFOLIYO_SCRIPTS: "+ShareUtil.WATCHER_SCRIPT_SET);
		//req.setAttribute("ShareList", shareList);
		//req.get
		
		return new ModelAndView("Portfolio","ShareList", shareList).addObject("portFoliyoForm",form);
		
	}
@RequestMapping("/addToPortfoliyo")
	public ModelAndView addToPortfolio(HttpServletRequest request, HttpServletResponse responses,PotfoliyoForm formObj) throws Exception{
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat dateFormat= new SimpleDateFormat();
		ShareBucket bucket=new ShareBucket();
		PotfoliyoForm form=(PotfoliyoForm)formObj;
		
		bucket.setsSymbol(form.getAdSymbol());
		bucket.setsBuyPrice(form.getAdBuyPrice());
		bucket.setsQuantity(form.getAdQuantity());
		Date buyDate=null;	
		if(form.getAdBuyDate()==null||"".equals(form.getAdBuyDate()))
			buyDate=new Date();
		else
			buyDate=dateFormat.parse(form.getAdBuyDate());	
			System.out.println(buyDate);
		bucket.setsBuyDate(buyDate);
		System.out.println("add symbol: "+form.getAdSymbol());
		
		
		/*
		 * Save the detail
		 */
		bd.savePortfoliyo(bucket);
		
		//response.sendRedirect("portfoliyo.action");
		return new ModelAndView("Portfolio","portFoliyoForm",form);
	}
	
	
@RequestMapping("/removePortfoliyo")
	public ModelAndView deletePortfolio(HttpServletRequest request, HttpServletResponse responses) throws Exception{
		String deleteList=request.getParameter("deleteList");
		//System.out.println("deleteList: "+deleteList);
		ShareBucket share=null;
		
		if(deleteList!=null&&!"".equals(deleteList.trim())){
			StringTokenizer tokenizer=new StringTokenizer(deleteList,"|");
			
			String[] sharekeyArr=deleteList.trim().split("|");
			//System.out.println("sharekeyArr length : "+sharekeyArr.length);
			List<ShareBucket> sb=new ArrayList<ShareBucket>();
				while(tokenizer.hasMoreTokens()){
					String t=tokenizer.nextToken();
					share=new ShareBucket();
					if(!"".equals(t)){
						System.out.println("deliting "+t);
						
					share.setKey(new Long(t));
					sb.add(share);
					
					}
				}
				
				bd.deleteFromPortfoliyo(sb);
		
		}
		
		//response.sendRedirect("portfoliyo.action");
		return execute(request, responses, new PotfoliyoForm());
	}
	
	public List<ShareBucket> getSavedShareList(){
		//System.out.println("print share list"+bd.getSavedShareList());
		return bd.getSavedShareList();
	}


	
}
