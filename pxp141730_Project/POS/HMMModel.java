import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Prashant
 * @created_date 30th Nov 2015
 * @Description Generate HMM model
 *
 */

/**
 * @author Prashant
 *
 */
/**
 * @author Prashant
 *
 */
public class HMMModel {

	// all instance variables decalration

	private Map<String, Map<String, Double>> transition; // previous tag
															// -> (current
															// tag ->
															// P(current|previous))
	private Map<String, Map<String, Double>> emission; // tag -> (word ->
														// P(tag|word))

	/**
	 * constructor to initialize instance variables
	 */
	public HMMModel() {
		transition = new TreeMap<>();
		emission = new TreeMap<>();
	}

	/**
	 * @param trainFile
	 *            contains all the sentences
	 * @param tagFile
	 *            contains all the tags for the words mentioned in trainFile
	 * @function build training model using hidden Markov Model
	 */
	public void trainModel(String trainFile, String tagFile) {
		BufferedReader wordFileReader = null, tagFileReader = null;
		try {
			wordFileReader = new BufferedReader(new FileReader(trainFile)); // Open
																			// train
																			// file
			tagFileReader = new BufferedReader(new FileReader(tagFile)); // Open
																			// tag
																			// file
		} catch (Exception ex) {
			System.err.println("Error in opening training Files");
			ex.printStackTrace();
		}

		String word, tagword;

		try {
			while ((word = wordFileReader.readLine()) != null && (tagword = tagFileReader.readLine()) != null) { // read
				// split word and tag using spaces // the
				String[] wordArray = word.split(Constant.SPACES);
				String[] tagArray = tagword.split(Constant.SPACES); // file

				// Fill in the emission map with number of emissions
				for (int i = 0; i < wordArray.length; i++) {
					if (!emission.containsKey(tagArray[i])) { // if the tag
																// appears for
																// the first
																// time
						Map<String, Double> wordtoValue = new TreeMap<>();
						wordtoValue.put(wordArray[i], Constant.DOUBLEONE);
						emission.put(tagArray[i], wordtoValue);
					} else {
						if (!emission.get(tagArray[i]).containsKey(wordArray[i])) { // if
																					// the
																					// word
																					// appeared
																					// with
																					// the
																					// tag
																					// for
																					// the
																					// first
																					// time
							emission.get(tagArray[i]).put(wordArray[i], Constant.DOUBLEONE);
						} else { // if the word has already appeared with the
									// tag
							double newValue = emission.get(tagArray[i]).get(wordArray[i]) + 1;
							emission.get(tagArray[i]).put(wordArray[i], newValue);
						}
					}
				}

				// Fill in the transition map with number of transitions
				for (int i = 0; i < wordArray.length; i++) {
					if (i == 0) { // the first transition is # -> (first tag ->
									// P(first tag|#)
						if (!transition.containsKey(Constant.START)) {
							Map<String, Double> first2value = new TreeMap<>();
							first2value.put(tagArray[i], Constant.DOUBLEONE);
							transition.put(Constant.START, first2value);
						} else if (!transition.get(Constant.START).containsKey(tagArray[i])) {
							transition.get(Constant.START).put(tagArray[i], Constant.DOUBLEONE);
						} else {
							double newvalue = transition.get(Constant.START).get(tagArray[i]) + Constant.DOUBLEONE;
							transition.get(Constant.START).put(tagArray[i], newvalue);
						}

					} else if (!transition.containsKey(tagArray[i - 1])) { // if
																			// transition
																			// doesn't
																			// contain
																			// the
																			// previous
																			// tag
						Map<String, Double> curr2value = new TreeMap<>();
						curr2value.put(tagArray[i], Constant.DOUBLEONE);
						transition.put(tagArray[i - 1], curr2value);
					} else { // if transition contains the previous tag
						if (!transition.get(tagArray[i - 1]).containsKey(tagArray[i])) { // but
																							// not
																							// to
																							// the
																							// current
																							// tag
							transition.get(tagArray[i - 1]).put(tagArray[i], Constant.DOUBLEONE);
						} else { // and to the current tag
							double newvalue = transition.get(tagArray[i - 1]).get(tagArray[i]) + Constant.ONE;
							transition.get(tagArray[i - 1]).put(tagArray[i], newvalue);
						}
					}
				}

			}
		} catch (Exception ex) {
			System.err.println("Error in reading training File");
			ex.printStackTrace();
		} finally {
			// Close file if file exist. If not, catch the exception
			try {
				wordFileReader.close();
				tagFileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Function to normalize the probablity of the emission and transition maps
	 * and convert it to log10
	 */
	public void normalizeProbability() {

		for (String keya : emission.keySet()) {
			int count = 0;
			for (String keyb : emission.get(keya).keySet())
				count += emission.get(keya).get(keyb); // count the number of
														// words in each tag
			for (String keyb : emission.get(keya).keySet()) { // normalize the
																// probability
																// by dividing
																// by count and
																// taking log10
				double logprob = Math.log10(emission.get(keya).get(keyb) / count);
				emission.get(keya).put(keyb, logprob);
			}
		}

		for (String keya : transition.keySet()) {
			int count = 0;
			for (String keyb : transition.get(keya).keySet())
				count += transition.get(keya).get(keyb); // count the number of
															// transitions
			for (String keyb : transition.get(keya).keySet()) { // normalize the
																// probability
																// by dividing
																// by count and
																// taking log10
				double logprob = Math.log10(transition.get(keya).get(keyb) / count);
				transition.get(keya).put(keyb, logprob);
			}
		}
	}

	public Map<String, Map<String, Double>> getTransition() {
		return transition;
	}

	public void setTransition(Map<String, Map<String, Double>> transition) {
		this.transition = transition;
	}

	public Map<String, Map<String, Double>> getEmission() {
		return emission;
	}

	public void setEmission(Map<String, Map<String, Double>> emission) {
		this.emission = emission;
	}

}