package model;

public enum MovingState {
	Up, Down, SlowUp, SlowDown, Idle, Off;
	
	
	public boolean isGoingDown() {
		return this==Down  || this == SlowDown;
	}
	
	public boolean isGoingUp() {
		return this==Up || this == SlowUp;
	}
	
	public boolean isGoingSlow() {
		return this==SlowUp || this==SlowDown;
	}
	
	public MovingState slow() {
		switch( this){
		case Down:
			return SlowDown;
		case Up:
			return SlowUp;
		case SlowUp:
		case SlowDown:
			return this;
		default:
			throw new IllegalStateException( "ERROR - " + this.name() + " has no slow");
	}
		
	}
	
	public MovingState normal() {
		switch( this){
		case SlowDown:
			return Down;
		case SlowUp:
			return Up;
		case Up:
		case Down:
			return this;
		default:
			throw new IllegalStateException( "ERROR - " + this.name() + " has no normal");
	}
		
	}
	
	
	
	public MovingState opposite() {
		switch( this){
		case Down:
			return Up;
		case Up:
			return Down;
		case SlowDown:
			return SlowUp;
		case SlowUp:
			return SlowDown;
		default:
			throw new IllegalStateException( "ERROR - " + this.name() + " has no opposite");
	}
}
	
}
