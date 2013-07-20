package com.share.trade.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.TradeSummaryBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.database.TradeSummary;
import com.share.trade.vo.TradeSummaryVo;

@Controller
public class TradeSummaryController {
	TradeSummaryBD tradeSummaryBD=new TradeSummaryBD();
	@RequestMapping("/viewtradesumary")
	public ModelAndView viewTradeSummary(){
		ModelAndView mvc=new ModelAndView("monthlysummary");
		List<TradeSummary> tsl=tradeSummaryBD.getTradeMonthlySummary();
		List<TradeSummary> tslf=new ArrayList<TradeSummary>();
		TradeSummaryVo tsv=null;
		double totalProfitLoss=0;
		double totalBuy=0;
		double totalSell=0;
		SimpleDateFormat sdf = new SimpleDateFormat ("MMM-dd");
		sdf.setTimeZone (TimeZone.getTimeZone ("IST"));
		for(TradeSummary ts:tsl){
			tsv=new TradeSummaryVo();
			totalBuy=totalBuy+ts.getBuyPrice();
			totalSell=totalSell+ts.getSellPrice();
			//System.out.println("formated buyp "+MethodUtil.roundOff(ts.getBuyPrice()));
			//ts.setBuyAmt(MethodUtil.roundOff(ts.getBuyPrice())+"");
			//tsv.setSellAmt(MethodUtil.roundOff(ts.getSellPrice())+"");
			
			
				ts.setFormatedDate(sdf.format(ts.getTradeDate()));
				tslf.add(ts);
			
		}
		
		totalProfitLoss=totalSell-totalBuy;
		totalProfitLoss=MethodUtil.roundOff(totalProfitLoss);
		mvc.addObject("totalProfitLoss",totalProfitLoss);
		mvc.addObject("tradeSummaryList",tslf);
		return mvc;
	}
}
