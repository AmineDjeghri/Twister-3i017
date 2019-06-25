package tools;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBCollection;
import com.mongodb.MapReduceCommand;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class TwistTools {

	public static void addTwist(String id_user,String login, String firstname, String familyname, String message, MongoCollection<Document> collection) {
		Document doc =new Document();
		
		doc.append("id_user", id_user);
		
		doc.append("firstname", firstname);
		doc.append("familyname", familyname);
		
		
		doc.append("login", login);
		
		doc.append("id_message", ObjectId.get().toHexString());
		doc.append("date",new Timestamp(System.currentTimeMillis()) );
		doc.append("text", message);

		List<Document> comments = new ArrayList<>();
		doc.append("comments", comments);
		
		List<Document> likes = new ArrayList<>();
		doc.append("likes", likes);
		collection.insertOne(doc);
	}
	
	public static boolean checkAuthor(String id_user, String id_message, MongoCollection<Document> collection) {
		Document doc =new Document();
		doc.append("id_message", id_message);
		doc.append("id_user", id_user);
		
		MongoCursor<Document> result = (MongoCursor<Document>) collection.find(doc).iterator();
		
		if(result.hasNext()) {
			return true;
		}
		return false;
	}
	
	
	public static void removeTwist(String id_user, String id_message, MongoCollection<Document> collection) {
		Document doc =new Document();
		doc.append("id_message", id_message);		
		collection.deleteOne(doc);

	}

	public static boolean twistExists(String id_message, MongoCollection<Document> collection) {
		Document doc =new Document();
		doc.append("id_message", id_message);
		
		MongoCursor<Document> result = (MongoCursor<Document>) collection.find(doc).iterator();
		
		if(result.hasNext()) {
			return true;
		}
		return false;
	}

	public static JSONObject listTwists(String id_user, MongoCollection<Document> collection) throws JSONException {
		JSONObject ob = new JSONObject();
		
		Document doc =new Document();
		doc.append("id_user", id_user);
		
		MongoCursor<Document> result = (MongoCursor<Document>) collection.find(doc).sort(new Document("date",-1)).iterator();
		
			
		ArrayList<JSONObject> messages = new ArrayList<>();
		
		while(result.hasNext()) {
			Document o = result.next();
			JSONObject message = new JSONObject();
			message.put("id_user", o.get("id_user"));
			message.put("id_message", o.get("id_message"));
			message.put("login", o.get("login"));
			message.put("firstname", o.get("firstname"));
			message.put("familyname", o.get("familyname"));
			message.put("date", o.get("date"));
			message.put("text", o.get("text"));
			if(o.get("twist") != null) {
				message.put("twist", o.get("twist"));
			}
			message.put("comments", o.get("comments"));
			message.put("likes", o.get("likes"));
			messages.add(message);
		}
		
		ob.put("twists", messages);
		
		return ob;
		
	}
	
	public static Document getTwist(String id_message,  MongoCollection<Document> collection) {
		Document doc =new Document();
		doc.append("id_message", id_message);
	
		Document message = new Document();
		
		MongoCursor<Document> result = (MongoCursor<Document>) collection.find(doc).sort(new Document("date",-1)).iterator();
		if(result.hasNext()) {
			message = result.next();
		}
		return message;
	}

	public static JSONObject listTwists(MongoCollection<Document> collection) throws JSONException {
		JSONObject ob = new JSONObject();
		
		MongoCursor<Document> result = (MongoCursor<Document>) collection.find().iterator();
		
		ArrayList<JSONObject> messages = new ArrayList<>();
		
		while(result.hasNext()) {
			Document o = result.next();
			JSONObject message = new JSONObject();
			message.put("id_user", o.get("id_user"));
			message.put("firstname", o.get("firstname"));
			message.put("familyname", o.get("familyname"));
			message.put("id_message", o.get("id_message"));
			message.put("date", o.get("date"));
			message.put("text", o.get("text"));
			message.put("comments", o.get("comments"));
			message.put("likes", o.get("likes"));

			messages.add(message);
		}
		ob.put("twists", messages);
		return ob;
	}



	public static void reTwist(String id_message, String id_user, String message,
		MongoCollection<Document> collection) {
		Document doc =new Document();
		
		doc.append("id_user", id_user);
		doc.append("id_message", ObjectId.get().toHexString());
		doc.append("date",new Timestamp(System.currentTimeMillis()) );
		if(message != null) {
			doc.append("text", message);
		}
		Document old_message =new Document();
		old_message= TwistTools.getTwist(id_message, collection);
		
		
		Document twist =new Document();
		twist.append("id_user", old_message.get("id_user"));
		twist.append("id_message",old_message.get("id_message"));
		twist.append("date",old_message.get("date") );
		twist.append("text", old_message.get("text"));
		twist.append("comments", old_message.get("comments"));
		twist.append("likes", old_message.get("likes"));
		
		doc.append("twist", twist);
		
		List<Document> comments = new ArrayList<>();
		doc.append("comments", comments);
		
		List<Document> likes = new ArrayList<>();
		doc.append("likes", likes);
		collection.insertOne(doc);
	}

	public static void editTwist(String id_message, String message,
			MongoCollection<Document> collection) {
		
		// on récupére le message
		Document twist = new Document();
		twist = TwistTools.getTwist(id_message, collection);
		
		Document set = new Document();
		set.append("text", message);
		// On crée un nouvel élement 
		Document remplacement = new Document();
		remplacement.append("$set" , set);
		

		// On modifie la base de données 
		collection.updateOne(twist, remplacement);
	}

	public static String getnbTwists(String id_user, 
			MongoCollection<Document> collection) {
		
		Document doc =new Document();
		doc.append("id_user", id_user);

		long count = collection.count(doc);

	
		return Integer.toString((int) count);
	}

	
	public static JSONObject search(String query, String friends, DBCollection index) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
