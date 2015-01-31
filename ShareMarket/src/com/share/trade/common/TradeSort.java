package com.share.trade.common;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import com.share.trade.bd.StrategyBD;
import com.share.trade.bd.TradeSummaryBD;
import com.share.trade.bd.TradeWatcherBD;
import com.share.trade.common.order.EquityOrder;
import com.share.trade.common.order.FutureOrder;
import com.share.trade.database.ScriptMapper;
import com.share.trade.database.Strategy;
import com.share.trade.database.TradeWatcher;
import com.share.trade.vo.ShareBean;

public class TradeSort {
	private static final Logger log = Logger.getLogger(TradeSort.class
			.getName());
	private static final long serialVersionUID = 1L;
	boolean isByable = false;
	boolean isSellable = false;
	boolean isBuyAlerted = false;
	boolean isSellAlerted = false;
	double dh = 0;
	double dl = 0;
	double cp = 0;
	double bp = 0;
	double sp = 0;
	double pc = 0;
	double cbp = 0;
	double cap = 0;
	double buyOrSellFactor = 0.015;
	double profitFactor = 0.005;
	static double increadedBy = 0.0;
	static double decreadedBy = 0.0;
	final double increaseDecreaseFactor = 0.0005;
	Calendar time = Calendar.getInstance();
	int HOUR = 0;
	int MINUT = 0;
	int SECOND = 0; 

	boolean forceSquareOff = false;
	int positonOpenB4Hour = 14;
	int positonCloseAtHour = 15;
	int positonOpenB4Minut = 00;
	int positonCloseAtMinut = 15;
	boolean isTime4Position = true;
	boolean istime4SquareOff = false;
	TradeWatcherBD watcherBD = new TradeWatcherBD();
	TradeSummaryBD tradeSummaryBD = new TradeSummaryBD();
	StrategyBD strategyBD = new StrategyBD();

	public synchronized void watch(ShareBean sb, String b, Strategy stg,
			ScriptMapper sm, boolean updateCommanData) throws Exception {
		time.setTimeZone(TimeZone.getTimeZone("IST"));
		time.setTime(new Date());
		HOUR = time.get(Calendar.HOUR_OF_DAY);
		MINUT = time.get(Calendar.MINUTE);
		SECOND = time.get(Calendar.SECOND);
		String msg = "";
		String tradeQuantity = sm.getTradeQuantity();
		String scriptNameAndMonth = sm.getBroker_script();
		String scriptName = "";
		String expiryDateStr = "";
		String token="";
		boolean isEquityScript=false;
		boolean isFutureScript=false;

		if (stg.getBuyFactor() != null && !"".equals(stg.getBuyFactor())) {
			buyOrSellFactor = Double.parseDouble(stg.getBuyFactor());
		}
		if (stg.getSellFactor() != null && !"".equals(stg.getSellFactor())) {
			profitFactor = Double.parseDouble(stg.getSellFactor());
		}
		String delemiter = "|";

		if (stg.isForcesSquareOff()) {
			forceSquareOff = true;
			if (stg.getPositionOpenHour() != null) {
				positonOpenB4Hour = Integer.parseInt(stg.getPositionOpenHour());
			}
			if (stg.getPositionOpenMinut() != null) {
				positonOpenB4Minut = Integer.parseInt(stg
						.getPositionOpenMinut());
			}
			if (stg.getPositionCloseHour() != null) {
				positonCloseAtHour = Integer.parseInt(stg
						.getPositionCloseHour());
			}
			if (stg.getPositionCloseMinut() != null) {
				positonCloseAtMinut = Integer.parseInt(stg
						.getPositionCloseMinut());
			}
			if (HOUR <= positonOpenB4Hour) {
				isTime4Position = true;
				if (HOUR == positonOpenB4Hour) {
					isTime4Position = true;
					if (!(positonOpenB4Minut <= MINUT)) {
						isTime4Position = false;
					}
				} else {
					// isTime4Position = false;
				}

			} else {
				isTime4Position = false;
			}
			if (HOUR >= positonCloseAtHour && MINUT > positonCloseAtMinut) {
				istime4SquareOff = true;
			} else {
				istime4SquareOff = false;
			}
		}

		TradeWatcher stockWatchData = watcherBD.getWatchDataForScript(b);
		if (stockWatchData != null) {

		} else {
			stockWatchData = watcherBD.addScriptToWatch(b);
		}

		isBuyAlerted = stockWatchData.isBuyAlerted();

		bp = stockWatchData.getBuyPrice();

		isSellAlerted = stockWatchData.isSellAlerted();

		sp = stockWatchData.getSellPrice();
		// reset flag everyday
		if (HOUR == 9 && MINUT <= 33 && isBuyAlerted && isSellAlerted) {
			stockWatchData.setBuyAlerted(false);
			stockWatchData.setSellAlerted(false);
			watcherBD.updateWatchScript(stockWatchData);
		}

		dh = MethodUtil.roundOff(sb.getDayHigh());
		cp = MethodUtil.roundOff(sb.getCurrentTradePrice());
		cbp = MethodUtil.roundOff(sb.getCurrentBid());
		cap = MethodUtil.roundOff(sb.getCurrentAsk());
		dl = MethodUtil.roundOff(sb.getDayLow());
		pc = MethodUtil.roundOff(sb.getPreviousClose());
		log.info(b + " current price cp: " + cp + " cbp: " + cbp + " cap: "
				+ cap + " buy not alerted: " + !isBuyAlerted);
		log.info("sell aletred: " + isSellAlerted);
		if (!isSellAlerted) {
			if (dl > pc) {
				sp = (pc + (pc * (buyOrSellFactor)));
			} else {
				sp = (dl + (dl * (buyOrSellFactor)));
			}
			sp = MethodUtil.roundOff(sp);
			log.info(b + " CP = " + cp + " SP=" + sp + " cap=" + cap + " cbp="
					+ cbp + " DL=" + dl);

			/**
			 * Logic :if current ask price is less than buy price and current
			 * ask price is greater than [n] mint old ask price means day spread
			 * reached to limit
			 */
			// boolean startMonitor=false;
			double oldPrice = stockWatchData.getCurrentPrice1();
			double latPrice = stockWatchData.getCurrentPrice2();
			System.out.println("CMP1 " + oldPrice + "CMP2 " + latPrice);
			double considerableDecrease = oldPrice * increaseDecreaseFactor;

			ShareUtil.PRICE_CHANGE_DEC_MAP.put(b, decreadedBy);

			if (considerableDecrease < 0.05) {
				considerableDecrease = 0.05;// 5 paise
			}

			// oldPrice=MethodUtil.roundOff(oldPrice-considerableIncrease);
			if (cbp>0 && (cbp > sp) && (cbp < oldPrice)) {
				// ===================
				// adding up total increase decrease
				try {
					decreadedBy = ShareUtil.PRICE_CHANGE_DEC_MAP.get(b);
				} catch (NullPointerException nullEx) {
					ShareUtil.PRICE_CHANGE_DEC_MAP.put(b, 0.00);
				}
				decreadedBy = decreadedBy + (latPrice - cp);
				System.out.println("decreadedBy " + decreadedBy );
				if (decreadedBy < 0) {
					decreadedBy = 0;
				}
				// ===================
				if (decreadedBy >0) {
					isSellable = true;
					decreadedBy = 0;
					ShareUtil.PRICE_CHANGE_DEC_MAP.put(b, decreadedBy);
				}
			} else {
				isSellable = false;
			}

			if (isSellable && isTime4Position && !isBuyAlerted) {
			//if (true) {
				// CHECK ACOUNT BALANCE

				double tradeAmt = 0;

				if (tradeQuantity != null) {
					tradeAmt = cp * Double.parseDouble(tradeQuantity);
				}
				boolean isBalanced = MethodUtil.checkAccountBalance(tradeAmt
						+ "");

				if (isBalanced) {

					// GET ORDER DETAIL
					if (stg.isAutoTrade()) {
						log.info("=========Auto Trade Start========");
						// lotsize,scriptname and month
						try {
							
							if (!MethodUtil.isEmpty(scriptNameAndMonth)) {
								String[] fotureScriptNameAndMonth = scriptNameAndMonth
										.trim().split(" ");
								
								
								int length=fotureScriptNameAndMonth.length;
								
								
								if(length==3){
									scriptName = fotureScriptNameAndMonth[0];
									expiryDateStr = fotureScriptNameAndMonth[1];
									token = fotureScriptNameAndMonth[2];
									isFutureScript=true;
								}
								
								if(length==1){
									scriptName = fotureScriptNameAndMonth[0];
									isEquityScript=true;
								}
							}
							if(isFutureScript){
								FutureOrder fo = new FutureOrder();
								fo.setBuyOrSell(ShareUtil.SELL_ORDER);
								fo.setLotSize(Long.parseLong(tradeQuantity));
								fo.setNameAndMonth(scriptNameAndMonth);
								fo.setScriptName(scriptName);
								fo.setMonthString(expiryDateStr);
								fo.setToken(token);
								
								fo.placeOrder();
							}
							
							if(isEquityScript){
								EquityOrder eo = new EquityOrder();			
								eo.setBuyOrSell(ShareUtil.SORTSELL_ORDER);
								eo.setLotSize(Long.parseLong(tradeQuantity));								
								eo.setScriptName(scriptName);
								
								if(stg.isTradeOnMarket()){
									eo.setPrice(cbp);
									}else{
										eo.setPrice(0);	
									}
																
								eo.placeOrder();
							}


						} catch (Exception e) {
							MethodUtil.uiLog(
									"<font color=red>Auto Trade: Error</font>"
											+ b + " is signeled to sell @"
											+ cbp, ShareUtil.ORDER);
						}

						MethodUtil.uiLog(
								"<font color=green>Auto Trade: </font>" + b
										+ " is signeled to sell @" + cbp,
								ShareUtil.ORDER);

						MethodUtil.debitTradeAmt(tradeAmt + "");
					} else {
						MethodUtil.uiLog("<font color=green>Order: </font>" + b
								+ " is signeled to sell @" + cbp,
								ShareUtil.ORDER);
					}
					log.info(b+ " is signeled to sell @" + cbp+
								ShareUtil.ORDER);
					stockWatchData.setSellAlerted(true);
					// PLACE ORDER
				} else {
					stockWatchData.setSellAlerted(false);
					MethodUtil.uiLog("<font color=red>ERROR: </font>" + b
							+ " Insufficiant Account Balance", ShareUtil.SORT);
					MethodUtil.uiLog("<font color=green>Order: </font>" + b
							+ " is signeled to sell @" + cbp, ShareUtil.ORDER);
				}
				log.info(b+ " updating trade summery amount: " + tradeAmt);
				tradeSummaryBD.addOrUpdateTradeSummary(0, tradeAmt, b);

				
				stockWatchData.setSellPrice(cbp);
				stockWatchData.setCurrentPrice2(cap);// this is required when
														// watcher first time
														// switch for squaring
														// the trade
				msg = msg + b + ":SOLD @ " + cbp + delemiter;
				SendMail mail = new SendMail("samirmishra2004",
						"24930840@way2sms.com",
						msg + ":T" + HOUR + ":" + MINUT, "..");
				mail.send();
			}
		}
		if (isSellAlerted && !isBuyAlerted) {
			bp = (sp - (sp * (profitFactor)));
			bp = MethodUtil.roundOff(bp);
			log.info(b + " CP = " + cp + " BP=" + bp + " cap=" + cap + " cbp="
					+ cbp + " DL=" + dl);

			// boolean startMonitor=false;
			double hookPrice=0.0;
			double oldPrice = stockWatchData.getCurrentPrice1();
			double lastPrice = stockWatchData.getCurrentPrice2();
			double considerableChange = oldPrice * increaseDecreaseFactor;

			if (considerableChange < 0.05) {
				considerableChange = 0.05;// 5 paise
			}
			if (considerableChange < 0.10) {
				considerableChange = 0.10;// 5 paise
			}
			try {
				hookPrice = ShareUtil.hookPrice.get(b);			

			} catch (NullPointerException npe) {
				//ShareUtil.hookPrice.put(b, cp);
			}
			System.out.println("CMP1 " + oldPrice + "CMP2 " + lastPrice);
			System.out.println("current hookPrice price " + hookPrice);
			System.out.println("current ask price " + cap);
			System.out.println("current buy price " + bp);
			System.out.println("current old price price " + oldPrice);
			System.out.println("(cap < bp)"
					+ ((cap < bp)));
			if ((cap < bp)) {
				// ============
				try{
					 hookPrice=ShareUtil.hookPrice.get(b);
					if(cp<hookPrice){
						ShareUtil.hookPrice.put(b, cp);
					}
					
				}catch(NullPointerException npe){
					ShareUtil.hookPrice.put(b, cp);
				}
				try {
					increadedBy = ShareUtil.PRICE_CHANGE_INC_MAP.get(b);
				} catch (NullPointerException nullEx) {
					ShareUtil.PRICE_CHANGE_INC_MAP.put(b, 0.00);
				}
				increadedBy = increadedBy + (cp - lastPrice);
				if (increadedBy < 0) {
					increadedBy = 0;
				}
				ShareUtil.PRICE_CHANGE_INC_MAP.put(b, increadedBy);
				// =============
				if((cp > lastPrice) && (lastPrice>oldPrice)){//continuous increase
					System.out.println("increased continuously thrise");
					MethodUtil.uiLog("SORT Squareoff : " + b
							+ " increased continuously thrise cp=" + cp+" lastPrice "+lastPrice+" oldPrice "+oldPrice, ShareUtil.ORDER);
					isByable = true;
				}else
				if (increadedBy >= considerableChange) {
					System.out.println("increased considerabley");
					MethodUtil.uiLog("SORT Squareoff : " + b
							+ " increased considerabley increadedBy" + increadedBy, ShareUtil.ORDER);
					isByable = true;
					increadedBy = 0;
					ShareUtil.PRICE_CHANGE_INC_MAP.put(b, increadedBy);
				}
				else if(hookPrice>0&&((cp-hookPrice)>=considerableChange)){
					System.out.println("increase considerabley since hooked price");
					MethodUtil.uiLog("SORT Squareoff : " + b
							+ " increase considerabley since hooked price (cp-hookPrice)" + (cp-hookPrice), ShareUtil.ORDER);
					isByable = true;
				}
			} else if(cp>bp && hookPrice>0){// its increased after  decreasing
				System.out.println("its increasing after hooking up");
				MethodUtil.uiLog("SORT Squareoff : " + b
						+ "its increasing after hooking up" + cp, ShareUtil.ORDER);
				isByable = true;
			}else{
				//stop loss logic
				double slp = sp + (sp*0.015);
				System.out.println("its stoploss price is "+slp);
				if(cp>slp){
				System.out.println("its stoploss is triggered ");
				MethodUtil.uiLog("SORT Squareoff : " + b
						+ "its stoploss is triggered slp: " + slp+ "cp: "+cp, ShareUtil.ORDER);
					isByable = true;
				}else{
					isByable = false;
				}
			}
			
			if (forceSquareOff && istime4SquareOff) {
				isByable = true;
			}

			if (isByable) {
			//if (true) {
				// CHECK ACOUNT BALANCE

				double tradeAmt = 0;

				if (tradeQuantity != null) {
					tradeAmt = cp * Double.parseDouble(tradeQuantity);
				}
				boolean isBalanced = MethodUtil.checkAccountBalance(tradeAmt
						+ "");

				if (true) {//no need to check balance while squaring off trade
					
					// GET ORDER DETAIL
					if (stg.isAutoTrade()) {
						log.info("=========Auto Trade Start========");
						// lotsize,scriptname and month
						try {
							if (!MethodUtil.isEmpty(scriptNameAndMonth)) {
								String[] fotureScriptNameAndMonth = scriptNameAndMonth
										.trim().split(" ");
								int length=fotureScriptNameAndMonth.length;
								
								if(length==3){
									scriptName = fotureScriptNameAndMonth[0];
									expiryDateStr = fotureScriptNameAndMonth[1];
									token = fotureScriptNameAndMonth[2];
									isFutureScript=true;
								}
								
								if(length==1){
									scriptName = fotureScriptNameAndMonth[0];
									isEquityScript=true;
								}
							}
							if(isFutureScript){
								FutureOrder fo = new FutureOrder();
								fo.setBuyOrSell(ShareUtil.BUY_ORDER);
								fo.setLotSize(Long.parseLong(tradeQuantity));
								fo.setNameAndMonth(scriptNameAndMonth);
								fo.setScriptName(scriptName);
								fo.setMonthString(expiryDateStr);
								fo.setToken(token);
								fo.placeOrder();
							}
							
							if(isEquityScript){
								EquityOrder eo = new EquityOrder();			
								eo.setBuyOrSell(ShareUtil.BUY_ORDER);
								eo.setLotSize(Long.parseLong(tradeQuantity));								
								eo.setScriptName(scriptName);
								if(stg.isTradeOnMarket()){
								eo.setPrice(cap);
								}else{
									eo.setPrice(0);	
								}
								eo.placeOrder();
							}
						} catch (Exception e) {
							MethodUtil.uiLog(
									"<font color=red>Auto Trade: Error</font>"
											+ b + " is signeled to squareoff @"
											+ cap, ShareUtil.ORDER);
						}

						MethodUtil.uiLog(
								"<font color=green>Auto Trade: </font>" + b
										+ " is signeled to squareoff @" + cap,
								ShareUtil.ORDER);

						MethodUtil.creditTradeAmt(tradeAmt + "");
					} else {
						MethodUtil.uiLog("<font color=green>Order: </font>" + b
								+ " is signeled to sqareoff @" + cap,
								ShareUtil.ORDER);
					}
				} 
				tradeSummaryBD.addOrUpdateTradeSummary(tradeAmt, 0, b);
				
				stockWatchData.setBuyAlerted(true);
				stockWatchData.setBuyPrice(cap);

				msg = msg + b + ":BOUGHT @ " + cap + delemiter;
				SendMail mail = new SendMail("samirmishra2004",
						"24930840@way2sms.com",
						msg + ":T" + HOUR + ":" + MINUT, "..");
				mail.send();
			}
		}
		if (updateCommanData) {
			//if (!isSellAlerted) {
				stockWatchData.setCurrentPrice1(stockWatchData
						.getCurrentPrice2());
				stockWatchData.setCurrentPrice2(cp);
			//} else {

			//	stockWatchData.setCurrentPrice1(stockWatchData
			//			.getCurrentPrice2());
			//	stockWatchData.setCurrentPrice2(cp);
			//}
		}
		log.info("Updating stockWatchData for "+stockWatchData.getScriptName());
		log.info("Updating stockWatchData isSellAlerted "+stockWatchData.isSellAlerted());
		log.info("Updating stockWatchData isBuyAlerted "+stockWatchData.isBuyAlerted());
		watcherBD.updateWatchScript(stockWatchData);

	}
}
