package tools;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import db.Database;

public class LikeTools {

	public static void addLike(String id_message, String id_user, String firstname, String familyname, String login,
		MongoCollection<Document> collection)  throws JSONException{
		
		Document like = new Document();
		LocalDateTime date = LocalDateTime.now();

		// on récupére le message
		Document message = new Document();
		message = TwistTools.getTwist(id_message, collection);
		
		// on recupére la liste des message (un seul normalement)
		MongoCursor<Document> result = (MongoCursor<Document>) collection.find(message).iterator();
		
		// on récupére le message dans un Document
		Document res = new Document();
		if (result.hasNext()) {
			res = result.next();
		}
		
		// on récupére la liste des likes du messages
		ArrayList<Document> list_likes = (ArrayList<Document>) res.get("likes");
		
		ObjectId objectId = new ObjectId();
		like.append("id_like", objectId);
		like.append("firstname", firstname);
		like.append("familyname", familyname);
		like.append("id_user", id_user);
		like.append("login", login);
		like.append("date", date);
		
		// On rajoute ce like à la liste récupére précedament
		list_likes.add(like);
		
		// On crée un nouvel élement 
		Document remplacement = new Document();
		remplacement.append("$set" , new Document().append("likes", list_likes));

		// On modifie la base de données 
		collection.updateOne(message, remplacement);
		
	}

	public static void removeLike(String id_message, String id_like, MongoCollection<Document> collection){
		Document filter = new Document("id_like", new ObjectId(id_like));
		Document query = new Document("likes", filter);
		Document msg_query = new Document("$pull", query);
		
		Document updateOperationDocument = new Document("id_message", id_message);
		collection.updateOne(updateOperationDocument, msg_query);
	}

	public static JSONObject listLike(String id_message, MongoCollection<Document> collection) throws JSONException{
		JSONObject message_likes = new JSONObject();
		ArrayList<JSONObject> likes = new ArrayList<>() ;
		
		// on récupére le message
		Document message = new Document();
		message = TwistTools.getTwist(id_message, collection);
		
		// puis on récupére les likes
		ArrayList<Document> list_likes = (ArrayList<Document>) message.get("likes");
		
		Iterator<Document> cur = list_likes.iterator();
		
		while(cur.hasNext()) {
			Document like = cur.next();
			JSONObject oneLike = new JSONObject();
			oneLike.put("id_like", like.get("id_like"));
			oneLike.put("id_user", like.get("id_user"));
			oneLike.put("login", like.get("login"));			
			oneLike.put("date", like.get("date"));
			likes.add(oneLike);
		}
		
		message_likes.put("likes", likes);
		return message_likes;
	}
	
	public static boolean checkAuthor(String id_message, String id_user, MongoCollection<Document> collection) {
		// on récupére le message
		Document message = new Document();
		message = TwistTools.getTwist(id_message, collection);
		
		// on recupére la liste des likes 
		ArrayList<Document> list_likes = (ArrayList<Document>) message.get("likes");
		Iterator<Document> cur = list_likes.iterator();
		
		while(cur.hasNext()) {
			Document like = cur.next();
			String author_message = (String) like.get("id_user");
			
			if(author_message != null) {
				if(author_message.compareTo(id_user) == 0) {
					return true;
				}
			}
		}
		return false;
		
	}
	

	public static boolean likeExists(String id_message, String id_like, MongoCollection<Document> collection) {
		// on récupére le message
		Document message = new Document();
		message = TwistTools.getTwist(id_message, collection);
		
		// on recupére la liste des comments
		ArrayList<Document> list_likes = (ArrayList<Document>) message.get("likes");
		Iterator<Document> cur = list_likes.iterator();
		
		while(cur.hasNext()) {
			Document like = cur.next();
			ObjectId id_like_message = (ObjectId) like.get("id_like");
			
			if(id_like_message != null) {
				if((id_like_message.toString()).equals(id_like)) {
					return true;
				}
			}
	
		}
		return false;
	}
	
	
	
	
}
