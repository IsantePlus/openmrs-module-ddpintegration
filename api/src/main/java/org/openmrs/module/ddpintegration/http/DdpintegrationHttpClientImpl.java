package org.openmrs.module.ddpintegration.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Component;

@Component
public class DdpintegrationHttpClientImpl /*implements DdpintegrationHttpClient*/{
	
	public static HttpURLConnection getHttpConnection(String url, String type) {
		HttpURLConnection httpURLConnection = null;
		try {
			URL uri = new URL(url);
			httpURLConnection = (HttpURLConnection) uri.openConnection();
			httpURLConnection.setRequestMethod(type); //type: POST, PUT, DELETE, GET
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setConnectTimeout(60000); //60 secs
			httpURLConnection.setReadTimeout(60000); //60 secs
			httpURLConnection.setRequestProperty("Accept-Encoding", "utf-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
		}
		catch (Exception e) {
			e.getSuppressed();
		}
		return httpURLConnection;
	}
	
	public static List<?> getObject(HttpURLConnection httpURLConnection) {
		InputStreamReader inputStreamReader = null;
		List<Object> objects = null;
		BufferedReader bufferedReader = null;
		String output = "";
		try {
			inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			objects = new ArrayList<Object>();
			while ((output = bufferedReader.readLine()) != null)
				objects.add(output);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			httpURLConnection.disconnect();
			try {
				inputStreamReader.close();
				bufferedReader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return objects;
	}
	
	public static List<?> postObject(HttpURLConnection httpURLConnection, String reqbody) {
		OutputStream outputStream = null;
		List<Object> objects = null;
		try {
			outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			byte[] input = reqbody.getBytes("utf-8");
			outputStream.write(input, 0, input.length);
			
			objects = new ArrayList<Object>();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
			String responseLine = null;
			while ((responseLine = br.readLine()) != null)
				objects.add(responseLine);
			
			/*  catch(){
			  	
			  }*/
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				outputStream.flush();
				outputStream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return objects;
	}
	
	public void getAccessToken() {
		String url = "https://ddp.mesi.ht/DDP_WebAPI/token";
		try {
			URL obj = new URL(url);
			try {
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				String userCredentials = "charess:D4-4B-9E-2C-4C-25-8E-B2-5D-F2-4B-F2-BF-27-AF-34";
				String auth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes());
				con.setRequestProperty("Authorization", auth);
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				con.setRequestProperty("Accept", "application/json");
				
				String parameters = "grant_type=password";
				con.setDoOutput(true);
				DataOutputStream dataOutput = new DataOutputStream(con.getOutputStream());
				dataOutput.writeBytes(parameters);
				dataOutput.close();
				dataOutput.flush();
				
				int responseCode = con.getResponseCode();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				String inputLine;
				StringBuffer response = new StringBuffer();
				
				while ((inputLine = bufferedReader.readLine()) != null) {
					response.append(inputLine);
				}
				bufferedReader.close();
				
				//JSONObject json = new JSONObject(response.toString());
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
}
