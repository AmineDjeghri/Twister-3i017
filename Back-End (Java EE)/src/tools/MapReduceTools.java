package tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;


import db.Database;

public class MapReduceTools {
	
	//  	A JavaScript function that associates or “maps” a value with a key and emits the key and value pair.
	static String mapDF="function map(){" +
			"var text=this.text;" +
			"var words=text.match(/\\w+/g);" +
			"var df=[];" +
			"for (w in words){" +
				"if(df[words[w]]==null){" +
					"df[words[w]]=1;" +
				"}for(w in df){" +
					"emit(w,{df:1});" +
				"}" +
			"}" +
		"}";
	
	// 		A JavaScript function that “reduces” to a single object all the values associated with a particular key.
	static String reduceDF="function reduce(key, values){	" +
			"var total=0;	" +
			"for(i in values){		" +
				"total+=values[i].df;	" +
				"};	" +
				"return({word: key, df:total});" +
				"}";
	
	static String mapTF="function mapTF(){	" +
			"						var text=this.text;	" +
									"var words=text.match(/\\w+/g);	" +
									"var tf=[];	" +
									"for(i in words){" +
										"if(tf[words[i]]==null){	" +
											"tf[words[i]]=1;" +
											"}else{" +
												"tf[words[i]]++;" +
											"}" +
									"}" +
									"for(w in tf){" +
									"	emit(this._id, {words: w, tf: tf[w]});" +
									"}" +
								"}";
	
	static String reduceTF="function reduceTF(key, value){	" +
			"return({doc: key, tfs: value});" +
			"}";
	
	
	private static void indexDf(Connection connection, MongoCollection<Document> collection) throws SQLException{
		
		//		The mapReduce command allows you to run map-reduce aggregation operations over a collection.
		MapReduceIterable<Document> outDF=collection.mapReduce(mapDF,reduceDF);
		
		for(Document obj: outDF){
			System.out.println(obj);
			
			// On trouve la valeur
			Document value = (Document) obj.get("value");	
			String word=(String) obj.get("_id");
			
			int df=((Double)value.get("df")).intValue();
			
			String query = "INSERT INTO "
					+ "df_table(mot, df) "
					+ "VALUES (?,?);";

			PreparedStatement preparedStatement =null;
			try{
				preparedStatement=	connection.prepareStatement(query);
				preparedStatement.setString(1, word);
				preparedStatement.setLong(2, df);

				preparedStatement.executeQuery();
			}finally {
				if(preparedStatement!=null)
					preparedStatement.close();
			}	
		}
		
	}
	
	private static void indexTf(Connection connection, MongoCollection<Document> collection) throws SQLException{
		MapReduceIterable<Document> outDF=collection.mapReduce(mapTF,reduceTF);
		for (Document document : outDF) {
			Document value = (Document) document.get("value");
			String idPost=((document.get("_id"))).toString();
			ArrayList<Object> tfs=new ArrayList<Object>();
			if(value.containsKey("tfs"))
				tfs= (ArrayList<Object>) value.get("tfs");
			else{
				tfs.add(value);
			}
			
			for (Object ob2 : tfs) {
				Document inst= (Document) ob2;
				String words=(String)inst.get("words");
				int tf=(int)((Double)inst.get("tf")).intValue();
				
				String query = "INSERT INTO "
						+ "tf_table(document, word, tf) "
						+ "VALUES (?,?,?)";
				PreparedStatement preparedStatement =null;
				try{
					preparedStatement=	connection.prepareStatement(query);
					preparedStatement.setString(1, idPost);
					preparedStatement.setString(2, words);
					preparedStatement.setLong(3, tf);

					preparedStatement.executeQuery();
				}finally {
					if(preparedStatement!=null)
						preparedStatement.close();
				}	
				
				
			}
		}
	}
	public static void reinitTables(Connection connection) throws SQLException{	
		// Supression de df_Table
		String queryDelete = "DELETE FROM df_table WHERE 1;";
		PreparedStatement preparedStatementDeleteDf =null;
		try{
			preparedStatementDeleteDf=	connection.prepareStatement(queryDelete);
			preparedStatementDeleteDf.executeQuery();
		}finally {
			if(preparedStatementDeleteDf!=null)
				preparedStatementDeleteDf.close();
		}	
		
		// Supression de tf_Table
		queryDelete= "DELETE FROM tf_table WHERE 1;";
		PreparedStatement preparedStatemenDeleteTf =null;
		try{
			preparedStatemenDeleteTf=	connection.prepareStatement(queryDelete);
			preparedStatemenDeleteTf.executeQuery();
		}finally {
			if(preparedStatemenDeleteTf!=null)
				preparedStatemenDeleteTf.close();
		}
		
		// Supression de tf_Table
		queryDelete= "DELETE FROM length_message WHERE 1;";
		PreparedStatement preparedStatemenDeleteSize =null;
		try{
			preparedStatemenDeleteSize=	connection.prepareStatement(queryDelete);
			preparedStatemenDeleteSize.executeQuery();
		}finally {
			if(preparedStatemenDeleteSize!=null)
				preparedStatemenDeleteSize.close();
		}
	}
	
	
	public static JSONArray search(String query, MongoCollection<Document> collection) throws SQLException, JSONException{
		reinitTables(Database.getMySQLConnection());
		indexDf(Database.getMySQLConnection(), collection);
		indexTf(Database.getMySQLConnection(), collection);
		
		JSONArray res=new JSONArray();
		
		//initialisation de la liste des mots de la requete
		Pattern pattern = Pattern.compile("\\w+");
		Matcher matcher = pattern.matcher(query);
		ArrayList<String> words=new ArrayList<String>();
		while(matcher.find())
			words.add(matcher.group());
		StringBuffer listWords=new StringBuffer(256);
		listWords.append('(');
		listWords.append("'"+words.get(0)+"'");
		for(int i=1; i<words.size(); i++){
			listWords.append(",'"+words.get(i)+"'");
		}
		listWords.append(')');

		
		
		//requete
		String requete="SELECT document as idPost, sum(tf_idf) as rsv " +
				"FROM (SELECT document, log(tf*log(8/df)) as tf_idf " +
				"		FROM tf_table, df_table " +
						"WHERE df_table.mot in "+listWords+" and df_table.mot=tf_table.word) as tab_idf group by document order by rsv";
		

		Connection conn = Database.getMySQLConnection();
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery(requete);
		while(rs.next()){
			res.put(TwistTools.getTwist(rs.getString("id_message"), collection));
		}
		rs.close();
		st.close();
		conn.close();
		
		return res;
	}


}
