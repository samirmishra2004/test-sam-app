package com.share.trade.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ActivityLog implements Comparable<ActivityLog> {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	@Persistent
	private String timeStamp;
	@Persistent
	private String processName;
	@Persistent 
	private String log;
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
	@Override
	public int compareTo(ActivityLog al){
		String dateStr=al.getTimeStamp();
		Date d1=null;
		Date d2=null;
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		try {
			d1=sdf.parse(dateStr);
			d2=sdf.parse(this.timeStamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		System.err.println("Can not sort activity log");
		}
		
		if(d1.after(d2)){
			return 1;
		}else
			if(d2.after(d1)){
				return -1;
			}else{
				return 0;
			}
		
	}

}
