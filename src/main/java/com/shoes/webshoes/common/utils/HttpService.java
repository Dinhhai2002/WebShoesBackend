package com.shoes.webshoes.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpService {

	

	public static String login(String username, String password, String baseUrl) throws Exception {
		URL url = new URL(baseUrl + "/api/v1/authentication/login");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");

		String jsonInputString = "{ \"user_name\": \"" + username + "\", \"password\": \"" + password + "\" }";

		// Gửi yêu cầu POST
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(jsonInputString);
		wr.flush();
		wr.close();

		// Lấy phản hồi từ máy chủ
//		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.toString());
		return jsonNode.get("data").get("token").asText();

	}

}
