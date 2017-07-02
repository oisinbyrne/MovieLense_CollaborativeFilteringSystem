package Recommenders;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Data.Item;
import Data.RatingsData;
import Data.User;

import java.util.Set;

public class SimilarityRecommender {
	private RatingsData ratingsData;
	public int totalUserItemPairs;
	public double coverage;
	public int neighbourhoodSize;
	public int maxDifference;

	public SimilarityRecommender(RatingsData data) {
		ratingsData = data;
		coverage = 0;
		totalUserItemPairs = data.userCount * data.itemCount;
		createSimilarityScoresForAll();
	}
	
	//Calculates the similarity between each user-user pair
	//Stores value in the similarUsers hashMap of each
	private void createSimilarityScoresForAll() {
		//Array of User objects
		User[] users = ratingsData.userMap.values().toArray(new User[ratingsData.userCount]);
		int j;
		double sim=0;
		for(int i=0; i < users.length-1; i++) {
			for(j=i+1; j < users.length; j++) {
				sim = calculateSimilarity(users[i], users[j]);
				users[i].similarUsers.put(users[j].id, sim);
				users[j].similarUsers.put(users[i].id, sim);
			}
		}
	}

	// Returns 0 if identical, NaN if no ratings in common, otherwise the MSD
	private double calculateSimilarity(User a, User b) {
		double similarity = 0;
		double bothRatedCount = 0;

		for(Item item : ratingsData.itemMap.values()) {
			if((a.hasRated(item.id))&&(b.hasRated(item.id))) {
				similarity += Math.pow((a.ratings.get(item.id) - b.ratings.get(item.id)), 2);
				bothRatedCount++;
			}
		}
		similarity = similarity/bothRatedCount;
		return similarity;
	}

	//Finds the n nearestNeighbours to each User
	//Generates prediction for each
	public void generatePredictions(int n, int maxDiff) {
		neighbourhoodSize = n;
		maxDifference = maxDiff;
		double prediction = 0;
		double itemsPredictable = 0;
		//User_id and similarity of similarUsers
		HashMap<Integer, Double> similarUsers;
		double totalWeight;
		double weight = 0;
		double sumRating;
		User neighbour;
		
		for(User user : ratingsData.userMap.values()) {
			//Generates a hashMap of user's n nearest neighbours
			similarUsers = nearestNeighboursTo(user);
			
			for(int item : ratingsData.itemMap.keySet()) {
				totalWeight=0;
				sumRating=0;
				for(int key : similarUsers.keySet()) {
					neighbour = ratingsData.userMap.get(key);
					if(neighbour.hasRated(item)) {
						weight = 1-((similarUsers.get(key))/maxDifference);
						totalWeight += weight;
						sumRating += weight * neighbour.ratings.get(item);
					}
				}
				
				//If no neighbours have rated this item
				if(totalWeight == 0) prediction = 0;
				else prediction = sumRating/totalWeight;
				
				user.predictedRatings.put(item, prediction);
				if(prediction != 0) itemsPredictable++;
			}
		}
		coverage = (itemsPredictable/totalUserItemPairs) * 100;
	}
	
	//Generates predictions for each user item pair
	//according to Resnick's formula
	public void generateResnickPredictions(int n, int maxDiff) {
		neighbourhoodSize = n;
		maxDifference = maxDiff;
		double prediction = 0;
		double itemsPredictable = 0;
		//User_id and similarity of similarUsers
		HashMap<Integer, Double> similarUsers;
		double totalSim;
		double sumRating;
		User neighbour;
		
		for(User user : ratingsData.userMap.values()) {
			//Generates a hashMap of user's n nearest neighbours
			similarUsers = nearestNeighboursTo(user);
			
			for(int item : ratingsData.itemMap.keySet()) {
				totalSim=0;
				sumRating=0;
				for(int key : similarUsers.keySet()) {
					neighbour = ratingsData.userMap.get(key);
					if(neighbour.hasRated(item)) {
						sumRating += (neighbour.ratings.get(item) - neighbour.getMeanRating()) * similarUsers.get(key);
						totalSim += similarUsers.get(key);
					}
				}

				//If no neighbours have rated this item
				if(totalSim == 0) prediction = user.getMeanRating();
				else prediction = user.getMeanRating() + (sumRating/totalSim);
				
				user.predictedRatings.put(item, prediction);
				if(prediction != 0) itemsPredictable++;
			}
		}
		coverage = (itemsPredictable/totalUserItemPairs) * 100;
	}
	
	private HashMap<Integer, Double> nearestNeighboursTo(User user) {
		HashMap<Integer, Double> nearestNeighbours = new HashMap<Integer, Double>();

		Set<Entry<Integer, Double>> entries = user.similarUsers.entrySet();
		ArrayList<Entry<Integer, Double>> list = new ArrayList<Entry<Integer, Double>>(entries);
		
		double minSim;
		double sim;
	    int indexofMinSim = 0;
	    for(int j=0; j < neighbourhoodSize; j++){
	    	minSim = Integer.MAX_VALUE;
	    	//Find lowest similarity in array
	    	for(int i=0; i < list.size(); i++) {
	    		//If entry has not been set to null
	    		if(list.get(i) == null) continue;
	    		else {
	    			sim = list.get(i).getValue();
	    			//If Users have no ratings in common
	    			if(Double.isNaN(sim)) {
	    				continue;
	    			}
	    			if(list.get(i).getValue() <= minSim) {
			    		minSim = list.get(i).getValue();
			    		indexofMinSim = i;
			    	}
	    		}
		    }
	    	nearestNeighbours.put(list.get(indexofMinSim).getKey(), list.get(indexofMinSim).getValue());
	    	//Set entry to null
	    	list.set(indexofMinSim, null);
	    }
	    return nearestNeighbours;
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
