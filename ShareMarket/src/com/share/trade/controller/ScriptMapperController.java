package com.share.trade.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.ScriptMapperBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.database.ScriptMapper;
import com.share.trade.vo.ScriptMapperDTO;

@Controller
public class ScriptMapperController {
	ScriptMapperBD mapperBD=new ScriptMapperBD();
	
	@RequestMapping("/showMappedScript")
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		List<ScriptMapper> mapperDTOs=mapperBD.getMappedScript();
		if(mapperDTOs!=null){
		System.out.println("script list size= "+mapperDTOs.size());
		for(ScriptMapper mapper:mapperDTOs){
			System.out.println("script list size= "+mapper.getBroker_script());
	
		}
		
		}
		ModelAndView mvc=new ModelAndView("ScriptMapper");
		mvc.addObject("mapperDTOs",mapperDTOs );
		
		return mvc;
		
	}
	
	@RequestMapping("/addnewscript")
	public ModelAndView addScript(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String watcherScript=request.getParameter("watcherScript");
		String traderScript=request.getParameter("traderScript");
		String tradeQuantity=request.getParameter("tradeQuantity");
		
		System.out.println("adding watcherScript: "+watcherScript);
		//boolean isError=MethodUtil.checkAccountBalance(tradeQuantity);
		
		ScriptMapper mapperDTO=new ScriptMapper();
		mapperDTO.setBroker_script(traderScript);
		mapperDTO.setHighBeta("0");
		mapperDTO.setIsActive("0");
		mapperDTO.setWtcher_script(watcherScript);
		mapperDTO.setTradeQuantity(tradeQuantity);
		
		mapperDTO.setUpdatedDate(new Date());
		
		mapperBD.addNewScript(mapperDTO);
		 
		ModelAndView mvc=execute(request, response);
		
		return mvc;
		
	}
	
	@RequestMapping("/updatescript")
	public ModelAndView updateScript(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String watcherScript=request.getParameter("watcherScript");
		String traderScript=request.getParameter("traderScript");
		String highBeta=request.getParameter("highBeta");
		String isActive=request.getParameter("activeChkBox");
		String scriptId=request.getParameter("scriptId");
		String tradeQuantity=request.getParameter("tradeQuantity");
		System.out.println("updating watcherScript: "+watcherScript+" scriptid "+scriptId);
		ScriptMapper mapperDTO=new ScriptMapper();
		mapperDTO.setKey(Long.parseLong(scriptId));
		mapperDTO.setBroker_script(traderScript);
		mapperDTO.setHighBeta(highBeta);
		mapperDTO.setIsActive(isActive);
		mapperDTO.setWtcher_script(watcherScript);
		mapperDTO.setTradeQuantity(tradeQuantity);
		mapperDTO.setUpdatedDate(new Date());
		
		mapperBD.updateScript(mapperDTO);
		return execute(request, response);
		
	}
	
	@RequestMapping("/deletescript")
	public ModelAndView deleteScript(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String watcherScript=request.getParameter("watcherScript");
		String traderScript=request.getParameter("traderScript");
		String isActive=request.getParameter("activeChkBox");
		String scriptId=request.getParameter("scriptId");
		System.out.println("deleting watcherScript: "+watcherScript+" scriptId "+scriptId);
		ScriptMapper mapperDTO=new ScriptMapper();
		mapperDTO.setKey(Long.parseLong(scriptId));
		mapperDTO.setBroker_script(traderScript);
		mapperDTO.setIsActive(isActive);
		mapperDTO.setWtcher_script(watcherScript);
		mapperDTO.setUpdatedDate(new Date());
		
		mapperBD.deleteScript(mapperDTO);
		return execute(request, response);
		
		
	}

}
