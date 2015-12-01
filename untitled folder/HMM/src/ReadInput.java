import java.io.File;
import java.util.Scanner;

public class ReadInput {
	
	public static void main(String[]  args) {
		File file = new File("/Users/Prashant/Downloads/brown/cc01");
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (Exception ex) {
			// System.err.println("Error in reading file " + inputFileName);
			ex.printStackTrace();
		}
		while(sc.hasNext()) {
			System.out.println(sc.next());
		}
	}

}
