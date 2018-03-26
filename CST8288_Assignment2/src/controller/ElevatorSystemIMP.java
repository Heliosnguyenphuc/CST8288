package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import model.Elevator;
import model.MovingState;
import sorting.QuickSort;

public class ElevatorSystemIMP implements ElevatorPanel, ElevatorSystem {

	/* fields */
	private final int MAX_FLOOR;
	private final int MIN_FLOOR;
	private AtomicBoolean[]	inUse;
	private Map<Elevator, List<Integer>> stops;
	private ExecutorService service;
	private final Object REQUEST_LOCK = new Object();
	private AtomicBoolean shutdown = new AtomicBoolean(false);
	private MovingState callDirection;


	private Runnable run = () -> {
		AtomicInteger counters[] = new AtomicInteger[stops.size()];

		for (int i = 0; i < counters.length; i++) {
			counters[i] = new AtomicInteger();
		}

		while (!shutdown.get()) 
		{
			for (Elevator e : stops.keySet()) {
				List<Integer> l = stops.get(e);
				if (!e.isIdle() || l.isEmpty() || counters[e.id()].get() != 0) {
					continue;
				}

				synchronized (REQUEST_LOCK)
				{
					int f = l.remove(0);			
					counters[e.id()].incrementAndGet();
					service.submit(() -> {
						e.moveTo(f);
						counters[e.id()].decrementAndGet();
					});

				}
			}

		}
	};

	public ElevatorSystemIMP(int MIN_FLOOR, int MAX_FLOOR) {
		this.MIN_FLOOR = MIN_FLOOR;
		this.MAX_FLOOR = MAX_FLOOR;
		stops = new HashMap<>();
		service = Executors.newCachedThreadPool();
	}

	@Override
	public void addElevator(Elevator elevator) {
		stops.put(elevator, new LinkedList<>());
	}

	@Override
	public Elevator callUp(int floor) {
		return call(floor, MovingState.Up);

	}

	@Override
	public Elevator callDown(int floor) {
		return call(floor, MovingState.Down);
	}

	@Override
	public int getCurrentFloor() {
		throw new UnsupportedOperationException("this method is currently not supported");
	}

	@Override
	public int getFloorCount() {
		return Math.abs(MIN_FLOOR - MAX_FLOOR) + 1;
	}

	@Override
	public int getMaxFloor() {
		return this.MAX_FLOOR;
	}

	@Override
	public int getMinFloor() {
		return this.MIN_FLOOR;
	}

	@Override
	public double getPowerConsumed() {
		double sumPowered = 0;
		for (Elevator e : stops.keySet()) {

			sumPowered+= e.getPowerConsumed();
		}
		return sumPowered;
	}

	@Override
	public void requestStop(Elevator elevator, int floor) {
		elevator.moveTo(floor);
	}

	private void floorCheck(int floor) {
		if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
			throw new IllegalArgumentException("floor out of range");
		}
	}

	private Elevator call(int floor, MovingState direction) {
		floorCheck(floor);
		checkForElevator();
		callDirection = direction;
		Elevator e = getAvailableElevatorIndex(floor);
		e.moveTo(floor);
		return e;
	}

	private void checkForElevator() {

		if (stops.isEmpty()) {
			throw new IllegalStateException("no elevators have been added");
		}

	}

	@Override
	public int getElevatorCount() {
		// TODO Auto-generated method stub
		return stops.size();
	}

	@Override
	public void addObserver(Observer observer) {
		for (Elevator e : stops.keySet() ) {
			e.addObserver(observer);
		}
	}

	@Override
	public void shutdown() {
		shutdown.set(true);
		service.shutdown();

	}

	@Override
	public void start() {
		// initial inUse  by for loop
		//by default is is false ;
		inUse = new AtomicBoolean[stops.size()];
		for (int i =0;i< 4;i++) {
			inUse[i]= new AtomicBoolean();
		}
		service.submit(run);
	}


	/*
	 * (non-Javadoc)
	 * requestStops accept int arrays of floors , need to sort in order 
	 * @see controller.ElevatorPanel#requestStops(model.Elevator, int[])
	 */

	@Override
	public void requestStops(Elevator elevator, int... floors) {

		synchronized (REQUEST_LOCK) {

			if (callDirection == MovingState.Up)
				QuickSort.quicksort(floors, 0, floors.length-1);
			else
				QuickSort.quicksortReverse(floors, 0, floors.length-1);

			List<Integer> l = stops.get(elevator);
			IntStream.of(floors).forEach(f -> l.add(f));
		}
	}


	public Elevator getAvailableElevatorIndex(int floor) {
		synchronized (REQUEST_LOCK) {
			int smallest=Integer.MAX_VALUE;

			Elevator bestElevator = null;
			for(Elevator elevator:stops.keySet()) {
				if (elevator.isIdle()&& stops.get(elevator).isEmpty())
				{
					int temp= Math.abs(elevator.getFloor()-floor)+1; //if elevator is closest to floor
					if (temp<smallest) {
						bestElevator =elevator;
						inUse[bestElevator.id()].set(true);
						smallest=temp;
					}
				}
			}
			return bestElevator;
		}
	}

}
