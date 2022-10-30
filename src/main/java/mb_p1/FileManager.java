package mb_p1;

import java.io.*;
import java.util.*;
import java.io.IOException;
import java.util.LinkedList;

public class FileManager
{
	
    Scanner scanner;
    String lastLine;

	// path includes the name of the file
	public FileManager(String path) throws IOException
	{
        scanner = new Scanner(new File(path));
        lastLine = "";
	}
	
	public LinkedList<String[]> getDocuments_CISI_ALL()
	{
		LinkedList<String[]> result = new LinkedList<String[]>();
	
		// reading document:
		// .I $id
		// .T \n $title
		// .A \n $author
		// .W \n $content
		// .X \n $other
		// title, author, content and other may have multiple lines

		String doc_id;
		String doc_title;
		String doc_author;
		String doc_content;
		
		lastLine = scanner.nextLine();
		while(scanner.hasNextLine())
		{
			
			// read id
			String[] words = lastLine.strip().split(" +");
			doc_id = words[1];
			
			// read title
			scanner.nextLine(); // .T
			doc_title = readPart("\\.A", true);
			doc_author = readPart("\\.W|A", true);
			while(!lastLine.strip().matches("\\.W")) doc_author = doc_author + "\n" + readPart("\\.W|A", true);
			doc_content = readPart("\\.X", true); // read content
			readPart("\\.I [0-9]+", false); // read other
			
			// add document
			String[] document = {doc_id, doc_title, doc_author, doc_content};
			result.add(document);
		}
		
		return result;
	}
	
	public String getQuery_CISI_QRY(String queryID)
	{
		lastLine = scanner.nextLine();
		while(scanner.hasNextLine())
		{
			if(lastLine.strip().matches("\\.I +" + queryID))
			{
				// read document
				// we only read .W
				readPart("\\.W", false);
				String doc_content = readPart("\\..", true);
			
				String[] splitContent = doc_content.strip().split("([ \t\n])+");
				
				String query = splitContent[0];
				for(int i = 1; i < 5; i++) query = query + " " + splitContent[i];
				
				// return query
				return query;
			}
		}
		
		return "*:*";
	}
	
	private String readPart(String pattern, boolean saveContent)
	{
		String content = "";
		lastLine = scanner.nextLine();
		
		while(scanner.hasNextLine() && !lastLine.strip().matches(pattern))
		{
			if(saveContent) content = content + "\n" + lastLine;
			lastLine = scanner.nextLine();
		}
		
		if(!scanner.hasNextLine())
		{
			if(saveContent) content = content + "\n" + lastLine;
		}
		
		return content;
	}
	
}
