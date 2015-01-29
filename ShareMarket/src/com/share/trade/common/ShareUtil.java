package com.share.trade.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.share.trade.database.FutureGapScript;

public class ShareUtil {
public static String MONEY_CONTROLL_URL="http://www.appuonline.com/n_most_active_securities.html";
public static String MONEY_CONTROL_FNO_URL="http://www.moneycontrol.com/india/fnoquote/";
public static Set<String> WATCHER_SCRIPT_SET=new HashSet<String>();
public static Set<FutureGapScript> WATCHER_FNO_SCRIPT_SET=new HashSet<FutureGapScript>();
public static LinkedList<String> WATCHER_LOG=new LinkedList<String>();
public static HashMap<String, Double> PRICE_CHANGE_INC_MAP=new HashMap<String, Double>();
public static HashMap<String, Double> PRICE_CHANGE_DEC_MAP=new HashMap<String, Double>();
public static HashMap<String,Integer> PORTFOLIYO_SCRIPTS_COUNTER_MAP=new HashMap<String, Integer>();
public static HashMap<String, HashMap<String, Object>> stockWatcherData=new HashMap<String,HashMap<String, Object>>();
public static HashMap<String, Double> hookPrice=new HashMap<String,Double>();

public static String DEFAULTSTRATEGY="DEFAULTSTRATEGY";

public static String TRADE_ON="1";
public static String BUY_ALTERD="BUY_ALTERD";
public static String SELL_ALTERD="SELL_ALTERD";
public static String PRICE_Q="PQ";
public static String BUY_SIGNEL="BUY_SIGNEL";
public static String BUY_PRICE="BUY_PRICE";
public static String SELL_SIGNEL="SELL_SIGNEL";
public static String SELL_PRICE="SELL_PRICE";
public static String BULISH="1";
public static String BEARISH="-1";
public static String NEUTRAL="0";
public static String LONG="LONG";
public static String SORT="SORT";
public static String BUY_ORDER="B";
public static String SELL_ORDER="S";
public static String SORTSELL_ORDER="SS";
public static String ORDER="ORDER";
public static HashMap<String, String> sentiment=new HashMap<String, String>();
static{
	sentiment.put("BULISH",BULISH);
	sentiment.put("BEARISH",BEARISH);
	sentiment.put("NEUTRAL",NEUTRAL);
}

public static String BROKER_SHAREKHAN="SHAREKHAN";
public static double LONG_TRADE_SQ_HOOK_P=0.0;
public static double SORT_TRADE_SQ_HOOK_P=0.0;

public static String OPEN_BUY_FAR="buyFarSellNear";
public static String OPEN_SELL_FAR="sellFarBuyNear";
public static String SQUREOFF="squareOff";
}
