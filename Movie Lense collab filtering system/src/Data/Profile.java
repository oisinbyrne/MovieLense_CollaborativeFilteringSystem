package Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class Profile {
	public int id;
	public String type;
	// Maps Item_id/User_id to Ratings
	public HashMap<Integer, Integer> ratings;
	public int maxRating = Integer.MIN_VALUE;
	public int minRating = Integer.MAX_VALUE;
	public int oneStarCount = 0;
	public int twoStarCount = 0;
	public int threeStarCount = 0;
	public int fourStarCount = 0;
	public int fiveStarCount = 0;

	public Profile(int id, String type) {
		this.id = id;
		this.type = type;
		ratings = new HashMap<Integer, Integer>();
	}

	public void addRating(int id, int rating) {
		ratings.put(id, rating);
		if (rating > maxRating)
			maxRating = rating;
		if (rating < minRating)
			minRating = rating;

		switch (rating) {
		case 1:
			oneStarCount++;
			break;
		case 2:
			twoStarCount++;
			break;
		case 3:
			threeStarCount++;
			break;
		case 4:
			fourStarCount++;
			break;
		case 5:
			fiveStarCount++;
			break;
		}
	}

	public double getMeanRating() {
		int sum = 0;
		for (int key : ratings.keySet()) {
			sum += ratings.get(key);
		}
		return ((double) sum) / ratings.size();
	}

	public int getMedianRating() {
		// Sort ratings values
		List<Integer> ratingsList = new ArrayList<Integer>();
		ratingsList.addAll(ratings.values());
		Collections.sort(ratingsList);

		int middle = ratingsList.size() / 2;
		if(ratingsList.size() % 2 == 1) {
			return ratingsList.get(middle);
		} else {
			return (ratingsList.get(middle - 1) + ratingsList.get(middle)) / 2;
		}
	}

	public double getStandardDeviation() {
		double mean = getMedianRating();
		double stdev = 0;
		for(double val : ratings.values()) {
			stdev += (val - mean) * (val - mean);
		}
		stdev = stdev / ratings.size();
		return Math.sqrt(stdev);
	}
}
