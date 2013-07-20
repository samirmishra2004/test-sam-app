package com.share.trade.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.MethodUtil;
import com.share.trade.common.PMF;
import com.share.trade.database.ScriptMapper;
import com.share.trade.vo.ScriptMapperDTO;
import com.share.trade.vo.ShareBucket;

public class ScriptMapperDAO {
	
	public void updateScrip(ScriptMapper updatScripDTO) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try {
			ScriptMapper s = pm.getObjectById(ScriptMapper.class, updatScripDTO.getKey());
		    s.setBroker_script(updatScripDTO.getBroker_script());
		    s.setIsActive(updatScripDTO.getIsActive());
			s.setUpdatedDate(updatScripDTO.getUpdatedDate());
			s.setWtcher_script(updatScripDTO.getWtcher_script());
			s.setTradeQuantity(updatScripDTO.getTradeQuantity());
			
		} finally {
		    pm.close();
		}
		}
	
	public void addScrip(ScriptMapper newScripDTO) throws Exception{
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction(); 
		   try { 
		        tx.begin(); 
		        Query q=pm.newQuery(ScriptMapper.class);
		         pm.makePersistent(newScripDTO);
		        tx.commit(); 
		        

				
		    } catch (Exception ex) { 
		        if (tx.isActive()) { 
		            tx.rollback(); 
		        } 
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
	}
	
public boolean deleteScript(ScriptMapper shares)throws Exception{
	PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ArrayList<Object> ol=new ArrayList<Object>();
		
		Object o=pm.getObjectById(ScriptMapper.class,shares.getKey());
		
		 
		   try { 
		       
			   pm.deletePersistent(o);
				
				
				
		    } catch (Exception ex) { 
		    	
		    	
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
		//System.out.println("in dao:"+ share.getKey());
		
		return true;
	}
		
	public List<ScriptMapper> getMappedScripts(){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		List<ScriptMapper> mapperDTOs=new ArrayList<ScriptMapper>();
		Query q=pm.newQuery(ScriptMapper.class);
		//q.setFilter("isActive == activeForTrade");		
		//q.declareParameters("String activeForTrade");

	
		mapperDTOs=(List<ScriptMapper>)q.execute();
		
				
		return mapperDTOs;
	}
	public ScriptMapper getMappedScriptByName(String watcherName){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		List<ScriptMapper> mapperDTOs=new ArrayList<ScriptMapper>();
		Query q=pm.newQuery(ScriptMapper.class);
		q.setFilter("wtcher_script == wtchScrpt");		
		q.declareParameters("String wtchScrpt");

		Object result=q.execute(watcherName);
		
		if(result!=null){
		mapperDTOs=(List<ScriptMapper>)q.execute(watcherName);
		}
		
		if(mapperDTOs.size()>0)return mapperDTOs.get(0);else return null;
	}
}
