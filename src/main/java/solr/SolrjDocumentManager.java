package solr;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrDocumentList;

@SuppressWarnings("deprecation")
public abstract class SolrjDocumentManager
{
	
	public static void insert(String collection, String[] attributes, String[] values) throws SolrServerException, IOException
	{

		final SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr").build();
		
		final SolrInputDocument document = new SolrInputDocument();
		for(int i = 0; i < attributes.length; i++)
		{
			document.addField(attributes[i], values[i]);
		}
		
		final UpdateResponse updateResponse = client.add(collection, document);
		
		client.commit(collection);
		
	}
	
	// each String[] has its attributes sorted in the same order as fields
	public static LinkedList<String[]> query(String collection, String queryString, String[] filters, String[] fields, int numRows) throws SolrServerException, IOException
	{

		HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/" + collection).build();
		
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
        
        SolrDocumentList docs = solr.query(query).getResults();
        
        LinkedList<String[]> docList = new LinkedList<String[]>();
        for(int i = 0; i < docs.size(); i++)
        {
        	String[] doc_i = new String[fields.length];
        	for(int j = 0; j < fields.length; j++)
        	{
        		doc_i[j] = docs.get(i).get(fields[j]).toString();
        	}
        	docList.add(doc_i);
        }
        
        return docList;
	}
	
}
