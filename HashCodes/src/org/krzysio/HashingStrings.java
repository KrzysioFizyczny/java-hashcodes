package org.krzysio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HashingStrings {

	/**
	 * {hashCode; word1,word2,...}
	 */
	static final Map<Integer, String> HISTOGRAM = new HashMap<Integer, String>();

	/*
	 * public void run() { String word; Integer hashCode; StringBuilder
	 * currentStringBuilder; Map<Integer, StringBuilder> localHistogram = new
	 * HashMap<Integer, StringBuilder>();
	 * 
	 * while ((word = nextWord()) != null) { hashCode = word.hashCode();
	 * 
	 * currentStringBuilder = localHistogram.get(hashCode); if
	 * (currentStringBuilder == null) { currentStringBuilder = new
	 * StringBuilder(word); } else {
	 * currentStringBuilder.append(',').append(word); }
	 * 
	 * localHistogram.put(hashCode, currentStringBuilder); }
	 * 
	 * synchronized (HISTOGRAM) { Integer key; StringBuilder localValue; String
	 * value;
	 * 
	 * for (Map.Entry<Integer, StringBuilder> localEntry :
	 * localHistogram.entrySet()) { key = localEntry.getKey(); localValue =
	 * localEntry.getValue();
	 * 
	 * if (HISTOGRAM.containsKey(key)) { value = HISTOGRAM.get(key);
	 * localValue.append(',').append(value);
	 * 
	 * System.out.println("HashCode: " + key + ", values: " +
	 * localValue.toString()); }
	 * 
	 * value = localValue.toString(); HISTOGRAM.put(key, value); }
	 * 
	 * // if the last one // StringBuilder output; // for (Map.Entry<Integer,
	 * String> entry : HISTOGRAM.entrySet()) { // output = new StringBuilder();
	 * //
	 * output.append("HashCode: ").append(entry.getKey()).append(", values: ")
	 * .append(entry.getValue()); // System.out.println(output.toString()); // }
	 * } }
	 */

	public static void main(String[] args) {
		DbOperations dbOperations = new OracleOperations();
		String lastString = dbOperations.getLastString();

		if (lastString == null || lastString.isEmpty()) {
			char first = 'A' - 1;
			lastString = String.valueOf(first);
		}

		int loopSize = 1000, batchSize = 10000;
		long t1 = System.currentTimeMillis();

		for (int i = 0; i < loopSize; i++) {
			NextStringGenerator generator = new NextStringGenerator(lastString);
			String[] nextWords = generator.nextWords(batchSize);
			lastString = nextWords[nextWords.length - 1];

			dbOperations.saveNextStrings(nextWords);
			System.out.println("Loop nr " + (i + 1));
		}

		long time = System.currentTimeMillis() - t1;
		time = time / 1000L;
		String logInfo = "DB: %s,    LoopSize: %5d, batchSize: %7d, Multiplied: %12d, time: %6d seconds\n";
		logInfo = String.format(logInfo, dbOperations.getDbName(), loopSize, batchSize, loopSize * batchSize, time);
		System.out.print(logInfo);

		File logFile = new File("hashcodes.log");
		try {
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(logFile, true);
			fileWriter.append(logInfo);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * int[] marks = {Integer.MIN_VALUE, Integer.MIN_VALUE / 4 * 3,
		 * Integer.MIN_VALUE / 2, Integer.MIN_VALUE / 4, 0, Integer.MAX_VALUE /
		 * 4, Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 4 * 3,
		 * Integer.MAX_VALUE};
		 * 
		 * for (int i = 1; i < marks.length; i++) { Thread t = new Thread(new
		 * EqHashFinder(marks[i- 1], marks[i], i)); t.start(); }
		 */
	}
}

class EqHashFinder implements Runnable {

	int min, max, rangeNo;

	public EqHashFinder(int min, int max, int rangeNo) {
		this.min = min;
		this.max = max;
		this.rangeNo = rangeNo;
	}

	@Override
	public void run() {
		int hash;
		for (int i = min; i <= max; i++) {
			hash = String.valueOf(i).hashCode();

			if (hash == i) {
				System.out.println("hash == i dla i == " + i);
			}

			if (i == max) {
				break;
			}
		}

		System.out.println("Done for " + rangeNo + ". range");
	}

}
