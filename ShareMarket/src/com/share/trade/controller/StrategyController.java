package com.share.trade.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.StrategyBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.common.ShareUtil;
import com.share.trade.database.Strategy;

@Controller
public class StrategyController{
	Logger log=Logger.getLogger(StrategyController.class.getName());
	StrategyBD bd=new StrategyBD();
	@RequestMapping("/strategy/viewstrategy")
	public ModelAndView execute(HttpServletRequest req,HttpServletResponse resp){
		ModelAndView mvc=new ModelAndView("Strategy");
		
		
		Strategy strategy=bd.getStrategy(ShareUtil.DEFAULTSTRATEGY);
		if(strategy!=null){
			log.info("strategy name "+strategy.getStrategyName());
		}
		mvc.addObject("strategy",strategy);
		return mvc;
	}
	@RequestMapping("/strategy/savestrategy")
	public ModelAndView saveStrategy(HttpServletRequest req,HttpServletResponse resp){
		ModelAndView mvc=new ModelAndView("Strategy");
		
		String buyFactor=req.getParameter("buyFactor");
		String sellFactor=req.getParameter("sellFactor");
		String tradeOnOff=req.getParameter("tradeOnOff");
		String isAutoTrade=req.getParameter("autoTrade");
		String globalSentiment=req.getParameter("globalSentiment");
		String tradeSegment=req.getParameter("tradeSegment");
		String tradeValue=req.getParameter("tradeAmount");
		String isForceSquareOffStr=req.getParameter("forceSquareOff");
		String openPositionHour=req.getParameter("openPositionHour");
		String openPositionMinut=req.getParameter("openPositionMinut");
		String closePositionHour=req.getParameter("closePositionHour");
		String closePositionMinut=req.getParameter("closePositionMinut");
		log.info("buyFactor "+buyFactor);
		log.info("sellFactor "+sellFactor);
		log.info("tradeOnOff "+tradeOnOff);
		log.info("isAutoTrade "+isAutoTrade);
		log.info("globalSentiment "+globalSentiment);
		log.info("tradeSegment "+tradeSegment);
		log.info("isForceSquareOffStr "+isForceSquareOffStr);
		log.info("openPositionHour "+openPositionHour);
		log.info("openPositionMinut "+openPositionMinut);
		log.info("closePositionHour "+closePositionHour);
		log.info("closePositionMinut "+closePositionMinut);
		log.info("tradeValue "+tradeValue);
		
		Strategy stg=new Strategy();
		stg.setBuyFactor(buyFactor);
		stg.setGlobalSentiment(globalSentiment);
		stg.setSellFactor(sellFactor);
		stg.setStrategyName("");
		stg.setTradeOnOff(tradeOnOff);
		stg.setTradeSegment(tradeSegment);
		stg.setTradeAmount(tradeValue);
		stg.setForcesSquareOff(Boolean.parseBoolean(isForceSquareOffStr));
		stg.setAutoTrade(Boolean.parseBoolean(isAutoTrade));
		stg.setPositionOpenHour(openPositionHour);
		stg.setPositionOpenMinut(openPositionMinut);
		stg.setPositionCloseHour(closePositionHour);
		stg.setPositionCloseMinut(closePositionMinut);
		
		if(MethodUtil.isEmpty(buyFactor)&&
				MethodUtil.isEmpty(globalSentiment)&&
				MethodUtil.isEmpty(sellFactor)&&
				MethodUtil.isEmpty(tradeOnOff)&&
				MethodUtil.isEmpty(tradeValue)){
			mvc=execute(req, resp);
			mvc.addObject("ERROR","Mandatory fields are missing");
			return mvc;
		}
		bd.addOrUpdateStrategy(stg);
		mvc.addObject("strategy",stg);
		return mvc;
	}
}
