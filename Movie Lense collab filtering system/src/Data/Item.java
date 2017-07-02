package Data;
public class Item extends Profile {
	public Item(int item_id) {
		super(item_id, "Item");
	}

	public double getMeanRatingForUser(int user_id) {
		if(ratings.isEmpty()) return 0;
		int sum = 0;
		for(int key : ratings.keySet()) {
			if(key == user_id) continue;
			else sum += ratings.get(key);
		}
		return ((double) sum) / ratings.size();
	}
}
