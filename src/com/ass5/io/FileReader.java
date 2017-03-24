package com.ass5.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bharatjain
 *
 */
public class FileReader {

	public static Map<Integer, List<String>> transaction = new HashMap<Integer, List<String>>();
		
	public static Map<Integer, List<String>> testData = new HashMap<Integer, List<String>>();

	public static Map<String, List<String>> columnList = new HashMap<String, List<String>>();

	public static Map<String, List<String>> uniqueConbination = new HashMap<String, List<String>>();

	public static Map<String, Double> attributeCount = new HashMap<String, Double>();

	public static void readFile(String string) throws IOException {

		FileInputStream fis = new FileInputStream(string);

		// Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		int i = 0;
		while ((line = br.readLine()) != null) {

			String[] split = line.split(" +");

			List<String> _row = new ArrayList<String>();
			for (String _sp : split) {
				_row.add(_sp);
			}
			transaction.put(i++, _row);
		}

		for (int count = 0; count < transaction.get(0).size(); count++) {
			List<String> set = new ArrayList<String>();

			for (int column = 0; column < i; column++) {
				if (column != 0) {
					set.add(transaction.get(column).get(count));
				}
			}
			columnList.put(transaction.get(0).get(count), set);
		}

		br.close();

		for (Map.Entry<String, List<String>> _columnList : columnList.entrySet()) {

			List<String> _str = new ArrayList<>();

			for (String _str1 : _columnList.getValue()) {
				if (!_str.contains(_str1)) {
					_str.add(_str1);
				}
			}
			uniqueConbination.put(_columnList.getKey(), _str);
		}
	}

	/**
	 * @param attr
	 * Counts the target attribute 
	 */
	public static void countTargetAttribute(String attr) {

		List<String> unique = FileReader.uniqueConbination.get(attr);
		List<String> targetAll = FileReader.columnList.get(attr);

		for (String _unique : unique) {
			Double i = 0d;
			for (String _targetAll : targetAll) {
				if (_unique.equals(_targetAll)) {
					i++;
				}
			}
			attributeCount.put(_unique, i);
		}
	}
	
	/**
	 * @param string
	 * @throws IOException
	 * Reads the Test Data Set
	 */
	public static void readTestDataSet(String string) throws IOException {
		
		FileInputStream fis = new FileInputStream(string);

		// Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		int i = 0;
		while ((line = br.readLine()) != null) {

			String[] split = line.split(" +");

			List<String> _row = new ArrayList<String>();
			for (String _sp : split) {
				_row.add(_sp);
			}
			testData.put(i++, _row);
		}
		br.close();
	}

}
