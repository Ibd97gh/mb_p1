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
		
		String[] filters = {};
		String[] fields = {"id", "score"};
		makeTRECFile(filters, fields, 10);
		
	}
	
	public static void insertDocuments()
	{
		try
		{
			FileReadManager filerdr = new FileReadManager("./corpus/CISI.ALL");
			SolrjManager solrmgr = new SolrjManager();
			
			LinkedList<String[]> list = filerdr.readDocumentFile();
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
	
	public static void query(String queryID, String[] filters, String[] fields, int numRows)
	{
		try
		{
			FileReadManager filerdr = new FileReadManager("./corpus/CISI.QRY");
			SolrjManager solrmgr = new SolrjManager();
			
			String[] queryStrings = filerdr.readQueryFileSingle(queryID);
			
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
			
			SolrDocumentList docs = solrmgr.query("http://localhost:8983/solr/coleccion", queryString, filters, fields, numRows);

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
	
	public static void makeTRECFile(String[] filters, String[] fields, int numRows)
	{
		try
		{
			FileReadManager filerdr = new FileReadManager("./corpus/CISI.QRY");
			FileWriteManager filewtr = new FileWriteManager("./corpus/trec_solr_file");
			SolrjManager solrmgr = new SolrjManager();
			
			LinkedList<String[]> queryStrings = filerdr.readQueryFile();
			
			for(String[] currentQueryValues: queryStrings)
			{
				// we dont use the 112th query
				if(currentQueryValues[0].equals("112")) break;
				
				// construct the query
				String currentQuery = "";
				
				// take only the first 5 words of the content field
				String currentQueryValuesContent = currentQueryValues[3].replaceAll("[()]+", "");
				String[] tokens = currentQueryValuesContent.split("[ \n]+");
				currentQuery = "content:";

				int numTokens = Math.min(5, tokens.length);
				for(int i = 0; i < numTokens; i++) currentQuery = currentQuery + tokens[i] + " ";
				currentQuery = currentQuery.strip();
				
				// query
				System.out.println(currentQuery);
				SolrDocumentList docs = solrmgr.query("http://localhost:8983/solr/coleccion", currentQuery, filters, fields, numRows);
				
				for(int i = 0; i < docs.size(); i++)
				{
					String documentLine = "";

					// query id
					documentLine = documentLine + currentQueryValues[0];
					
					// iteration (we only do 1)
					documentLine = documentLine + "\t" + "Q0";
					
					// document id
					String formattedId = String.format("%04d", (String)docs.get(i).get("id"));
					documentLine = documentLine + "\t" + formattedId;
					
					// ranking
					String formattedRanking = String.format("%02d", (i+1));
					documentLine = documentLine + "\t" + formattedRanking;
					
					// document score
					//String formattedScore = String.format("%2.7f", value))
					documentLine = documentLine + "\t" + docs.get(i).get("score");
					
					// team
					documentLine = documentLine + "\t" + "IVAN";
					
					filewtr.addLine(documentLine);
				}
				
			}
			
			
			filewtr.closeFile();
		}
		catch (SolrServerException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
