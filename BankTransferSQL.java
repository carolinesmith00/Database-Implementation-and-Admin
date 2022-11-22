import java.sql.*;
import java.util.Scanner;
import java.io.*;
 
public class Assignment1 {
 
   /**
    * @param args the command line arguments
    */
 
   public static void main(String[] args) throws SQLException, IOException {
       try {
           Class.forName("oracle.jdbc.driver.OracleDriver"); //Load the Oracle JDBC driver
       }
       catch(ClassNotFoundException e) {
           System.out.println("Could not load the driver.");
       }
       
       try {
           String Oracleuser, Oraclepass;
           Oracleuser = readEntry("Oracle Username: ");
           Oraclepass = readEntry("Oracle Password: ");
           String url = "jdbc:oracle:thin:@10.251.216.48:1521:xe";
           Connection conn = DriverManager.getConnection(url, Oracleuser, Oraclepass); //Connect to Oracle
          
           ///////////////////////////////////////////////////////////////////////////////////
           //	I commented out previous insert statements and added a couple of extra		//
           //	account numbers with balances for testing purposes. Each time the program	//
           //	runs, it resets the accounts back to the amounts from the schema in BB. 	//
           //	I was unsure if you wanted it to be done that way or if you wanted a		//
           //	running balance after multiple transactions. I also set the table to be		//
           //	"bankaccount1" because "bankaccount" was not cooperating for some reason.	//
           ///////////////////////////////////////////////////////////////////////////////////
           
                       Statement st = conn.createStatement();
                       //st.executeUpdate("delete from bankaccount1");
                       //st.executeUpdate("insert into bankaccount1 values('111','1000')");
                       //st.executeUpdate("insert into bankaccount1 values('222','2000')");
                       //st.executeUpdate("insert into bankaccount1 values('333','3000')");
                       //st.executeUpdate("insert into bankaccount1 values('444','4000')");
                       st.executeUpdate("UPDATE bankaccount1 SET balance = '1000' WHERE accnum = '111'");
                       st.executeUpdate("UPDATE bankaccount1 SET balance = '2000' WHERE accnum = '222'");
                       st.executeUpdate("UPDATE bankaccount1 SET balance = '3000' WHERE accnum = '333'");
                       st.executeUpdate("UPDATE bankaccount1 SET balance = '4000' WHERE accnum = '444'");
                      
 
                       ResultSet rs=st.executeQuery("select * from bankaccount1"); //Show all fields of bankaccount1 table
                       String accnum="";
                       String balance="";
                       while (rs.next())   {
                          accnum=rs.getString(1);  //First column of table
                          balance=rs.getString(2); //Second column of table
                          System.out.println("Account Number: " + accnum + "   Balance: " + balance);                          
                       }
                      
                       //Scanner to read user input for account to transfer from, balance to transfer, and account to receive transfer
                       try {
                    	   Scanner sc = new Scanner(System.in);
                       
                    	   System.out.println("Enter account to transfer from: ");
                    	   int initialAcc = sc.nextInt();
                    	   System.out.println("Enter amount: ");
                    	   int bal = sc.nextInt();
                    	   System.out.println("Enter account to transfer to: ");
                    	   int newAcc = sc.nextInt();
                      
                    	   //Update both accounts with new balance after $ transfer
                    	   st.executeUpdate("UPDATE bankaccount1 SET balance = balance + '" + bal + "' WHERE accnum = " + newAcc);
                    	   st.executeUpdate("UPDATE bankaccount1 SET balance = balance - '" + bal + "' WHERE accnum = " + initialAcc);
                    	   conn.commit();	//Commit
                    	   System.out.println("Transaction successful.");
                    	   
                    	   sc.close(); //Close scanner
                       }
                       
                       //Catch exception & rollback
                    	catch(SQLException e) {
                    		System.out.println("Transaction not successful.");
                    		conn.rollback();
                    	}
                       //Close result set, statement, & connection
                       rs.close();
                       st.close();
                       conn.close();                        
       		}
      
       catch(Exception e)  {
           System.err.println(e.getMessage());        
       }
   }
  
   //Utility function to read a line from standard input - get Username and Password to connect to server
  static String readEntry(String prompt)   {
      try  {       
        StringBuffer buffer = new StringBuffer();
        System.out.print(prompt);
        System.out.flush();
        int c = System.in.read();
       
        while (c != '\n' && c != -1)   {
           buffer.append((char)c);
           c = System.in.read();
        }
        return buffer.toString().trim();
     }
     catch(IOException e)  {
        return "";
     }
  }
}
