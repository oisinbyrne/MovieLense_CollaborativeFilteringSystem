package Testing;
import Data.Item;
import Data.Profile;
import Data.RatingsData;
import Data.User;
import Recommenders.MeanItemRatingRecommender;

public class TaskOneTest {
	
	public static void main(String[] args) throws Exception {
		RatingsData ratingsData = new RatingsData("ratings.csv");
		System.out.println("Part 1\n--------------------");
		System.out.println("- Number of users: " + ratingsData.userCount);
		System.out.println("- Number of items: " + ratingsData.itemCount);
		System.out.println("- Number of ratings: " + ratingsData.ratingsCount);
		System.out.println("- Ratings density metric: " + String.format("%.2f", ratingsData.ratingsDensity) + "%");
		
		System.out.println("\n- Stats per user");
		for(User user : ratingsData.userMap.values()) {
			printProfileData(user);
		}
		System.out.println("\n- Stats per Item");
		for(Item item : ratingsData.itemMap.values()) {
			printProfileData(item);
		}
		
		MeanItemRatingRecommender recommender = new MeanItemRatingRecommender(ratingsData);
		System.out.println("\n- Baseline Prediction for user 22 and item 377: " + recommender.meanItemRating(22, 377));
		System.out.println("\n- Actual Rating for user 22 and item 377: " + ratingsData.userMap.get(22).ratings.get(377));
		System.out.println("Generating predictions for all user item pairs...");
		recommender.generatePredictions();
		System.out.println("\n- Coverage of meanItemRating: " + String.format("%.2f", recommender.coverage) + "%");
	}
	
	private static void printProfileData(Profile profile) {
		System.out.println(profile.type + " id: " + profile.id
				+ "\n\t" + "Mean Rating: " + profile.getMeanRating()
				+ "\n\t" + "Median Rating: " + profile.getMedianRating()
				+ "\n\t" + "Standard Deviation: " + profile.getStandardDeviation()
				+ "\n\t" + "Max Rating: " + profile.maxRating
				+ "\n\t" + "Min Rating: " + profile.minRating
				+ "\n\t" + "Number of rating of each rating class [*, **, ***, ****, *****]: ["
					+ profile.oneStarCount + ", "
					+ profile.twoStarCount + ", "
					+ profile.threeStarCount + ", "
					+ profile.fourStarCount + ", "
					+ profile.fiveStarCount + "]"
		);
	}
}