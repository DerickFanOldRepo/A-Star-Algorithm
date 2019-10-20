import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Tile extends Pane implements Comparable{

	private static int size = 50;
	private int xPos, yPos;
//	gCost = distance from player
//	hCost = distance from goal
//	fCost = gCost + hCost
	private int fCost, gCost, hCost;
	private boolean open, player, goal, wall;

	private Label fLabel, gLabel, hLabel;
	private Canvas canvas;
	private GraphicsContext context;
	private Tile parent;
	private static Tile playerTile, goalTile;
	
	public Tile(int x, int y) {
		super();
		xPos = x;
		yPos = y;
		open = true;
		player = false;
		goal = false;
		wall = false;
		
		this.setPrefSize(size, size);
		this.setLayoutX(x * 50);
		this.setLayoutY(y * 50);
		
		gLabel = new Label();
		gLabel.setLayoutX(0);
		gLabel.setLayoutY(0);
		hLabel = new Label();
		hLabel.setLayoutX(30);
		hLabel.setLayoutY(0);
		fLabel = new Label();
		fLabel.setLayoutX(25);
		fLabel.setLayoutY(25);
		
		canvas = new Canvas(size, size);
		context = canvas.getGraphicsContext2D();
		context.strokeRect(0, 0, size, size);
		
		this.getChildren().add(canvas);
		this.getChildren().add(gLabel);
		this.getChildren().add(hLabel);
		this.getChildren().add(fLabel);
	}
	
	public void setPlayer() {
		playerTile = this;
		open = false;
		context.setFill(Color.YELLOW);
		context.fillRect(1, 1, size - 2, size - 2);
		player = true;
	}
	public boolean getPlayer() {
		return player;
	}
	
	public void setGoal() {
		goalTile = this;
		context.setFill(Color.CYAN);
		context.fillRect(1, 1, size - 2, size - 2);
		goal = true;
	}
	public boolean getGoal() {
		return goal;
	}
	
	public void setWall() {
		context.setFill(Color.BLACK);
		context.fillRect(1, 1, size - 2, size - 2);
		open = false;
		wall = true;
	}
	
	public void calcFCost() {
		open = false;
//		this.parent = parent;
		calcGCost();
		calcHCost();
		fCost = gCost + hCost;
		fLabel.setText(Integer.toString(fCost));
//		context.setFill(Color.WHITE);
//		context.fillRect(1, 1, size - 2, size - 2);
	}
	
	public void calcGCost() {
		int xDiff = Math.abs(playerTile.xPos - xPos);
		int yDiff = Math.abs(playerTile.yPos - yPos);
		int straight = Math.max(xDiff, yDiff) - Math.min(xDiff, yDiff);
		int diagonal = Math.min(xDiff, yDiff);
		int distance = straight * 10 + diagonal * 14;
		gCost = distance;
		gLabel.setText(Integer.toString(gCost));
	}
	
	public void calcHCost() {
		int xDiff = Math.abs(goalTile.xPos - xPos);
		int yDiff = Math.abs(goalTile.yPos - yPos);
		int straight = Math.max(xDiff, yDiff) - Math.min(xDiff, yDiff);
		int diagonal = Math.min(xDiff, yDiff);
		int distance = straight * 10 + diagonal * 14;
		hCost = distance;
		hLabel.setText(Integer.toString(hCost));
	}
	
	public void setColor(Color c) {
		context.setFill(c);
		context.fillRect(1, 1, size - 2, size - 2);
	}
	
	public int getXPos() {
		return xPos;
	}
	public int getYPos() {
		return yPos;
	}
	
	public Tile getParentTile() {
		return parent;
	}
	public void setParentTile(Tile t) {
		parent = t;
	}
	
	public boolean getOpen() {
		return open;
	}
	
	@Override
	public int compareTo(Object o) {
		Tile t = (Tile)o;
		if(fCost < t.fCost) return -1;
		else if(fCost == t.fCost) {
			if(hCost < t.hCost) return -1;
			else if(hCost > t.hCost) return 1;
		} else if(fCost > t.fCost) return 1;
		return 0;
	}
	
}
