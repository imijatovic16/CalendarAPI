package calendarAPI;

import java.util.ArrayList;
import java.util.TreeMap;

public class Main {
	private static ReadCSV file = new ReadCSV();

	public static void main(String[] args) {
		TreeMap<String, ArrayList<String>> a = file.readFile("csv2.txt");

		for (ArrayList<String> b : a.values()) {
			// System.out.println(b);
			for (String s : b.toString().split(", ")) {
				System.out.println(s);
			}
			System.out.println(a.keySet());
			break;
		}

	}
}
