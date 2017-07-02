package Testing;
import Data.RatingsData;
import Recommenders.SimilarityRecommender;

public class TaskFourTest {
	static RatingsData ratingsData;
	static SimilarityRecommender recommender;
	
	public static void main(String[] args) throws Exception {
		System.out.println("Reading in data file...");
		ratingsData = new RatingsData("ratings.csv");
		System.out.println("Calculating Similarity scores for all " + ratingsData.userCount + " users...");
		System.out.println("(This may take a minute)\n");
		recommender = new SimilarityRecommender(ratingsData);
		System.out.println("------------------------------------------------");

		long startTime, endTime;
		String filename = "";
		int n;

		for(int i=1; i < 11; i++){
			startTime = System.currentTimeMillis() / 1000;
			
			n = i*5;
			filename = "task_4_predictions_n" + n + ".csv";
			
			System.out.println("Generating predictions with neighbourhood size " + n + " and maximum difference 15...");
			recommender.generateResnickPredictions(n, 15);
			System.out.println("Writing RMSE User-Item values to " + filename + "...");
			System.out.println("Average error (RMSE): " + String.format("%.2f", recommender.calculateRMSE(filename)));
			System.out.println("Coverage: " + String.format("%.2f", recommender.coverage) + "%");

			endTime = System.currentTimeMillis() / 1000;
			System.out.println("\tTime: " + (endTime-startTime) + " seconds");
			System.out.println("------------------------------------------------");
		}
	}
}
