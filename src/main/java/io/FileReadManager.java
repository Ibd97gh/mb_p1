package io;

import java.io.*;
import java.util.*;
import java.util.LinkedList;

public abstract class FileReadManager
{
	// reads a document file in cisi format
	public static LinkedList<String[]> readDocumentFile(String path) throws Exception, FileNotFoundException
	{
		
		// reading document:
		// .I id
		// .T title
		// .A author
		// .W content
		// .X other
		
		Scanner scanner = new Scanner(new File(path));

		int n = 0;
		String lastLine = getLine(scanner);
		LinkedList<String[]> result = new LinkedList<String[]>();
		
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
				lastLine = getLine(scanner);
			}
			else
			{
				throw new Exception("CISI file has incorrect format.");
			}
			
			// while last line does not belong to the next document
			while(!lastLine.strip().matches("\\.I +[0-9]+") && !lastLine.isEmpty())
			{
				String firstLinePattern = " *\\.[ITAWX] *[0-9]* *"; // these lines should not have \t characters
				
				String[] part = readPart(scanner, firstLinePattern, true);
				
				switch(lastLine.strip().charAt(1))
				{
				case 'T':
					if(doc_title.isEmpty()) doc_title = part[0];
					else doc_title = doc_title + " " + part[0];
					break;
				case 'A':
					if(doc_author.isEmpty()) doc_author = part[0];
					else doc_author = doc_author + " " + part[0];
					break;
				case 'W':
					if(doc_content.isEmpty()) doc_content = part[0];
					else doc_content = doc_content + " " + part[0];
					break;
				case 'X':
					break;
				default:
					throw new Exception("CISI file has incorrect format.");
				}

				lastLine = part[1];
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
	public static LinkedList<String[]> readQueryFile(String path) throws Exception, FileNotFoundException
	{
	
		// reading query:
		// .I id
		// .T title
		// .A author
		// .W content
		// .B other

		Scanner scanner = new Scanner(new File(path));

		int n = 0;
		String lastLine = getLine(scanner);
		LinkedList<String[]> result = new LinkedList<String[]>();
		
		// while there are new lines in the file
		while(!lastLine.isEmpty())
		{
			String doc_id = "";
			String doc_title = "";
			String doc_author = "";
			String doc_content = "";
			String doc_other = "";
			
			// first, read id
			if(lastLine.strip().matches(".I +[0-9]+"))
			{
				String[] tokens = lastLine.strip().split(" +");
				doc_id = tokens[1];
				lastLine = getLine(scanner);
			}
			else
			{
				throw new Exception("CISI file has incorrect format.");
			}
			
			// while last line does not belong to the next document
			while(!lastLine.strip().matches("\\.I +[0-9]+") && !lastLine.isEmpty())
			{
				String firstLinePattern = " *\\.[ITAWX] *[0-9]* *"; // these lines should not have \t characters
				
				String[] part = readPart(scanner, firstLinePattern, true);
				
				switch(lastLine.strip().charAt(1))
				{
				case 'T':
					if(doc_title.isEmpty()) doc_title = part[0];
					else doc_title = doc_title + " " + part[0];
					break;
				case 'A':
					if(doc_author.isEmpty()) doc_author = part[0];
					else doc_author = doc_author + " " + part[0];
					break;
				case 'W':
					if(doc_content.isEmpty()) doc_content = part[0];
					else doc_content = doc_content + " " + part[0];
					break;
				case 'B':
					if(doc_other.isEmpty()) doc_other = part[0];
					else doc_other = doc_other + " " + part[0];
					break;
				default:
					throw new Exception("CISI file has incorrect format.");
				}

				lastLine = part[1];
			}

			doc_id = doc_id.strip();
			doc_title = doc_title.strip();
			doc_author = doc_author.strip();
			doc_content = doc_content.strip();
			
			String[] document = {doc_id, doc_title, doc_author, doc_content, doc_other};
			result.add(document);
			
			n++;
			System.out.println("Read query " + n);
		}
		
		return result;
	}
	
	// read lines and add them to a String until a line matches the pattern
	private static String[] readPart(Scanner scanner, String pattern, boolean saveContent)
	{
		String content = "";
		String lastLine = getLine(scanner);
		
		// while the new line exists and does not match the pattern
		while(!lastLine.matches(pattern) && !lastLine.isEmpty())
		{
			if(saveContent)
			{
				if(content.isEmpty()) content = lastLine;
				else content = content + "\n" + lastLine;
			}
			lastLine = getLine(scanner);
		}
		
		return new String[] {content, lastLine};
	}
	
	// returns the next line that is not empty or "" if it reaches end of file
	private static String getLine(Scanner scanner)
	{
		String lastLine = "";
		
		while(lastLine.isBlank() && scanner.hasNextLine())
		{
			lastLine = scanner.nextLine();
		}
		
		if(lastLine.isBlank()) return ""; // end of file
		else return lastLine; // non-empty line found
	}
	
}
