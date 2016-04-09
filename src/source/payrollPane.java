package source;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

public class payrollPane extends BorderPane{

	private payrollDatabase database;
	private Tab employeeList;
	private boolean clicked = false;

	public payrollPane(Stage primaryStage){

		getStylesheets().add("styles/guiStyle.css");
		getStyleClass().add("root");

		Label loginTitle = new Label("HR/Payroll Login");
		loginTitle.getStyleClass().add("loginText");
		HBox hb = new HBox();
		hb.getStyleClass().add("loginTitle");
		hb.getChildren().add(loginTitle);

		TextField tf = new TextField();	
		tf.setFocusTraversable(false);
		tf.setPromptText("User Name");
		tf.setMaxWidth(175);

		final PasswordField pb = new PasswordField();
		pb.setFocusTraversable(false);
		pb.setPromptText("Password");
		pb.setMaxWidth(175);

		Button loginButton = new Button();
		loginButton.setText("Login");
		loginButton.getStyleClass().add("loginButton");	

		VBox loginFields = new VBox(5);
		loginFields.setAlignment(Pos.CENTER);
		loginFields.getChildren().addAll(tf, pb, loginButton);

		setTop(hb);
		setCenter(loginFields);

		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				database = new payrollDatabase(pb.getText(), tf.getText());
				pb.clear();
				tf.clear();
				if(database.isConnected()){
					TabPane tabPane = mainMenu();
					getChildren().clear();
					primaryStage.setWidth(650);
					primaryStage.setHeight(650);
					setTop(menuBar());
					setCenter(tabPane);
				}
				else{
					JOptionPane.showMessageDialog(null, "Incorrect username or password. Try again.", null, JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		primaryStage.setWidth(375);
		primaryStage.setHeight(350);
		primaryStage.setTitle("HR/Payroll");
		primaryStage.setResizable(false);

	}

	public TabPane mainMenu(){

		TabPane tabPane = new TabPane();
		Tab welcome = new Tab();
		Tab addEmployee = new Tab();
		Tab deleteEmployee = new Tab();
		Tab modifyEmployee = new Tab();
		Tab employeeSearch = new Tab();
		employeeList = new Tab();


		welcome.setText("Welcome");
		addEmployee.setText("Add Employee");
		deleteEmployee.setText("Delete Employee");
		modifyEmployee.setText("Modify Employee");
		employeeSearch.setText("Employee Search");
		employeeList.setText("Employee List");


		// Welcome Tab
		Label welcomeMessage = new Label("Welcome!"); 
		welcomeMessage.getStyleClass().add("welcome");
		HBox message = new HBox();
		message.getChildren().add(welcomeMessage);
		message.setAlignment(Pos.CENTER);
		// Add Employee Tab
		BorderPane aEmployee = addEmployee();
		// Delete Employee
		BorderPane dEmployee = deleteEmployee();
		//Modify Employee
		BorderPane mEmployee = modifyEmployee();
		//Employee Search
		BorderPane sEmployee = employeeSearch();


		welcome.setContent(message);
		addEmployee.setContent(aEmployee);
		deleteEmployee.setContent(dEmployee);
		modifyEmployee.setContent(mEmployee);
		employeeSearch.setContent(sEmployee);

		//EmployeeList
		try {
			employeeList.setContent(showEmployeeList());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tabPane.getTabs().addAll(welcome, addEmployee, deleteEmployee, modifyEmployee, employeeSearch, employeeList);
		for(Tab t : tabPane.getTabs())t.setClosable(false);

		return tabPane;
	}

	public BorderPane addEmployee(){
		BorderPane content = new BorderPane();

		HBox fields = new HBox(11);
		fields.getStyleClass().add("fields");

		VBox labels = new VBox(19);
		VBox textFields = new VBox(9);

		Label message = new Label("Enter new employee information on all of the following fields:");
		message.getStyleClass().add("message");
		content.setTop(message);

		Label fowardSlash1 = new Label("/");
		Label fowardSlash2 = new Label("/");
		Label fowardSlash3= new Label("/");
		Label fowardSlash4 = new Label("/");

		Label name = new Label("Last Name:");
		Label hire = new Label("Hire Date:");
		Label birth = new Label("Birth Date:");
		Label sex = new Label("Sex:");
		Label status = new Label("Status:");
		Label payType = new Label("Pay Type:");
		Label salary = new Label("Annual Salary:");

		TextField lastNameAdd = new TextField();
		lastNameAdd.setMaxWidth(175);
		TextField sexAdd= new TextField();
		sexAdd.setMaxWidth(175);
		TextField statusAdd = new TextField();
		statusAdd.setMaxWidth(175);
		TextField payTypeAdd= new TextField();
		payTypeAdd.setMaxWidth(175);
		TextField annualSalaryAdd = new TextField();
		annualSalaryAdd.setMaxWidth(175);

		HBox hireDate = new HBox(5);
		TextField monthHire = new TextField();
		monthHire.setPromptText("mm");
		monthHire.setMaxWidth(45);
		TextField dayHire = new TextField();
		dayHire.setPromptText("dd");
		dayHire.setMaxWidth(45);
		TextField yearHire = new TextField();
		yearHire.setPromptText("yyyy");
		yearHire.setMaxWidth(50);
		hireDate.getChildren().addAll(monthHire, fowardSlash1, dayHire, fowardSlash2, yearHire );


		HBox birthDate = new HBox(5);
		TextField monthBirth = new TextField();
		monthBirth.setPromptText("mm");
		monthBirth.setMaxWidth(45);
		TextField dayBirth = new TextField();
		dayBirth.setPromptText("dd");
		dayBirth.setMaxWidth(45);
		TextField yearBirth = new TextField();
		yearBirth.setPromptText("yyyy");
		yearBirth.setMaxWidth(50);
		birthDate.getChildren().addAll(monthBirth, fowardSlash3, dayBirth, fowardSlash4, yearBirth );

		HBox button = new HBox();
		Button addButton = new Button("Add");
		addButton.getStyleClass().add("loginButton");	
		button.getChildren().add(addButton);
		button.getStyleClass().add("fieldButtons");

		labels.getChildren().addAll(name,hire,birth,sex,status, payType,salary);
		for(Node n: labels.getChildren()) n.getStyleClass().add("fieldLabelText");
		textFields.getChildren().addAll(lastNameAdd, hireDate, birthDate, sexAdd, statusAdd,payTypeAdd, annualSalaryAdd, button);
		fields.getChildren().addAll(labels, textFields);
		content.setCenter(fields);

		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) {
				try {
					database.addEmployee(lastNameAdd.getText(), yearHire.getText() + "/" + monthHire.getText() + "/" + dayHire.getText(), yearBirth.getText() + "/" + monthBirth.getText() + "/" + dayBirth.getText(), sexAdd.getText(), statusAdd.getText(), payTypeAdd.getText(), annualSalaryAdd.getText());
					lastNameAdd.clear();
					yearHire.clear();
					monthHire.clear();
					dayHire.clear();
					yearBirth.clear();
					monthBirth.clear();
					dayBirth.clear();
					sexAdd.clear();
					statusAdd.clear();
					payTypeAdd.clear();
					annualSalaryAdd.clear();
					employeeList.setContent(showEmployeeList());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		return content;
	}

	public BorderPane deleteEmployee(){
		BorderPane content = new BorderPane();

		Label message = new Label("Enter ID of the employee you wish to delete:");
		message.getStyleClass().add("message");
		content.setTop(message);

		HBox employeeID = new HBox(5);
		Label fieldName = new Label("Employee ID:");
		fieldName.getStyleClass().add("fieldLabelText");
		TextField deleteIDIn = new TextField();
		Button deleteButton = new Button("Delete");
		deleteButton.getStyleClass().add("loginButton");
		employeeID.getStyleClass().add("fields2");

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) {
				try {
					database.deleteEmployee(deleteIDIn.getText());
					deleteIDIn.clear();
					employeeList.setContent(showEmployeeList());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		employeeID.getChildren().addAll(fieldName, deleteIDIn, deleteButton);

		content.setCenter(employeeID);

		return content;
	}

	public BorderPane modifyEmployee(){
		
		BorderPane content = new BorderPane();

		VBox iD = new VBox(); 
		iD.setAlignment(Pos.CENTER);
		Label message1 = new Label("Enter ID of employee you wish to modify:");
		message1.getStyleClass().add("messageModify");
		content.setTop(message1);

		HBox employeeID = new HBox(5);
		Label fieldName = new Label("Employee ID:");
		fieldName.getStyleClass().add("fieldLabelText");
		TextField iDIn = new TextField();
		employeeID.getStyleClass().add("fields");
		employeeID.getChildren().addAll(fieldName, iDIn);

		iD.getChildren().addAll(message1, employeeID);


		VBox modify = new VBox();
		modify.setAlignment(Pos.CENTER);
		Label message2 = new Label("Enter new information on fields you wish to modify: ");
		message2.getStyleClass().add("messageModify");

		HBox fields = new HBox(11);
		fields.getStyleClass().add("fields");

		VBox labels = new VBox(19);
		VBox textFields = new VBox(9);

		Label fowardSlash1 = new Label("/");
		Label fowardSlash2 = new Label("/");
		Label fowardSlash3= new Label("/");
		Label fowardSlash4 = new Label("/");

		Label name = new Label("Last Name:");
		Label hire = new Label("Hire Date:");
		Label birth = new Label("Birth Date:");
		Label sex = new Label("Sex:");
		Label status = new Label("Status:");
		Label payType = new Label("Pay Type:");
		Label salary = new Label("Annual Salary:");

		TextField lastNameMod = new TextField();
		lastNameMod.setMaxWidth(175);
		TextField sexMod= new TextField();
		sexMod.setMaxWidth(175);
		TextField statusMod = new TextField();
		statusMod.setMaxWidth(175);
		TextField payTypeMod= new TextField();
		payTypeMod.setMaxWidth(175);
		TextField annualSalaryMod = new TextField();
		annualSalaryMod.setMaxWidth(175);

		HBox hireDate = new HBox(5);
		TextField monthHire = new TextField();
		monthHire.setPromptText("mm");
		monthHire.setMaxWidth(45);
		TextField dayHire = new TextField();
		dayHire.setPromptText("dd");
		dayHire.setMaxWidth(45);
		TextField yearHire = new TextField();
		yearHire.setPromptText("yyyy");
		yearHire.setMaxWidth(50);
		hireDate.getChildren().addAll(monthHire, fowardSlash1, dayHire, fowardSlash2, yearHire );


		HBox birthDate = new HBox(5);
		TextField monthBirth = new TextField();
		monthBirth.setPromptText("mm");
		monthBirth.setMaxWidth(45);
		TextField dayBirth = new TextField();
		dayBirth.setPromptText("dd");
		dayBirth.setMaxWidth(45);
		TextField yearBirth = new TextField();
		yearBirth.setPromptText("yyyy");
		yearBirth.setMaxWidth(50);
		birthDate.getChildren().addAll(monthBirth, fowardSlash3, dayBirth, fowardSlash4, yearBirth );

		HBox button = new HBox();
		Button modifyButton = new Button("Modify");
		modifyButton.getStyleClass().add("loginButton");	
		button.getChildren().add(modifyButton);
		button.getStyleClass().add("fieldButtons");

		labels.getChildren().addAll(name,hire,birth,sex,status, payType,salary);
		for(Node n: labels.getChildren()) n.getStyleClass().add("fieldLabelText");
		textFields.getChildren().addAll(lastNameMod, hireDate, birthDate, sexMod, statusMod,payTypeMod, annualSalaryMod, button);
		fields.getChildren().addAll(labels, textFields);
		modify.getChildren().addAll(message2,fields);

		content.setTop(iD);
		content.setCenter(modify);

		modifyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) {
				try {
					database.modifyEmployee(iDIn.getText(), lastNameMod.getText(), yearHire.getText() + "/" + monthHire.getText() + "/" + dayHire.getText(), yearBirth.getText() + "/" + monthBirth.getText() + "/" + dayBirth.getText(), sexMod.getText(), statusMod.getText(), payTypeMod.getText(), annualSalaryMod.getText());
					iDIn.clear();
					lastNameMod.clear();
					yearHire.clear();
					monthHire.clear();
					dayHire.clear();
					yearBirth.clear();
					monthBirth.clear();
					dayBirth.clear();
					sexMod.clear();
					statusMod.clear();
					payTypeMod.clear();
					annualSalaryMod.clear();
					employeeList.setContent(showEmployeeList());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		return content;
	}

	public BorderPane employeeSearch(){
		BorderPane content = new BorderPane();

		VBox search = new VBox();
		search.setAlignment(Pos.CENTER);
		Label message2 = new Label("Enter information on the search fields: ");
		message2.getStyleClass().add("messageModify");

		HBox fields = new HBox(11);
		fields.getStyleClass().add("fields");

		VBox labels = new VBox(19);
		VBox textFields = new VBox(9);

		Label fowardSlash1 = new Label("/");
		Label fowardSlash2 = new Label("/");
		Label fowardSlash3= new Label("/");
		Label fowardSlash4 = new Label("/");

		Label fieldName = new Label("Employee ID:");
		Label name = new Label("Last Name:");
		Label hire = new Label("Hire Date:");
		Label birth = new Label("Birth Date:");
		Label sex = new Label("Sex:");
		Label status = new Label("Status:");
		Label payType = new Label("Pay Type:");
		Label age = new Label("Age:");
		Label salary = new Label("Annual Salary:");
		Label years = new Label("Years in Service:");

		TextField iDIn = new TextField();
		iDIn.setMaxWidth(175);
		TextField lastNameSearch = new TextField();
		lastNameSearch.setMaxWidth(175);
		TextField sexSearch= new TextField();
		sexSearch.setMaxWidth(175);
		TextField statusSearch = new TextField();
		statusSearch.setMaxWidth(175);
		TextField payTypeSearch= new TextField();
		payTypeSearch.setMaxWidth(175);

		HBox yearsSearch = new HBox(15);
		TextField lessYears = new TextField();
		lessYears.setPromptText("   <");
		lessYears.setMaxWidth(45);
		TextField equalYears = new TextField();
		equalYears.setPromptText("   =");
		equalYears.setMaxWidth(45);
		TextField greaterYears = new TextField();
		greaterYears.setPromptText("   >");
		greaterYears.setMaxWidth(45);
		yearsSearch.getChildren().addAll(lessYears, equalYears, greaterYears );

		HBox annualSalarySearch = new HBox(15);
		TextField lessSalary = new TextField();
		lessSalary.setPromptText("   <");
		lessSalary.setMaxWidth(45);
		TextField equalSalary = new TextField();
		equalSalary.setPromptText("   =");
		equalSalary.setMaxWidth(45);
		TextField greaterSalary = new TextField();
		greaterSalary.setPromptText("   >");
		greaterSalary.setMaxWidth(45);
		annualSalarySearch.getChildren().addAll(lessSalary, equalSalary, greaterSalary );


		HBox ages = new HBox(15);
		TextField lessAge = new TextField();
		lessAge.setPromptText("   <");
		lessAge.setMaxWidth(45);
		TextField equalAge = new TextField();
		equalAge.setPromptText("   =");
		equalAge.setMaxWidth(45);
		TextField greaterAge = new TextField();
		greaterAge.setPromptText("   >");
		greaterAge.setMaxWidth(45);
		ages.getChildren().addAll(lessAge, equalAge, greaterAge );

		HBox hireDate = new HBox(5);
		TextField monthHire = new TextField();
		monthHire.setPromptText("mm");
		monthHire.setMaxWidth(45);
		TextField dayHire = new TextField();
		dayHire.setPromptText("dd");
		dayHire.setMaxWidth(45);
		TextField yearHire = new TextField();
		yearHire.setPromptText("yyyy");
		yearHire.setMaxWidth(50);
		hireDate.getChildren().addAll(monthHire, fowardSlash1, dayHire, fowardSlash2, yearHire );


		HBox birthDate = new HBox(5);
		TextField monthBirth = new TextField();
		monthBirth.setPromptText("mm");
		monthBirth.setMaxWidth(45);
		TextField dayBirth = new TextField();
		dayBirth.setPromptText("dd");
		dayBirth.setMaxWidth(45);
		TextField yearBirth = new TextField();
		yearBirth.setPromptText("yyyy");
		yearBirth.setMaxWidth(50);
		birthDate.getChildren().addAll(monthBirth, fowardSlash3, dayBirth, fowardSlash4, yearBirth );

		HBox button = new HBox(8);
		Button sameName = new Button("Same Name");
		sameName.getStyleClass().add("loginButton");
		Button searchButton = new Button("Search");
		searchButton.getStyleClass().add("loginButton");	
		button.getChildren().addAll(sameName,searchButton);
		button.getStyleClass().add("fieldButtons");

		labels.getChildren().addAll(fieldName,name,hire,birth,sex,status, payType,age,salary,years);
		for(Node n: labels.getChildren()) n.getStyleClass().add("fieldLabelText");
		textFields.getChildren().addAll(iDIn, lastNameSearch, hireDate, birthDate, sexSearch, statusSearch, payTypeSearch, ages, annualSalarySearch,yearsSearch, button);
		fields.getChildren().addAll(labels, textFields);
		search.getChildren().addAll(message2,fields);

		content.setCenter(search);
		
		sameName.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) {
					try {
						clicked = true;
						int row = 0;
						VBox result = new VBox(15);
						result.setAlignment(Pos.CENTER);
						ScrollPane sp = new ScrollPane();
						sp.setPrefHeight(200);
						sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
						Label resultLabel = new Label("Search Results");
						resultLabel.getStyleClass().add("results");
						Button okBtn = new Button("Ok");
						okBtn.getStyleClass().add("loginButton");
						GridPane list = new GridPane();
						ResultSet rs = database.searchEmployee(clicked,iDIn.getText(), lastNameSearch.getText(), yearHire.getText() + "/" + monthHire.getText() + "/" + 
								dayHire.getText(), yearBirth.getText() + "/" + monthBirth.getText() + "/" + dayBirth.getText(), sexSearch.getText(), statusSearch.getText(), payTypeSearch.getText(), lessAge.getText(),equalAge.getText(),greaterAge.getText(),
								lessSalary.getText(), equalSalary.getText(), greaterSalary.getText(), lessYears.getText(), equalYears.getText(), greaterYears.getText());
						while (rs.next()){
							list.addRow(row, new Label (rs.getString(1)),new Label (rs.getString(2)),new Label (rs.getString(3)),new Label (rs.getString(4)),new Label (rs.getString(5)),new Label (rs.getString(6)),new Label (rs.getString(7)),new Label (rs.getString(8)),new Label (rs.getString(9)));
							row++;
						}

						row = 0;

						for(Node l: list.getChildren()){
							l.getStyleClass().add("list");
							if(row == 0){
								((Label) l).setPrefSize(54, 10);	
							}
							else if(row == 4 || row == 5 || row == 6 || row == 8 ){
								((Label) l).setPrefSize(40, 10);	
							}
							else{
								((Label) l).setPrefSize(90, 10);
							}

							if (row == 8){
								row = 0;
							}
							else{
								row++;
							}
						}


						iDIn.clear();
						lastNameSearch.clear();
						yearHire.clear();
						monthHire.clear();
						dayHire.clear();
						yearBirth.clear();
						monthBirth.clear();
						dayBirth.clear();
						sexSearch.clear();
						statusSearch.clear();
						payTypeSearch.clear();
						lessAge.clear();
						equalAge.clear();
						greaterAge.clear();
						lessSalary.clear();
						equalSalary.clear();
						greaterSalary.clear(); 
						lessYears.clear();
						equalYears.clear();
						greaterYears.clear();


						okBtn.setOnAction(new EventHandler<ActionEvent>() {
							@Override 
							public void handle(ActionEvent e) {
								content.setCenter(search);
							}
						});


						sp.setContent(list);
						result.getChildren().addAll(resultLabel, sp,okBtn);
						content.setCenter(result);
						clicked = false;

					}

					catch (SQLException e1) {
						e1.printStackTrace();
					}
			}
		});
		
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) {
				try {
					int row = 0;
					VBox result = new VBox(15);
					result.setAlignment(Pos.CENTER);
					ScrollPane sp = new ScrollPane();
					sp.setPrefHeight(200);
					sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
					Label resultLabel = new Label("Search Results");
					resultLabel.getStyleClass().add("results");
					Button okBtn = new Button("Ok");
					okBtn.getStyleClass().add("loginButton");
					GridPane list = new GridPane();
					ResultSet rs = database.searchEmployee(clicked,iDIn.getText(), lastNameSearch.getText(), yearHire.getText() + "/" + monthHire.getText() + "/" + 
							dayHire.getText(), yearBirth.getText() + "/" + monthBirth.getText() + "/" + dayBirth.getText(), sexSearch.getText(), statusSearch.getText(), payTypeSearch.getText(), lessAge.getText(),equalAge.getText(),greaterAge.getText(),
							lessSalary.getText(), equalSalary.getText(), greaterSalary.getText(), lessYears.getText(), equalYears.getText(), greaterYears.getText());
					while (rs.next()){
						list.addRow(row, new Label (rs.getString(1)),new Label (rs.getString(2)),new Label (rs.getString(3)),new Label (rs.getString(4)),new Label (rs.getString(5)),new Label (rs.getString(6)),new Label (rs.getString(7)),new Label (rs.getString(8)),new Label (rs.getString(9)));
						row++;
					}

					row = 0;

					for(Node l: list.getChildren()){
						l.getStyleClass().add("list");
						if(row == 0){
							((Label) l).setPrefSize(54, 10);	
						}
						else if(row == 4 || row == 5 || row == 6 || row == 8 ){
							((Label) l).setPrefSize(40, 10);	
						}
						else{
							((Label) l).setPrefSize(90, 10);
						}

						if (row == 8){
							row = 0;
						}
						else{
							row++;
						}
					}


					iDIn.clear();
					lastNameSearch.clear();
					yearHire.clear();
					monthHire.clear();
					dayHire.clear();
					yearBirth.clear();
					monthBirth.clear();
					dayBirth.clear();
					sexSearch.clear();
					statusSearch.clear();
					payTypeSearch.clear();
					lessAge.clear();
					equalAge.clear();
					greaterAge.clear();
					lessSalary.clear();
					equalSalary.clear();
					greaterSalary.clear(); 
					lessYears.clear();
					equalYears.clear();
					greaterYears.clear();


					okBtn.setOnAction(new EventHandler<ActionEvent>() {
						@Override 
						public void handle(ActionEvent e) {
							content.setCenter(search);
						}
					});


					sp.setContent(list);
					result.getChildren().addAll(resultLabel, sp,okBtn);
					content.setCenter(result);
					clicked = false;

				}

				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		return content;
	}

	public VBox showEmployeeList() throws SQLException{
		int row = 0;

		VBox content = new VBox(15);
		content.setAlignment(Pos.CENTER);
		Label listLabel = new Label("Employee List");
		listLabel.getStyleClass().add("results");
		ScrollPane sp = new ScrollPane();
		GridPane list = new GridPane();
		list.setAlignment(Pos.CENTER);
		list.setPrefWidth(620);
		ResultSet rs = database.getList();	
		while (rs.next()){
			list.addRow(row, new Label (rs.getString(1)),new Label (rs.getString(2)),new Label (rs.getString(3)),new Label (rs.getString(4)),new Label (rs.getString(5)),new Label (rs.getString(6)),new Label (rs.getString(7)),new Label (rs.getString(8)),new Label (rs.getString(9)));
			row++;
		}

		row = 0;

		for(Node l: list.getChildren()){
			l.getStyleClass().add("list");
			if(row == 0){
				((Label) l).setPrefSize(54, 10);	
			}
			else if(row == 4 || row == 5 || row == 6 || row == 8 ){
				((Label) l).setPrefSize(40, 10);	
			}
			else{
				((Label) l).setPrefSize(90, 10);
			}

			if (row == 8){
				row = 0;
			}
			else{
				row++;
			}
		}
		sp.setContent(list);
		content.getChildren().addAll(listLabel,sp);
		return content;
	}


	public MenuBar menuBar() {
		MenuBar menuBar = new MenuBar();
		menuBar.getStyleClass().add("menu_bar");
		Menu fileMenu = new Menu("File");
		MenuItem importCSV = new MenuItem("Import CSV");
		importCSV.setOnAction(e -> {
			try {
				database.addCSVFile();
				employeeList.setContent(showEmployeeList());
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "File has already been imported.", null, JOptionPane.ERROR_MESSAGE);
			}
		});
		
		MenuItem createTable = new MenuItem("Create Table");
		createTable.setOnAction(e -> {
			try {
				database.createTable();
				employeeList.setContent(showEmployeeList());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		MenuItem dropTable = new MenuItem("Drop Table");
		dropTable.setOnAction(e -> {
			try {
				database.dropTable();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});


		MenuItem quitItem = new MenuItem("Quit");
		quitItem.setOnAction(e -> {
			System.exit(0);
		});

		quitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q,KeyCombination.CONTROL_DOWN));

		fileMenu.getItems().addAll(importCSV, createTable,dropTable, quitItem);
		menuBar.getMenus().add(fileMenu);
		return menuBar;
	}

}

