package com.share.trade.common;

import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.share.trade.bd.PortfoliyoBD;
import com.share.trade.bd.ScriptMapperBD;
import com.share.trade.database.ScriptMapper;




public class InitListener implements ServletContextListener,
HttpSessionListener, HttpSessionAttributeListener {
@Override
public void contextInitialized(ServletContextEvent sce) {
	
	MethodUtil.createCronObjectMap();
	
	System.out.println(ShareUtil.WATCHER_SCRIPT_SET);

}

@Override
public void attributeAdded(HttpSessionBindingEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void attributeRemoved(HttpSessionBindingEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void attributeReplaced(HttpSessionBindingEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void sessionCreated(HttpSessionEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void sessionDestroyed(HttpSessionEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void contextDestroyed(ServletContextEvent arg0) {
	// TODO Auto-generated method stub
	
}



} 