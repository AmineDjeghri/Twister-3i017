package test.dbTests;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnectionTest {

	public static void main(String[] args) {
		//testMongoDBCloudDB();
		testMongoDBLocalDB();
	}

	private static void testMongoDBLocalDB() {
		MongoClientURI uri= new MongoClientURI("mongodb://localhost");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("twister_BD");
		MongoCollection<Document> collection = database.getCollection("comment");
		
		System.out.println("Connection Established");
		Document d= new Document();
		d.append("name", "Amine");
		collection.insertOne(d);
		
		mongoClient.close();
	}
	
	
	private static void testMongoDBCloudDB() {
		MongoClientURI uri = new MongoClientURI("mongodb://root:root@cluster0-shard-00-00-loipi.mongodb.net:27017,cluster0-shard-00-01-loipi.mongodb.net:27017,cluster0-shard-00-02-loipi.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
	    MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("twister_BD");
        MongoCollection<Document> collection = database.getCollection("comment");
		
		System.out.println("Connection Established");
		
		Document d= new Document();
		d.append("name", "Amine");
		collection.insertOne(d);
		
		mongoClient.close();
	}
}
