package source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class payrollDatabase {

	private Statement stmt;
	private boolean connected = true;

	public payrollDatabase(String passwordIn, String usernameIn){
		try {

			String url = "jdbc:mysql://localhost/cs245s7";
			String username = usernameIn;
			String password = passwordIn;
			Connection connection = null;


			System.out.println("Connecting database...");
			connection = DriverManager.getConnection(url,username,password);
			System.out.println("Database connected!");



			stmt = connection.createStatement();


			try{
				String table = "CREATE TABLE Employee(EmployeeID integer not null primary key AUTO_INCREMENT, LastName varchar(20), HireDate date, Birthdate date, Sex varchar(2), Status varchar(4), PayType varchar(4), AnnualSalary double, YearsOfService integer)";
				stmt.executeUpdate(table);
				System.out.println("Table creation process successfully!");
			}
			catch(SQLException s){
				System.out.println("Table already exists!");
			}
		}
		catch (Exception e){
			e.printStackTrace();
			setConnected(false);
		}
	}

	public void addCSVFile() throws SQLException{
		stmt.executeQuery("LOAD DATA LOCAL INFILE 'E:/Computer Science/CS245/src/csvFile/employeeFile.csv' INTO TABLE employee FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'");
	}
	
	public void createTable() {
		try {
			stmt.executeUpdate("CREATE TABLE Employee(EmployeeID integer not null primary key AUTO_INCREMENT, LastName varchar(20), HireDate date, Birthdate date, Sex varchar(2), Status varchar(4), PayType varchar(4), AnnualSalary double, YearsOfService integer)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void dropTable() {
		try {
			stmt.executeUpdate("DROP TABLE Employee");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void addEmployee(String lastName,String hireDate,String birthDate,String sex,String status,String payType,String annualSalary) throws SQLException{
		String yearsInService = null;
		ResultSet rs = stmt.executeQuery("SELECT year(curdate())-year('" + hireDate + "')");
		if (rs.next()){
			yearsInService = rs.getString(1);
		}
		String insertValues = "INSERT INTO Employee VALUES (" + 0 + ",'" + lastName + "','" + hireDate + "','" + birthDate + "','" + sex + "','" + status + "','" + payType + "','" + annualSalary + "','" + yearsInService + "')"; 
		stmt.executeUpdate(insertValues);
	}

	public void deleteEmployee(String ID) throws SQLException{
		ResultSet rs = stmt.executeQuery("SELECT EmployeeID FROM Employee WHERE EmployeeID =" + ID);
		if (rs.next()){
			stmt.executeUpdate("DELETE FROM employee WHERE EmployeeID =" + rs.getString(1));
		}
		else{
			JOptionPane.showMessageDialog(null, "Employee ID " + ID + " does not exist. Please enter a valid Employee ID.", null, JOptionPane.ERROR_MESSAGE);
		}
	}

	public ResultSet getList() throws SQLException{
		ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");
		return rs;
	}
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean idExists(String iD) throws SQLException {
		boolean idExists = false;;
		ResultSet rs = stmt.executeQuery("SELECT EmployeeID FROM Employee WHERE EmployeeID =" + iD);
		if (rs.next()){
			idExists = true;
		}
		return idExists;
	}

	public void modifyEmployee(String iD, String lastName,String hireDate,String birthDate,String sex,String status,String payType,String annualSalary) throws SQLException {

		String column = null; 
		String[] values = {lastName, hireDate, birthDate, sex, status, payType, annualSalary};

		if (idExists(iD)){

			int i = 0;
			while(i < values.length){
				if(!values[i].equals("") && !values[i].equals("//")){
					switch(i){
					case 0: column = "LastName";
					break;
					case 1: column = "HireDate";
					break;
					case 2: column = "Birthdate";
					break;
					case 3: column = "Sex";
					break;
					case 4: column = "Status";
					break;
					case 5: column = "PayType";
					break;
					case 6: column = "AnnualSalary";
					break;
					}
					try {
						if(i == 1){
							stmt.executeUpdate("UPDATE Employee SET YearsOfService = (SELECT year(curdate())-year('" + hireDate + "')) WHERE EmployeeID =" + iD);
						}
						stmt.executeUpdate("UPDATE Employee SET " + column + "='" + values[i] + "' WHERE EmployeeID =" + iD);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else{
					i++;
					continue;
				}
				i++;
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Employee ID " + iD + " does not exist. Please enter a valid Employee ID.", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public ResultSet searchEmployee(Boolean clicked, String iD, String lastName,String hireDate,String birthDate,String sex,String status,String payType,String lessAge,String equalAge,String greaterAge,
			String lessPay,String equalPay,String greaterPay,String lessYears,String equalYears,String greaterYears) throws SQLException {
		ResultSet rs;
		String column = null; 
		String queryWHERE = "SELECT * FROM Employee WHERE ";
		String queryHAVING = "SELECT * FROM Employee HAVING ";
		String sameName = "SELECT *, COUNT(LastName) FROM Employee GROUP BY LastName HAVING COUNT(*) > 1 ";
		String conditional = " AND ";
		int count = 0;
		int conditionalCount = -1;
		String[] values = {iD,lastName, hireDate, birthDate, sex, status, payType, lessAge, equalAge, greaterAge, lessPay, equalPay, greaterPay, lessYears, equalYears, greaterYears};
		String[] useValues = new String[16];


		for(int i = 0; i < values.length; i++){
			if(!values[i].equals("") && !values[i].equals("//")){
				switch(i){
				case 0: column = "EmployeeID = " + iD;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 1: column = "LastName = '" + lastName + "'";
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 2: column = "HireDate = '" + hireDate + "'";
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 3: column = "Birthdate = '" + birthDate + "'";
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 4: column = "Sex = '" + sex + "'";
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 5: column = "Status = '" + status + "'";
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 6: column = "PayType = '" + payType + "'";
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 7: column = "FLOOR(DATEDIFF(curdate(),Birthdate)/365) < " + lessAge;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 8: column = "FLOOR(DATEDIFF(curdate(),Birthdate)/365) = " + equalAge;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 9: column = "FLOOR(DATEDIFF(curdate(),Birthdate)/365) > " + greaterAge;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 10: column = "AnnualSalary < " + lessPay;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 11: column = "AnnualSalary = " + equalPay;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 12: column = "AnnualSalary > " + greaterPay;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 13: column = "YearsOfService < " + lessYears;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 14: column = "YearsOfService = " + equalYears;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				case 15: column = "YearsOfService > " + greaterYears;
				System.out.println(column);
				conditionalCount++;
				useValues[i] = column;
				break;
				}
			}
		}
		
		if(clicked == true){
			conditionalCount += 1;
		}

		for(int i = 0; i < useValues.length; i++){
			if(useValues[i] != null){
				if(clicked == true){
					if(count < conditionalCount){
						sameName = sameName + conditional + useValues[i];
					}
					else{
						sameName = sameName + useValues[i];
					}
				}
				else{
					if(useValues[7] != null || useValues[8] != null || useValues[9] != null){
						if(count == conditionalCount){
							queryHAVING = queryHAVING + useValues[i];
						}
						else{
							queryHAVING = queryHAVING + useValues[i] + conditional;
						}
					}
					else{
						if(count == conditionalCount){
							queryWHERE = queryWHERE + useValues[i];
						}
						else{
							queryWHERE = queryWHERE + useValues[i] + conditional;
						}
					}
				}
				count++;
			}
		}

		System.out.println(conditionalCount);
		System.out.println(queryWHERE);
		System.out.println(queryHAVING);
		System.out.println(sameName);

		if(queryHAVING.equals("SELECT * FROM Employee HAVING ") && queryWHERE.equals("SELECT * FROM Employee WHERE ")){
			rs = stmt.executeQuery(sameName);
		}
		else if(queryWHERE.equals("SELECT * FROM Employee WHERE ") && sameName.equals("SELECT *, COUNT(LastName) FROM Employee GROUP BY LastName HAVING COUNT(*) > 1 ")){
			rs = stmt.executeQuery(queryHAVING);
		}
		else{
			rs = stmt.executeQuery(queryWHERE);
		}

		return rs;
	}
}




