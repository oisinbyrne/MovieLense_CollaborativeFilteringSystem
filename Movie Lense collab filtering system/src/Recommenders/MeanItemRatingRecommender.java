package Recommenders;
import java.io.FileWriter;
import java.io.IOException;

import Data.Item;
import Data.RatingsData;
import Data.User;

public class MeanItemRatingRecommender {
	private RatingsData ratingsData;
	public int totalUserItemPairs;
	public double coverage;

	public MeanItemRatingRecommender(RatingsData data) {
		ratingsData = data;
		coverage = 0;
		totalUserItemPairs = data.userCount * data.itemCount;
	}
	
	public void generatePredictions() {
		double prediction = 0;
		double itemsPredictable = 0;
		for(User user : ratingsData.userMap.values()) {
			for(Item item : ratingsData.itemMap.values()) {
				prediction = meanItemRating(user.id, item.id);
				if(prediction != 0) itemsPredictable++;
			}
		}
		coverage = (((double)itemsPredictable)/totalUserItemPairs) * 100;
	}
	
	// Returns the average rating for this item calculated across all ratings
	// for this item, other that the rating given by user_id
	// Saves average rating to user's predictedRatings HashMap
	public double meanItemRating(int user_id, int item_id) {
		Item item = ratingsData.itemMap.get(item_id);
		User user = ratingsData.userMap.get(user_id);
		double predictedRating;

		predictedRating = item.getMeanRatingForUser(user_id);
		user.predictedRatings.put(item_id, predictedRating);
		return predictedRating;
	}

	public double calculateRMSE(String file) throws IOException {
		double error = 0;
		double sum = 0;
		int count = 0;
		double prediction = 0;
		String predictionOutput = "";
		String errorOutput = "";
		FileWriter fw = null;
		if(!file.equals("")) fw = new FileWriter(file);
		
		for(User user : ratingsData.userMap.values()) {
			//For each item user has rated
			for(int key : user.ratings.keySet()) {
				prediction = user.predictedRatings.get(key);
				//If rating can be predicted
				if(prediction != 0) {
					error = Math.abs(prediction - user.ratings.get(key));
					errorOutput = String.format("%.2f", error);
					error = Math.pow(error, 2);
					sum += error;
					count++;
					predictionOutput = String.format("%.2f", prediction);
				}
				else {
					predictionOutput = "N/A";
					errorOutput = "N/A";
				}
				if(!file.equals("")) fw.write("" + user.id + "," + key + "," + user.ratings.get(key) + "," + predictionOutput + "," + errorOutput + "\n");
			}
		}
		if(!file.equals("")) fw.close();
		return Math.sqrt(sum/count);
	}
}
