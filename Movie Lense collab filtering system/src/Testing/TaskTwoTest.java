package Testing;
import java.util.Scanner;

import Data.RatingsData;
import Recommenders.MeanItemRatingRecommender;

public class TaskTwoTest {
	static RatingsData ratingsData;
	static MeanItemRatingRecommender recommender;
	
	public static void main(String[] args) throws Exception {
		System.out.println("Would you like to Print RMSE values or calculate average runttime of a L1O cycle?");
		Scanner scan = new Scanner(System.in);
		String input = "";
		do {
			System.out.println("Please enter p for print or r for runttime");
			input = scan.nextLine();
			if(input.equalsIgnoreCase("p")) {
				printRMSE();
				break;
			}
			else if(input.equalsIgnoreCase("r")) {
				calculateAverageRunttime();
				break;
			}
			else continue;
		}while(true);
		
		scan.close();
	}
	
	private static void printRMSE() throws Exception {
		System.out.println("Reading in data file...");
		ratingsData = new RatingsData("ratings.csv");
		recommender = new MeanItemRatingRecommender(ratingsData);
		
		String filename = "task_2_predictions.csv";
		System.out.println("Generating predictions...");
		recommender.generatePredictions();
		System.out.println("Writing RMSE User-Item values to " + filename + "...");
		System.out.println("Average error (RMSE): " + String.format("%.2f", recommender.calculateRMSE(filename)));
		System.out.println("Coverage: " + String.format("%.2f", recommender.coverage) + "%");
	}
	
	private static void calculateAverageRunttime() throws Exception {
		System.out.println("Reading in data file...");
		ratingsData = new RatingsData("ratings.csv");
		recommender = new MeanItemRatingRecommender(ratingsData);
		
		long startTime, endTime;
		long sum = 0;
		
		for(int i=0; i < 10; i++){
			startTime = System.currentTimeMillis() / 1000;
			
			recommender.generatePredictions();
			System.out.print((i+1) + "/ RMSE: " + String.format("%.2f", recommender.calculateRMSE("")));
			System.out.print("\tCoverage: " + String.format("%.2f", recommender.coverage) + "%");
			
			endTime = System.currentTimeMillis() / 1000;
			System.out.println("\tTime: " + (endTime-startTime) + " seconds");
			sum += (endTime-startTime);
		}

		System.out.println("\nAverage runttime for L1O test cycle: " + (sum/10) + " seconds");
	}
}
