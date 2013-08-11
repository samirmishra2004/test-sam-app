package com.share.trade.controller;

import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.FutureGapTradeBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.common.ShareUtil;
import com.share.trade.database.FutureGapScript;

@Controller
public class FutGapUpTradeController {

	
	Logger log=Logger.getLogger(this.getClass().getName());
	FutureGapTradeBD bd=new FutureGapTradeBD();
	
	@RequestMapping("/viewFnOGapStrategy")
	public ModelAndView viewMappedFnoScripts(){
		ModelAndView mvc =new ModelAndView("FutureScripts");
		
		bd.getMappedScript();
		mvc.addObject("mapperDTOs",ShareUtil.WATCHER_FNO_SCRIPT_SET);
		return mvc;
	}
	@RequestMapping("/addOrUpdateFnoScript")
	public ModelAndView addOrUpdateFnoScript(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mvc =new ModelAndView("FutureScriptsContent");
		
		String scId=request.getParameter("scId");
		String scUrl=request.getParameter("watcherScriptUrl");
		String expD1=request.getParameter("exp1");
		String expD2=request.getParameter("exp2");
		String trdSc1=request.getParameter("trdsc1");
		String trdSc2=request.getParameter("trdsc2");
		String lotSize=request.getParameter("lotSize");

		
		FutureGapScript script=new FutureGapScript();
		if(!MethodUtil.isEmpty(scId))
		script.setKey(Long.parseLong(scId));
		script.setWatcherScriptUrl(scUrl);
		script.setExpDate1(expD1);
		script.setExpDate2(expD2);
		script.setTradeScript1(trdSc1);
		script.setTradeScript2(trdSc2);
		if(!MethodUtil.isEmpty(lotSize))
		script.setLotSize(Long.parseLong(lotSize));
		script.setUpdateDate(new Date());
		bd.addOrUpdateScript(script);
		mvc.addObject("mapperDTOs",ShareUtil.WATCHER_FNO_SCRIPT_SET);
		
		return mvc;
	}
	@RequestMapping("/deleteFnoScript")
	public ModelAndView deleteFnoScript(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mvc =new ModelAndView("FutureScriptsContent");
		String scId=request.getParameter("scriptId");
		FutureGapScript script=new FutureGapScript();
		script.setKey(Long.parseLong(scId));
		bd.deleteScript(script);
		mvc.addObject("mapperDTOs",ShareUtil.WATCHER_FNO_SCRIPT_SET);

		return mvc;
	}
}
