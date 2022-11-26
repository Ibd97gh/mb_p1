package solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.SolrInputDocument;

@SuppressWarnings("deprecation")
public abstract class SolrjConfigManager
{
	
	public static void clearCollection(String collection) throws SolrServerException, IOException 
	{
		String urlString = "http://localhost:8983/solr/" + collection;
		SolrClient solr = new HttpSolrClient.Builder(urlString).build();
		
		SolrInputDocument doc = new SolrInputDocument();
		
		solr.deleteByQuery("*:*");

		solr.commit();
	}

	/*
	public static void createCollection(String collection) throws SolrServerException, IOException
	{
		String urlString = "http://localhost:8983/solr/" + collection;
		SolrClient solr = new HttpSolrClient.Builder(urlString).build();
		
		CollectionAdminRequest.Create creator = CollectionAdminRequest.createCollection(collection, 1, 1);
		
		creator.process(solr);
	}
	*/
	
	/*
	public static void removeCollection(String collection) throws SolrServerException, IOException
	{
		String urlString = "http://localhost:8983/solr/" + collection;
		SolrClient solr = new HttpSolrClient.Builder(urlString).build();
		
		CollectionAdminRequest.Delete deleter = CollectionAdminRequest.deleteCollection(collection);
		
		deleter.process(solr);
	}
	*/
	
	/*
	public static void configCollection(String collection)
	{
		
	}
	*/
}
