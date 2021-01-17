public class RotateFunction
{
    static void rotate(int[][] grid, int k) {
        k = k % 4;
        
        if (k == 0) {
            for (int row = 0; row < grid.length; row++) {
                for (int column = 0; column < grid[0].length - 1; column++) {
                    System.out.print(grid[row][column] + ",\t");
                }
                System.out.println(grid[row][grid[0].length - 1]);
            }
        } else if (k == 1) {
            for (int column = 0; column < grid[0].length; column++) {
                for (int row = grid.length - 1; row > 0; row--) {
                    System.out.print(grid[row][column] + ",\t"); 
                }
                System.out.println(grid[0][column]);
            }
        } else if (k == 3) {
            for (int column = grid[0].length - 1; column >= 0; column--) {
                for (int row = 0; row < grid.length - 1; row++) {
                    System.out.print(grid[row][column] + ",\t");
                }
                System.out.println(grid[grid.length - 1][column]);
            }
        } else {
            for (int row = grid.length - 1; row >= 0; row--) {
                for (int column = grid[0].length - 1; column > 0; column--) {
                    System.out.print(grid[row][column] + ",\t");
                }
                System.out.println(grid[row][0]);
            }
        }
    }
    
	public static void main(String[] args) {
		int[][] array = {
		    {1, 2, 3, 4, 5}, 
		    {6, 7, 8, 9, 10}, 
		    {11, 12, 13, 14, 15}
		}; 
		rotate(array, 0);
	}
}
