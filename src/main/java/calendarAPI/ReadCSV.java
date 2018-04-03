package calendarAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadCSV {
	public static String stranica = "";
	private static HashMap<String, ArrayList<String>> grupe;
	private static ArrayList<String> value_grupe;
	private static String replaceString;
	private static String[] arr;
	private static String[] array;

	public ReadCSV(HashMap<String, ArrayList<String>> grupe) {
		super();
		this.grupe = grupe;
	}

	private static String readFile() {
		try {
			FileReader fileReader = new FileReader("csv2.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();

			while ((line = bufferedReader.readLine()) != null) {
				// stranica += line + '\n';
				array = line.split("\",\"");

				// = line.split(",");
				// System.out.println(Arrays.toString(array));

				for (int i = 0; i < array.length; i++) {
					String element = array[i];
					if (i == 3) {
						System.out.println("OVO SU GRUPE " + array[i]);
						String[] razvojene_grupe = element.split(",");
						String new_element = element.replaceAll("\"", "");
						String key_grupe = array[i];
						grupe.put(key_grupe, value_grupe);
					} else {
						value_grupe.add(element);
					}
					// System.out.println(element + '\t' + i);
					String new_element = element.replaceAll("\"", "");
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

		return stranica;
	}

	// public static String normalize(String input) {
	// String output = Normalizer.normalize(input, Normalizer.Form.NFD);
	// Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	//
	// return pattern.matcher(output).replaceAll("");
	// }

	public static void main(String[] args) {
		readFile();
	}
}
