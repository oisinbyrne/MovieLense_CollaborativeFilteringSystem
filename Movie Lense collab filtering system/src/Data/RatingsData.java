package Data;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

//Reads in and stores the data
public class RatingsData {
	public HashMap<Integer, User> userMap = new HashMap<Integer, User>();
	public HashMap<Integer, Item> itemMap = new HashMap<Integer, Item>();
	public int userCount = 0;
	public int itemCount = 0;
	public int ratingsCount = 0;
	public double ratingsDensity = 0;

	public RatingsData(String filename) throws Exception {
		loadContentsFromFile(filename);
		userCount = userMap.size();
		itemCount = itemMap.size();
		ratingsDensity = (((double) ratingsCount) / (userCount * itemCount) * 100);
	}

	private void loadContentsFromFile(String file) throws Exception {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringTokenizer tokens = null;
		int user_id, item_id, rating;

		try {
			String line = null;
			is = new FileInputStream(file);
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			while((line = br.readLine()) != null) {
				ratingsCount++;

				tokens = new StringTokenizer(line, ",");
				user_id = Integer.parseInt(tokens.nextToken());
				item_id = Integer.parseInt(tokens.nextToken());
				rating = Integer.parseInt(tokens.nextToken());
				Integer.parseInt(tokens.nextToken());

				User user;
				if(userMap.containsKey(user_id)) user = userMap.get(user_id);
				else user = new User(user_id);

				user.addRating(item_id, rating);
				userMap.put(user_id, user);

				Item item;
				if(itemMap.containsKey(item_id)) item = itemMap.get(item_id);
				else item = new Item(item_id);

				item.addRating(user_id, rating);
				itemMap.put(item_id, item);
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				is.close();
			}
			if(isr != null) {
				isr.close();
			}
			if(br != null) {
				br.close();
			}
		}
	}
}
