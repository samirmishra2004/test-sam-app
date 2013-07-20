package com.share.trade.bd;

import com.share.trade.dao.BrokerDAO;
import com.share.trade.database.BrokerDetail;

public class BrokerBD {

	BrokerDAO brokerDAO=new BrokerDAO();
	
	public BrokerDetail getBrokerDetail(String broker){
		return brokerDAO.getBrokerDetailByName(broker);
	}
	public void saveBrokerDetail(BrokerDetail bd){
		try {
			brokerDAO.addOrUpdateBroker(bd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
