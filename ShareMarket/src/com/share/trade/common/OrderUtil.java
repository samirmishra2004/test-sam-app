package com.share.trade.common;

import com.share.trade.bd.BrokerBD;
import com.share.trade.common.LenientCookieSpec;
import com.share.trade.common.order.EquityOrder;
import com.share.trade.common.order.FutureOrder;
import com.share.trade.database.BrokerDetail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OrderUtil {
	
	String usr;
	String pwd1;
	String pwd2;
	private static OrderUtil orderUtil=null;
	public static synchronized OrderUtil getInstance(){
		
			orderUtil=new OrderUtil();
		
		return orderUtil;
	}
	
	public OrderUtil() {
		BrokerBD bd=new BrokerBD();
		BrokerDetail brokerDetail=bd.getBrokerDetail(ShareUtil.BROKER_SHAREKHAN);
		usr=brokerDetail.getUserid();
		pwd1=brokerDetail.getPwd1();
		pwd2=brokerDetail.getPwd2();
	}
	
	
	
	static Header[] headers = null;
	
	//required variables
	//userId, password1, password2
	//scriptName, scriptCode, buyOrsell
	//quantity, triggerPrice, orderPrice
	
	/***
	 * this method will place the order in sharekhan trade account
	 * currently it allows only future order
	 * @return
	 */
	public String placeOrder1(FutureOrder fo){
		System.out.println("User: "+usr);
		System.out.println("pwd1: "+pwd1);
		System.out.println("pwd2: "+pwd2);
		System.out.println("BuyOrSell: "+fo.getBuyOrSell());
		System.out.println("fo getNameAndMonth: "+fo.getNameAndMonth());
		System.out.println("fo getLotSize: "+fo.getLotSize());
		System.out.println("fo getPrice: "+fo.getPrice());
		System.out.println("fo getScriptName: "+fo.getScriptName());
		System.out.println("fo getMonthString: "+fo.getMonthString());
		System.out.println("fo token: "+fo.getToken());
		return null;
	}
	
	public  synchronized String placeOrder(FutureOrder fo) {
		
		System.out.println("User: "+usr);
		System.out.println("pwd1: "+pwd1);
		System.out.println("pwd2: "+pwd2);
		System.out.println("BuyOrSell: "+fo.getBuyOrSell());
		System.out.println("fo getNameAndMonth: "+fo.getNameAndMonth());
		System.out.println("fo getLotSize: "+fo.getLotSize());
		System.out.println("fo getPrice: "+fo.getPrice());
		System.out.println("fo getScriptName: "+fo.getScriptName());
		System.out.println("fo getMonthString: "+fo.getMonthString());
		HttpParams httpParams = new BasicHttpParams();
		ClientConnectionManager connectionManager = new GAEConnectionManager();
		// Create a new HttpClient and Post Header
		DefaultHttpClient httpclient = new DefaultHttpClient(connectionManager,
				httpParams);
		httpclient.getCookieSpecs().register("lenient",
				new CookieSpecFactory() {
					public CookieSpec newInstance(HttpParams params) {
						return new LenientCookieSpec();
					}
				});
		HttpClientParams.setCookiePolicy(httpclient.getParams(), "lenient");

		HttpPost httppost = new HttpPost(
				"https://strade.sharekhan.com/rmmweb/mcs.sk");
		HttpContext localContext = new BasicHttpContext();

		CookieStore cookieStore = new BasicCookieStore();
		Cookie cookie = new BasicClientCookie("__utmmobile",
				"0xfe0c66e8e940a67e");

		cookieStore.addCookie(cookie);
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.setCookieStore(cookieStore);

		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		String responsetxt = null;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("login_id", usr));
			nameValuePairs.add(new BasicNameValuePair("br_pwd", pwd1));
			nameValuePairs.add(new BasicNameValuePair("tr_pwd", pwd2));

			nameValuePairs.add(new BasicNameValuePair("collabration", "LBW"));
			nameValuePairs.add(new BasicNameValuePair("e", "350"));
			nameValuePairs.add(new BasicNameValuePair("flag", ""));
			nameValuePairs.add(new BasicNameValuePair("w", "null"));
			nameValuePairs.add(new BasicNameValuePair("submit", "LOGIN"));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost, localContext);

			HttpEntity entity = response.getEntity();
			System.out.println(EntityUtils.toString(entity));
			

			
			headers = response.getAllHeaders();
			for (Header h : headers) {
				// System.out.println(h.getName()+":"+h.getValue());

			}
			CookieStore store = httpclient.getCookieStore();
			for (Cookie c : store.getCookies()) {
				System.out.println(" is expired " + c.isExpired(new Date())
						+ ":" + c.getName() + ":" + c.getExpiryDate());

			}
			try {

				System.out
						.println("###########################Place Order ###################");
				HttpGet getReq = new HttpGet(
						"https://strade.sharekhan.com/rmmweb/mcs.sk?e=120&s="+fo.getScriptName()+"&scode="+fo.getScriptName()+"+"+fo.getMonthString()+"&token="+fo.getToken()+"&ex=NSEFO");

				response = httpclient.execute(getReq, localContext);
				for (Header h : response.getAllHeaders()) {
					System.out.println(h.getName() + ":" + h.getValue());
					if (h.getName().endsWith("Cookie")) {

					}
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String page = sb.toString();
				
				
				//System.out.println(page);
				System.out
				.println("###########################get Order screen ###################");
		 getReq = new HttpGet("https://strade.sharekhan.com/rmmweb/mcs.sk?e=121&s="+fo.getScriptName()+"&scode="+fo.getScriptName()+"+"+fo.getMonthString()+"&token="+fo.getToken()+"&ex=NSEFO"+"&b="+fo.getBuyOrSell());

		response = httpclient.execute(getReq, localContext);
		for (Header h : response.getAllHeaders()) {
			System.out.println(h.getName() + ":" + h.getValue());
			if (h.getName().endsWith("Cookie")) {

			}
		}
		 in = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		 sb = new StringBuffer("");
		 line = "";
		 NL = System.getProperty("line.separator");
		while ((line = in.readLine()) != null) {
			sb.append(line + NL);
		}
		in.close();
		 page = sb.toString();
//System.out.println(page);
				System.out
						.println("###########################search script end- ###################");

				HttpPost request = new HttpPost(
						"https://strade.sharekhan.com/rmmweb/mcs.sk");

				request.setHeader("Set-Cookie",
						"__utmmobile=0xfe0c66e8e940a67e");
				for (Header h : headers) {

					if (h.getName().equals("Set-Cookie")) {
						request.setHeader(h);
					}
				}
				store = httpclient.getCookieStore();
				for (Cookie c : httpclient.getCookieStore().getCookies()) {
					System.out.println(" is expired " + c.isExpired(new Date())
							+ ":" + c.getName() + ":" + c.getExpiryDate());

				}
				// request.setHeaders(headers);
				// Add your data
				nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("flag", "1"));
				nameValuePairs.add(new BasicNameValuePair("request_status",
						"NEW"));
				// nameValuePairs.add(new BasicNameValuePair("disclosed_qty",
				// "0"));
				nameValuePairs.add(new BasicNameValuePair("validity", "1"));
				nameValuePairs.add(new BasicNameValuePair("qty", ""+fo.getLotSize()));
				nameValuePairs
						.add(new BasicNameValuePair("trigger_price", "0"));
				nameValuePairs.add(new BasicNameValuePair("ex", "NSEFO"));
				nameValuePairs.add(new BasicNameValuePair("dp_id", "1564715"));
				// nameValuePairs.add(new BasicNameValuePair("afterhour", "Y"));
				nameValuePairs.add(new BasicNameValuePair("price", ""+fo.getPrice()));
				//nameValuePairs.add(new BasicNameValuePair("validity", "1"));
				nameValuePairs.add(new BasicNameValuePair("s", ""+fo.getScriptName()));
				nameValuePairs.add(new BasicNameValuePair("scode",
						""+fo.getNameAndMonth()));
				nameValuePairs.add(new BasicNameValuePair("token", fo.getToken()));
				nameValuePairs.add(new BasicNameValuePair("e", "121"));
				nameValuePairs.add(new BasicNameValuePair("b", fo.getBuyOrSell()));
				nameValuePairs.add(new BasicNameValuePair("submit",
						"Place Order"));

				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = httpclient.execute(request, localContext);
				for (Header h : response.getAllHeaders()) {
					System.out.println(h.getName() + ":" + h.getValue());
					if (h.getName().endsWith("Cookie")) {

					}
				}
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				sb = new StringBuffer("");
				line = "";
				NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				page = sb.toString();
				System.out.println(page);
				request = new HttpPost(
						"https://strade.sharekhan.com/rmmweb/mcs.sk");

				nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("flag", "2"));
				nameValuePairs.add(new BasicNameValuePair("request_status",
						"NEW"));
				// nameValuePairs.add(new BasicNameValuePair("disclosed_qty",
				// "0"));
				nameValuePairs.add(new BasicNameValuePair("validity", "1"));
				nameValuePairs.add(new BasicNameValuePair("qty", ""+fo.getLotSize()));
				nameValuePairs
						.add(new BasicNameValuePair("trigger_price", "0"));
				nameValuePairs.add(new BasicNameValuePair("ex", "NSEFO"));
				nameValuePairs.add(new BasicNameValuePair("dp_id", "1564715"));
				// nameValuePairs.add(new BasicNameValuePair("afterhour", "Y"));
				nameValuePairs.add(new BasicNameValuePair("price", ""+fo.getPrice()));
				//nameValuePairs.add(new BasicNameValuePair("validity", "1"));
				nameValuePairs.add(new BasicNameValuePair("s", ""+fo.getScriptName()));
				nameValuePairs.add(new BasicNameValuePair("token", fo.getToken()));
				nameValuePairs.add(new BasicNameValuePair("scode",
						""+fo.getNameAndMonth()));
				nameValuePairs.add(new BasicNameValuePair("e", "121"));
				nameValuePairs.add(new BasicNameValuePair("b", fo.getBuyOrSell()));
				nameValuePairs.add(new BasicNameValuePair("submit", "Confirm"));

				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = httpclient.execute(request, localContext);
				for (Header h : response.getAllHeaders()) {
					System.out.println(h.getName() + ":" + h.getValue());
					if (h.getName().endsWith("Cookie")) {

					}
				}
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				sb = new StringBuffer("");
				line = "";
				NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				page = sb.toString();
				System.out
						.println("#################Order Placed###################");
				System.out.println(page);

			} catch (Exception e) {
				e.printStackTrace();
			}
			

			return "sdfsd";
		} catch (ClientProtocolException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		} finally {

		}

	}
	public String getOrderStatus(EquityOrder eo){
		
		return null;
	}
	public String getOrderStatus(FutureOrder fo){
		

		
		System.out.println("User: "+usr);
		System.out.println("pwd1: "+pwd1);
		System.out.println("pwd2: "+pwd2);
		System.out.println("BuyOrSell: "+fo.getBuyOrSell());
		System.out.println("fo getNameAndMonth: "+fo.getNameAndMonth());
		System.out.println("fo getLotSize: "+fo.getLotSize());
		System.out.println("fo getPrice: "+fo.getPrice());
		System.out.println("fo getScriptName: "+fo.getScriptName());
		System.out.println("fo getMonthString: "+fo.getMonthString());
		String status="";
		HttpParams httpParams = new BasicHttpParams();
		ClientConnectionManager connectionManager = new GAEConnectionManager();
		// Create a new HttpClient and Post Header
		DefaultHttpClient httpclient = new DefaultHttpClient(connectionManager,
				httpParams);
		httpclient.getCookieSpecs().register("lenient",
				new CookieSpecFactory() {
					public CookieSpec newInstance(HttpParams params) {
						return new LenientCookieSpec();
					}
				});
		HttpClientParams.setCookiePolicy(httpclient.getParams(), "lenient");

		HttpPost httppost = new HttpPost(
				"https://strade.sharekhan.com/rmmweb/mcs.sk");
		HttpContext localContext = new BasicHttpContext();

		CookieStore cookieStore = new BasicCookieStore();
		Cookie cookie = new BasicClientCookie("__utmmobile",
				"0xfe0c66e8e940a67e");

		cookieStore.addCookie(cookie);
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.setCookieStore(cookieStore);

		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		String responsetxt = null;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("login_id", usr));
			nameValuePairs.add(new BasicNameValuePair("br_pwd", pwd1));
			nameValuePairs.add(new BasicNameValuePair("tr_pwd", pwd2));

			nameValuePairs.add(new BasicNameValuePair("collabration", "LBW"));
			nameValuePairs.add(new BasicNameValuePair("e", "350"));
			nameValuePairs.add(new BasicNameValuePair("flag", ""));
			nameValuePairs.add(new BasicNameValuePair("w", "null"));
			nameValuePairs.add(new BasicNameValuePair("submit", "LOGIN"));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost, localContext);

			HttpEntity entity = response.getEntity();
			System.out.println(EntityUtils.toString(entity));
			

			
			headers = response.getAllHeaders();
			for (Header h : headers) {
				// System.out.println(h.getName()+":"+h.getValue());

			}
			CookieStore store = httpclient.getCookieStore();
			for (Cookie c : store.getCookies()) {
				System.out.println(" is expired " + c.isExpired(new Date())
						+ ":" + c.getName() + ":" + c.getExpiryDate());

			}
			try {

				System.out
						.println("###########################get order Status page ###################");
				HttpGet getReq = new HttpGet(
						"https://strade.sharekhan.com/rmmweb/mcs.sk?e=124&pType=903&ex=NSEFO");

				response = httpclient.execute(getReq, localContext);
				for (Header h : response.getAllHeaders()) {
					System.out.println(h.getName() + ":" + h.getValue());
					if (h.getName().endsWith("Cookie")) {

					}
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String page = sb.toString();
				Document doc=Jsoup.parse(page);
				Elements elements=doc.select("table[class^=rpm mkt]");
	    		String quote=fo.getNameAndMonth();
	    		String orderType=fo.getBuyOrSell();
	    		if("B".equals(orderType)){
	    			orderType="Buy";
	    		}else if("S".equals(orderType)){
	    			orderType="Sell";
	    		}
	    		for(Element e:elements){
	    			
	    			Elements allOrderRows=e.children().get(0).children();
	    			
	    			for(int i=1;i<allOrderRows.size();i++){
	    				Elements tds=allOrderRows.get(i).children();
	    				
	    				for(Element td:tds){
	    					
	    					if(td.text().contains(quote)&&td.nextElementSibling().text().contains(orderType)){
	    						System.out.println("Its "+orderType+" Order");	    						
	    						status=td.nextElementSibling().children().get(1).text();
	    						System.out.println("Status is : "+status);
	    					}
	    					
	    				}
	    				
	    				
	    				
	    			}
	    		}
	    	
				System.out.println("=============End Order Status check===============");
				
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			

			return status;
		} catch (ClientProtocolException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		} finally {

		}

	
		
		
	}
	
	public  synchronized String placeOrder(EquityOrder eo) {
		
		System.out.println("User: "+usr);
       
		
		
		HttpParams httpParams = new BasicHttpParams();
		ClientConnectionManager connectionManager = new GAEConnectionManager();
		// Create a new HttpClient and Post Header
		DefaultHttpClient httpclient = new DefaultHttpClient(connectionManager,
				httpParams);
		httpclient.getCookieSpecs().register("lenient",
				new CookieSpecFactory() {
					public CookieSpec newInstance(HttpParams params) {
						return new LenientCookieSpec();
					}
				});
		HttpClientParams.setCookiePolicy(httpclient.getParams(), "lenient");
		
        HttpPost httppost = new HttpPost("https://strade.sharekhan.com/rmmweb/mcs.sk");
        HttpContext localContext = new BasicHttpContext();
       
        CookieStore cookieStore = new BasicCookieStore();
        Cookie cookie=new BasicClientCookie("__utmmobile", "0xfe0c66e8e940a67e");
        
        cookieStore.addCookie(cookie);
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(cookieStore);
        
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        String responsetxt=null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
           nameValuePairs.add(new BasicNameValuePair("login_id", usr));
           nameValuePairs.add(new BasicNameValuePair("br_pwd", pwd1));
           nameValuePairs.add(new BasicNameValuePair("tr_pwd", pwd2));
           
           nameValuePairs.add(new BasicNameValuePair("collabration", "LBW"));
           nameValuePairs.add(new BasicNameValuePair("e", "350"));
           nameValuePairs.add(new BasicNameValuePair("flag", ""));
           nameValuePairs.add(new BasicNameValuePair("w", "null"));
           nameValuePairs.add(new BasicNameValuePair("submit", "LOGIN"));
           
           httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost,localContext);
            
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity));
           // System.out.println(EntityUtils.getContentCharSet(entity));
            
           // responsetxt=response.getAllHeaders().toString();
            headers=response.getAllHeaders();
            for(Header h:headers){
            	//System.out.println(h.getName()+":"+h.getValue());
            	
            }
           CookieStore store= httpclient.getCookieStore();
           for(Cookie c:store.getCookies()){
        	  System.out.println(" is expired "+c.isExpired(new Date())+":"+c.getName()+":"+c.getExpiryDate()); 
        	  
           }
            try {
            	
            	System.out.println("########################### Start Order Placement ###################");
            	HttpGet getReq=new HttpGet("https://strade.sharekhan.com/rmmweb/mcs.sk?e=352&b="+eo.getBuyOrSell()+"&s="+eo.getScriptName()+"&ex=NSE");
            	 
                 
                 
                 response = httpclient.execute(getReq,localContext);
                 for(Header h:response.getAllHeaders()){
                 	System.out.println(h.getName()+":"+h.getValue());
                 	if(h.getName().endsWith("Cookie")){
                 		
                 	}
                 }
                 BufferedReader  in = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                String page = sb.toString();
                System.out.println(page);
                
            	
            	
            	System.out.println("###########################search script- ###################");
               
                HttpPost request = new HttpPost("https://strade.sharekhan.com/rmmweb/mcs.sk?e=352&b="+eo.getBuyOrSell()+"&t=0&s0="+eo.getScriptName()+"&ex0=NSE");
                
                request.setHeader("Set-Cookie", "__utmmobile=0xfe0c66e8e940a67e");
                for(Header h:headers){
                	
                	if(h.getName().equals("Set-Cookie")){
                		request.setHeader(h);
                	}
                }
                 store= httpclient.getCookieStore();
                for(Cookie c:httpclient.getCookieStore().getCookies()){
             	  System.out.println(" is expired "+c.isExpired(new Date())+":"+c.getName()+":"+c.getExpiryDate()); 
             	  
                }
                //request.setHeaders(headers);
                // Add your data
               nameValuePairs = new ArrayList<NameValuePair>();
             
               nameValuePairs.add(new BasicNameValuePair("flag", "1"));
               nameValuePairs.add(new BasicNameValuePair("request_code", "NEW"));              
               nameValuePairs.add(new BasicNameValuePair("disclosed_qty", "0"));
               nameValuePairs.add(new BasicNameValuePair("qty", ""+eo.getLotSize()));
               nameValuePairs.add(new BasicNameValuePair("trigger_price", "0"));
               nameValuePairs.add(new BasicNameValuePair("ex", "NSE"));
               nameValuePairs.add(new BasicNameValuePair("dp_id", "1564715"));               
               nameValuePairs.add(new BasicNameValuePair("afterhour", "N"));
               nameValuePairs.add(new BasicNameValuePair("price", ""+eo.getPrice()));
               nameValuePairs.add(new BasicNameValuePair("validity", "GFD"));
               nameValuePairs.add(new BasicNameValuePair("s", eo.getScriptName()));
               nameValuePairs.add(new BasicNameValuePair("e", "352"));
               nameValuePairs.add(new BasicNameValuePair("b", eo.getBuyOrSell()));
               nameValuePairs.add(new BasicNameValuePair("submit", "Place Order"));
               
               
               request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                
                
                 response = httpclient.execute(request,localContext);
                 for(Header h:response.getAllHeaders()){
                 	System.out.println(h.getName()+":"+h.getValue());
                 	if(h.getName().endsWith("Cookie")){
                 		
                 	}
                 }
                   in = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));
                 sb = new StringBuffer("");
                 line = "";
                 NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                 page = sb.toString();
                System.out.println(page);
                 request = new HttpPost("https://strade.sharekhan.com/rmmweb/mcs.sk");
                
                nameValuePairs = new ArrayList<NameValuePair>();
                
                nameValuePairs.add(new BasicNameValuePair("flag", "2"));
                nameValuePairs.add(new BasicNameValuePair("request_code", "NEW"));              
                nameValuePairs.add(new BasicNameValuePair("disclosed_qty", "0"));
                nameValuePairs.add(new BasicNameValuePair("qty", ""+eo.getLotSize()));
                nameValuePairs.add(new BasicNameValuePair("trigger_price", "0"));
                nameValuePairs.add(new BasicNameValuePair("ex", "NSE"));
                nameValuePairs.add(new BasicNameValuePair("dp_id", "1564715"));               
                nameValuePairs.add(new BasicNameValuePair("afterhour", "N"));
                nameValuePairs.add(new BasicNameValuePair("price", ""+eo.getPrice()));
                nameValuePairs.add(new BasicNameValuePair("validity", "GFD"));
                nameValuePairs.add(new BasicNameValuePair("s", eo.getScriptName()));
                nameValuePairs.add(new BasicNameValuePair("e", "352"));
                nameValuePairs.add(new BasicNameValuePair("b", eo.getBuyOrSell()));
                nameValuePairs.add(new BasicNameValuePair("submit", "Confirm"));
                
                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                
                
                response = httpclient.execute(request,localContext);
                for(Header h:response.getAllHeaders()){
                	System.out.println(h.getName()+":"+h.getValue());
                	if(h.getName().endsWith("Cookie")){
                		
                	}
                }
                  in = new BufferedReader
               (new InputStreamReader(response.getEntity().getContent()));
                sb = new StringBuffer("");
                line = "";
                NL = System.getProperty("line.separator");
               while ((line = in.readLine()) != null) {
                   sb.append(line + NL);
               }
               in.close();
                page = sb.toString();
                System.out.println("#################Order Placed###################");
               System.out.println(page);
                
                }catch(Exception e){
                	e.printStackTrace();
                }
            System.out.println("");
            
           
            return "success";
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	return e.getMessage();
        } catch (IOException e) {
        	return e.getMessage();    	
        }catch (Exception e) {
        	return e.getMessage();
		}finally{
			
		}
    
		
	}
}
