package calendarAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

public class EventHandling {
	private static Quickstart quickstart;
	private static Calendar service;
	private static String calendarId = "primary";

	@SuppressWarnings("static-access")
	public EventHandling() {
		quickstart = new Quickstart();
		try {
			service = quickstart.getCalendarService();
			Event event = new Event().setSummary("Moj event3").setLocation("800 Howard St., San Francisco, CA 94103")
					.setDescription("A chance to hear more about Google's developer products.");

			DateTime startDateTime = new DateTime("2018-05-08T09:00:00Z");
			EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Belgrade");
			event.setStart(start);

			DateTime endDateTime = new DateTime("2018-05-08T11:00:00Z");
			EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Belgrade");
			event.setEnd(end);

			String[] recurrence = new String[] { "RRULE:FREQ=WEEKLY;COUNT=5;BYDAY=TU",
					"EXDATE;VALUE=DATE-TIME:20180515T090000Z" };
			event.setRecurrence(Arrays.asList(recurrence));

			EventReminder[] reminderOverrides = new EventReminder[] {
					// new EventReminder().setMethod("email").setMinutes(24 *
					// 60),
					new EventReminder().setMethod("popup").setMinutes(60), };
			Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
					.setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);
			// System.out.println(event);
			// event = service.events().insert(calendarId, event).execute();
			// System.out.println(event);
			event = new Event().setSummary("USPEH").setLocation("800 Howard St., San Francisco, CA 94103")
					.setDescription("A chance to hear more about Google's developer products.");

			startDateTime = new DateTime("2018-05-08T11:00:00Z");
			start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Belgrade");
			event.setStart(start);

			endDateTime = new DateTime("2018-05-08T13:00:00Z");
			end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Belgrade");
			event.setEnd(end);

			recurrence = new String[] { "RRULE:FREQ=WEEKLY;UNTIL=20180522T090000Z;BYDAY=TH",
					"EXDATE;VALUE=DATE-TIME:20180515T090000Z,20180515T110000Z" };
			event.setRecurrence(Arrays.asList(recurrence));

			reminderOverrides = new EventReminder[] { new EventReminder().setMethod("email").setMinutes(24 * 60),
					new EventReminder().setMethod("popup").setMinutes(10), };
			reminders = new Event.Reminders().setUseDefault(false).setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);
			System.out.println(event);
			event = service.events().insert(calendarId, event).execute();
			System.out.println(event);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<DateObject> createListFromDatesJSON(JSONObject obj) {
		ArrayList<DateObject> dates = new ArrayList<DateObject>();
		JSONArray dateObjJSON = null;

		if (obj.has("dates")) {
			JSONObject dateObj;
			dateObjJSON = obj.getJSONArray("dates");
			for (int i = 0; i < dateObjJSON.length(); i++) {
				dateObj = dateObjJSON.getJSONObject(i);
				if (dateObj.has("start") && dateObj.has("end")) {
					if (dateObj.getString("end").equals("") || dateObj.getString("start").equals("")) {
						System.err.println("Greska");
						return null;
					}
					dates.add(new DateObject(dateObj.getString("start"), dateObj.getString("end")));
				} else if (dateObj.has("start") && !dateObj.has("end")) {
					if (dateObj.getString("start").equals("")) {
						System.err.println("Greska");
						return null;
					}
					dates.add(new DateObject(dateObj.getString("start"), ""));
				} else {
					System.err.println("Greska");
					return null;
				}

			}
		}
		System.out.println(dates);
		return dates;
	}

	public static Event createEvenetObject(JSONObject obj) throws JSONException {
		String summary = null;
		String location;
		String description = null;
		String start = null;
		String end = null;
		String timezone = "Europe/Belgrade";
		JSONArray recurrence = null;

		Event event = new Event();
		if (obj.has("summary")) {
			summary = (String) obj.getString("summary");
			event.setSummary(summary);
		}
		if (obj.has("location")) {
			location = obj.getString("location");
			event.setLocation(location);
		}
		if (obj.has("start")) {
			start = obj.getString("start");
			DateTime startDateTime = new DateTime(start);
			EventDateTime startEDT = new EventDateTime().setDateTime(startDateTime).setTimeZone(timezone);
			event.setStart(startEDT);
		}
		if (obj.has("end")) {
			end = obj.getString("end");
			DateTime endDateTime = new DateTime(end);
			EventDateTime endEDT = new EventDateTime().setDateTime(endDateTime).setTimeZone(timezone);
			event.setEnd(endEDT);
		}
		if (obj.has("description")) {
			description = obj.getString("description");
			event.setDescription(description);
		}
		if (obj.has("recurrence")) {
			recurrence = obj.getJSONArray("recurrence");
			ArrayList<String> listdata = new ArrayList<String>();
			for (int i = 0; i < recurrence.length(); i++) {
				listdata.add(recurrence.getString(i));
			}
			// event.setRecurrence(listdata);

		}
		System.out.println(recurrence);
		System.out.println(summary);
		System.out.println(start);
		System.out.println(end);
		System.out.println(description);
		return event;
	}

	public static void main(String[] args) throws Exception {
		new EventHandling();
		// try {
		// BufferedReader br = null;
		// br = new BufferedReader(
		// new InputStreamReader(new FileInputStream(new
		// File("src/main/resources/schedule.json"))));
		// JSONTokener tokener = new JSONTokener(br);
		// JSONObject root = new JSONObject(tokener);
		// createEvenetObject(root);
		// // quickstart = new Quickstart();
		// // service = quickstart.getCalendarService();
		// // service.events().insert(calendarId,
		// // createEvenetObject(root)).execute();
		// // System.out.println(service.events().get(calendarId,
		// // "qtv46g0vp68gan1mqb8i397aho").execute());
		// br = new BufferedReader(
		// new InputStreamReader(new FileInputStream(new
		// File("src/main/resources/dates.json"))));
		// tokener = new JSONTokener(br);
		// root = new JSONObject(tokener);
		// createListFromDatesJSON(root);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

	}
}
