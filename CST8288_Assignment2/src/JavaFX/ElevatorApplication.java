package JavaFX;


import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.ElevatorSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Elevator;
import model.ElevatorImp;

public class ElevatorApplication  extends Application implements Observer{
	private static final int FLOOR_COUNT =21;
	private Label[] floors;
	private Simulator simulator;
	private Queue <List<Number>> queue = new LinkedList<>();
	private int targetFloor;
	private int currentFloor =0;

	private TextField t1 = new TextField();
	private TextField t2 = new TextField();
	private TextField t3 = new TextField();
	private Label l1 = new Label(" Current Floor : ");
	private Label l2 = new Label(" Target Floor : ");
	private Label l3 = new Label(" Total Power Consumed : ");

	private TextField t5 = new TextField();

	private ElevatorAnimation elevatorAnimation;

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
		floors= new Label[FLOOR_COUNT];
		for(int i=0; i<FLOOR_COUNT; i++) {
			floors[i] = new Label();
			floors[i].setText("floor:"+i+"");
			floors[i].setId("empty");
			floors[i].setMinSize(150, 20);


		}
		
		floors[0].setId("elevator");

	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Button control = new Button("Move");
		
		simulator = new Simulator(this);

			
			

		control.setOnAction(e->{
			simulator.start();

		});

		elevatorAnimation = new ElevatorAnimation();

		GridPane rootPane = new GridPane();
		GridPane floorPane = new GridPane();

		for(int i =0; i<FLOOR_COUNT; i++) {
			floorPane.add(floors[i],0,(FLOOR_COUNT-1)-i);

		}


		rootPane.add(floorPane, 0, 0);
	
		//adding label 
		floorPane.add(l1, 1, 2);
		floorPane.add(l2, 1, 3);
		floorPane.add(l3, 1, 4);
		//adding textfield
		floorPane.add(t1, 2, 2);
		floorPane.add(t2, 2, 3);
		floorPane.add(t3, 2, 4);
		//move to
		floorPane.add(control, 2, 5);

		Scene scene = new Scene(rootPane);
		scene.getStylesheets().add(ElevatorApplication.class.getResource("style.css").toExternalForm());
		primaryStage.setTitle("Elevator Simulator");
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	private class ElevatorAnimation extends AnimationTimer{

		private static final long SECOND = 1000000001;
		private static final long normal_speed = SECOND/7;
		private static final long slow_speed = SECOND/3;
		private long timeCheck =0;
		private long prevFrame =0;


		@Override
		public void handle (long now) {
			
			
			if(now-prevFrame<timeCheck) {
				return;
			}prevFrame = now;
			
			
			
			
			
			List<Number> data = queue.poll();
			
			currentFloor = (int) data.get(0);
			targetFloor = (int) data.get(1);
			
			
			if (currentFloor< targetFloor) {
				floors[currentFloor].setId("target");

				floors[currentFloor].setId("empty");
				floors[targetFloor].setId("target");

				timeCheck=(int) (Math.abs(currentFloor - targetFloor)==1?slow_speed:normal_speed);}
			else if (targetFloor<currentFloor) {
				floors[targetFloor].setId("elevator");
				floors[targetFloor].setId("target");
				floors[currentFloor].setId("empty");


				timeCheck=(int) (Math.abs(currentFloor - targetFloor)==1?slow_speed:normal_speed);
			}

			else  {
				//current == target 
				floors[currentFloor].setId("elevator");

			}
			
			if (queue.isEmpty())
				elevatorAnimation.stop();
			
			t1.setText(Integer.toString(currentFloor));
			t2.setText(Integer.toString(targetFloor));
			t3.setText(Double.toString((double)data.get(2)));
			
		}
	}// end handle




	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		Platform.exit();
		
	}


	public static void main (String [] args) {
		launch(args);

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (o instanceof ElevatorImp) {
			List<Number> arr = (List<Number>) arg;
			addData(arr);
			elevatorAnimation.start();

		}
	}
	public void addData(List<Number> data) {
		queue.add(data);
	}

}
