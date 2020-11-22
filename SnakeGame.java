import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

class Point {
    int row; 
    int col; 
    
    Point(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

class Node {
    Point point; 
    Node next; 
    
    Node(Point point, Node next) {
        this.point = point; 
        this.next = next; 
    }
}

class Snake {
    Node head; 
    Node tail; 
    int growAmount; 
    Point boundary; 
    
    Snake(Point boundary) {
        this.boundary = boundary; 
        head = new Node(new Point(boundary.row - 1, boundary.col / 2), null);
        tail = head; 
        growAmount = 3;
    }
    
    int move(Point direction) {
        Point newPosition = new Point(head.point.row + direction.row, head.point.col + direction.col);
        if (newPosition.row >= boundary.row || newPosition.row < 0) {
            return -1;
        } 
        if (newPosition.col >= boundary.col || newPosition.col < 0) {
            return -1;
        }
        head.next = new Node(newPosition, null); 
        head = head.next; 
        if (growAmount > 0) {
            growAmount--; 
            return 1;
        }
        tail = tail.next; 
        return 0; 
    }
    
    void consume() {
        growAmount += 3; 
    }
}

class Game {
    char[][] grid; 
    Snake snake; 
    Point boundary; 
    Input input; 
    BufferedOutputStream buffer; 
    Random random; 
    
    Game(Point boundary) {
        buffer = new BufferedOutputStream(System.out);
        this.boundary = boundary;
        input = new Input(); 
        grid = new char[boundary.row][boundary.col];
        for (int i = 0; i < boundary.row; i++) {
            for (int j = 0; j < boundary.col; j++) {
                grid[i][j] = ' ';
            }
        }
        snake = new Snake(boundary); 
        grid[snake.head.point.row][snake.head.point.col] = 'O';
        random = new Random(); 
        generateFood();
    }
    
    void generateFood() {
      int row = random.nextInt(boundary.row);
      int col = random.nextInt(boundary.col);
      while (grid[row][col] == 'O') {
        col++; 
        if (col >= boundary.col) {
          col = 0;
          row++; 
          if (row >= boundary.row) {
            row = 0;
          }
        }
      }
      grid[row][col] = '+';
    }

    boolean update() {
        int ogTailRow = snake.tail.point.row; 
        int ogTailCol = snake.tail.point.col; 
        int status = snake.move(input.direction); 
        if (status == -1) {
            return false; 
        }
        if (grid[snake.head.point.row][snake.head.point.col] == 'O') {
            return false; 
        }
        if (grid[snake.head.point.row][snake.head.point.col] == '+') {
            snake.consume(); 
            generateFood(); 
        }
        grid[snake.head.point.row][snake.head.point.col] = 'O';
        if (status == 0) {
            grid[ogTailRow][ogTailCol] = ' ';
        }
        return true;
    }
    
    void printState() throws IOException {
        for (int i = 0; i < boundary.row + 2; i++) {
            buffer.write('-');
        }
        buffer.write('\n');
        for (int i = 0; i < boundary.row; i++) {
            buffer.write('|');
            for (int j = 0; j < boundary.col; j++) {
                buffer.write(grid[i][j]); 
            }
            buffer.write('|');
            buffer.write('\n');
        }
        for (int i = 0; i < boundary.row + 2; i++) {
            buffer.write('-');
        }
        buffer.write('\n');
        buffer.flush();
    }
    
    void start() throws IOException, InterruptedException {
        while (update()) {
            printState();
            Thread.sleep(200); 
        }
    }
} 

class Input extends Frame implements KeyListener {
  private TextArea area;
  public Point direction;

  Input() {
    direction = new Point(-1, 0);
    area = new TextArea();
    area.addKeyListener(this);
    setSize(100,100);
    add(area);
    setLayout(null);
    setVisible(true);  
  }

public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
	    if (direction.row != 1) {
	        direction.row = -1;
	        direction.col = 0;
	    }
    } 
    else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        if (direction.row != -1) {
            direction.row = 1;
	        direction.col = 0;
        }
    } 
    else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        if (direction.col != 1) {
            direction.row = 0;
	        direction.col = -1;
        }
    } 
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        if (direction.col != -1) {
            direction.row = 0;
	        direction.col = 1;
        }
    }
}

  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}  
}

public class SnakeGame {
  public static void main(String[] args) throws IOException, InterruptedException {
    Game game = new Game(new Point(20, 20));
    game.start();
  }
}
