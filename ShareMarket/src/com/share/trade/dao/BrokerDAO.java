package com.share.trade.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.PMF;
import com.share.trade.database.BrokerDetail;

public class BrokerDAO {
	public BrokerDetail getBrokerDetailByName(String broker){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		List<BrokerDetail> brokerDetail=new ArrayList<BrokerDetail>();
		Query q=pm.newQuery(BrokerDetail.class);
		q.setFilter("name == broker");		
		q.declareParameters("String broker");

		Object result=q.execute(broker);
		
		if(result!=null){
		brokerDetail=(List<BrokerDetail>)q.execute(broker);
		}
		
		if(brokerDetail.size()>0)return brokerDetail.get(0);else return null;
	}
	public BrokerDetail addOrUpdateBroker(BrokerDetail broker)throws Exception{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		BrokerDetail bdtl=null;
		List<BrokerDetail> brokerDetail=new ArrayList<BrokerDetail>();
		Query q=pm.newQuery(BrokerDetail.class);
		q.setFilter("name == broker");		
		q.declareParameters("String broker");

		Object result=q.execute(broker.getName());
		
		if(result!=null&&((List<BrokerDetail>)result).size()>0){
			//System.out.println("in if condition name"+broker.getName());
		brokerDetail=(List<BrokerDetail>)result;		
		}else{
			//System.out.println("in else condition name"+broker.getName());
			Transaction tx = pm.currentTransaction(); 
			   try { 
			        tx.begin(); 
			         q=pm.newQuery(BrokerDetail.class);
			         pm.makePersistent(broker);
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
		
		if(brokerDetail.size()>0){
			 bdtl=brokerDetail.get(0);
			 //System.out.println("updating  name"+broker.getName());
			try {
				bdtl.setName(broker.getName());
				bdtl.setUserid(broker.getUserid());
				bdtl.setPwd1(broker.getPwd1());
				bdtl.setPwd2(broker.getPwd2());
				bdtl.setUpdatedDate(new Date());
			
			} finally {
			    pm.close();
			}
		}
		return bdtl;
	}
	
	public void deleteBorkerDetail(BrokerDetail bdtl)throws Exception{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		
		Object o=pm.getObjectById(BrokerDetail.class,bdtl.getName());
		
		 
		   try { 
		       
			   pm.deletePersistent(o);
				
				
				
		    } catch (Exception ex) { 
		    	
		    	
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
		//System.out.println("in dao:"+ share.getKey());
		
		
	}
}
