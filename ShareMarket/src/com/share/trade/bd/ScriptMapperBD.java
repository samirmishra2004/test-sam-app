package com.share.trade.bd;

import java.util.HashMap;
import java.util.List;

import com.share.trade.common.MethodUtil;
import com.share.trade.common.ShareUtil;
import com.share.trade.dao.ScriptMapperDAO;
import com.share.trade.database.ScriptMapper;
import com.share.trade.vo.ScriptMapperDTO;

public class ScriptMapperBD {
	
	ScriptMapperDAO mapperDAO=new ScriptMapperDAO();
	
	public List<ScriptMapper> getMappedScript(){
		List<ScriptMapper> mappedScripts= mapperDAO.getMappedScripts();
		HashMap<String, String> mappedScriptMap=new HashMap<String, String>();
		
		for(ScriptMapper ms:mappedScripts){
			if(ms.getIsActive()!=null&&Integer.parseInt(ms.getIsActive())>0)
			mappedScriptMap.put(ms.getWtcher_script(),ms.getBroker_script());
		}
		ShareUtil.WATCHER_SCRIPT_SET.clear();
		ShareUtil.WATCHER_SCRIPT_SET.addAll(mappedScriptMap.keySet());
		return mappedScripts;
	}
	public ScriptMapper getMappedScriptByScriptName(String watcherName){
		return mapperDAO.getMappedScriptByName(watcherName);
		
	}
	public void addNewScript(ScriptMapper  mapperDTO){
		try {
			mapperDAO.addScrip(mapperDTO);
		} catch (Exception e) {
			System.err.println("Exception occurred while adding new script");
		}
		MethodUtil.createCronObjectMap();
	}

	public void updateScript(ScriptMapper  mapperDTO){
		try {
			mapperDAO.updateScrip(mapperDTO);
		} catch (Exception e) {
			System.err.println("Exception occurred while adding new script");
		}
		MethodUtil.createCronObjectMap();
	}
	public void deleteScript(ScriptMapper  mapperDTO){
		try {
			mapperDAO.deleteScript(mapperDTO);
		} catch (Exception e) {
			System.err.println("Exception occurred while adding new script"+e.getMessage());
		}
		MethodUtil.createCronObjectMap();
	}
}
