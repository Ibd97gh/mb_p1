package mb_p1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

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
			//showQueryVocabulary();
			//showDocumentVocabulary();
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
		
		LinkedList<String[]> list = CISIReader.readDocumentFile("./corpus/CISI.ALL");
		String[] attributes = {"id", "title", "author", "content"};
		
		if(list == null) return;
		
		for(String[] s: list)
		{
			
			SolrjDocumentManager.insert("coleccion", attributes, s);
			System.out.println("Interted document " + s[0]);
			
		}
		
	}
	
	private static void makeTRECFile() throws IOException, SolrServerException, Exception
	{
		
		// file writer
		FileLineWriter filewtr = new FileLineWriter("./corpus/trec_solr_file.trec");
		
		// query properties
		String[] fields = {"id", "score"}; // fields we need
		String[] filters = {}; // filters we apply
		int numRows = 50;
		
		// read query file
		LinkedList<String[]> queries = CISIReader.readQueryFile("./corpus/CISI.QRY");

		// create a dictionary to select the least frequent terms
		LinkedList<String[]> documents = CISIReader.readQueryFile("./corpus/CISI.QRY");
		final HashMap<String, Integer> dictionary = TextProcessor.getDictionary(documents);
		
		// make each query and 
		for(String[] query: queries) // query[0]: id, query[1]: title, query[2]: author, query[3]: content
		{
			// we dont use the 112th query
			if(query[0].equals("112")) break;
			
			// create the query
			String[] queryFields = {"title", "author", "content"};
			String[] queryValues = {TextProcessor.extractTerms(query[1]), TextProcessor.extractTerms(query[2]), TextProcessor.extractTerms(query[3])};
			String[] queryWeights = {"0.8", "0.8", "1.0"};
			
			//queryValues[0] = TextProcessor.getNLeastFrequentTerms(queryValues[0], 3, dictionary);
			//queryValues[1] = TextProcessor.getNLeastFrequentTerms(queryValues[1], 3, dictionary);
			//queryValues[2] = TextProcessor.getNLeastFrequentTerms(queryValues[2], 10, dictionary);
			
			// get the documents retrieved
			LinkedList<String[]> docs = SolrjDocumentManager.query("coleccion", queryFields, queryValues, queryWeights, filters, fields, numRows);
				
			// write documents to trec file
			for(int i = 0; i < docs.size(); i++)
			{
				String documentLine = "";

				documentLine = documentLine + query[0].toString(); // query id
				documentLine = documentLine + "\t" + "Q0"; // iteration (we only do 1)
				documentLine = documentLine + "\t" + docs.get(i)[0]; // document id
				documentLine = documentLine + "\t" + (i+1); // ranking
				documentLine = documentLine + "\t" + docs.get(i)[1]; // document score
				documentLine = documentLine + "\t" + "IVAN"; // team
				
				filewtr.addLine(documentLine);
			}
			
		}
		
		filewtr.closeFile();
		
	}
	
	private static void showQueryVocabulary() throws FileNotFoundException, Exception
	{
		// String[0]: id, String[1]: title, String[2]: author, String[3]: content, String[4]: other
		LinkedList<String[]> documents = CISIReader.readQueryFile("./corpus/CISI.QRY");

		final HashMap<String, Integer> dictionary = TextProcessor.getDictionary(documents);
		PriorityQueue<String> queue = TextProcessor.getSortedVocabulary(dictionary);

		while(!queue.isEmpty())
		{
			String str = queue.poll();
			System.out.println(str + " - " + dictionary.get(str));
		}
	}
	
	private static void showDocumentVocabulary() throws FileNotFoundException, Exception
	{
		// String[0]: id, String[1]: title, String[2]: author, String[3]: content
		LinkedList<String[]> documents = CISIReader.readDocumentFile("./corpus/CISI.ALL");

		final HashMap<String, Integer> dictionary = TextProcessor.getDictionary(documents);
		PriorityQueue<String> queue = TextProcessor.getSortedVocabulary(dictionary);

		while(!queue.isEmpty())
		{
			String str = queue.poll();
			System.out.println(str + " - " + dictionary.get(str));
		}
	}
}
