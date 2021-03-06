import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

/**
 * @author Prashant
 * @created_date 2nd dec 2015
 * @description Store all the results and output operations
 */
public class HMMResult {

	/**
	 * PrintStat
	 *
	 * Method to compare 2 tag files and print out the number of correct and
	 * incorrect tags
	 * 
	 * @param testTagfile
	 * @param modelTagFile
	 */
	public void PrintStat(String testTagfile, String modelTagFile) {
		BufferedReader testTag = null, modelTag = null;
		try {
			testTag = new BufferedReader(new FileReader(testTagfile));
			modelTag = new BufferedReader(new FileReader(modelTagFile));
			String string1 = null, string2 = null;
			String[] stringarray1 = null, stringarray2 = null;
			int totalcount = 0, totalmistake = 0, totalcorrect = 0;
			while ((string1 = testTag.readLine()) != null && (string2 = modelTag.readLine()) != null) {
				stringarray1 = string1.split(Constant.SPACES);
				stringarray2 = string2.split(Constant.SPACES);
				for (int i = 0; i < stringarray1.length; i++) { // count the
																// number of
																// mistakes
					if (!stringarray1[i].equals(stringarray2[i])) {
						totalmistake++;
					}
				}
				
				totalcount += stringarray1.length; // count the number of total
													// tags
			}
			totalcorrect = totalcount - totalmistake;

			// print the statistics
			System.out.println("Testing with tagging " + testTagfile);
			System.out.println("There are " + totalcount + " tags in total.");
			System.out.println(totalcorrect + " tags are correct and " + totalmistake + " tags are wrong.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				testTag.close();
				modelTag.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
     * ViterbiConsole
     *
     * Method to perform Viterbi tagging on a user input from the console. Results will be printed on the console
     */
    public void ViterbiConsole(Viterbi viterbi , HMMModel hmmModel){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a sentence for tagging");
        String sentence = scan.nextLine();
        String result = viterbi.viterbiAlgorithm(sentence,hmmModel);
        System.out.println(result);
    }

	
	

}
