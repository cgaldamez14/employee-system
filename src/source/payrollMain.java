package source;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import source.payrollPane;

public class payrollMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			payrollPane payroll = new payrollPane(primaryStage);			

			Scene scene = new Scene(payroll);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

