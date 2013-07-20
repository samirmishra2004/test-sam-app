package com.share.trade.bd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jdo.Query;

import com.share.trade.common.ShareUtil;
import com.share.trade.dao.PortfoliyoDAO;
import com.share.trade.vo.ShareBucket;

public class PortfoliyoBD {
	PortfoliyoDAO dao=new PortfoliyoDAO();
	public void savePortfoliyo(ShareBucket share){
		try {
			dao.savePortfoliyo(share);
			addToShareUniqueList(share);
		} catch (Exception e) {
			removeFromShareUniqueList(share);
		}
	}
	
	public List<ShareBucket> getSavedShareList(){
		System.out.println("print share list");
		List<ShareBucket> shareList=dao.getPortfoliyo();
		return shareList;
	} 
	
	public boolean deleteFromPortfoliyo(List<ShareBucket> shares){
		System.out.println("deleting shares: "+shares);
		try {
			dao.deleteFromPortfoliyo(shares);
			for(ShareBucket b:shares){
				//removeFromShareUniqueList(b);
				}
		} catch (Exception e) {
			System.err.println("Exception indeleteFromPortfoliyo "+e.getMessage());
			for(ShareBucket b:shares){
			addToShareUniqueList(b);
			}
		}
		System.out.println(" "+shares);
		return true;
	}
	
	public List<ShareBucket> getDistinctShare(){
		
		List<ShareBucket> shareList=null;
		//shareList=dao.getDistinctShare();
		return shareList;
	}
	
	public Set<String> getUniqueShareList(){
		List<String> allShareNameList=new ArrayList<String>();
		
		allShareNameList=dao.getDistinctShare();
		
		for(String s:allShareNameList){
			if(!ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.containsKey(s)){
				ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.put(s, 1);
			}else{
				int count=ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.get(s);
				ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.put(s, ++count);
			}
		}
		ShareUtil.WATCHER_SCRIPT_SET.addAll(ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.keySet());
		return null;
		
	}
	public void addToShareUniqueList(ShareBucket s){
		
		if(!ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.containsKey(s.getsSymbol())){
				ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.put(s.getsSymbol(), 1);
			}else{
				int count=ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.get(s.getsSymbol());
				ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.put(s.getsSymbol(), ++count);
			}
		
		ShareUtil.WATCHER_SCRIPT_SET.addAll(ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.keySet());
		//return uniqueNameList;
		
	}
	public void removeFromShareUniqueList(ShareBucket s){
System.out.println("PORTFOLIYO_SCRIPTS_COUNTER_MAP: "+ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP);
		if(ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.containsKey(s.getsSymbol())){
			int count=ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.get(s.getsSymbol());
			if(count<=1)	
			ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.remove(s.getsSymbol());
			else
			ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.put(s.getsSymbol(), --count);	
			}else{
				System.out.println("List does not have value to remove");
			}
		
		ShareUtil.WATCHER_SCRIPT_SET.addAll(ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.keySet());
		//return uniqueNameList;
		
	}
	
}
