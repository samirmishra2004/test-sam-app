package com.share.trade.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.ScriptMapperBD;
import com.share.trade.bd.TradeSummaryBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.database.ScriptMapper;
import com.share.trade.database.TradeSummary;
import com.share.trade.vo.TradeSummaryVo;

@Controller
public class TradeSummaryController {
	TradeSummaryBD tradeSummaryBD=new TradeSummaryBD();
	private ScriptMapperBD mapperBD=new ScriptMapperBD();
	@RequestMapping("/viewtradesumary")
	public ModelAndView viewTradeSummary(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mvc=new ModelAndView("monthlysummary");
		String filterScript=request.getParameter("scriptName");
		System.out.println("Filter script: "+filterScript);
		List<ScriptMapper> sml=mapperBD.getMappedScript();
		List<TradeSummary> tsl=tradeSummaryBD.getTradeMonthlySummary(filterScript);
		List<TradeSummary> tslf=new ArrayList<TradeSummary>();
		TradeSummaryVo tsv=null;
		double totalProfitLoss=0;
		double totalBuy=0;
		double totalSell=0;
		SimpleDateFormat sdf = new SimpleDateFormat ("MMM-dd");
		sdf.setTimeZone (TimeZone.getTimeZone ("IST"));
		if(tsl!=null){
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
		}
		totalProfitLoss=totalSell-totalBuy;
		totalProfitLoss=MethodUtil.roundOff(totalProfitLoss);
		mvc.addObject("totalProfitLoss",totalProfitLoss);
		mvc.addObject("tradeSummaryList",tslf);
		mvc.addObject("scriptMaperList",sml);
		mvc.addObject("filterScript",filterScript);
		return mvc;
	}
}
