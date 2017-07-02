Recommender System
Oisín Byrne


Project Structure
—————————————————
Data:
Each Item and User object extends the Abstract class Profile. Each Profile contains a HashMap mapping user_id/item_id to a rating. This makes it easy to find all ratings for a given item or user. Profile id’s are mapped to their respective Object in two HashMaps in RatingsData

Recommenders:
The MeanItemRatingRecommender and the SimilarityRecommender are created by passing in a RatingsData object

Testing:
Testing for each part of the project is controlled by a testing class. TaskTwoTest.java requires user input to either print results or calculate the average running time.


CSV Files
—————————
CSV files are created for tasks 2,3&4 in the format of ‘task_2_predictions.csv’, ‘task_3_predictions_n[N].csv’ or ‘task_4_predictions_n[N].csv’ where N represents the neighbourhood size