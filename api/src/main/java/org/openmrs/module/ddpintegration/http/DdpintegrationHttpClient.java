package org.openmrs.module.ddpintegration.http;

import java.net.HttpURLConnection;

public interface DdpintegrationHttpClient {
	
	HttpURLConnection getHttpConnection(String url, String type);
}
