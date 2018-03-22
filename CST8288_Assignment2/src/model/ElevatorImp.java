package model;

import java.util.Arrays;
import java.util.Observable;

import com.sun.javafx.collections.ObservableSetWrapper;

import controller.ElevatorPanel;
import controller.ElevatorSystemIMP;

public class ElevatorImp extends Observable implements Elevator {

	/* fields */
	private final static int POWER_START_TOP =2;
	private final static int POWER_CONTINOUS =1;
	private final static long SLEEP_START_TOP =500;
	private final static  long SLEEP_CONTINOUS =250;

	private final double MAX_CAPACITY_PERSON ;

	private final boolean delay;
	private final int ID;
	
	private double powerUsed =0;
	private int currentFloor =0;
	private int capacity;
	private ElevatorPanel panel;
	private MovingState state = MovingState.Idle ;




	/*CONSTRUCTOR */

	public ElevatorImp(int CAPACITY_PERSONS, ElevatorPanel p, int ID) {
		this(CAPACITY_PERSONS, p, ID, true);
	}
	

	public ElevatorImp(final double CAPACITY_PERSON, ElevatorPanel panel,int ID, boolean delay ) {
		this.MAX_CAPACITY_PERSON =CAPACITY_PERSON;
		this.panel = panel;
		if (CAPACITY_PERSON > MAX_CAPACITY_PERSON)
			throw new IllegalArgumentException("Invalid Capacity Input");
		this.ID = ID;
		this.delay = delay;
		
	}
	
	public void notifyObserver(int previousFloor, int currentFloor, int floor, double powerUsed) {
		setChanged();
		notifyObservers(Arrays.asList(previousFloor, currentFloor, floor, powerUsed));
		try {
			Thread.sleep(state.isGoingSlow() ? SLEEP_START_TOP: SLEEP_CONTINOUS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private void processMovingState(int floor) {

	}

	@Override
	public void moveTo( int floor) {
		// TODO Auto-generated method stub
		while (currentFloor!= floor) {
		
		
			switch(state) {

			case Idle:
				
				System.out.println("this is Idle");
				if (floor >currentFloor) {
					state = MovingState.SlowUp;
					break;
				}else if (floor<currentFloor) {
					System.out.println("**********");
					state = MovingState.SlowDown;
					break;
				}
	
		
			case SlowUp:
				
				powerUsed += POWER_START_TOP;
				System.out.println("this is Slow -up ");
				currentFloor ++;
				if (floor == currentFloor )	
				{
					state = MovingState.Idle;
				} else if (Math.abs(floor-currentFloor)!=1) {
					state=MovingState.Up;
				}
				break;
				

			case SlowDown:
				powerUsed += POWER_START_TOP ;
				currentFloor --;
				System.out.println("Slow Down!"+currentFloor);
				if((currentFloor -floor)==0)
				{
					state = MovingState.Idle;
				} else if (Math.abs(currentFloor-floor)!=1) {
					state = state.normal();
				}
				break;
				
			case Up:
				
				powerUsed += POWER_CONTINOUS;
				currentFloor ++;
				System.out.println("this is up ");
				
				if (floor - currentFloor >=2 ) {
					state = MovingState.Up;
					break;
				}
				else
				{
				state = MovingState.SlowUp;
				break;
				}
				
			case Down: 
				powerUsed += POWER_CONTINOUS;
				currentFloor--;
				System.out.println(" Down!"+currentFloor);
				if ( currentFloor-floor >=2 ) {
					state = MovingState.Down;
					break;
				}else
				{
				
				state = MovingState.SlowDown;
				break;
				}
			
				
				default :
					throw new IllegalStateException( "ERROR - " + this.state + " is invalid");

			}//end switch
			
			
			setChanged();
			notifyObservers(Arrays.asList(currentFloor,floor,powerUsed));

		}
	}

	@Override
	public void addPersons(int person) {
		if (person + capacity > MAX_CAPACITY_PERSON) {
			throw new IllegalArgumentException("over capacity");
		}
		capacity = person;
	}

	

	@Override
	public void requestStop(int floor) {
		panel.requestStop(this, floor);
		state = MovingState.Idle;
	}

	@Override
	public int getCapacity() {
	
		return capacity;
	}

	@Override
	public int getFloor() {
		return this.currentFloor;
	}

	@Override
	public double getPowerConsumed() {
		return powerUsed;
	}

	

	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		return capacity == MAX_CAPACITY_PERSON;
	}


	@Override
	public boolean isEmpty() {
		
		return capacity == 0;
	}

	@Override
	public boolean isIdle() {
		
		return state == MovingState.Idle;
	}

	@Override
	public MovingState getState() {
		// TODO Auto-generated method stub
		return this.state;
	}

	@Override
	public int id() {
		return this.ID;
	}

	@Override
	public void requestStops(int... floors) {
		panel.requestStops(this, floors);
		
	}
	
	
}
