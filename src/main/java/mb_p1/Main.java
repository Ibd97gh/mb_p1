package mb_p1;

import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

public class Main {

	public static void main(String[] args)
	{
		
		//insertDocuments_CISI_ALL();
		doQuery_CISI_QRY("1");
		
	}
	
	public static void insertDocuments_CISI_ALL()
	{
		try
		{
			FileManager filemgr = new FileManager("./corpus/CISI.ALL");
			SolrjManager solrmgr = new SolrjManager();
			
			LinkedList<String[]> list = filemgr.getDocuments_CISI_ALL();
			String[] attributes = {"id", "title", "author", "content"};
			
			for(String[] s: list)
			{
				
				solrmgr.insert("http://localhost:8983/solr", "coleccion", attributes, s);
				System.out.println(s[0]);
				
			}
		}
		catch (SolrServerException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void doQuery_CISI_QRY(String queryID)
	{
		try
		{
			FileManager filemgr = new FileManager("./corpus/CISI.QRY");
			SolrjManager solrmgr = new SolrjManager();
			
			String queryString = filemgr.getQuery_CISI_QRY(queryID);
			
			SolrDocumentList docs = solrmgr.query("http://localhost:8983/solr/coleccion", queryString);

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
			LinkedList<String[]> list = filemgr.getDocuments_CISI_ALL();
			
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

}
