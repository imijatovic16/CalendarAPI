package calendarAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ReadCSV {
	private static TreeMap<String, ArrayList<String>> grupe = new TreeMap<String, ArrayList<String>>();
	private static String[] array;
	public static JSONObject obj = new JSONObject();

	public ReadCSV() {
	}

	public TreeMap<String, ArrayList<String>> readFile(String pathToFile, String group, String semStart,
			String semEnd) {
		try {
			FileReader fileReader = new FileReader(pathToFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			if (semStart.equals("")) {
				semStart = "2018-02-26";
			}
			if (semEnd.equals("")) {
				semEnd = "2018-06-18";
			}
			obj.put("startSemestar", semStart);
			obj.put("endSemestar", semEnd);
			obj.put("events", new JSONArray());
			JSONObject event;
			// ----Koji je dan prvi dan semestra----
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.contains(group)) {
					continue;
				}
				array = line.split("\",\"");
				event = new JSONObject();
				System.out.println(Arrays.asList(array));
				event.put("summary", array[0].replaceAll("\\s+", " ").replaceAll("\"", ""));
				event.put("location", array[6].replaceAll("\\s+", " ").replaceAll("\"", ""));
				String start = array[5].split("-")[0].replaceAll("\\s+", " ").replaceAll("\"", "") + ":00";
				int sat = Integer.parseInt(start.split(":")[0]) + 1;
				start = sat + ":" + start.split(":")[1] + ":" + start.split(":")[2];
				event.put("start", start);
				start = array[5].split("-")[1].replaceAll("\\s+", " ").replaceAll("\"", "") + ":00:00";
				sat = Integer.parseInt(start.split(":")[0]) + 1;
				start = sat + ":" + start.split(":")[1] + ":" + start.split(":")[2];
				event.put("end", start);
				event.put("description", array[1].replaceAll("\\s+", " ").replaceAll("\"", "") + "\n"
						+ array[2].replaceAll("\\s+", " ").replaceAll("\"", ""));
				event.put("recurrence", new JSONArray());
				event.put("day", array[4].replaceAll("\\s+", "").replaceAll("\"", ""));

				obj.append("events", event);
			}
			System.out.println(obj.toString(2));
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return grupe;
	}

	public void addRecurrence() {
		String endOfSemester = obj.getString("endSemestar");
		ArrayList<String> dates = readDateJSON();
		JSONObject event;
		for (int i = 0; i < obj.getJSONArray("events").length(); i++) {

			String day = obj.getJSONArray("events").getJSONObject(i).getString("day");
			String dayName = "";
			if (day.contains("PON")) {
				day = "MO";
				dayName = "pon";
			} else if (day.contains("UTO")) {
				day = "TU";
				dayName = "uto";
			} else if (day.contains("SRE")) {
				day = "WE";
				dayName = "sre";
			} else if (day.contains("ČET")) {
				day = "TH";
				dayName = "Čet";
			} else if (day.contains("PET")) {
				day = "FR";
				dayName = "pet";
			}
			// obj.getJSONArray("events").getJSONObject(0)
			System.out.println(obj.getJSONArray("events").getJSONObject(i).getString("start"));
			obj.getJSONArray("events").getJSONObject(i).put("start", getFirstDayDateOfDate(dayName,
					obj.getString("startSemestar"), obj.getJSONArray("events").getJSONObject(i).getString("start")));
			System.out.println(obj.getJSONArray("events").getJSONObject(i).getString("start"));
			obj.getJSONArray("events").getJSONObject(i).put("end", getFirstDayDateOfDate(dayName,
					obj.getString("startSemestar"), obj.getJSONArray("events").getJSONObject(i).getString("end")));
			System.out.println(obj);
			String rule = "RRULE:FREQ=WEEKLY;UNTIL=" + endOfSemester.replace("-", "") + "T090000Z" + ";BYDAY=" + day;
			String exdate = "EXDATE;VALUE=DATE-TIME:";
			for (int j = 0; j < dates.size(); j++) {
				exdate += dates.get(j) + "T"
						+ obj.getJSONArray("events").getJSONObject(i).getString("start").split("T")[1].split("Z")[0]
								.replace(":", "")
						+ "Z,";

			}
			exdate = exdate.substring(0, exdate.length() - 1);
			// exdate = "EXDATE;VALUE=DATE-TIME:" + "20180515" + "T"
			// +
			// obj.getJSONArray("events").getJSONObject(i).getString("start").split("T")[1].split("Z")[0]
			// .replace(":", "")
			// + "Z" + ",20180515T110000Z";
			ArrayList<String> rec = new ArrayList<>();
			rec.add(rule);
			rec.add(exdate);
			obj.getJSONArray("events").getJSONObject(i).put("recurrence", new JSONArray(rec));
		}

	}

	public static void main(String[] args) {
		// getDayNameFromDate("2018-05-30");
		// getFirstDayDateOfDate("uto", "2018-02-26", "09:00:00");
		// readFile("csv2.txt");
		// addRecurrence();
		System.out.println(obj.toString(2));
	}

	public String getFirstDayDateOfDate(String day, String startingDate, String time) {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date startDate = null;
		try {
			startDate = df.parse(startingDate + " " + time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// c.set(Calendar.HOUR_OF_DAY, 17);
		c.setTime(startDate);
		if (day.equalsIgnoreCase("pon")) {
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else if (day.equalsIgnoreCase("uto")) {
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		} else if (day.equalsIgnoreCase("sre")) {
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		} else if (day.equalsIgnoreCase("Čet")) {
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		} else if (day.equalsIgnoreCase("pet")) {
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		}

		return dateToISOString(c.getTime());
	}

	public String dateToISOString(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(tz);
		String dateStr = df.format(date);
		int st = Integer.parseInt(dateStr.split("T")[1].split(":")[0]) + 1;
		dateStr = dateStr.split("T")[0] + "T" + st + ":" + dateStr.split("T")[1].split(":")[1] + ":"
				+ dateStr.split("T")[1].split(":")[2];
		return dateStr;
	}

	public String getDayNameFromDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate;
		try {
			startDate = df.parse(date);
			return startDate.toString().substring(0, 3);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "No day name";
	}

	public ArrayList<String> readDateJSON() {
		BufferedReader br = null;
		JSONTokener tokener;
		JSONObject root = null;
		try {

			try {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(new File(getClass().getResource("/dates.json").toURI()))));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tokener = new JSONTokener(br);
			root = new JSONObject(tokener);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return createListFromDatesJSON(root);
	}

	public ArrayList<String> createListFromDatesJSON(JSONObject obj) {
		ArrayList<String> dates = new ArrayList<String>();
		JSONArray dateObjJSON = null;

		if (obj.has("dates")) {
			JSONObject dateObj;
			dateObjJSON = obj.getJSONArray("dates");
			for (int i = 0; i < dateObjJSON.length(); i++) {
				dateObj = dateObjJSON.getJSONObject(i);
				// if (dateObj.has("start") && dateObj.has("end")) {
				// if (dateObj.getString("end").equals("") ||
				// dateObj.getString("start").equals("")) {
				// System.err.println("Greska");
				// return null;
				// }
				// dates.add(new DateObject(dateObj.getString("start"),
				// dateObj.getString("end")));
				// } else if (dateObj.has("start") && !dateObj.has("end")) {
				// if (dateObj.getString("start").equals("")) {
				// System.err.println("Greska");
				// return null;
				// }
				// dates.add(new DateObject(dateObj.getString("start"), ""));
				// } else {
				// System.err.println("Greska");
				// return null;
				// }
				if (dateObj.has("start")) {
					for (int j = 0; j < dateObj.getJSONArray("start").length(); j++) {
						dates.add(dateObj.getJSONArray("start").getString(j));
					}

				}

			}
		}
		System.out.println(dates);
		return dates;
	}

}
