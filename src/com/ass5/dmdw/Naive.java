package com.ass5.dmdw;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.ass5.io.FileReader;

public class Naive {

	public static String trfileName = "";
	public static String tefileName = "";
	public static int attribute = 0;

	/**
	 * @param ar
	 */
	public static void main(String[] ar) {

		Scanner in = new Scanner(System.in);
		do {
			System.out.print("Please Enter the valid training data set name:");
			trfileName = in.nextLine();
		} while (!new File("/Users/bharatjain/Desktop/naive/" + trfileName).exists());

		do {
			System.out.print("Enter the valid testing data set name:");
			tefileName = in.nextLine();
		} while (!new File("/Users/bharatjain/Desktop/naive/" + tefileName).exists());

		try {

			// Read Training Data Set
			FileReader.readFile(trfileName);

			// Read Testing DataSet
			FileReader.readTestDataSet(tefileName);

			List<String> _header = FileReader.transaction.get(0);
			System.out.println("Choose an attribute (by number):");

			for (int i = 0; i < _header.size(); i++) {
				System.out.println("\t" + (i + 1) + ". " + _header.get(i));
			}
			do {
				System.out.println("Please select the valid attribute:");
				attribute = in.nextInt();
			} while (attribute < 1 || attribute > _header.size());

			// Get count for all the attributes
			FileReader.countTargetAttribute(_header.get(attribute - 1));

			// Calculate probability for Target Attribute
			CalculateProbablities.getPriorProbablitiesForTarget(_header.get(attribute - 1));

			// Calculate probability for all other Attributes
			CalculateProbablities.getPriorProbablitiesForOtherAttribute(_header.get(attribute - 1));

			// Find the classification
			CalculateProbablities.TestProbablity(_header.get(attribute - 1), attribute);

			System.out.println("Please check the output file named Result.txt");
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
