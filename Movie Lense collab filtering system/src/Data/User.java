package Data;
import java.util.HashMap;

public class User extends Profile {
	// Map Items to Predicted Ratings
	public HashMap<Integer, Double> predictedRatings;
	// Similar users, mapping user_id to Similarity
	public HashMap<Integer, Double> similarUsers;

	public User(int user_id) {
		super(user_id, "User");
		predictedRatings = new HashMap<Integer, Double>();
		similarUsers = new HashMap<Integer, Double>();
	}

	public boolean hasRated(int item_id) {
		return (ratings.get(item_id) != null);
	}
}