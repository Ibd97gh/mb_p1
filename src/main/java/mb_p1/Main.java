package mb_p1;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.solr.client.solrj.SolrServerException;

import io.*;
import solr.*;

public class Main
{

	public static void main(String[] args)
	{
		
		try
		{
			//clearSolrCollection();
			//insertDocuments();
			makeTRECFile();
		}
		catch(Exception exception)
		{
			System.out.println(exception.getClass().getName());
			exception.printStackTrace();
		}
		
	}
	
	private static void clearSolrCollection() throws SolrServerException, IOException 
	{
		SolrjConfigManager.clearCollection("coleccion");
	}
	
	private static void insertDocuments() throws SolrServerException, IOException, Exception
	{
		
		LinkedList<String[]> list = FileReadManager.readDocumentFile("./corpus/CISI.ALL");
		String[] attributes = {"id", "title", "author", "content"};
		
		if(list == null) return;
		
		for(String[] s: list)
		{
			
			SolrjDocumentManager.insert("coleccion", attributes, s);
			System.out.println("Inserted document " + s[0]);
			
		}
		
	}
	
	private static void makeTRECFile() throws IOException, SolrServerException, Exception
	{
		
		// file writer
		FileWriteManager filewtr = new FileWriteManager("./corpus/trec_solr_file.trec");
		
		// query properties
		String[] fields = {"id", "score"};
		String[] filters = {};
		int numRows = 10;
		
		// read query file
		LinkedList<String[]> queries = FileReadManager.readQueryFile("./corpus/CISI.QRY");
		
		// make each query and 
		for(String[] query: queries)
		{
			// we dont use the 112th query
			if(query[0].equals("112")) break;
			
			// create the query
			String[] queryFields = {"title", "author", "content"};
			String[] queryValues = {TextProcessor.fixString(query[1]), TextProcessor.fixString(query[2]), TextProcessor.fixString(query[3])};
			String[] queryWeights = {"0.8", "0.8", "1.0"};
			
			//System.out.println(TextProcessor.fixString(query[1]));
			//System.out.println(TextProcessor.fixString(query[2]));
			//System.out.println(TextProcessor.fixString(query[3]));
			
			//System.out.println();
			//System.out.println();
			
			// query
			LinkedList<String[]> docs = SolrjDocumentManager.query("coleccion", queryFields, queryValues, queryWeights,filters, fields, numRows);
			
			for(int i = 0; i < docs.size(); i++)
			{
				String documentLine = "";

				documentLine = documentLine + query[0].toString(); // query id
				documentLine = documentLine + "\t" + "Q0"; // iteration (we only do 1)
				documentLine = documentLine + "\t" + docs.get(i)[0].toString(); // document id
				documentLine = documentLine + "\t" + (i+1); // ranking
				documentLine = documentLine + "\t" + docs.get(i)[1].toString(); // document score
				documentLine = documentLine + "\t" + "IVAN"; // team
				
				filewtr.addLine(documentLine);
			}
			
		}
		
		filewtr.closeFile();
		
	}
	
}
