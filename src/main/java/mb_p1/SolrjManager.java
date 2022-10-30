package mb_p1;

import java.io.IOException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrDocumentList;

@SuppressWarnings("deprecation")
public class SolrjManager {
	
	public SolrjManager()
	{
		
	}
	
	public void insert(String url, String collection, String[] attributes, String[] values) throws SolrServerException, IOException
	{

		final SolrClient client = new HttpSolrClient.Builder(url).build();
		
		final SolrInputDocument document = new SolrInputDocument();
		for(int i = 0; i < attributes.length; i++)
		{
			
			document.addField(attributes[i], values[i]);
			
		}
		
		final UpdateResponse updateResponse = client.add(collection, document);
		
		client.commit(collection);
		
	}
	
	public SolrDocumentList query(String url, String queryString) throws SolrServerException, IOException
	{

		HttpSolrClient solr = new HttpSolrClient.Builder(url).build();
		
		SolrQuery query = new SolrQuery();
		
        query.setQuery(queryString);
        //query.setQuery("Apple");
        //query.addFilterQuery("cat:electronics");
        //query.setFields("id","price","merchant","cat","store");
        
        QueryResponse rsp = solr.query(query);
        SolrDocumentList docs = rsp.getResults();
        
        return docs;
	}
	
}
