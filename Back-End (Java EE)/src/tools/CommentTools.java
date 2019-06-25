package tools;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CommentTools {

	public static void addComment(String id_message,String firstname, String familyname, String comment, String id_user,String login, MongoCollection<Document> collection) throws JSONException {
		Document comments = new Document();

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
		
		// on récupére la liste des commentaires du messages
		ArrayList<Document> list_comments = (ArrayList<Document>) res.get("comments");
		ObjectId objectId = new ObjectId();
		comments.append("id_comment", objectId);
		comments.append("id_user", id_user);
		
		//ici aussi
		comments.append("firstname", firstname);
		comments.append("familyname", familyname);
		comments.append("login", login);
		comments.append("date", new Timestamp(System.currentTimeMillis()));
		comments.append("text", comment);
		
		// On rajoute ce commentaire à la liste récupére précedament
		list_comments.add(comments);
		
		// On crée un nouvel élement 
		Document remplacement = new Document();
		remplacement.append("$set" , new Document().append("comments", list_comments));

		// On modifie la base de données 
		collection.updateOne(message, remplacement);
	}

	
	public static void removeComment(String id_message, String id_comment, MongoCollection<Document> collection) throws JSONException {
		Document filter = new Document("id_comment", new ObjectId(id_comment));
		Document query = new Document("comments", filter);
		Document msg_query = new Document("$pull", query);
		
		Document updateOperationDocument = new Document("id_message", id_message);
		System.out.println(updateOperationDocument);
		System.out.println(msg_query);
		collection.updateOne(updateOperationDocument, msg_query);
		
		
	//	Document id_message_D = new Document().append("id_message", id_message);
	//	Document id_comment_D = new Document().append("id_comment", id_comment);
	//	Document pull= new Document().append("$pull", id_comment_D);
	//	collection.updateOne( id_message_D, pull );
	}

	public static JSONObject listComment(String id_message, MongoCollection<Document> collection) throws JSONException {
		JSONObject message_comments = new JSONObject();
		ArrayList<JSONObject> comments = new ArrayList<>() ;
		
		// on récupére le message
		Document message = new Document();
		message = TwistTools.getTwist(id_message, collection);
		
		// puis on récupére les commentaires
		ArrayList<Document> list_comments = (ArrayList<Document>) message.get("comments");
		
		Iterator<Document> cur = list_comments.iterator();
		
		while(cur.hasNext()) {
			Document comment = cur.next();
			JSONObject oneComment = new JSONObject();
			oneComment.put("id_comment", comment.get("id_comment"));
			oneComment.put("firstname", comment.get("firstname"));
			oneComment.put("familyname", comment.get("familyname"));
			oneComment.put("id_user", comment.get("id_user"));
			oneComment.put("login", comment.get("login"));			
			oneComment.put("date", comment.get("date"));
			oneComment.put("text", comment.get("text"));
			comments.add(oneComment);
		}
		
		message_comments.put("comments", comments);
		return message_comments;
	}


	public static boolean commentExists(String id_message, String id_comment, MongoCollection<Document> collection) {
		// on récupére le message
		Document message = new Document();
		message = TwistTools.getTwist(id_message, collection);
		
		// on recupére la liste des comments
		ArrayList<Document> list_comments = (ArrayList<Document>) message.get("comments");
		Iterator<Document> cur = list_comments.iterator();
		
		while(cur.hasNext()) {
			Document comment = cur.next();
			ObjectId id_comment_message = (ObjectId) comment.get("id_comment");
			
			if(id_comment_message != null) {
				if((id_comment_message.toString()).equals(id_comment)) {
					return true;
				}
			}
	
		}
		return false;
	
	}

	public static boolean checkAuthor(String id_user, String id_message, String id_comment, MongoCollection<Document> collection) {
		// on récupére le message
		Document message = new Document();
		message = TwistTools.getTwist(id_message, collection);
		
		// on recupére la liste des likes 
		ArrayList<Document> list_comments = (ArrayList<Document>) message.get("comments");
		Iterator<Document> cur = list_comments.iterator();
		
		while(cur.hasNext()) {
			Document comment = cur.next();
			String author_comment = (String) comment.get("id_user");
			
			if(author_comment != null) {
				if(author_comment.compareTo(id_user) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	// ne marche pas
	public static void editComment(String id_message, String id_comment, String comment,
			MongoCollection<Document> collection) throws JSONException {
		
			Document filter = new Document("comment", comment);
			Document query = new Document("$set", filter);
			
			Document where = new Document("id_message", id_message);
			where.append("comments.id_comment", id_comment);
			
			collection.updateOne(where, query);
			
	}
}
