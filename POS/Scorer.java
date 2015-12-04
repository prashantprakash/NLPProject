import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Prashant
 * @created_date 2nd Dec 2015
 * @description This is a scorer class to calculate the score and build the
 *              table to show the differences
 *
 */
public class Scorer {

	public static HashMap<String, Integer> tagCounts = new HashMap<String, Integer>();
	public static void main(String[] args) {
		int agreeCount = 0;
		int disagreeCount = 0;

		HashMap<String, HashMap<String, ArrayList<String>>> tagChosenForTag = new HashMap<String, HashMap<String, ArrayList<String>>>();

		// File testTagFile = new File("/Users/Prashant/NLPProject/brown-test-tags.txt");
		// c = new File("/Users/Prashant/NLPProject/brown-test-sentences_Viterbi_tag.txt");
		// File testFile = new File("/Users/Prashant/NLPProject/brown-test-sentences.txt");
		// File trainFile = new File("/Users/Prashant/NLPProject/brown-train-tags.txt");
		File testTagFile = new File(args[0]);
		File outputTagFile = new File(args[1]);
		File testFile = new File(args[2]);
		File trainFile = new File(args[3]);
		
		FileWriter outFile;

		try {
			outFile = new FileWriter(new File("score.html"));

			Scanner testScanner = new Scanner(testTagFile);
			Scanner outputScanner = new Scanner(outputTagFile);
			Scanner test = new Scanner(testFile);

			String testPOS;
			String outputPOS;
			int i = 0;

			while (testScanner.hasNext() && outputScanner.hasNext() && test.hasNext()) {
				i++;
				// Get the parts of speech
				testPOS = testScanner.next();
				outputPOS = outputScanner.next();

				// Get the associated words

				String testWord = test.next();
				// Count agreement and disagreement
				if (testPOS.equals(outputPOS)) {
					agreeCount++;
				} else {
					disagreeCount++;
				}

				// Count which tag is mistaken for which
				if (!tagChosenForTag.containsKey(testPOS)) {
					tagChosenForTag.put(testPOS, new HashMap<String, ArrayList<String>>());
				}
				if (!tagChosenForTag.get(testPOS).containsKey(outputPOS)) {
					tagChosenForTag.get(testPOS).put(outputPOS, new ArrayList<String>());
				}
				tagChosenForTag.get(testPOS).get(outputPOS).add(testWord);

			} // end of while loop

			// Start printing the scoring output
			outFile.write(
					"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
			outFile.write("<html lang=\"en\">");
			outFile.write("<head>");
			outFile.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
			outFile.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
			outFile.write("<title>Part of Speech Scoring</title>");
			outFile.write("</head>");
			outFile.write("<body>");

			outFile.write("<div>Agree: " + agreeCount + "</div>");
			outFile.write("<div>Disagree: " + disagreeCount + "</div>");
			outFile.write("<div>Percentage Right: "
					+ (100.0 * (double) agreeCount / (double) (agreeCount + disagreeCount)) + "%</div>");

			outFile.write("<table rules='all' cellpadding='5'>");
			
			parseTrainFile(trainFile);
			outFile.write("<tr><th scope='col'></th>");
			for (String tag : tagCounts.keySet()) {
				if (tag.equals("<s>")) {
					outFile.write("<th scope='col'>&lt;s&gt;</th>");
				} else {
					outFile.write("<th scope='col'>" + tag + "</th>");
				}
			}
			outFile.write("</tr>");
			for (String testTag : tagCounts.keySet()) {
				if (testTag.equals("<s>")) {
					outFile.write("<tr>\n<th scope='row'>&lt;s&gt;</th>");
				} else {
					outFile.write("<tr>\n<th scope='row'>" + testTag + "</th>");
				}
				for (String outputTag : tagCounts.keySet()) {
					String htmlClass = " class='normal'";
					if (tagChosenForTag.containsKey(testTag) && tagChosenForTag.get(testTag).containsKey(outputTag)) {
						ArrayList<String> list = tagChosenForTag.get(testTag).get(outputTag);
						int num = list.size();
						if (testTag.equals(outputTag)) {
							htmlClass = " class='self'";
						} else if (num > 1000) {
							htmlClass = " class='reallybad'";
						} else if (num > 500) {
							htmlClass = " class='bad'";
						} else if (num > 100) {
							htmlClass = " class='prettybad'";
						}
						outFile.write("<td" + htmlClass
								+ " onclick='if(this.childNodes[1].style.display===\"none\"){this.childNodes[1].style.display=\"block\"}else{this.childNodes[1].style.display=\"none\"}'>"
								+ num + "<div style='display:none'>");
						for (String item : list) {
							outFile.write(item + ", ");
						}
						outFile.write("</div></td>");
					} else {
						outFile.write("<td class='zero'>0</td>");
					}
				}
				outFile.write("</tr>");
			}
			outFile.write("</table>");

			outFile.write("</body>");
			outFile.write("</html>");

			outFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}
	
	
	
	private static void parseTrainFile(File trainFile) {
		Scanner sc = null ;
		try {
			sc = new Scanner(trainFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		while (sc.hasNext()) {
			String currentTag = sc.next();
			addOne(tagCounts, currentTag);
		}
	}
	
	
	public static void addOne(HashMap<String, Integer> map, String keya) {
		if (map.containsKey(keya)) {
			int tempCount = map.get(keya);
			map.put(keya, tempCount + Constant.ONE);
		} else {
			map.put(keya, Constant.ONE);
		}
	}


}
