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
	
	public SolrDocumentList query(String url, String queryString, String[] filters, String[] fields, int numRows) throws SolrServerException, IOException
	{

		HttpSolrClient solr = new HttpSolrClient.Builder(url).build();
		
		SolrQuery query = new SolrQuery();
		
		query.setRows(numRows);
        query.setQuery(queryString);
        
        if(filters != null)
        {
        	for(String filter: filters)
            {
            	query.addFilterQuery(filter);
            }
        }

        if(fields != null)
        {
        	for(String field: fields)
	        {
	        	query.addField(field);
	        }
        }
        
        QueryResponse rsp = solr.query(query);
        SolrDocumentList docs = rsp.getResults();
        
        return docs;
	}
	
}
