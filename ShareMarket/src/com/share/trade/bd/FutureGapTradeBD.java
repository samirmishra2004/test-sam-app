package com.share.trade.bd;

import java.util.HashMap;
import java.util.List;

import com.share.trade.common.MethodUtil;
import com.share.trade.common.ShareUtil;
import com.share.trade.dao.FutureGapTradeDAO;
import com.share.trade.database.FutureGapScript;
import com.share.trade.vo.FutureScriptQuote;

public class FutureGapTradeBD {

	
	FutureGapTradeDAO futureGapDAO=new FutureGapTradeDAO();
	
	public List<FutureGapScript> getMappedScript(){
		List<FutureGapScript> mappedScripts= futureGapDAO.getMappedScripts();
		HashMap<String, String> mappedScriptMap=new HashMap<String, String>();
		
		for(FutureGapScript ms:mappedScripts){
			
			//mappedScriptMap.put(ms.getWatcherScriptUrl(),ms.getWatcherScriptUrl().substring(0,ms.getWatcherScriptUrl().indexOf("/")));
		}
		ShareUtil.WATCHER_FNO_SCRIPT_SET.clear();
		ShareUtil.WATCHER_FNO_SCRIPT_SET.addAll(mappedScripts);
		return mappedScripts;
	}
	public FutureGapScript getMappedScriptByScriptName(String scurl){
		return futureGapDAO.getMappedScriptByName(scurl);
		
	}
	public void addOrUpdateScript(FutureGapScript  mapperDTO){
		try {
			
			System.out.println("Key in bd :"+mapperDTO.getKey()+"");
			if(mapperDTO.getKey()==null||mapperDTO.getKey()<=0)
			futureGapDAO.addScrip(mapperDTO);
			else
				updateScript(mapperDTO);
		} catch (Exception e) {
			System.err.println("Exception occurred while adding new fno script");
		}
		MethodUtil.createCronObjectMap();
	}

	public void updateScript(FutureGapScript  mapperDTO){
		try {
			futureGapDAO.updateScrip(mapperDTO);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception occurred while adding new script");
		}
		MethodUtil.createCronObjectMap();
	}
	public void deleteScript(FutureGapScript  mapperDTO){
		try {
			futureGapDAO.deleteScript(mapperDTO);
		} catch (Exception e) {
			System.err.println("Exception occurred while adding new script"+e.getMessage());
		}
		MethodUtil.createCronObjectMap();
	}
	
	public void updateCalculatedGap(FutureGapScript gs){
		futureGapDAO.updateCalculatedGap(gs);
	}
}
