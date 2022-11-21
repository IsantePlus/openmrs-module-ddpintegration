package org.openmrs.module.ddpintegration;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.ddpintegration.api.DdpintegrationService;
import org.openmrs.module.ddpintegration.model.DdpIntegrationDrugModel;

public class DdpLoadingDataTask extends SessionTask {
	
	private static Log log = LogFactory.getLog(DdpLoadingDataTask.class);
	
	/**
	 * Does the actual data aggregation
	 */
	//protected void onExecute() {
	public void onExecute() {
		try {
			
			//List<DdpIntegrationDrugModel> res = Context.getService(DdpintegrationService.class).loadingDdpData();
			String res = "Papa";
			if (res != null) {
				log.error(res);
			}
		}
		catch (Exception ex) {
			log.error("Unable to load data from DDP server ! Please verify if the DDP server is online");
			ex.printStackTrace();
		}
	}
	
}
