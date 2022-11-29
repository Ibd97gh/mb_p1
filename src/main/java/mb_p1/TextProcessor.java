package mb_p1;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public abstract class TextProcessor
{
	
	public static String removeNonAlphanumeric(String str)
	{
		return str.replaceAll("[^A-Za-z]+", " ");
	}
	
	public static String fixWhitespaces(String str)
	{
		return str.replaceAll("[ \\n\\t]+", " ").strip();
	}
	
	public static String removeShortWords(String str, int n)
	{
		String[] tokens = splitString(str);
		
		String result = "";
		for(int i = 0; i < tokens.length; i++)
		{
			if(tokens[i].length() > n)
			{
				if(result.isBlank()) result = tokens[i];
				else result = result + " " + tokens[i];
			}
		}
		
		return result;
	}

	public static String extractTerms(String str)
	{
		return fixWhitespaces(removeShortWords(removeNonAlphanumeric(str), 3)).toLowerCase();
	}
	
	public static String[] splitString(String str)
	{
		return str.split("[ \t\n]+");
	}
	
	public static String extractFirstNWords(String str, int n)
	{
		
		if(n <= 0) return str;
		
		String[] tokens = str.split(" ");
		String result = "";
		
		int m;
		if(tokens.length < n) m = tokens.length;
		else m = n;
		
		for(int i = 0; i < m; i++)
		{
			if(i == 0) result = tokens[i];
			else result = result + " " + tokens[i];
		}
		
		return result;
		
	}
	
	public static void fillDictionary(LinkedList<String[]> documents, final HashMap<String, Integer> dictionary)
	{
		for(String[] texts: documents)
		{
			for(int i = 0; i < 4; i++)
			{
				String[] tokens = TextProcessor.splitString(TextProcessor.extractTerms(texts[i]));
				
				for(String token: tokens)
				{
					if(dictionary.containsKey(token))
					{
						dictionary.put(token, dictionary.get(token) + 1);
					}
					else
					{
						dictionary.put(token, 1);
					}
				}
			}
		}
	}
	
	public static PriorityQueue<String> getSortedVocabulary(final HashMap<String, Integer> dictionary)
	{
		Comparator comparator = new Comparator()
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				String str1 = (String)o1;
				String str2 = (String)o2;

				int n1 = dictionary.get(str1);
				int n2 = dictionary.get(str2);
				
				if (n1 > n2) return 1;
				if (n1 < n2) return -1;
				
				return 0;
			}
		};
		PriorityQueue<String> queue = new PriorityQueue<String>(comparator);
		queue.addAll(dictionary.keySet());
		
		return queue;
	}
	
	public static String getNLeastFrequentTerms(String str, int n, final HashMap<String, Integer> dictionary)
	{
		
		// build a comparator to sort the terms
		Comparator comparator = new Comparator()
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				String str1 = (String)o1;
				String str2 = (String)o2;

				int n1 = dictionary.get(str1);
				int n2 = dictionary.get(str2);
				
				if (n1 > n2) return 1;
				if (n1 < n2) return -1;
				
				return 0;
			}
		};
		
		// add terms to a list so there is only one instance of each
		String[] tokens = splitString(str);
		PriorityQueue<String> terms = new PriorityQueue<String>();
		for(String s: tokens)
		{
			if(!terms.contains(s))
			{
				terms.add(s);
			}
		}
		
		// join the terms
		String result = "";
		int m = 0;
		while(!terms.isEmpty() && m < n)
		{
			if(result.isBlank())
			{
				result = terms.poll();
			}
			else
			{
				result = result + " " + terms.poll();
			}
			
			m++;
		}
		
		return result;
	}
}
