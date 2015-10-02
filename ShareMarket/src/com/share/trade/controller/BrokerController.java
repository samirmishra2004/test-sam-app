package com.share.trade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.BrokerBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.common.OrderUtil;
import com.share.trade.common.ShareUtil;
import com.share.trade.common.order.EquityOrder;
import com.share.trade.common.order.FutureOrder;
import com.share.trade.database.BrokerDetail;

@Controller
public class BrokerController {
	Logger log=Logger.getLogger(this.getClass().getName());
	BrokerBD brokerBD =new BrokerBD();
	@RequestMapping("/viewBrokerDetail")
	public ModelAndView viewBrokerDetail(){
		ModelAndView mvc =new ModelAndView("BrokerDetail");
		BrokerDetail bd=brokerBD.getBrokerDetail(ShareUtil.BROKER_SHAREKHAN);
		mvc.addObject("brokerDetail",bd);
		return mvc;
	}
	@RequestMapping("/updateBrokerDetail")
	public ModelAndView saveBrokerDetail(HttpServletRequest req,HttpServletResponse res){
		ModelAndView mvc =new ModelAndView("BrokerDetail");
		log.info("req.userName"+req.getParameter("brkrUserId"));
		log.info("req.password1"+req.getParameter("brkrPwd1"));
		log.info("req.password2"+req.getParameter("brkrPwd2"));
		String usr=req.getParameter("brkrUserId");
		String pwd1=req.getParameter("brkrPwd1");
		String pwd2=req.getParameter("brkrPwd2");
		
		if(!MethodUtil.isEmpty(usr)&&
				!MethodUtil.isEmpty(pwd1)&&	
				!MethodUtil.isEmpty(pwd2)){
			BrokerDetail bd=new BrokerDetail(ShareUtil.BROKER_SHAREKHAN,usr,pwd1,pwd2,new Date());
			
			brokerBD.saveBrokerDetail(bd);	
		}
		
		mvc=viewBrokerDetail();
		return mvc;
	}
	@RequestMapping("/placeorder")
	public void placeOrder(HttpServletRequest req,HttpServletResponse res){
		ModelAndView mvc =new ModelAndView("incorrectUrl");		
		res.setContentType("text/html");
		String message="order success";
		boolean isEquityScript=false;
		boolean isFutureScript=false;
		
		try {
			String futureScript=req.getParameter("tradingScript");
			String watcherScript=req.getParameter("watcherScript");
			String tradeQuantity=req.getParameter("tradeQuantity");
			String buyOrSell=req.getParameter("buyOrSell");
			String futureScriptName="";
			String futureMonth="";
			String token="";
			String scriptName=null;
			String expiryDateStr=null;
			
			if (!MethodUtil.isEmpty(futureScript)) {
				String[] fotureScriptNameAndMonth = futureScript
						.trim().split(" ");
				int length=fotureScriptNameAndMonth.length;
				
				if(length==3){
					scriptName = fotureScriptNameAndMonth[0];
					expiryDateStr = fotureScriptNameAndMonth[1];
					token = fotureScriptNameAndMonth[2];
					isFutureScript=true;
				}
				
				if(length==1){
					scriptName = fotureScriptNameAndMonth[0];
					isEquityScript=true;
				}
			
			}
			
			System.out.println("isEquityScript: "+isEquityScript);
			if(isEquityScript){
			EquityOrder eo = new EquityOrder();			
				eo.setBuyOrSell(buyOrSell);
				eo.setLotSize(Long.parseLong(tradeQuantity));								
				eo.setScriptName(scriptName);
				//if(stg.isTradeOnMarket()){
					eo.setPrice(0);
				//	}else{
				//		eo.setPrice(0);	
				//	}			
				 
				/*EquityOrder eo = new EquityOrder();			
				eo.setBuyOrSell("B");// trade strategy
				eo.setLotSize(Long.parseLong("100"));
				//eo.setPrice("")
				eo.setScriptName("SAIL");
				eo.setIsSquareOff("true");
				
					eo.setPrice(51.45);	*/ 
				eo.placeOrderManual();
			}	
		if(isFutureScript)	{
		FutureOrder futureOrder=new FutureOrder();
		
		if(!MethodUtil.isEmpty(futureScript)){
			String[] fotureScriptNameAndMonth=futureScript.trim().split(" ");
			futureScriptName=fotureScriptNameAndMonth[0];
			futureMonth=fotureScriptNameAndMonth[1];
			token =fotureScriptNameAndMonth[2];
			
		}
		
		//create order
		futureOrder.setBuyOrSell(buyOrSell);
		futureOrder.setLotSize(Long.parseLong(tradeQuantity));		
		futureOrder.setScriptName(futureScriptName);
		futureOrder.setMonthString(futureMonth);
		futureOrder.setNameAndMonth(futureScriptName+" "+futureMonth);
		futureOrder.setToken(token);
		
			futureOrder.placeOrderManual();
		}
		} catch (Exception e) {
			message="error while placing order";
			e.printStackTrace();
		}
		try {
		 PrintWriter rw=	res.getWriter();
		 rw.write(message);
		 } catch (IOException e) {
			
			e.printStackTrace();
		}
		mvc.addObject("message",message);
		//return mvc;
	}

}
