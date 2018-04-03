package calendarAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

public class ReadCSV {
	private static String key_grupe;
	private static TreeMap<String, ArrayList<String>> grupe = new TreeMap<String, ArrayList<String>>();
	private static String replaceString;
	private static String[] arr;
	private static String[] array;

	public ReadCSV() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static TreeMap<String, ArrayList<String>> readFile(String pathToFile) {
		try {
			FileReader fileReader = new FileReader(pathToFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				array = line.split("\",\"");
				line = line.replaceAll("\",\"", " ");
				line = line.replaceAll("\"", "").trim();
				// System.out.println(Arrays.toString(array));
				ArrayList<String> value_grupe = new ArrayList<String>();
				for (int i = 0; i < array.length; i++) {
					String element = array[i];
					String new_element = element.replaceAll("\"", "").trim();
					if (i == 3) {
						// System.out.println("OVO SU GRUPE " + array[i]);

						String[] razvojene_grupe = new_element.split(",");
						for (String ss : razvojene_grupe) {

							// System.out.println(ss);
							ss = ss.trim();
							if (grupe.get(ss) == null) {
								grupe.put(ss, new ArrayList<String>());
							}
							grupe.get(ss).add(line);
						}
						break;

					} else {
						value_grupe.add(element);
					}
					// System.out.println(element + '\t' + i);
					// String new_element = element.replaceAll("\"", "");
					// System.out.println(new_element);

				}
				// replaceString += line.replaceAll("\"", "") + "\n";
			}
			// arr = replaceString.split(",");
			// System.out.println(Arrays.toString(arr));

			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return grupe;
	}

	// public static String normalize(String input) {
	// String output = Normalizer.normalize(input, Normalizer.Form.NFD);
	// Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	//
	// return pattern.matcher(output).replaceAll("");
	// }

}
