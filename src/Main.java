import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{

	private Pane root;
	
	private Tile[][] grid;
	private Tile player, goal;
	private PriorityQueue<Tile> queue;
	
	public void start(Stage primaryStage) throws Exception {
		root = new Pane();
		Scene scene = new Scene(root, 500, 600);
		
		loadLevel("level.txt");
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Button calc = new Button("Next");
		calc.setLayoutY(500);
		calc.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Tile t = queue.poll();
				if(t == goal) {
					retrace(t);
					calc.setDisable(true);
				} else {
					t.setColor(Color.RED);
					algo(t);					
				}
			}			
		});
		root.getChildren().add(calc);
		
	}
	
	public void loadLevel(String filename) throws IOException{
		grid = new Tile[10][10];
		queue = new PriorityQueue<>();
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		for(int y = 0; y < 10; y++) {
			String line = scanner.nextLine();
			char[] arr = line.toCharArray();
			for(int x = 0; x < 10; x ++) {
				Tile t = new Tile(x, y);
				if(arr[x] == 'P') {
					player = t;
					t.setPlayer();
					queue.offer(t);
				}
				else if(arr[x] == 'G') {
					t.setGoal();
					goal = t;
				}
				else if(arr[x] == 'W') t.setWall();
				grid[y][x] = t;
				root.getChildren().add(t);
			}
		}
	}
	
	public void algo(Tile t) {
		int xPos = t.getXPos();
		int yPos = t.getYPos();
		
		for(int y = yPos -1; y <= yPos + 1; y++) {
			for(int x = xPos -1; x <= xPos + 1; x++) {
				try {
					if(x == xPos && yPos == 0) continue;
					else if(!grid[y][x].getOpen()) continue;
					if(grid[y][x] != goal) grid[y][x].calcFCost();
					grid[y][x].setParentTile(t);
					queue.offer(grid[y][x]);					
				} catch (IndexOutOfBoundsException e) {
					System.out.println("INDEX ERROR");
				}
			}	
		}
	}
	
	public void retrace(Tile t) {
		if(t == null) return;
		t.setColor(Color.CYAN);
		retrace(t.getParentTile());
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
