import java.util.HashMap;
import java.util.ArrayList;

public class 2dEscapableFunction {

static boolean escapable(int[][] maze) {
    HashMap<Integer, ArrayList<ArrayList<Integer>>> map = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
    for (int row = 0; row < maze.length; row++) {
        for (int col = 0; col < maze[0].length; col++) {
            if (map.get(maze[row][col]) == null) {
                map.put(maze[row][col], new ArrayList<ArrayList<Integer>>());
            }
            ArrayList<Integer> position = new ArrayList<Integer>(2); 
            position.add(row); 
            position.add(col); 
            map.get(maze[row][col]).add(position);
        } 
    }
    boolean[][] visited = new boolean[maze.length][maze[0].length];
    
    return escapableHelper(visited, map, maze.length - 1, maze[0].length - 1);
}

static boolean escapableHelper(boolean[][] visited, HashMap<Integer, ArrayList<ArrayList<Integer>>> map, int row, int col) {
    if (row == 0 && col == 0) {
        return true; 
    } else if (row > visited.length - 1 || col > visited[0].length - 1 || row < 0 || col < 0) {
        return false; 
    }
    
    if (visited[row][col]) {
        return false;
    }
    visited[row][col] = true; 
    
    int number = (row + 1) * (col + 1);
    if (map.get(number) == null) {
        return false; 
    }
    
    for (ArrayList<Integer> pos: map.get(number)) {
        if (escapableHelper(visited, map, pos.get(0), pos.get(1))) {
            return true; 
        }
    }
    return false; 
}

	public static void main(String[] args) {
		int[][] maze = {
		    {2, 1, 8, 7}, 
		    {8, 7, 2, 9}, 
		    {10, 18, 12, 3}
		};
		System.out.println(escapable(maze));
	}
}
