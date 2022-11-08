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
	
	// reads a document file in cisi format
	public LinkedList<String[]> readDocumentFile()
	{
		int n = 0;
		
		LinkedList<String[]> result = new LinkedList<String[]>();
	
		// reading document:
		// .I id
		// .T title
		// .A author
		// .W content
		// .X other

		lastLine = getLine();
		
		// while there are new lines in the file
		while(!lastLine.isEmpty())
		{
			String doc_id = "";
			String doc_title = "";
			String doc_author = "";
			String doc_content = "";
			
			// first, read id
			if(lastLine.strip().matches(".I +[0-9]+"))
			{
				String[] tokens = lastLine.strip().split(" +");
				doc_id = tokens[1];
				lastLine = getLine();
			}
			else
			{
				System.out.println(lastLine);
				System.out.println("ERROR");
				return null; // ERROR
			}
			
			// while last line does not belong to the next document
			while(!lastLine.strip().matches("\\.I +[0-9]+") && !lastLine.isEmpty())
			{
				String firstLinePattern = " *\\.[ITAWX] *[0-9]* *"; // these lines should not have \t characters
				switch(lastLine.strip().charAt(1))
				{
				case 'T':
					if(doc_title.isEmpty()) doc_title = readPart(firstLinePattern, true);
					else doc_title = doc_title + " " + readPart(firstLinePattern, true);
					break;
				case 'A':
					if(doc_author.isEmpty()) doc_author = readPart(firstLinePattern, true);
					else doc_author = doc_author + " " + readPart(firstLinePattern, true);
					break;
				case 'W':
					if(doc_content.isEmpty()) doc_content = readPart(firstLinePattern, true);
					else doc_content = doc_content + " " + readPart(firstLinePattern, true);
					break;
				case 'X':
					readPart(firstLinePattern, false);
					break;
				default:
					System.out.println(lastLine);
					System.out.println("ERROR");
					return null; // ERROR
				}
			}

			doc_id = doc_id.strip();
			doc_title = doc_title.strip();
			doc_author = doc_author.strip();
			doc_content = doc_content.strip();
			
			String[] document = {doc_id, doc_title, doc_author, doc_content};
			result.add(document);
			
			n++;
			System.out.println("Read document " + n);
		}
		
		return result;
	}

	// reads a query file in cisi format
	// the positions of the String array are:
	// 0- title
	// 1- author
	// 2- content
	public String[] readDocumentQuery(String queryID)
	{
		String doc_title = "";
		String doc_author = "";
		String doc_content = "";
		String doc_other = "";
		
		lastLine = getLine();
		
		// find the query with the specified id
		while(!lastLine.strip().matches(".I +" + queryID) && !lastLine.isEmpty())
		{
			lastLine = getLine();
		}
		
		if(lastLine.isEmpty())
		{
			System.out.println(lastLine);
			System.out.println("ERROR");
			return null; // ERROR
		}
		
		lastLine = getLine();
		
		// while last line does not belong to the next query
		while(!lastLine.strip().matches("\\.I +[0-9]+") && !lastLine.isEmpty())
		{
			String firstLinePattern = " *\\.[ITAWXB] *[0-9]* *"; // these lines should not have \t characters
			switch(lastLine.strip().charAt(1))
			{
			case 'T':
				if(doc_title.isEmpty()) doc_title = readPart(firstLinePattern, true);
				else doc_title = doc_title + " " + readPart(firstLinePattern, true);
				break;
			case 'A':
				if(doc_author.isEmpty()) doc_author = readPart(firstLinePattern, true);
				else doc_author = doc_author + " " + readPart(firstLinePattern, true);
				break;
			case 'W':
				if(doc_content.isEmpty()) doc_content = readPart(firstLinePattern, true);
				else doc_content = doc_content + " " + readPart(firstLinePattern, true);
				break;
			case 'B':
				if(doc_other.isEmpty()) doc_other = readPart(firstLinePattern, true);
				else doc_other = doc_other + " " + readPart(firstLinePattern, true);
				break;
			default:
				System.out.println(lastLine);
				System.out.println("ERROR");
				return null; // ERROR
			}
		}

		doc_title = doc_title.strip();
		doc_author = doc_author.strip();
		doc_content = doc_content.strip();
		
		return new String[] {doc_title, doc_author, doc_content, doc_other};
	}
	
	// read lines and add them to a String until one line matches the pattern
	private String readPart(String pattern, boolean saveContent)
	{
		String content = "";
		lastLine = getLine();
		
		// while the new line exists and does not match the pattern
		while(!lastLine.matches(pattern) && !lastLine.isEmpty())
		{
			if(saveContent)
			{
				if(content.isEmpty()) content = lastLine;
				else content = content + "\n" + lastLine;
			}
			lastLine = getLine();
		}
		
		return content;
	}
	
	// returns the next line that is not empty or "" if it reaches end of file
	private String getLine()
	{
		lastLine = "";
		
		while(lastLine.isBlank() && scanner.hasNextLine())
		{
			lastLine = scanner.nextLine();
		}
		
		if(lastLine.isBlank()) return ""; // end of file
		else return lastLine; // non-empty line found
	}
	
}
