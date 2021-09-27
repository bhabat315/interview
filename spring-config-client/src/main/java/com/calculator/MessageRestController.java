package com.calculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RefreshScope
@RestController
class MessageRestController {
 
	@Value("${stringA}")
	private String stringA;

	@Value("${stringB}")
	private String stringB;
	@Value("${serverName}")
	private String serverName;
	
	@Value("${port}")
	private Integer port;
	 
	
	@Value("${collectionName}")
	private String collectionName;
	
	@Value("${databaseName}")
	private String databaseName;
	

	@Value("${userName}")
	private String userName;

	@Value("${password}")
	private String password;
	
	 
	
 //serverName

	@GetMapping("/stringOperations")
	public String calculator() {
		char[] charsA = stringA.toCharArray();
        
        char[] charsB = stringB .toCharArray();
         
        Set<Character> setA = new TreeSet<>();
         
        Set<Character> setB = new TreeSet<>();
        
         for (char c : charsA)  {
            setA.add(c);
        }
         
        for (char c : charsB)
        {
            setB.add(c);
        }
 
       //seta:  1 3 5 7 9 4 6
        //setb: 0 2 4 6 8 1 5 
        //common : 1 4 6 5 
        //a hs but b doesnt have  : a-common=a has but b doesnt have 
        Set<Character> Bcopy =   new HashSet<>(setB);
        StringBuffer strBuff = new StringBuffer();
        strBuff.append(" String A elements "+ setA);
        strBuff.append(" <br> \n");
        strBuff.append(" String B elements "+ setB);
        strBuff.append(" <br> \n");
        setB.retainAll(setA);
        strBuff.append("Common elements "+setB);
        
        strBuff.append(" <br> \n");
        
        setA.removeAll(setB);
        strBuff.append("\n A has but B doesnt have  "+setA);
        
        strBuff.append(" <br> \n");
        Bcopy.removeAll(setB);
        strBuff.append("\n B has but A doesnt have  "+Bcopy);
       
		return strBuff.toString();
	}
	
	@GetMapping("/streamOperations")
	private String method() {

		StringBuffer strBuff = new StringBuffer();
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "naresh");
		map.put(2, "vijay");
		map.put(3, "vishal");
		map.put(4, "venkat");
		map.put(5, "bala");
		strBuff.append("<br>. Export Map values to List...<br>");

		List<String> result2 = map.values().stream().collect(Collectors.toList());
		result2.forEach(elem -> strBuff.append(elem + "<br>"));

		strBuff.append("<br> Parallel stream <br>");

		result2.parallelStream().forEach(elem -> strBuff.append(elem + "<br>"));

		return strBuff.toString();

	}
	@GetMapping("/databaseOperations")
	public String  databaseOperations() {
		StringBuffer strBuff = new StringBuffer();
		
		MongoCredential credential = MongoCredential.createCredential(userName, databaseName, password.toCharArray());
		MongoDatabase database = new MongoClient(serverName, port).getDatabase(databaseName);
		MongoCollection<Document> collection = database.getCollection(collectionName);
		strBuff.append("<br> ****** Collection myCollection selected successfully");

		Document document = new Document("title", "MongoDB").append("description", "database").append("mychoice", 100);
		collection.insertOne(document);

		// Retrieving a collection
		MongoCollection<Document> retrivedCollection = database.getCollection(collectionName);
		strBuff.append("<br> ====== Collection collectionName selected successfully");

		FindIterable<Document> iterDoc = retrivedCollection.find();
		
		
		  
		Iterator it = iterDoc.iterator();
		while (it.hasNext()) {
			strBuff.append("<br> "+it.next());
			 
		}
		
		return strBuff.toString();
	}
	    	 

}