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
	
	public LinkedList<String[]> getDocuments()
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
			doc_title = readPart("\\.A");
			doc_author = readPart("\\.W"); // read author
			doc_content = readPart("\\.X"); // read content
			readPart("\\.I [0-9]+"); // read other
			
			// add document
			String[] document = {doc_id, doc_title, doc_author, doc_content};
			result.add(document);
		}
		
		return result;
	}
	
	private String readPart(String pattern)
	{
		String content = "";
		lastLine = scanner.nextLine();
		
		while(scanner.hasNextLine() && !lastLine.strip().matches(pattern))
		{
			content = content + "\n" + lastLine;
			lastLine = scanner.nextLine();
		}
		
		if(!scanner.hasNextLine())
		{
			content = content + "\n" + lastLine;
		}
		
		return content;
	}
	
}
