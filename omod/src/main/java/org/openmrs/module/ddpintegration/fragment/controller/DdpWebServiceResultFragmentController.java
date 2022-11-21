package org.openmrs.module.ddpintegration.fragment.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.ddpintegration.api.DdpintegrationService;
import org.openmrs.module.ddpintegration.model.DdpIntegrationDrugModel;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class DdpWebServiceResultFragmentController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public void controller(FragmentModel model, @FragmentParam("patientId") Patient patient) {
		List<DdpIntegrationDrugModel> res = Context.getService(DdpintegrationService.class).loadingDdpData();
		model.put("items", res);
		String token = Context.getService(DdpintegrationService.class).findDdpToken();
		model.put("token", token);
		
	}
	
}
