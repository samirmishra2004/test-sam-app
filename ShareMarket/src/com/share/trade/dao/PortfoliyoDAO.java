package com.share.trade.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.MethodUtil;
import com.share.trade.common.PMF;
import com.share.trade.vo.ShareBucket;

public class PortfoliyoDAO {
	PersistenceManager pm 	=PMF.getInstance().getPersistenceManager();
	public void savePortfoliyo(ShareBucket share)throws Exception{
		Transaction tx = pm.currentTransaction(); 
		   try { 
		        tx.begin(); 
		        Query q=pm.newQuery(ShareBucket.class);
		         pm.makePersistent(share);
		        tx.commit(); 
		        
		     
//					if(!ShareUtil.PORTFOLIYO_SCRIPTS.contains(share.getsSymbol())){
//						ShareUtil.PORTFOLIYO_SCRIPTS.add(share.getsSymbol());
//					}
				
		    } catch (Exception ex) { 
		        if (tx.isActive()) { 
		            tx.rollback(); 
		        } 
		        throw ex;
//		        if(ShareUtil.PORTFOLIYO_SCRIPTS.contains(share.getsSymbol())){
//					ShareUtil.PORTFOLIYO_SCRIPTS.remove(share.getsSymbol());
//				}
		    } finally { 
		      pm.close(); 
		    } 
		
		
		
	}

	public List<ShareBucket> getPortfoliyo(){
		pm 	=PMF.getInstance().getPersistenceManager();
		Query q=pm.newQuery(ShareBucket.class);
		List<ShareBucket> shareList=null;
	
		shareList=(List<ShareBucket>) q.execute();
		
		return shareList;
	}
	
	public boolean deleteFromPortfoliyo(List<ShareBucket> shares)throws Exception{
		
		ArrayList<Object> ol=new ArrayList<Object>();
		for(ShareBucket s:shares){
		Object o=pm.getObjectById(ShareBucket.class,s.getKey());
		ol.add(o);
		}
		//Transaction tx = pm.currentTransaction(); 
		   try { 
		       // tx.begin(); 
		       // pm.detachCopyAll(shares);
		        //for(ShareBucket s:shares){
		       // Object o=pm.getObjectById(ShareBucket.class,s.getKey());
		        
				//System.out.println("in dao delete:"+ o);
			   for(int i=0;i<ol.size();i++){
					ShareBucket b= (ShareBucket)ol.get(i);
					MethodUtil.removeFromShareUniqueList(b);
				}
			   
			   pm.deletePersistentAll(ol);
				
				
				
		        //}
		        //tx.commit(); 
		    } catch (Exception ex) { 
		    	
		        //if (tx.isActive()) { 
		          //  tx.rollback(); 
		        //} 
		    	MethodUtil.getUniqueShareList();
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
		//System.out.println("in dao:"+ share.getKey());
		
		return true;
	}
	
	
	public List<String> getDistinctShare(){
		Query q=pm.newQuery(" select sSymbol from com.share.trade.vo.ShareBucket");
		List<String> shareList=new ArrayList<String>();
		List<String> uniqueNameList=new ArrayList<String>();
		//q.setResult("unique ShareBucket.sSymbol");
		shareList=(List<String>) q.execute();
		/*for(String b:shareList){
			if(!ShareUtil.PORTFOLIYO_SCRIPTS.contains(b)){
				ShareUtil.PORTFOLIYO_SCRIPTS.add(b);
			}
		}*/
		System.out.println("in dao :"+shareList);
		return shareList;
		
		
	}
}
