package com.share.trade.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mortbay.log.Log;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Text;
import com.share.trade.common.PMF;
import com.share.trade.common.ShareUtil;
import com.share.trade.vo.FutureScriptQuote;
import com.share.trade.vo.ShareBean;
import com.share.trade.vo.StaticHtmlStore;

public class ShareHomeDAO {
	Logger logger=Logger.getLogger(ShareHomeDAO.class.getName());
	
	 private static HashMap<String, ShareBean> stocks = new HashMap<String, ShareBean>();
	public String getInfoFromMoneyControll(){
		String datahtml=getData(ShareUtil.MONEY_CONTROLL_URL);
		
		return datahtml;
	}

	
	@SuppressWarnings("unused")
	public String getData(String url){ 
		String html="";
		Elements elements		=null;
		Query q 				=null;
		Date lastUpdatedDate	=null;
		PersistenceManager pm 	=PMF.getInstance().getPersistenceManager();	
		q = pm.newQuery(StaticHtmlStore.class);
		
		List<StaticHtmlStore> results = (List)q.execute();
		if(results!=null)
			for(StaticHtmlStore htm:results){
				html=htm.getDescription().getValue();
				lastUpdatedDate=htm.getLastUpdatedDate();
			}
			
		System.out.println("result size: "+results.size());
	boolean update=false;
		Calendar lastUpdatedDateCal= Calendar.getInstance();
		if(lastUpdatedDate!=null)
		lastUpdatedDateCal.setTime(lastUpdatedDate);
		else
			lastUpdatedDateCal.setTime(new Date());
		Calendar currentDateCal= Calendar.getInstance();
		int lastUpdatedYr=lastUpdatedDateCal.get(Calendar.YEAR);
		int lastUpdatedMonth=lastUpdatedDateCal.get(Calendar.MONTH);
		int lastUpdatedDt=lastUpdatedDateCal.get(Calendar.DATE);
		int currYr=currentDateCal.get(Calendar.YEAR);
		int currMonth=currentDateCal.get(Calendar.MONTH);
		int currDate=currentDateCal.get(Calendar.DATE);
		if(lastUpdatedYr<currYr){
			update=true;
		}else if(lastUpdatedMonth<currMonth){
			update=true;
		}else if(lastUpdatedDt<currDate){
			update=true;
		}else if(currentDateCal.get(Calendar.HOUR)>=4){
			update=true;
		}
			//logic to fetch the data once in a day at 4:00 PM IST
		//if(lastUpdatedDate!=null&lastUpdatedDateCal.getTime().after(lastUpdatedDate)){
		if(update){	
			try {
				Document doc = Jsoup.connect(url).get();
				elements= doc.select("div[class^=w00]");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println("elements  :"+elements.html());
			//System.out.println("elements  :"+elements.size());
			if(elements!=null){
				Iterator<Element> iterator=elements.listIterator();
				
				
				while(iterator.hasNext()){
					Element element=iterator.next();
					html=element.html();
				}
		
			
				StaticHtmlStore htmlStore = new StaticHtmlStore(new Text(html), new Date(), new Date()) ;
				try {
		            pm.makePersistent(htmlStore);
		        } finally {
		            pm.close();
		        }
			}
		}
		
//		
		return html;
	}
	
	public ShareBean getRealTimeFinanceData(String symbol,long time) throws Exception{
		ShareBean bean=null;
		 System.out.println("in dao in getRealTimeFinanceData");
		bean=getRealTimeQuoteFromMoneyControll(symbol);
		
		return bean;
	}
	/**This is synched so we only do one request at a time
     *If yahoo doesn't return stock info we will try to return it from the map in memory
     *s=symbol,l1 = last trade price, b2=ask ,b3=bid, g= day low, h=day high, p= previous close
	 * @throws Exception 
	 */
    private synchronized ShareBean refreshStockInfo(String symbol, long time) throws Exception {
        
    	System.out.println("in dao in refreshStockInfo");
    	try {
            URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=sl1b2b3ghp&e=.csv");
            URLConnection yc = yahoofin.openConnection();
            yc.setConnectTimeout(1000*60);
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            ShareBean stockInfo;
            System.out.println("in dao in refreshStockInfo");
            while ((inputLine = in.readLine()) != null) {
                String[] yahooStockInfo = inputLine.split(",");
                stockInfo = new ShareBean();
                System.out.println(""+inputLine);
                stockInfo.setCurrentTradePrice(Float.valueOf(yahooStockInfo[1]));
                stockInfo.setCurrentAsk(Float.valueOf(yahooStockInfo[2]));
                stockInfo.setCurrentBid(Float.valueOf(yahooStockInfo[3]));
                stockInfo.setDayLow(Float.valueOf(yahooStockInfo[4]));
                stockInfo.setDayHigh(Float.valueOf(yahooStockInfo[5]));
                stockInfo.setPreviousClose(Float.valueOf(yahooStockInfo[6]));                
               
                stocks.put(symbol, stockInfo);
                System.out.println("symbol 1"+symbol+" object"+stocks);
                break;
            }
            in.close();
        } catch (Exception ex) {
            logger.info("Unable to get stockinfo for: " + symbol + ex);
            throw new Exception("");
        }
    	System.out.println("symbol"+symbol+" object"+stocks);
        return stocks.get(symbol);
     }
    
    public synchronized ShareBean getRealTimeQuoteFromMoneyControll(String scriptId) throws Exception{
    	ShareBean stockInfo = new ShareBean();
    	
    	String url="http://www.moneycontrol.com/mccode/common/get_pricechart_div.php?bse_id=Y&nse_id=Y&sc_id=534149&BNsetick=51.84|01.84&ins=&sc_mapindex=21";
    	url="http://www.moneycontrol.com/mccode/common/get_pricechart_div.php?bse_id=Y&nse_id=Y&sc_id="+scriptId+"&BNsetick=&ins=&sc_mapindex=21";
    	Document doc;
    	try {
    		doc = Jsoup.connect(url).get();
    	
    	//Elements elements= doc.select("div[class^=w00]");
    	Elements elementsNse= doc.select("div[id^=content_nse]");
    	Elements elementsBse= doc.select("div[id^=content_bse]");
    	Float currPrice;
    	Float dayHigh;
    	Float previosClose;
    	Float bidPrice;
    	Float askPrice;
    	Float dayLow;
    	
    	
    	if(elementsNse!=null){
    		Iterator<Element> iterator=elementsNse.listIterator();
    		System.out.println(elementsNse.size());
    		
    		while(iterator.hasNext()){
    			Element element=iterator.next();
    			
    			Elements elementsNseContent= element.select("span[id^=Nse_Prc_tick]");
    			
    			String curPriceStr=elementsNseContent.get(0).text();
    			System.out.println("Current Price NSE : "+curPriceStr);
    			currPrice=Float.valueOf(curPriceStr);
    			
    			//
    			Elements elemtBOPC= element.select("div[class^=gD_12 PB3]");
    			System.out.println("elemtBOPC size "+elemtBOPC.size());
    			//previous close
    			Elements previousClose=elemtBOPC.get(0).getAllElements();
    			
    			System.out.println("previousClose: "+previousClose.get(1).text());
    			previosClose=Float.valueOf(previousClose.get(1).text());
    			
    			//openPrice 
    			Elements openPrice=elemtBOPC.get(1).getAllElements();
    			
    			System.out.println("openPrice: "+openPrice.get(1).text());
    			//openPrice=Float.valueOf(previousClose.get(1).text());
    			
    			//bid  
    			Elements bidPriceAndQnty=elemtBOPC.get(2).getAllElements();
    			
    			System.out.println("bidPrice: "+bidPriceAndQnty.get(1).text().substring(0,bidPriceAndQnty.get(1).text().indexOf("(")-1).trim());
    			bidPrice=Float.valueOf(bidPriceAndQnty.get(1).text().substring(0,bidPriceAndQnty.get(1).text().indexOf("(")-1).trim());
    			//ask  
    			Elements askPriceAndQnty=elemtBOPC.get(3).getAllElements();
    			
    			System.out.println("askPrice: "+askPriceAndQnty.get(1).text().substring(0,askPriceAndQnty.get(1).text().indexOf("(")-1).trim());
    			askPrice=Float.valueOf(askPriceAndQnty.get(1).text().substring(0,askPriceAndQnty.get(1).text().indexOf("(")-1).trim());
    			//
    			Elements elemtDL= element.select("span[class^=PR5]");
    			System.out.println("elemtDHDL size "+elemtDL.size());
    			System.out.println("day low: "+elemtDL.get(0).getAllElements().get(0).text());
    			
    			dayLow = Float.valueOf(elemtDL.get(0).getAllElements().get(0).text());
    			//day low
    			Elements elemtDH= element.select("span[class^=PL5]");
    			System.out.println("elemtDHDL size "+elemtDH.size());
    			
    			System.out.println("day high: "+elemtDH.get(0).text());
    			dayHigh = Float.valueOf(elemtDH.get(0).text());
    			
    			
    			stockInfo.setCurrentTradePrice(currPrice);
                stockInfo.setCurrentAsk(askPrice);
                stockInfo.setCurrentBid(bidPrice);
                stockInfo.setDayLow(dayLow);
                stockInfo.setDayHigh(dayHigh);
                stockInfo.setPreviousClose(previosClose);
    	
    		}
    	}
    	}catch(Exception ex){
    		System.err.println("Error in quote service "+ex.getMessage()+ex);
    		throw new Exception("can not fetch quote"+ex);
    	}
    	return stockInfo;
    }
    public synchronized FutureScriptQuote getRealTimeFutureQuote(String  scUrl) throws Exception{
		
		String url=ShareUtil.MONEY_CONTROL_FNO_URL+scUrl;
		
		
		FutureScriptQuote quote=new FutureScriptQuote();
		Document doc;
    	try {
    		doc = Jsoup.connect(url).timeout(10*1000).get();
    		
    		
    		Elements futureCurPriceElements= doc.select("p[class^=r_28 FL]");
    		System.out.println("Current Price : "+futureCurPriceElements.get(0).text());
    		//set quote
    		quote.setCurrentPrice(futureCurPriceElements.get(0).text());
    		
    		Elements futureQuoteHtmlElements1= doc.select("div[class^=FL PA10 brdr]");
    		
    		Elements futureQuoteHtmlElements2= doc.select("div[class^=FR PA10]");

        	if(futureQuoteHtmlElements1!=null){
        		//Iterator<Element> iterator=futureQuoteHtmlElements1.listIterator();
        		//System.out.println(futureQuoteHtmlElements1.get(0).text());
        		Elements otherParameter=futureQuoteHtmlElements1.get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        		
        		
        		Iterator<Element> ohereElementIterator=otherParameter.listIterator();
        		if(ohereElementIterator.hasNext()){
        			Element element=ohereElementIterator.next();
        			
        			System.out.println("Oherer Param: "+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
        			//set quote
        			quote.setOpenPrice(element.getElementsByTag("td").text());
        			
        			if(ohereElementIterator.hasNext()){
        		    element=ohereElementIterator.next();
        		    logger.info("day high :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
        		    quote.setDayHigh(element.getElementsByTag("td").text());
        			}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();
            		    logger.info("day low :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setDayLow(element.getElementsByTag("td").text());
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();
            		    logger.info("previous close :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setPreviousClose(element.getElementsByTag("td").text());
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();     //escAPE       		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();         //escAPE        		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();
            		    logger.info("bid price :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setBidPrice(element.getElementsByTag("td").text());
            		}
        			//System.out.println("Oherer Param: "+element.html());

        		}
        		
        	}
        	if(futureQuoteHtmlElements2!=null){
        		//Iterator<Element> iterator=futureQuoteHtmlElements2.listIterator();
        		Elements otherParameter=futureQuoteHtmlElements2.get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        		Iterator<Element> ohereElementIterator=otherParameter.listIterator();
        		if(ohereElementIterator.hasNext()){
        			Element element=ohereElementIterator.next();
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();
            		    logger.info("lot size :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setLotSize(element.getElementsByTag("td").text());
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();   
            		    logger.info("Offer Price :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setOffferPrice(element.getElementsByTag("td").text());
            		}
        			//System.out.println("Oherer Param1: "+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
        			//Elements elementsNseContent= element.select("span[id^=gr_28 FL]");
        		}
        	}
    		//System.out.println("previousClose: "+previousClose.get(1).text());
    	}
    	catch (Exception e) {
    		System.err.println("Jsoup can not get the document :"+e.getMessage());
			throw new Exception("Jsoup can not get the document :"+e.getMessage());
		}
		
		return quote;
		
	}
    public static void main(String[] args) {
	//	ShareHomeDAO homeDAO=new ShareHomeDAO();
	//	homeDAO.getRealTimeFutureQuote("sail/SAI/2013-08-29");
	}
}
