package mb_p1;

import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrServerException;

public class Main {

	public static void main(String[] args)
	{
		
		insertDocuments();
		
	}
	
	public static void insertDocuments()
	{
		try
		{
			FileManager filemgr = new FileManager("./corpus/CISI.ALL");
			SolrjManager solrmgr = new SolrjManager();
			
			LinkedList<String[]> list = filemgr.getDocuments();
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
	
	public static void queryExample()
	{
		
		SolrjManager solrmgr = new SolrjManager();
		
		try
		{
			
			solrmgr.query("http://localhost:8983/solr/coleccion", "*:*");
			
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
			LinkedList<String[]> list = filemgr.getDocuments();
			
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
