package junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import controller.ElevatorPanel;
import controller.ElevatorSystemIMP;
import model.Elevator;
import model.ElevatorImp;
import model.MovingState;
import sorting.QuickSort;



public class Junit {
	
	static ElevatorSystemIMP system ;
	
	@Before
	public void setUp() throws Exception {
		 system = new ElevatorSystemIMP(0,20);
	}
	
	@After
	public void tear() {
		system = null;

	}
	
	@Test(expected =IllegalStateException.class)
	public void expectedException() {
		
		system.callDown(20);
		assertTrue(system.getElevatorCount()==0);
	}
	
	@Test(expected =UnsupportedOperationException.class)
	public void expectedExceptionWhenGetCurrentFloor() {
		
		assertTrue(system.getCurrentFloor()==2);
	}
	
	
	@Test
	public void testQuickSortAsc() 
	{
		int arr[]= { 12, 2, 5, 9, 20, 3 };
		int result[]= {2,3,5,9,12,20 };
		
		QuickSort.quicksort(arr,0, arr.length-1);
		assertArrayEquals(arr, result);
		
	}
	
	
	@Test
	public void testQuickSortDESC() 
	{
		int arr[]= { 12, 2, 5, 9, 20, 3 };
		
		int result[]= {20,12,9,5,3,2 };
		
		QuickSort.quicksortReverse(arr,0, arr.length-1);
		assertArrayEquals(arr, result);
		
	}
	
	@Test
	public void testRequestStop (){
		ElevatorImp e = new  ElevatorImp( 5, (ElevatorPanel) system, 0);
		e.requestStop(20);
		assertTrue(e.getFloor()==20);
	
	}
	
	
	@Test(expected =IllegalArgumentException.class)
	public void testAddPerson (){
		ElevatorImp e = new  ElevatorImp( 5, (ElevatorPanel) system, 0);
		e.addPersons(10);
		
	}
	
	
	@Test
	public void testGetCapacity (){
		ElevatorImp e = new  ElevatorImp( 5, (ElevatorPanel) system, 0);
		e.requestStop(2);
		assertTrue(e.getCapacity()==0);
	
	}
	@Test
	public void testGetState (){
		
		ElevatorImp e = new  ElevatorImp( 5, (ElevatorPanel) system, 0);
		system.addElevator(e);
		e.requestStop(2);
		assertTrue(e.getState() ==MovingState.Idle);
	
	}
	
	
}
