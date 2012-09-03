package org.krzysio;


public class NextStringGenerator {

	char[] WORD;
	int WORD_LENGTH;
	int LAST_CHAR_IDX;

	public NextStringGenerator(String lastGeneratedWord) {
		if (lastGeneratedWord == null || lastGeneratedWord.isEmpty()) {
			throw new IllegalArgumentException("beginnigWord can not be empty");
		}
		
		WORD = lastGeneratedWord.toCharArray();
		WORD_LENGTH = WORD.length;
		LAST_CHAR_IDX = WORD_LENGTH - 1;
	}
	
	public String nextWord() {
		moveUp(LAST_CHAR_IDX);
		
		return new String(WORD);
	}
	
	public String[] nextWords(int numOfWords) {
		String[] nextWords = new String[numOfWords];
		
		for (int i = 0; i < numOfWords; i++) {
			nextWords[i] = nextWord();
		}
		
		return nextWords;
	}
	
	char movingUp;
	
	private void moveUp(int idx) {
		movingUp = WORD[idx];
		if (idx == 0) {
			if (movingUp < 'Z') {
				WORD[idx] = (char) (movingUp + 1);
			} else {
				WORD_LENGTH++;
				LAST_CHAR_IDX = WORD_LENGTH - 1;
				WORD = new char[WORD_LENGTH];
				
				for (int i = 0; i < WORD_LENGTH; i++) {
					WORD[i] = 'A';
				}
			}
		} else {
			if (movingUp < 'Z') {
				WORD[idx] = (char) (movingUp + 1);
			} else {
				WORD[idx] = 'A';
				moveUp(idx - 1);
			}
		}
	}
}
