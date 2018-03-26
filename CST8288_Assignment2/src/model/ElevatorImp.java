package model;

import java.util.Arrays;
import java.util.Observable;

import controller.ElevatorPanel;



public class ElevatorImp extends Observable implements Elevator {

	/* fields */
	private final static int POWER_START_TOP =2;
	private final static int POWER_CONTINOUS =1;
	private final static long SLEEP_START_TOP =500;
	private final static  long SLEEP_CONTINOUS =250;
	private final double MAX_CAPACITY_PERSON ;
	private final int ID;

	private double powerUsed =0;
	private int currentFloor =0;
	private int capacity;
	private ElevatorPanel panel;
	private MovingState state = MovingState.Idle ;
	private boolean delay = false;
	
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
	
	private void processMovingState(int floor) {
		throw new UnsupportedOperationException("this method is currently not supported");
	}

	
	
	@Override
	public void moveTo( int floor) {
		// TODO Auto-generated method stub
		while (currentFloor!= floor) {

			int previousFloor = 0;

			switch(state) {

			case Idle:
				if (floor >currentFloor) {
					state = MovingState.SlowUp;
					break;
				}else if (floor<currentFloor) {
					state = MovingState.SlowDown;
					break;
				}

			case SlowUp:

				powerUsed += POWER_START_TOP;
				System.out.println("this is Slow -up ");
				previousFloor = currentFloor;
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
				previousFloor = currentFloor;
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
				previousFloor = currentFloor;
				currentFloor ++;


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
				previousFloor = currentFloor;
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
				throw new IllegalStateException( this.state + " is invalid");

			}//end switch


			setChanged();
			notifyObservers(Arrays.asList(currentFloor, floor, powerUsed, id(),previousFloor));
			try {
				Thread.sleep(state.isGoingSlow() ? SLEEP_START_TOP: SLEEP_CONTINOUS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addPersons(int person) {
		if (person + capacity > MAX_CAPACITY_PERSON) {
			throw new IllegalArgumentException("you get over capacity");
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElevatorImp other = (ElevatorImp) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	


}
