import java.util.HashMap;
import java.util.HashSet; 

public class HashMapAndMisc
{
    static HashMap<String, Integer> wordFrequencyTable(String[] strings) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < strings.length; i++) {
            if (map.get(strings[i]) == null) {
                map.put(strings[i], 1); 
            } else {
                map.put(strings[i], map.get(strings[i]) + 1);
            }
        }
        return map;
    }
    
    static int numDistinctStrings(String[] strings) {
        HashSet<String> map = new HashSet<String>();
        for (int i = 0; i < strings.length; i++) {
            map.add(strings[i]); 
        }
        return map.size();
    }
    
    static boolean escapable(int[] nums, int position, boolean[] visitedPositions) {
        if (position < 0 || position >= nums.length) {
            return false;
        } else if (nums[position] == 0) {
            return true;
        } else if (visitedPositions[position])  {
            return false; 
        }
        visitedPositions[position] = true; 
        return escapable(nums, position + nums[position], visitedPositions) || escapable(nums, position - nums[position], visitedPositions);
    }
    
	public static void main(String[] args) {
		String[] strings = {"aa", "ab", "ac", "ad", "ae", "af", "aa", "aa", "ab", "ge", "af"}; 
		//System.out.println(numDistinctStrings(strings)); 
		//System.out.println(wordFrequencyTable(strings).get("ge")); 
		int[] nums = {0, 2, 2, 2, 3, 4, 1, 9, 9, 10}; 
		boolean[] visitedPositions = new boolean[nums.length]; 
		System.out.println(escapable(nums, 1, visitedPositions));
	}
}
