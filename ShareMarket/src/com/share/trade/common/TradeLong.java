package com.share.trade.common;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import com.share.trade.bd.TradeSummaryBD;
import com.share.trade.bd.TradeWatcherBD;
import com.share.trade.common.order.EquityOrder;
import com.share.trade.common.order.FutureOrder;
import com.share.trade.database.ScriptMapper;
import com.share.trade.database.Strategy;
import com.share.trade.database.TradeWatcher;
import com.share.trade.vo.ShareBean;

public class TradeLong {
	private static final Logger log = Logger.getLogger(TradeLong.class
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
	static double increasedBy = 0.00;
	static double decreasedBy = 0.00;
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
	boolean updateWatcher = true;
	TradeWatcherBD watcherBD = new TradeWatcherBD();
	TradeSummaryBD tradeSummaryBD = new TradeSummaryBD();

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
		String token = "";
		String delemiter = "|";
		boolean isEquityScript=false;
		boolean isFutureScript=false;
		
		if (stg.getBuyFactor() != null && !"".equals(stg.getBuyFactor())) {
			buyOrSellFactor = Double.parseDouble(stg.getBuyFactor());
		}
		if (stg.getSellFactor() != null && !"".equals(stg.getSellFactor())) {
			profitFactor = Double.parseDouble(stg.getSellFactor());
		}

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
		pc = MethodUtil.roundOff(sb.getPreviousClose());
		log.info(b + " current price cp: " + cp + " cbp: " + cbp + " cap: "
				+ cap + " buy not alerted: " + !isBuyAlerted);
		log.info("sell aletred: " + isSellAlerted);
		if (!isBuyAlerted) {
			if (pc > dh) {
				bp = (pc - (pc * (buyOrSellFactor)));
			} else {
				bp = (dh - (dh * (buyOrSellFactor)));
			}
			bp = MethodUtil.roundOff(bp);
			log.info(b + " CP = " + cp + " BP=" + bp + " cap=" + cap + " cbp="
					+ cbp + " DH=" + dh + " Balert " + isBuyAlerted
					+ " Salert " + isSellAlerted);

			/**
			 * Logic :if current ask price is less than buy price and current
			 * ask price is greater than [n] mint old ask price means day spread
			 * reached to limit
			 */

			double oldPrice = stockWatchData.getCurrentPrice1();
			double lastPrice = stockWatchData.getCurrentPrice2();
			double considerableIncrease = oldPrice * increaseDecreaseFactor;

			if (considerableIncrease < 0.05) {
				considerableIncrease = 0.05;// 5 paise
			}

			// oldPrice=MethodUtil.roundOff(oldPrice+considerableIncrease);
			System.out.println("current ask price " + cap);
			System.out.println("current buy price " + bp);
			System.out.println("current old price price " + oldPrice);
			System.out.println("(cap < bp) && (cap > oldPrice) "
					+ ((cap < bp) && (cap > oldPrice)));

			if (cap > 0 && (cap < bp) && (cap > oldPrice)) {

				updateWatcher = false;

				// adding up total increase decrease
				// =====================
				try {
					increasedBy = ShareUtil.PRICE_CHANGE_INC_MAP.get(b);
				} catch (NullPointerException nullEx) {
					ShareUtil.PRICE_CHANGE_INC_MAP.put(b, 0.00);
				}
				System.out.println("increasedBy b4 " + increasedBy);
				increasedBy = increasedBy + (cp - lastPrice);
				System.out.println("increasedBy aftr " + increasedBy);
				if (increasedBy < 0) {
					increasedBy = 0;
				}

				ShareUtil.PRICE_CHANGE_INC_MAP.put(b, increasedBy);
				// =====================

				//if (increasedBy >= considerableIncrease) {
					isByable = true;
					increasedBy = 0.0;// reset
					//ShareUtil.PRICE_CHANGE_INC_MAP.put(b, increasedBy);
				//}

			} else {
				isByable = false;
			}
			System.out.println("isByable " + isByable + " isTime4Position "
					+ isTime4Position);
			if (isByable && isTime4Position && !isSellAlerted) {
				// if(true){
				// CHECK ACOUNT BALANCE

				double tradeAmt = 0;

				if (tradeQuantity != null) {
					tradeAmt = cp * Double.parseDouble(tradeQuantity);
				}
				boolean isBalanced = MethodUtil.checkAccountBalance(tradeAmt
						+ "");

				if (isBalanced) {// GET ORDER DETAIL
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
							MethodUtil
									.uiLog("<font color=red>Auto Trade: Error</font>"
											+ b + " is signeled to buy @" + cap,
											ShareUtil.ORDER);
						}

						MethodUtil.uiLog(
								"<font color=green>Auto Trade: </font>" + b
										+ " is signeled to buy @" + cap,
								ShareUtil.ORDER);

						MethodUtil.debitTradeAmt(tradeAmt + "");
					} else {
						MethodUtil.uiLog("<font color=green>Order: </font>" + b
								+ " is signeled to buy @" + cap,
								ShareUtil.ORDER);
					}
					stockWatchData.setBuyAlerted(true);
				} else {
					stockWatchData.setBuyAlerted(false);
					MethodUtil.uiLog("<font color=red>ERROR: </font>" + b
							+ " Insufficiant Account Balance", ShareUtil.LONG);
				}
				tradeSummaryBD.addOrUpdateTradeSummary(tradeAmt, 0, b);
				
				stockWatchData.setBuyPrice(cap);
				// MethodUtil.uiLog("<font color=green>Order: </font>" + b
				// + " is signeled to buy @" + cap, ShareUtil.ORDER);
				msg = msg + b + ":BOUGHT @ " + cap + delemiter;
				SendMail mail = new SendMail("samirmishra2004",
						"24930840@way2sms.com",
						msg + ":T" + HOUR + ":" + MINUT, "..");
				mail.send();
			}
		}

		if (!isSellAlerted && isBuyAlerted) {
			// if(true){
			sp = (bp + (bp * (profitFactor)));
			sp = MethodUtil.roundOff(sp);
			log.info(b + " CP = " + cp + " SP=" + sp + " cap=" + cap + " cbp="
					+ cbp + " DH=" + dh + " Balert " + isBuyAlerted
					+ " Salert " + isSellAlerted);
			double hookPrice = 0.0;
			double oldPrice = stockWatchData.getCurrentPrice1();
			double lastPrice = stockWatchData.getCurrentPrice2();
			double considerableChange = oldPrice * increaseDecreaseFactor;

			if (considerableChange < 0.05) {
				considerableChange = 0.10;// 5 paise
			}
			try {
				hookPrice = ShareUtil.hookPrice.get(b);			

			} catch (NullPointerException npe) {
				//ShareUtil.hookPrice.put(b, cp);
			}
			// oldPrice=MethodUtil.roundOff(oldPrice-incDec);
			System.out.println("current hookPrice price " + hookPrice);
			System.out.println("current bid price " + cbp);
			System.out.println("current sell price " + sp);
			System.out.println("current old price price " + oldPrice);
			System.out.println("(cp < oldPrice) " + ((cp < oldPrice)));
			if ((cbp > sp)) {
				// =====================
				try {
					hookPrice = ShareUtil.hookPrice.get(b);
					if (cp > hookPrice) {
						ShareUtil.hookPrice.put(b, cp);
					}

				} catch (NullPointerException npe) {
					ShareUtil.hookPrice.put(b, cp);
				}

				try {
					decreasedBy = ShareUtil.PRICE_CHANGE_DEC_MAP.get(b);
				} catch (NullPointerException nullEx) {
					ShareUtil.PRICE_CHANGE_DEC_MAP.put(b, 0.00);
				}
				decreasedBy = decreasedBy + (lastPrice - cp);
				if (decreasedBy < 0) {
					decreasedBy = 0;
				}
				ShareUtil.PRICE_CHANGE_DEC_MAP.put(b, decreasedBy);
				log.info("decreasedBy " + decreasedBy);
				// ======================
				if ((cp < lastPrice) && (lastPrice < oldPrice)) {// continuously
																	// decreased
					System.out.println("decreased continuously thrise");
					MethodUtil.uiLog(
							"Script " + b
									+ " decreased continuously thrise ."
									+ "lastPrice- "+lastPrice
									+ "oldPrice- "+oldPrice+" cp- "+cp, ShareUtil.ORDER
									);
					isSellable = true;
				} else if (decreasedBy >= considerableChange) {
					MethodUtil.uiLog(
							"Script " + b
									+ " decreased considerable.."
									+ "considerableChange- "+considerableChange
									+ "decreasedBy- "+decreasedBy+" cp- "+cp, ShareUtil.ORDER
									);
					System.out.println("decreased considerable..");
					isSellable = true;
					decreasedBy = 0;// reset
					ShareUtil.PRICE_CHANGE_DEC_MAP.put(b, decreasedBy);
				}
				else if(hookPrice>0&&(hookPrice-cp)>=considerableChange){
					MethodUtil.uiLog(
							"Script " + b
									+ " decreased considerabley since hooked price"
									+ "considerableChange- "+considerableChange
									+ "hookPrice- "+hookPrice+" cp- "+cp, ShareUtil.ORDER
									);
					System.out.println("decreased considerabley since hooked price");
					isSellable = true;
				}
			} else if (cp < sp && hookPrice > 0) {// its decreasing after
													// increase
				System.out.println("its decreasing after hooking up");
				isSellable = true;
			} else {
				//stop loss logic
				//if cp reduced 1.5 percent lower than bp triger stop loss
				double slp=	bp - (bp*0.015);
				System.out.println("Stop loss price : "+slp);
				if(cp<slp){
					System.out.println("Stop los triggered...");
					isSellable = true;
				}else{
				
				isSellable = false;
				}
			}

			if (forceSquareOff && istime4SquareOff) {
				isSellable = true;
			}
			if (isSellable) {
				// if(true){
				// CHECK ACOUNT BALANCE
				// String tradeQuantity = sm.getTradeQuantity();
				double tradeAmt = 0;

				if (tradeQuantity != null) {
					tradeAmt = cp * Double.parseDouble(tradeQuantity);
				}
				boolean isBalanced = MethodUtil.checkAccountBalance(tradeAmt
						+ "");

				if (true) {// while squaring-off trade balance is not required
							// so it true always

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
								eo.setBuyOrSell(ShareUtil.SELL_ORDER);
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
							MethodUtil
									.uiLog("<font color=red>Auto Trade: Error</font>"
											+ b
											+ " is signeled to  squareoff sell @"
											+ cbp, ShareUtil.ORDER);
						}

						MethodUtil.uiLog(
								"<font color=green>Auto Trade: </font>" + b
										+ " is signeled to squareoff sell @"
										+ cbp, ShareUtil.ORDER);

						MethodUtil.creditTradeAmt(tradeAmt + "");
					} else {
						MethodUtil.uiLog("<font color=green>Order: </font>" + b
								+ " is signeled to squareoff sell @" + cbp,
								ShareUtil.ORDER);
					}

					// PLACE ORDER
				} else {
					MethodUtil.uiLog("<font color=red>ERROR: </font>" + b
							+ " Insufficiant Account Balance", ShareUtil.LONG);
				}
				tradeSummaryBD.addOrUpdateTradeSummary(0, tradeAmt, b);
				// MethodUtil.uiLog("<font color=green>Order: </font>" + b
				// + " is signeled to squareoff sell @" + cbp,
				// ShareUtil.ORDER);
				stockWatchData.setSellAlerted(true);
				stockWatchData.setSellPrice(cbp);
				msg = msg + b + ":SOLD @ " + cbp + delemiter;
				SendMail mail = new SendMail("samirmishra2004",
						"24930840@way2sms.com",
						msg + ":T" + HOUR + ":" + MINUT, "..");
				mail.send();
			}
		}

		if (updateCommanData) {
			//if (!isBuyAlerted) {
				stockWatchData.setCurrentPrice1(stockWatchData
						.getCurrentPrice2());
				stockWatchData.setCurrentPrice2(cp);
			//} else {
				//stockWatchData.setCurrentPrice1(stockWatchData
				//		.getCurrentPrice2());
				//stockWatchData.setCurrentPrice2(cp);
			//}
		}
		log.info("Updating stockWatchData for "+stockWatchData.getScriptName());
		log.info("Updating stockWatchData isSellAlerted "+stockWatchData.isSellAlerted());
		log.info("Updating stockWatchData isBuyAlerted "+stockWatchData.isBuyAlerted());
		watcherBD.updateWatchScript(stockWatchData);

	}
}
