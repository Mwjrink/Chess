

import java.awt.image.BufferedImage;

public class Piece {

	String Symbol;
	boolean canJump;
	boolean hasBeenMoved;
	int x;
	int y;
	int[] moves;
	int originSide;
	
	BufferedImage sprite;
	
	public boolean getCanJump() {
		return this.canJump;
	}

	public String getSymbol() {
		return this.Symbol;
	}

	Piece(boolean canJump, String Symbol) {
		this.Symbol = Symbol;
		this.canJump = canJump;
	}
	
	Piece(boolean canJump, String Symbol, int originSide) {
		this.Symbol = Symbol;
		this.canJump = canJump;
		this.originSide = originSide;
	}
	
	Piece(boolean canJump, boolean hasBeenMoved, String Symbol) {
		this.Symbol = Symbol;
		this.canJump = canJump;
		this.hasBeenMoved = hasBeenMoved;
	}
	
	Piece(String Name) {
		this.Symbol = Name;
	}

	public String getName() {
		return Symbol;
	}
	
	public void Moved(){
		hasBeenMoved = true;
	}
	
	public void setSprite(BufferedImage sprite){
		this.sprite = sprite;
	}
	
	public BufferedImage getSprite(){
		return sprite;
	}
}