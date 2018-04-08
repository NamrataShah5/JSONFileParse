import org.json.simple.*;

import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import javax.swing.text.html.HTMLDocument.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONObject.*;
import org.json.simple.parser.JSONParser;
public class Json
{

	public static void main(String [] args)
	{
		//inline will store the JSON data streamed in string format
		String inline = "";
		HashMap<Integer, ArrayList>cyc = new HashMap<Integer,ArrayList>();
		try
		{
			URL url = new URL("https://backend-challenge-summer-2018.herokuapp.com/challenges.json?id=1&page=1");
			//Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//Set the request to GET or POST as per the requirements
			conn.setRequestMethod("GET");
			//Use the connect method to create the connection bridge
			conn.connect();
			//Get the response status of the Rest API
			int responsecode = conn.getResponseCode();
			System.out.println("Response code is: " +responsecode);
			
			//Iterating condition to if response code is not 200 then throw a runtime exception
			//else continue the actual process of getting the JSON data
			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else
			{
				//Scanner functionality will read the JSON data from the stream
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline+=sc.nextLine();
				}
				
				//JSONParser reads the data from string object and breaks each data into key value pairs
				JSONParser parse = new JSONParser();
				//Type caste the parsed json data in json object
				JSONObject jobj = (JSONObject)parse.parse(inline);
				//Store the JSON object in JSON array as objects 
				JSONArray jsonarr_1 = (JSONArray) jobj.get("menus");
				for(int i=0;i<jsonarr_1.size();i++){
					
					String ch = ((JSONObject) jsonarr_1.get(i)).get("child_ids").toString();
					String id;
					String c;
					int child;
					int fid;
					ArrayList<Integer>children = new ArrayList<>();
				if(ch.length()==3) {
					c= ch.substring(1, 2);
					id = ((JSONObject) jsonarr_1.get(i)).get("id").toString();
					child = Integer.parseInt(c);
					fid = Integer.parseInt(id);
					children.add(child);
					cyc.put(fid, children);
				}
				else if(ch.length()>3) {
					id = ((JSONObject) jsonarr_1.get(i)).get("id").toString();
					fid = Integer.parseInt(id);
					c  = ch.replaceAll("\\D+", "");
					for(int j =0 ; j<c.length();j++) {
						String a = c.substring(j,j+1);
						int b = Integer.parseInt(a);
						children.add(b);
						
					}
					cyc.put(fid,children);
				}
				else {
					id = ((JSONObject) jsonarr_1.get(i)).get("id").toString();
					fid = Integer.parseInt(id);
					cyc.put(fid, children);
				}
				}
				boolean[]visited=new boolean[cyc.size()];
				
				for(int i = 0; i<visited.length;i++) {
					
						visited[i]=false;
					
				}
				ArrayList<Integer>path = new ArrayList<>();
				boolean iff = false;
				ArrayList<String>poss=new ArrayList<String>();
				JSONArray valid = new JSONArray();
				JSONArray inv = new JSONArray();
				JSONArray children = new JSONArray();
				JSONObject obj = new JSONObject();
				
				for (Integer s : cyc.keySet()) {
					
		            if (visited[s-1]==false) {
		                visited[s-1]=true;
		                String ss = s.toString();
		              
		               
		                for(int i = 0; i<cyc.get(s).size();i++) {
		                			obj = new JSONObject();
		                			int adj = (int) cyc.get(s).get(i);
		                			 String childs = (DFS(cyc,adj,visited,path,iff)).toString();
		                		if(iff==false) {
		                			if(!poss.isEmpty()) {
		                				if(!poss.contains(childs)) { 
		                						poss.remove(0); 
		                						poss.add(0,childs); 
		                						String v = poss.toString();
				                				obj = new JSONObject();
				                				obj.put("children",v);
				                				obj.put("root_id",ss);
				                				obj = new JSONObject();
				                				poss.clear();
		                				}
		                				else {
			                				obj.put("root_id",ss);
		                					valid.add(obj);
		                					obj = new JSONObject();
		                				}
		                			 
		                			}
		                			else {
		                				poss.add(0,childs);
		                				String v = poss.toString();
		                				obj = new JSONObject();
		                				obj.put("root_id",ss);
		                				obj.put("children",v);	
		                				valid.add(obj);
		                				
		                			} 			
		                		}
		                		else {
		                			if(!poss.isEmpty()) {
		                				if(!poss.contains(childs)) { 
		                						poss.remove(0); 
		                						poss.add(0,childs); 
		                						String v = poss.toString();
				                				obj = new JSONObject();
				                				obj.put("children",v);
				                				obj.put("root_id",ss);
				                				obj = new JSONObject();
				                				poss.clear();
		                				}
		                				else {
			                				obj.put("root_id",ss);
		                					inv.add(obj);
		                					obj = new JSONObject();
		                				}
		                			 
		                			}
		                			else {
		                				poss.add(0,childs);
		                				String v = poss.toString();
		                				obj = new JSONObject();
		                				obj.put("root_id",ss);
		                				obj.put("children",v);	
		                				inv.add(obj);
		                				
		                			} 
		                		}
		        
		                }
		                
		                path=new ArrayList<>();
		              
		            }
		            
		        }
				
				try (FileWriter file = new FileWriter("/Users/tejalshah/Documents/file1.txt")) {
					file.write(obj.toJSONString());
					System.out.println("Successfully Copied JSON Object to File...");
					System.out.println("\nValid menus: " + valid);
					System.out.println("\nInvalid menus: " + inv);
				}
			
				
		           
					
				sc.close();		
			
			//Disconnect the HttpURLConnection stream
			conn.disconnect();
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static ArrayList<Integer> DFS(HashMap<Integer,ArrayList>cyc, int adj, boolean[]visited, ArrayList<Integer>path,boolean iff) {
	if(!path.contains(adj)) {
		path.add(adj);
		if(adj<=cyc.size()) {
			visited[adj-1]=true;
		}
	
		ArrayList<Integer>nbr = cyc.get(adj);
	 if(nbr!=null) {
		for(int i = 0; i<nbr.size();i++) {
			DFS(cyc,nbr.get(i),visited,path,iff);
		}
	 }
	}
	else {
		path.add(adj);
	}
	 return path;
	
	}
}
