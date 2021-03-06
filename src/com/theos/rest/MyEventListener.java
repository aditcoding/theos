package com.theos.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyEventListener implements Runnable {

	private String baseUrl = "https://api.spark.io/";
	private String version = "v1";
	private String eventListeningUrl = baseUrl + version + "/devices/events";
	private String accessToken = "f2a4f42f397ab12f4117e7d9bd80e62a0fb4f874";
	private final String ACCESS_TOKEN = "access_token";

	public static void main(String[] args) {
		MyEventListener myListener = new MyEventListener();
		Thread listenerThread = new Thread(myListener);
		listenerThread.start();
	}
	
	@Override
	public void run() {
		try {
			Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
			WebTarget target = client.target(eventListeningUrl).queryParam(ACCESS_TOKEN, accessToken);
			EventSource eventSource = EventSource.target(target).build();
			EventListener listener = new EventListener() {
				@Override
				public void onEvent(InboundEvent inboundEvent) {
					processEvent(inboundEvent);
				}
			};
			eventSource.register(listener);
			eventSource.open();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, Object> parseData(String data) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = null;
		try {
			map = mapper.readValue(data, Map.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	private void processEvent(InboundEvent inboundEvent) {
		try{
			String eventName = inboundEvent.getName();
			String data = inboundEvent.readData(String.class);
			if(eventName != null && data != null && !eventName.equalsIgnoreCase("null")){
				Map<String,Object> map = parseData(data);
				log(eventName + " ; " + data);
				double pressure = Double.parseDouble((String)map.get("data"));
				Map<String, String> queryParams = new HashMap<String, String>();
				String function = "toggle"; 
				String deviceId = "53ff71066667574841542567"; 
				String urlStr = baseUrl + version + "/devices/" + deviceId + "/" + function;
				queryParams.put(ACCESS_TOKEN, accessToken);
				queryParams.put("command", pressure < 100 ? "r1,low" : "r1,high");
				if(eventName.equalsIgnoreCase("pressureChanged")){
					MyEventDispatcher.httpPost(urlStr, queryParams);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	static void log(Object... obj) {
		for (Object o : obj) {
			System.out.println(o);
		}
	}
}
