import java.util.Scanner; 

public class ScrambleWord
{
    public static String scrambleWord(String word) {
        int aIndex = word.indexOf("A");
        if (aIndex == -1 || aIndex == word.length() - 1) {
            return word; 
        }
        if (word.substring(aIndex + 1, aIndex + 2).equals("A")) {
            return word.substring(0, aIndex + 1) + scrambleWord(word.substring(aIndex + 1));
        }
        return word.substring(0, aIndex) + word.substring(aIndex + 1, aIndex + 2) + "A" + scrambleWord(word.substring(aIndex + 2));
    }
    
	public static void main(String[] args) {
        System.out.println(scrambleWord(""));
	}
}
