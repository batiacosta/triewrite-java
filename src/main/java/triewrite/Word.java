package triewrite;

import java.util.ArrayList;
import java.util.List;

//Word is used to store each word with its positions in the document. 
//Each word will be stored in the node of the binary search tree, and therefore
//needs to be Comparable.
 
class Word implements Comparable<Word>{
	String word;
	List<Integer> positions = new ArrayList<>();
	@Override
	public int compareTo(Word o) {
		return this.word.compareTo(o.word);
	}
	Word(String word, int position) {
		//write your code here
		this.word = word;
		this.positions.add(position);
	}
}
