package com.ass5.dmdw;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ass5.io.FileReader;

/**
 * @author bharatjain
 *
 */
public class CalculateProbablities {

	public static Map<String, Float> _priorProb = new HashMap<String, Float>();
	public static Map<String, Map<String, Map<String, Double>>> _otherMap = new HashMap<String, Map<String, Map<String, Double>>>();

	public static Map<String, Map<String, Float>> _val = new HashMap<String, Map<String, Float>>();

	
	/**
	 * @param attribute
	 * This function calculates the probability for only target attribute
	 */ 
	public static void getPriorProbablitiesForTarget(String attribute) {

		List<String> unique = FileReader.uniqueConbination.get(attribute);
		List<String> _attribute = FileReader.columnList.get(attribute);

		for (String _attr : unique) {
			float i = 0;
			for (String _uniq : _attribute) {
				if (_attr.equals(_uniq)) {
					i++;
				}
			}
			_priorProb.put(_attr, (i / _attribute.size()));
		}
	}

	/**
	 * @param targetAttrName
	 * @param attribute
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * This function Calculates the Classification
	 */
	public static void TestProbablity(String targetAttrName, int attribute)
			throws FileNotFoundException, UnsupportedEncodingException {

		PrintWriter writer = new PrintWriter("Results.txt", "UTF-8");

		List<String> testDataHeader = FileReader.testData.get(0);
		for (int k = 0; k < testDataHeader.size(); k++) {
			writer.print(testDataHeader.get(k));
			writer.print("  ");
		}
		writer.print("Classification");
		writer.println("");
		int total = 0;
		long from = System.currentTimeMillis();

		for (int i = 1; i < FileReader.testData.size(); i++) {

			List<String> testData = FileReader.testData.get(i);

			// This will iterate based on no of unique attribute in target
			// Column
			Map<String, Double> _val = new HashMap<String, Double>();
			for (String targetColumn : FileReader.uniqueConbination.get(targetAttrName)) {
				Double probablity = FileReader.attributeCount.get(targetColumn);

				for (int j = 0; j < testDataHeader.size(); j++) {
					if (!testDataHeader.get(j).equals(targetAttrName)) {

						if (targetAttrName.equals("humidity")) {
							probablity += Math.log(CalculateProbablities._otherMap.get(targetColumn)
									.get(testDataHeader.get(j)).get(testData.get(j)));
						} else {
							probablity *= CalculateProbablities._otherMap.get(targetColumn).get(testDataHeader.get(j))
									.get(testData.get(j));
						}
					}
				}
				_val.put(targetColumn, probablity);
			}

			Double large = 0d;
			String classification = "";
			// Now check which value is greater in Map of Target Attribute
			for (Map.Entry<String, Double> _which : _val.entrySet()) {
				if (_which.getValue() > large) {
					large = _which.getValue();
					classification = _which.getKey();
				}
			}

			for (int k = 0; k < testData.size(); k++) {
				writer.print(testData.get(k));
				writer.print("  ");
			}
			writer.print(classification);
			writer.println("");

			if (testData.get(attribute - 1).equals(classification)) {
				total++;
			}
		}
		long to = System.currentTimeMillis();
		writer.println("Accuracy: " + total + "/" + (FileReader.testData.size() - 1));
		writer.println("Time taken: " + (to-from)+" Milliseconds");
		writer.close();
	}

	/**
	 * @param attribute
	 * This function calculates the probability for all other function other than Target attribute
	 */
	public static void getPriorProbablitiesForOtherAttribute(String attribute) {

		List<String> targetAttr = FileReader.uniqueConbination.get(attribute);

		// Iterate over target attribute
		for (String _target : targetAttr) {
			Map<String, Map<String, Double>> _attribute = new HashMap<String, Map<String, Double>>();

			// Iterate over unique entries of column
			for (Map.Entry<String, List<String>> _uniqueEntries : FileReader.uniqueConbination.entrySet()) {

				// Create Last Map Which will store the probablity
				Map<String, Double> _attrProbablity = new HashMap<String, Double>();

				// Exclude Target Attribute
				if (!_uniqueEntries.getKey().equals(attribute)) {
					for (String _eachAttr : _uniqueEntries.getValue()) {

						Double k = 0d;
						for (int j = 1; j < FileReader.transaction.size(); j++) {
							if (FileReader.transaction.get(j).contains(_eachAttr)
									&& FileReader.transaction.get(j).contains(_target)) {
								k++;
							}
						}
						_attrProbablity.put(_eachAttr, k / FileReader.attributeCount.get(_target));
					}
					_attribute.put(_uniqueEntries.getKey(), _attrProbablity);
				}
			}
			_otherMap.put(_target, _attribute);
		}
	}
}
