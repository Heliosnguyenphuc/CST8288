package JavaFX;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.ElevatorImp;

public class ApplicationView extends Application implements Observer {

	private static final int FLOOR_COUNT = 21;
	private Label[][] floors;
	private TextField[][] tv  ;
	private Queue<List<Number>> queue = new LinkedList<>();
	private int targetFloor = 0;
	private int currentFloor = 0;
	private double power =0.0;
	
	
	

	@Override
	public void init() throws Exception {

		super.init();
		tv = new TextField[4][4];
		floors = new Label[4][FLOOR_COUNT];
		for (int i = 0; i < FLOOR_COUNT; i++) {

			for (int j = 0; j < 4; j++) {

				floors[j][i] = new Label();
				floors[j][i].setText("floor:" + i + "");
				floors[j][i].setId("empty");
				floors[j][i].setMinSize(100, 20);
			}
		}
		floors[0][0].setId("elevator");
		floors[1][0].setId("elevator");
		floors[2][0].setId("elevator");
		floors[3][0].setId("elevator");
	
		for(int i =0;i<=3;i++) {
			for (int j =0;j <=3;j++) {
				tv [j][i]= new TextField();
			}
		}

	}//end of init



	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane rootPane = new GridPane();
		GridPane floorPane1 = new GridPane();
		GridPane floorPane2 = new GridPane();
		GridPane floorPane3 = new GridPane();
		GridPane floorPane4 = new GridPane();

		for (int i = 0; i < FLOOR_COUNT; i++) {

			floorPane1.add(floors[0][i], 0, (FLOOR_COUNT - 1) - i);
			floorPane2.add(floors[1][i], 0, (FLOOR_COUNT - 1) - i);
			floorPane3.add(floors[2][i], 0, (FLOOR_COUNT - 1) - i);
			floorPane4.add(floors[3][i], 0, (FLOOR_COUNT - 1) - i);

		}


		// adding textfield
		floorPane1.add(tv[0][0], 2, 2);
		floorPane1.add(tv[0][1], 2, 3);
		floorPane1.add(tv[0][2], 2, 4);
		//		
		floorPane2.add(tv[1][0], 2, 2);
		floorPane2.add(tv[1][1], 2, 3);
		floorPane2.add(tv[1][2], 2, 4);

		floorPane3.add(tv[2][0], 2, 2);
		floorPane3.add(tv[2][1], 2, 3);
		floorPane3.add(tv[2][2], 2, 4);


		floorPane4.add(tv[3][0], 2, 2);
		floorPane4.add(tv[3][1], 2, 3);
		floorPane4.add(tv[3][2], 2, 4);

		rootPane.add(floorPane1, 0, 0);
		rootPane.add(floorPane2, 1, 0);
		rootPane.add(floorPane3, 4, 0);
		rootPane.add(floorPane4, 6, 0);


		Simulator simulator = new Simulator(this);
		simulator.start();



		Scene scene = new Scene(rootPane);
		scene.getStylesheets().add(ApplicationView.class.getResource("style.css").toExternalForm());
		primaryStage.setTitle("Simulator No 2");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void  update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (o instanceof ElevatorImp) {
			List<Number> arr = (List<Number>) arg;
			addData(arr);

			List<Number> data = queue.poll();

			targetFloor = (int) data.get(1);
			currentFloor = (int) data.get(0);
			double power = (double) data.get(2);


			int id = (int) data.get(3);
			int previousFloor = (int) data.get(4);




			if (currentFloor < targetFloor) {//moving up 
				floors[id][currentFloor].setId("elevator");
				floors[id][previousFloor].setId("empty");
				floors[id][targetFloor].setId("target");

			} else if (targetFloor < currentFloor) {//moving down 

				floors[id][currentFloor].setId("elevator");
				floors[id][previousFloor].setId("empty");
				floors[id][targetFloor].setId("target");


			}

			else {
				// current == target
				floors[id][targetFloor].setId("empty");
				floors[id][currentFloor].setId("elevator");
				floors[id][previousFloor].setId("empty");

				tv[id][0].setText(Integer.toString(currentFloor));
				tv[id][1].setText(Integer.toString(targetFloor));
				tv[id][2].setText(Double.toString(power));

			}
		}

	}

	public void addData(List<Number> data) {
		queue.add(data);
	}

	@Override
	public void stop() throws Exception {
		Platform.exit();

	}

}
