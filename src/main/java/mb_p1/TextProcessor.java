package mb_p1;

public abstract class TextProcessor
{
	
	public static String removeSpecialCharacters(String str)
	{
		return str.replaceAll("[!?#$%/='^.,:;()\\[\\]\" _-]+", " ");
	}
	
	public static String fixWhitespaces(String str)
	{
		return str.replaceAll("[ \\n\\t]+", " ").strip();
	}

	public static String fixString(String str)
	{
		return fixWhitespaces(removeSpecialCharacters(str));
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
}
