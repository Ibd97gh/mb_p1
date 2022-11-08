package mb_p1;

import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

public class Main {

	public static void main(String[] args)
	{
		
		//insertDocuments();
		
		String queryId = "1";
		String[] filters = {};
		String[] fields = {"id", "title", "author", "content", "score"};
		makeQuery(queryId, filters, fields);
		
	}
	
	public static void insertDocuments()
	{
		try
		{
			FileManager filemgr = new FileManager("./corpus/CISI.ALL");
			SolrjManager solrmgr = new SolrjManager();
			
			LinkedList<String[]> list = filemgr.readDocumentFile();
			String[] attributes = {"id", "title", "author", "content"};
			
			if(list == null) return;
			
			for(String[] s: list)
			{
				
				solrmgr.insert("http://localhost:8983/solr", "coleccion", attributes, s);
				System.out.println("Inserted document " + s[0]);
				
			}
		}
		catch (SolrServerException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void makeQuery(String queryID, String[] filters, String[] fields)
	{
		try
		{
			FileManager filemgr = new FileManager("./corpus/CISI.QRY");
			SolrjManager solrmgr = new SolrjManager();
			
			String[] queryStrings = filemgr.readDocumentQuery(queryID);
			
			// only take the first 5 words:
			String[] tokens = queryStrings[2].split("[ \n\t]+");
			String firstWords = "";
			for(int i = 0; i < 5; i++)
			{
				if(firstWords.isBlank()) firstWords = tokens[i];
				else firstWords = firstWords + " " + tokens[i];
			}
			
			String queryString = "content:" + firstWords;

            System.out.println("");
			System.out.println("Query: " + queryString);
            System.out.println("");
            System.out.println("____________________________________________________________________________________________________");
            System.out.println("____________________________________________________________________________________________________");
            System.out.println("");
			
			SolrDocumentList docs = solrmgr.query("http://localhost:8983/solr/coleccion", queryString, filters, fields);

	        for (int i = 0; i < docs.size(); ++i)
	        {

	        	for(int j = 0; j < fields.length; j++) 
	        	{
	        		System.out.println("Field " + fields[j] + ":");
	        		if(docs.get(i).containsKey(fields[j])) System.out.println(docs.get(i).get(fields[j]));
	        		else System.out.println("NULL");
		            System.out.println("----------");
	        	}
	            System.out.println("");
	            System.out.println("____________________________________________________________________________________________________");
	            System.out.println("____________________________________________________________________________________________________");
	            System.out.println("");
	            
	        }
		}
		catch (SolrServerException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	public static void queryExample()
	{
		
		SolrjManager solrmgr = new SolrjManager();
		
		try
		{
			
			SolrDocumentList docs = solrmgr.query("http://localhost:8983/solr/coleccion", "*:*");

	        for (int i = 0; i < docs.size(); ++i)
	        {
	        	
	            System.out.println(docs.get(i));
	            
	        }
			
		}
		catch (SolrServerException | IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void insertExample()
	{
		
		SolrjManager solrmgr = new SolrjManager();
		
		try
		{
			
			String[] attributes = new String[] {"id", "name", "price"};
			String[] item = new String[] {UUID.randomUUID().toString(), "Amazon Kindle Paperwhite v10", ""+1000.0};
			solrmgr.insert("http://localhost:8983/solr", "coleccion", attributes, item);
			
		}
		catch (SolrServerException | IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void readExample()
	{
		
		try
		{
			FileManager filemgr = new FileManager("./corpus/file.txt");
			LinkedList<String[]> list = filemgr.readDocumentFile();
			
			for(int i = 0; i < list.size(); i++)
			{
				
				System.out.println("ID: " + list.get(i)[0]);
				System.out.println("TITLE: " + list.get(i)[1]);
				System.out.println("AUTHOR:" + list.get(i)[2]);
				//System.out.println("CONTENT:");
				//System.out.println(list.get(i)[3]);
				System.out.println();
				
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	*/
}
