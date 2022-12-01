/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ddpintegration;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

/**
 * Contains module's config.
 */
@Component("ddpintegration.DdpintegrationConfig")
public class DdpintegrationConfig {
	
	public final static String MODULE_PRIVILEGE = "Ddpintegration Privilege";
	
	private final static String DDP_SEVER_URL = "ddpintegration.server.url";
	
	private final static String DDP_SEVER_USERNAME = "ddpintegration.server.username";
	
	private final static String DDP_SEVER_PASSWORD = "ddpintegration.server.password";
	
	public String getDdpServerUrl() {
		return getProperty(DDP_SEVER_URL, "https://ddp.mesi.ht/DDP_WebAPI_Test/");
	}
	
	public String getDdpServerUsername() {
		return getProperty(DDP_SEVER_USERNAME, "charess");
	}
	
	public String getDdpServerPassword() {
		return getProperty(DDP_SEVER_PASSWORD, "D4-4B-9E-2C-4C-25-8E-B2-5D-F2-4B-F2-BF-27-AF-34");
	}
	
	private String getProperty(String name, String defaultVal) {
		return Context.getAdministrationService().getGlobalProperty(name, defaultVal);
	}
	
	private String getProperty(String propertyName) {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(propertyName);
		if (StringUtils.isBlank(propertyValue)) {
			throw new APIException(String.format("Property value for '%s' is not set", propertyName));
		}
		return propertyValue;
	}
	
	private boolean isPropertySet(String globalProperty) {
		return StringUtils.isNotBlank(Context.getAdministrationService().getGlobalProperty(globalProperty));
	}
	
	private <T> T getComponentByGlobalProperty(String propertyName, Class<T> type) {
		return Context.getRegisteredComponent(getProperty(propertyName), type);
	}
}
