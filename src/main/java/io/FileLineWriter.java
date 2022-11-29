package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLineWriter
{
	
	FileWriter writer;
	
	public FileLineWriter(String path) throws IOException
	{
		
		File file = new File(path);
		if(file.exists())
		{
			file.delete();
			file.createNewFile();
		}
		
		writer = new FileWriter(file);
		
	}
	
	public void closeFile() throws IOException
	{
		writer.close();
	}
	
	// creates a TREC file
	public void addLine(String line) throws IOException
	{
		writer.write(line + "\n");
	}
}
