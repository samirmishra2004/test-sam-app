package com.share.trade.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.MethodUtil;
import com.share.trade.common.PMF;
import com.share.trade.database.ActivityLog;
import com.share.trade.database.ScriptMapper;

public class CommonDAO {
	public void clearAllLog() throws Exception{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		
		 
		   try {		       
			   pm.deletePersistentAll(pm.newQuery(ActivityLog.class).execute());
							
		    } catch (Exception ex) { 
		    	
		    	
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
		
	}
	public void saveLog(ActivityLog activityLog) throws Exception{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction(); 
		   try { 
		        tx.begin(); 
		        Query q=pm.newQuery(ActivityLog.class);
		         pm.makePersistent(activityLog);
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
	public  List<ActivityLog>  getAllLog(){
		//LinkedList<ActivityLog> activityLogsSet=new LinkedList<ActivityLog>();
		List<ActivityLog> activityLogs=null;
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query query=pm.newQuery(ActivityLog.class);
		query.setOrdering("key desc");
		activityLogs=(List<ActivityLog>)query.execute();
		if(activityLogs!=null){

			//activityLogsSet.addAll(activityLogs);
		}
		return activityLogs;
	}
	public  List<ActivityLog>  getAllLog(String activity){
		//LinkedList<ActivityLog> activityLogsSet=new LinkedList<ActivityLog>();
		List<ActivityLog> activityLogs=null;
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q=pm.newQuery(ActivityLog.class);
		q.setFilter("processName == prs");		
		q.declareParameters("String prs");
		q.setOrdering("key desc");

		activityLogs=(List<ActivityLog>)q.execute(activity);
		if(activityLogs!=null){
			

			//activityLogsSet.addAll(activityLogs);
		
		}
		return activityLogs;
	}
	public void clearLog(String activity) throws Exception{
		List<Object> activityLogs=new ArrayList<Object>();
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q=pm.newQuery(ActivityLog.class);
		if(!MethodUtil.isEmpty(activity)){
		q.setFilter("processName == prs");		
		q.declareParameters("String prs");
		activityLogs=(List<Object>)q.execute(activity);
		}else{
			System.out.println("Executing Elese clear log");
			activityLogs=(List<Object>)q.execute();	
		}
		 try { 
		       
			   pm.deletePersistentAll(activityLogs);
				
				
				
		    } catch (Exception ex) { 
		    	
		    	
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
	}
	public List<ActivityLog> getLogForProcess(String process){
		List<ActivityLog> activityLogs=new ArrayList<ActivityLog>();
		return activityLogs;
	}
}
