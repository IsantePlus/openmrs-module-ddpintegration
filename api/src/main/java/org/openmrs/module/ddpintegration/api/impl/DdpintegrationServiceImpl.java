/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ddpintegration.api.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.APIException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.FormService;
import org.openmrs.api.ObsService;
import org.openmrs.api.UserService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ddpintegration.DdpintegrationConstants;
import org.openmrs.module.ddpintegration.Item;
import org.openmrs.module.ddpintegration.api.DdpintegrationService;
import org.openmrs.module.ddpintegration.api.dao.DdpintegrationDao;
import org.openmrs.module.ddpintegration.model.DdpIntegrationDrugModel;
import org.openmrs.module.ddpintegration.model.DdpIntegrationLastExecution;

public class DdpintegrationServiceImpl extends BaseOpenmrsService implements DdpintegrationService {
	
	DdpintegrationDao dao;
	
	UserService userService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(DdpintegrationDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public Item getItemByUuid(String uuid) throws APIException {
		return dao.getItemByUuid(uuid);
	}
	
	@Override
	public Item saveItem(Item item) throws APIException {
		if (item.getOwner() == null) {
			item.setOwner(userService.getUser(1));
		}
		
		return dao.saveItem(item);
	}
	
	@Override
	public List<DdpIntegrationDrugModel> loadingDdpData() throws APIException {
		List<DdpIntegrationDrugModel> listDrugs = new ArrayList<DdpIntegrationDrugModel>();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		//DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		try {
			listDrugs = listDrugsForPatient();
			DdpIntegrationLastExecution lastExecution = dao
			        .getDdpIntegrationLastExecutionByUuid(DdpintegrationConstants.DDP_LAST_EXECUTION_DATE_UUID);
			//LocalDate tomorrow = LocalDate.now().plusDays(1);
			lastExecution.setLastExecutionDate(Calendar.getInstance().getTime());
			dao.savelastExecution(lastExecution);
		}
		catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return listDrugs;
		
	}
	
	@Override
	public String findDdpToken() {
		
		String url = "https://ddp.mesi.ht/DDP_WebAPI/token";
		StringBuffer response = new StringBuffer();
		String result = null;
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
				
				String parameters = "grant_type=password&Username=charess&Password=D4-4B-9E-2C-4C-25-8E-B2-5D-F2-4B-F2-BF-27-AF-34";
				con.setDoOutput(true);
				DataOutputStream dataOutput = new DataOutputStream(con.getOutputStream());
				dataOutput.writeBytes(parameters);
				dataOutput.close();
				dataOutput.flush();
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				String inputLine;
				
				while ((inputLine = bufferedReader.readLine()) != null) {
					response.append(inputLine + "\n");
				}
				bufferedReader.close();
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		JSONObject JsonObj = new JSONObject(response.toString());
		
		return JsonObj.get("access_token").toString();
		
	}
	
	public StringBuffer loadingDdpDataConnection() {
		
		String url = "https://ddp.mesi.ht/DDP_WebAPI/api/Ordonnance/Search";
		StringBuffer response = new StringBuffer();
		String access_token = findDdpToken();
		try {
			
			URL obj = new URL(url);
			
			try {
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				String auth = "Bearer " + access_token;
				con.setRequestProperty("Authorization", auth);
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("User-Agent",
				    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				con.setRequestProperty("Accept", "application/json");
				/*con.setRequestProperty("patientID", "1110010261");*/
				con.setRequestProperty("siteCode", "11100");
				con.setRequestProperty("StartDate", "2015-04-28");
				con.setRequestProperty("EndDate", "2022-04-28");
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				String inputLine;
				
				while ((inputLine = bufferedReader.readLine()) != null) {
					response.append(inputLine + "\n");
				}
				bufferedReader.close();
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public List<DdpIntegrationDrugModel> listDrugsForPatient() throws JSONException, ParseException {
		
		List<DdpIntegrationDrugModel> drugModel = new ArrayList<DdpIntegrationDrugModel>();
		
		JSONArray array = new JSONArray(loadingDdpDataConnection().toString());
		DdpIntegrationDrugModel drugList;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			JSONArray drugArray = new JSONArray(object.getJSONArray("drugs").toString());
			for (int j = 0; j < drugArray.length(); j++) {
				JSONObject drugObject = drugArray.getJSONObject(j);
				drugList = new DdpIntegrationDrugModel();
				drugList.setPatientId(Integer.parseInt(object.getString("patientID")));
				drugList.setSiteCode(Integer.parseInt(object.getString("siteCode")));
				drugList.setVisitDate((Date) sdf.parse(object.getString("visitDate")));
				drugList.setCreatedDate((Date) sdf.parse(object.getString("createDate")));
				drugList.setDrugId(drugObject.getInt("drugID"));
				drugList.setDrugName(drugObject.getString("drugName"));
				//drugList.setDispenseDate((Date) sdf.parse(object.getString("dispenseDate")));
				//drugList.setNumberDay(Integer.parseInt(object.getString("numofdays")));
				
				drugModel.add(drugList);
			}
		}
		
		return drugModel;
	}
	
	private Patient findPatientByIsanteId(String isanteId) {
		
		return Context.getService(Patient.class).getPatientIdentifier(isanteId).getPatient();
	}
	
	private Location findPatientLocation(Patient patient) {
		Location location = patient.getPatientIdentifier().getLocation();
		
		return location;
		/*
		 * 
		 * 
		String siteCode = "";
		String uuid = "0e52924e-4ebb-40ba-9b83-b198b532653b";

		for (LocationAttribute locationAttribute : encounter.getLocation().getAttributes()) {

		if (locationAttribute.getAttributeType().getUuid().equals(uuid)) {
		siteCode = locationAttribute.getValueReference();
		}
		}
		 * 
		 * 
		 * 
		 * 
		 * 
		 * */
		
	}
	
	public Visit createDdpVisit(Patient patient, Location location, User creator, Date startDate) {
		VisitType visitType = Context.getService(VisitService.class).getVisitTypeByUuid(
		    DdpintegrationConstants.DDP_VISIT_TYPE_UUID);
		Visit visit = new Visit();
		visit.setPatient(patient);
		visit.setLocation(location);
		visit.setCreator(creator);
		if (visitType != null) {
			visit.setVisitType(visitType);
		}
		visit.setStartDatetime(startDate);
		visit.setStopDatetime(startDate);
		visit.setDateCreated(startDate);
		visit.setUuid(UUID.randomUUID().toString());
		
		return visit;
		
	}
	
	public Encounter createDdpEncounter(Patient patient, Location location, User creator, Date date, Visit visit) {
		
		Encounter encounter = new Encounter();
		Form form = null;
		EncounterType encounterType = null;
		encounter.setPatient(patient);
		encounter.setLocation(location);
		if (patient.getAge().intValue() > DdpintegrationConstants.PEDIATRIC_AGE.intValue()) {
			form = Context.getService(FormService.class).getFormByUuid(DdpintegrationConstants.ADULT_PRESCRIPTION_FORM_UUID);
			encounterType = Context.getService(EncounterService.class).getEncounterTypeByUuid(
			    DdpintegrationConstants.ADULT_PRESCRIPTION_ENCOUNTER_TYPE_UUID);
		} else {
			
			form = Context.getService(FormService.class).getFormByUuid(
			    DdpintegrationConstants.PEDIATRIC_PRESCRIPTION_FORM_UUID);
			encounterType = Context.getService(EncounterService.class).getEncounterTypeByUuid(
			    DdpintegrationConstants.PEDIATRIC_PRESCRIPTION_ENCOUNTER_TYPE_UUID);
		}
		
		encounter.setForm(form);
		encounter.setEncounterType(encounterType);
		encounter.setEncounterDatetime(date);
		encounter.setCreator(creator);
		encounter.setDateCreated(date);
		encounter.setVisit(visit);
		encounter.setUuid(UUID.randomUUID().toString());
		
		return encounter;
	}
	
	public void saveObsforDdp(Patient patient, Location location, Encounter encounter, Concept valueCoded) throws Exception {
		ObsService obsService = Context.getObsService();
		Obs parentObs = new Obs();
		parentObs.setConcept(Context.getConceptService().getConcept(DdpintegrationConstants.DISPENSATION_CONCEPT_GROUP));
		parentObs.setObsDatetime(new Date());
		parentObs.setPerson(new Patient(patient));
		parentObs.setLocation(new Location(location.getId()));
		parentObs.setEncounter(encounter);
		Obs groupMember = new Obs();
		groupMember.setConcept(Context.getConceptService().getConcept(
		    DdpintegrationConstants.CONCEPT_QUESTION_DRUG_PRESCRIPTION));
		groupMember.setValueCoded(valueCoded);
		groupMember.setObsDatetime(new Date());
		groupMember.setPerson(new Patient(patient));
		groupMember.setLocation(new Location(location.getId()));
		groupMember.setEncounter(encounter);
		parentObs.addGroupMember(groupMember);
		obsService.saveObs(parentObs, null);
	}
	
	public Encounter verifyIfEncounterExists(Patient patient, Date encounterDate) {
		Encounter encounter = null;
		String encounterUuid;
		List<Encounter> encounters = Context.getService(EncounterService.class).getEncountersByPatient(patient);
		
		if (patient.getAge() > 14)
			encounterUuid = DdpintegrationConstants.ADULT_PRESCRIPTION_ENCOUNTER_TYPE_UUID;
		else
			encounterUuid = DdpintegrationConstants.PEDIATRIC_PRESCRIPTION_ENCOUNTER_TYPE_UUID;
		
		for (Encounter e : encounters) {
			if (e.getEncounterType().getUuid().equals(encounterUuid) && e.getEncounterDatetime().equals(encounterDate)) {
				encounter = e;
			}
		}
		return encounter;
	}
	
}
