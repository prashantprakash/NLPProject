
/**
 * @author Prashant
 * @created_date 30th Nov 2015
 * @Description Hidden Markov Model Implementation using Vitrebi Algorithm
 */

public class HMMImpl {
	public static void main(String[] args) {
		// read train files
		String trainFile = "/Users/Prashant/NLPProject/brown-train-sentences.txt";
		String traintagFile = "/Users/Prashant/NLPProject/brown-train-tags.txt";

		// build HMM model using training data sentence and tags
		HMMModel hmmModel = new HMMModel();
		hmmModel.trainModel(trainFile, traintagFile);
		hmmModel.normalizeProbability();

		// read test files

		String testFile = "/Users/Prashant/NLPProject/brown-test-sentences.txt";
		String testtagFile = "/Users/Prashant/NLPProject/brown-test-tags.txt";
		
		Viterbi viterbi = new Viterbi();

		String newtagfile = viterbi.viterbionTestFile(testFile, hmmModel); // tag the file
		System.out.println("The file is tagged and save as " + newtagfile + ".");
	}
}
