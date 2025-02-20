package com.shoes.webshoes.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	public static Date convertStringToDate(String dateString) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			System.err.println("Không thể chuyển đổi chuỗi thành đối tượng Date. Lỗi: " + e.getMessage());
		}
		return new Date();
	}

	public static String getDateStringSupplier(String date) {
		String inputFormat = "yyyy-MM-dd";
		String outputFormat = "dd/MM/yyyy";

		SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
		SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
		try {
			Date newDate = inputFormatter.parse(date);
			return outputFormatter.format(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";

	}

	public static String encodeBase64(String s) {
		return Base64.getEncoder().encodeToString(s.getBytes());
	}

	public static String decodeBase64(String s) {
		return new String(Base64.getDecoder().decode(s));

	}

	public static String formatMillisecondsToTime(long totalMilliseconds) {
		long hours = totalMilliseconds / 3600000;
		long remainingMilliseconds = totalMilliseconds % 3600000;
		long minutes = remainingMilliseconds / 60000;
		long seconds = (remainingMilliseconds % 60000) / 1000;

		if (hours > 0)
			return String.format("%02d giờ %02d phút %02d giây", hours, minutes, seconds);
		else

			return String.format("%02d phút %02d giây", minutes, seconds);
	}

	public static String extractVideoId(String youtubeUrl) {
		String videoId = "";

		if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
			int index = youtubeUrl.indexOf("v=");
			if (index != -1) {
				videoId = youtubeUrl.substring(index + 2);
				int ampersandIndex = videoId.indexOf("&");
				if (ampersandIndex != -1) {
					videoId = videoId.substring(0, ampersandIndex);
				}
			}
		}

		return videoId;
	}

	public static Boolean isAccessNextVideo(long currentDuration, long durationLessons) {
		double progress = ((double) currentDuration / durationLessons) * 100;
		if (progress >= 80) {
			return true;
		}

		return false;
	}
	
	public static String convertListObjectToJsonArray(List<?> objects) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			mapper.writeValue(out, objects);
			final byte[] data = out.toByteArray();
			return new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
