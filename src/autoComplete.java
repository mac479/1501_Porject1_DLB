import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//Program must implement De La Briandais trie data structure
//Should create new DLB trie and add all words.

public class autoComplete {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		// Personal note: I have imported files with java at least 5 different times
		// now, I don't think I've ever been able to use the same import method.
		DLB dicTrie = new DLB();
		DLB userTrie = new DLB();
		File dic = new File("dictionary.txt");
		File userHistory = new File("user_history.txt");
		BufferedReader reader = new BufferedReader(new FileReader(dic));
		String line = reader.readLine();
		List<String> wordHistory = new ArrayList<String>();//list of words to be added to user history
		Long time;
		Long total=0L;
		int runs=0;
		while (line != null) {
			// System.out.println(line);
			dicTrie.add(line);
			line = reader.readLine();
		}
		reader.close();
		if(userHistory.exists()) {
			reader=new BufferedReader(new FileReader(userHistory));
			line = reader.readLine();
			while(line!=null) {
				userTrie.add(line);
				line = reader.readLine();
			}
		}
		// writer.close();

		/*
		 * Once the dictionary trie is established, you should prompt the user to start
		 * typing a word. For this project, you will be writing only a simple program to
		 * test autocomplete functionality. Due to complexities with gathering single
		 * character input in Java, you should accept a single character at a time, each
		 * followed by Enter.
		 * 
		 * After each character, present the user with the list of predictions of the
		 * word that they are trying to type. If the user types a number (1-5), you
		 * should consider the user to have selected the corresponding prediction, and
		 * restart the process for a new word.
		 * 
		 * Consider $ to be a terminator character; if the user enters a $, consider the
		 * characters input so far to be the full word as intended (regardless of
		 * suggestions).
		 * 
		 * Finally, if the user enters a ! at any point, your program should exit.
		 */
		String input = "";
		Scanner text = new Scanner(System.in);
		String suggestions[] = new String[5];
		System.out.println("NOTE: Only the first character entered will be accepted.");
		System.out.print("Enter a character, $ to submit current entry from input, ! to exit: ");
		input += text.nextLine().charAt(0);
		while (input.indexOf("!") == -1) {
			System.out.println("\n");
			//records time at start of search.
			time=System.nanoTime();
			//If input is empty the string was reset and needs to be treated as a reset.
			if (!input.equals("")) {
				suggestions = userTrie.suggest(input);
				suggestions = dicTrie.suggest(input,suggestions);
				for (int j = 0; j< suggestions.length; j++) {
					if (suggestions[j] != null)
						System.out.println((1 + j) + ". " + suggestions[j]);
					else
						break;
				}
				//For formatting reasons I limited it to 6 digits
				Long current=System.nanoTime()-time;
				total+=current;
				runs++;
				if(suggestions[0]!=null) {
					System.out.printf("Results found in: %.6f"+"s\n",((double)(current)/1000000000.0));
					System.out.println("Enter a character, $ to submit current entry from input, ! to exit\nor pick a suggestion from above.");
					System.out.print("Current string: " + input);
				}
				else {
					System.out.println("No suggestions found.");
					System.out.println("Enter a character, $ to submit current entry from input, ! to exit.");
					System.out.print("Current string: " + input);
				}
			}
			else {
				System.out.println();
				if(wordHistory.size()>0)
					System.out.println("Previous word: "+wordHistory.get(wordHistory.size()-1));
				System.out.println("NOTE: Only the first character entered will be accepted.");
				System.out.print("Enter a character, $ to submit current entry from input, ! to exit: ");
			}
			char nextChar = text.nextLine().charAt(0);
			
			if ((int) nextChar >= 49 && (int) nextChar <= 53) {
				// Saves entered word as one user has selected in the past and clears input.
				if(userTrie.add(suggestions[(int) nextChar - 49]))
					wordHistory.add(suggestions[(int) nextChar - 49]);
				input = "";
			} 
			else if (nextChar == '$') {
				if(userTrie.add(input))
					wordHistory.add(input);
				input = "";
			} 
			else {
				// if the nextChar was '!' then the loop will exit, if not it will just act as a
				// normal input.
				input += nextChar;
			}
		}
		double average=(double)total/(double)runs;
		System.out.printf("\n\nAverage search time: %.6f\n",average/1000000000.0);

		BufferedWriter writer= new BufferedWriter(new FileWriter(userHistory, true));
		for(int i=0;i<wordHistory.size();i++) {
			writer.append(wordHistory.get(i)+"\n");
		}
		writer.close();
		

	}

}
