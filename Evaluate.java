import java.util.Scanner; 

public class Evaluate
{
    static int evaluate(String s) {
        int timesIndex = s.lastIndexOf(" * "); 
        int plusIndex = s.lastIndexOf(" + ");
        int minusIndex = s.lastIndexOf(" - ");
        int index = Math.max(plusIndex, minusIndex);
        if (index != -1 && index == plusIndex) {
            return evaluate(s.substring(0, plusIndex)) + evaluate(s.substring(plusIndex + 3));
        } else if (index != -1 && index == minusIndex) {
            return evaluate(s.substring(0, minusIndex)) - evaluate(s.substring(index + 3));
        } else if (timesIndex != -1) {
            return evaluate(s.substring(0, timesIndex)) * evaluate(s.substring(timesIndex + 3));
        } else {
            return Integer.parseInt(s);
        }
    }
    
	public static void main(String[] args) {
		System.out.println(evaluate("10 * 10 + 99 - 1 * 3 + -1"));
	}
}
