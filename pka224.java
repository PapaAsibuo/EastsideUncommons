import java.sql.*;
import java.util.*;

/**
 * eastsideuncommons
 */
public class pka224 {

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
            boolean var = true;
            
            try { 
                Connection conn = null;
            do{
            String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";
          
            System.out.println("Username:");
            String username = scnr.nextLine();
            System.out.println("Password:");
            String password = scnr.nextLine();
            
            conn = DriverManager.getConnection(DB_URL, username, password);
            
            if(conn != null){
                System.out.println("Success, Connected to the database");
                break;
            }
            System.out.println("Invalid Credentials. Try again :(");
                } while (true);
                do {

                    printMenu();
                    int choice = validateInt(scnr, 1, 5);

                    switch(choice){
                        //Property Manager
                        case 1:     System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                                    System.out.println("MESSAGE FROM DEVELOPER:\n");
                                    System.out.println("Hi it's Papa! I chose to do the other 3 interfaces first and I'm currently developing this one. Kindly test any of the other 3 profiles instead. Happy Coding :) \n");
                                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                            break;
                        //Tenant --> Pay Rent, Add a roommate/Pet, check Payment Status, update personal data
                        case 2: 
                                String junk = scnr.nextLine();
                                
                                //if found print welcome and their name
                                
                                try {
                                    // ...

                                    System.out.println("Welcome to the Eastside Uncommons Tenant App! Enter your tenant ID (T_ID)");
                                    int tenant_ID = scnr.nextInt(); // Assuming Tenant_ID is an int
                                    scnr.nextLine(); // Consume the newline character

                                    // Prepare the statement
                                    PreparedStatement checkC_ID = conn.prepareStatement(
                                            "SELECT Name FROM Tenant JOIN Person ON Tenant.T_ID = Person.P_ID WHERE Tenant.T_ID = ?"); //get the real person's naem not name on card
                                    checkC_ID.setInt(1, tenant_ID);

                                    // Execute the query
                                    ResultSet rset_Check = checkC_ID.executeQuery();

                                    if (rset_Check.next()) {
                                        int tenantChoice = tenantMenu(rset_Check.getString("Name"));
                                        // System.out.println(T_Option);
                                        ifTenant(tenantChoice, tenant_ID, conn);
                                    } else {
                                        System.out.println("\n\nTenant ID does not exist. Returning to the main menu.");
                                    }

                                    // Close the ResultSet and PreparedStatement
                                    rset_Check.close();
                                    checkC_ID.close();
                                } catch (SQLException e) {
                                    // Handle SQL exceptions
                                    e.printStackTrace();
                                }

                               
                            break;
                        //Company Manager- Add a new property along with information pertaining to it
                        // (location, amenities, apartments, etc.) + data generation
                        
                        case 3:  
                                boolean valid = true;
                                try {
                                    do{
                                //Menu
                                System.out.println("What would you like to do today: ");
                                System.out.println("1. View A Property");
                                System.out.println("2. Add A Property");
                                System.out.println("3. Update A Property");
                                System.out.println("4. Back");
                                int companyManChoice = validateInt(scnr, 1, 4);
                                String trash = scnr.nextLine();
                                //do and try catch

                                switch (companyManChoice) {
                                    case 1: System.out.println("Enter the Property's ID: ");
                                            String pID = scnr.nextLine();

                                            PreparedStatement getPropertyStatement = conn.prepareStatement(
                                                    "SELECT * FROM Property WHERE property_id = ?");

                                            // Set the parameter for the statement
                                            getPropertyStatement.setString(1, pID);

                                            // Execute the query
                                            ResultSet propertyResultSet = getPropertyStatement.executeQuery();

                                            // Check if a property with the given ID exists
                                            if (propertyResultSet.next()) {
                                                // Retrieve data from the ResultSet
                                                String propertysName = propertyResultSet.getString("name");
                                                String propertysAddress = propertyResultSet.getString("address");
                                                int aptAvailable = propertyResultSet.getInt("apt_available");
                                                int aptsTotal = propertyResultSet.getInt("apt_total");


                                                System.out.println("Property found!");
                                                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

                                                // Display the property information
                                                System.out.println("Property ID: " + pID);
                                                System.out.println("Property Name: " + propertysName);
                                                System.out.println("Property Address: " + propertysAddress);
                                                System.out.println("Available Apartments: " + aptAvailable);
                                                System.out.println("Total Apartments: " + aptsTotal + "\n");
                                                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                                            } else {
                                                System.out.println("Property with ID " + pID + " not found.");
                                            }

                                            // Close the ResultSet and PreparedStatement
                                            propertyResultSet.close();
                                            getPropertyStatement.close();

                                        
                                        break;
                                    case 2:
                                            //Add a property
                                            System.out.println("Enter Property ID (max 7 characters): ");
                                            String propertyId = scnr.nextLine(); //if id between 1-30 must print this prop id is already taken

                                            System.out.println("Enter Property Name: ");
                                            String propertyName = scnr.nextLine();

                                            System.out.println("Enter Property Street Address: ");
                                            String propertyAddress = scnr.nextLine();

                                            System.out.println("Enter # of Available Apartments: ");
                                            int availableApartments = validateInt(scnr, 0, 80000);
                                            trash = scnr.nextLine();

                                            System.out.println("Enter Total # of Apartments: ");
                                            int totalApartments = validateInt(scnr, availableApartments, 90000);
                                            trash = scnr.nextLine();

                                            PreparedStatement insertPropertyStatement = conn.prepareStatement(
                                                    "INSERT INTO Property (property_id, name, address, apt_available, apt_total) "
                                                            +
                                                            "VALUES (?, ?, ?, ?, ?)");

                                            // Set parameters for the statement
                                            insertPropertyStatement.setString(1, propertyId);
                                            insertPropertyStatement.setString(2, propertyName);
                                            insertPropertyStatement.setString(3, propertyAddress);
                                            insertPropertyStatement.setInt(4, availableApartments);
                                            insertPropertyStatement.setInt(5, totalApartments);

                                            // Execute the INSERT statement
                                            insertPropertyStatement.executeUpdate();

                                            // Close the PreparedStatement
                                            insertPropertyStatement.close();

                                            System.out.println("Property " + propertyName + " created successfully!");
                                        
                                        break;
                                    case 3: //Update a property
                                    System.out.println("Enter Property ID (max 7 characters): ");
                                            String propertyId1 = scnr.nextLine(); //if id between 1-30 must print this prop id is already taken
                                            PreparedStatement checkPropertyStatement = conn.prepareStatement(
                                                "SELECT * FROM Property WHERE property_id = ?");
                                        
                                        // Set the parameter for the statement
                                        checkPropertyStatement.setString(1, propertyId1);
                                        
                                        // Execute the query
                                        ResultSet checkPropertyResultSet = checkPropertyStatement.executeQuery();
                                        
                                        if (checkPropertyResultSet.next()) {
                                            // Property exists, proceed with updating information
                                            System.out.println("Enter Property Name: ");
                                            String propertyName1 = scnr.nextLine();

                                            System.out.println("Enter Property Street Address: ");
                                            String propertyAddress1 = scnr.nextLine();

                                            System.out.println("Enter # of Available Apartments: ");
                                            int availableApartments1 = validateInt(scnr, 0, 80000);
                                            trash = scnr.nextLine();

                                            System.out.println("Enter Total # of Apartments: ");
                                            int totalApartments1 = validateInt(scnr, availableApartments1, 90000);
                                            trash = scnr.nextLine();

                                            // Prepare the UPDATE statement
                                            PreparedStatement updatePropertyStatement = conn.prepareStatement(
                                                    "UPDATE Property SET name = ?, address = ?, apt_available = ?, apt_total = ? WHERE property_id = ?");

                                            // Set parameters for the statement
                                            updatePropertyStatement.setString(1, propertyName1);
                                            updatePropertyStatement.setString(2, propertyAddress1);
                                            updatePropertyStatement.setInt(3, availableApartments1);
                                            updatePropertyStatement.setInt(4, totalApartments1);
                                            updatePropertyStatement.setString(5, propertyId1);

                                            // Execute the UPDATE statement
                                            updatePropertyStatement.executeUpdate();

                                            // Close the PreparedStatement
                                            updatePropertyStatement.close();

                                            System.out.println("Property with ID " + propertyId1 + " updated successfully!");
                                            System.out.println(" Choose Option 1 next to view the Property...");

                                            } else {
                                            // Property with the given ID does not exist
                                            System.out.println("Property with ID " + propertyId1 + " does not exist.");
                                        }

                                        // Close the ResultSet and PreparedStatement
                                        checkPropertyResultSet.close();
                                        checkPropertyStatement.close();
                                                                                
                                        
                                        break;
                                    case 4: System.out.println("Back to main menu...");
                                            valid = false;
                                        
                                        break;
                                
                                    default:
                                        break;
                                }
                            }while(valid);
                            } catch (SQLException e) {
                            e.printStackTrace();
                        }





                            break;
                        //Financial Manager
                        case 4:try {
                                boolean boolFlag = true;
                                do{
                                    System.out.println("Welcome to the Eastside Uncommons Financial Manager App!");
                                    System.out.println("What would you like to view data for: ");
                                    System.out.println("1. A property\n2.The Entire Eastside Uncommons Enterprise\n3.Back");
                                    int finManChoice = validateInt(scnr, 1, 3);

                                    switch (finManChoice) {
                                        case 1: System.out.println("Enter Property ID (property_id): ");
                                                int propID = validateInt(scnr, 1, 50); //max is 30 rn
                                                int totalRev = 0;

                                                //total Revenue
                                                PreparedStatement propertyPreparedStatement = conn.prepareStatement(
                                                        "select sum(apartment.rent) from Apartment where apartment_number in (select apartment_number from apartment where Property_id = ?)");
                                                propertyPreparedStatement.setInt(1, propID);
                                                ResultSet rs = propertyPreparedStatement.executeQuery();
                                                if (rs.next()) {
                                                    totalRev = 12 * rs.getInt(1);
                                                    
                                                   // System.out.println( "Total revenue from rent this year: " + rs.getInt(1));
                                                }
                                                System.out.println( "Total revenue from rent this year: $" + totalRev + " \n");

                                                //number of active leases
                                                PreparedStatement activeLeasesStatement = conn.prepareStatement(
                                                        "select count(*) from Lease where Property_id = ?");
                                                activeLeasesStatement.setInt(1, propID);
                                                ResultSet rs1 = activeLeasesStatement.executeQuery();
                                                if (rs1.next()) {
                                                    
                                                   System.out.println( "Number of active leases at this property: " + rs1.getInt(1) + "\n");

                                                }
                                                PreparedStatement totalAptInfo = conn.prepareStatement(
                                                        "SELECT apartment_number, SUM(rent) AS total_rent " +
                                                                "FROM Apartment " +
                                                                "WHERE Property_id = ? " +
                                                                "GROUP BY apartment_number");
                                                totalAptInfo.setInt(1, propID);
                                                ResultSet rsTotApt = totalAptInfo.executeQuery();

                                                while (rsTotApt.next()) {
                                                    int apartmentNumber = rsTotApt.getInt("apartment_number");
                                                    int totalRent = rsTotApt.getInt("total_rent");

                                                    // Display the results for each apartment
                                                    System.out.println("Apartment Number: " + apartmentNumber);
                                                    System.out.println("Total Revenue from Apartment" + apartmentNumber + " this year: $"
                                                            + (12 * totalRent) + "\n");
                                                }

                                                // Close the ResultSet and PreparedStatement
                                                rsTotApt.close();
                                                totalAptInfo.close();



                                            break;
                                        case 2: System.out.println("Printing data for entire Enterprise .......");
                                                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                                                PreparedStatement totalPropInfo = conn.prepareStatement(
                                                        "SELECT p.property_id, p.name, COALESCE(SUM(a.rent), 0) AS total_rent "
                                                                +
                                                                "FROM Property p " +
                                                                "LEFT JOIN Apartment a ON p.property_id = a.property_id "
                                                                +
                                                                "GROUP BY p.property_id, p.name");

                                                ResultSet rsTotProp = totalPropInfo.executeQuery();

                                                while (rsTotProp.next()) {
                                                    String propertyID = rsTotProp.getString("property_id");
                                                    String propertyName = rsTotProp.getString("name");
                                                    int totalRent = rsTotProp.getInt("total_rent");

                                                    // Display the results for each property
                                                    System.out.print("Property ID: " + propertyID + "\t");
                                                    System.out.print("Property Name: " + propertyName + "\t");
                                                    System.out.println("Total Revenue from this Property this year: $"
                                                            + (12 * totalRent) + "\n");
                                                }

                                                // Close the ResultSet and PreparedStatement
                                                rsTotProp.close();
                                                totalPropInfo.close();

                                                //tenant count, apartment count, 
                                        break;
                                        case 3: boolFlag = false;
                                        break;
                                        default: System.out.println("Invalid option");
                                            break;
                                    }
                                }while(boolFlag);
                                } catch (SQLException e) {
                                e.printStackTrace();
                            }
                                //1. Enter Property id you'd like to view reports for
                                //2. All
                                
                            break;
                        //QUIT
                        case 5: System.out.println("Exiting...");
                                var = false;
                            break;
                        default: System.out.println("Invalid input, Enter a number between 1 and 5: ");

                    }
                    
                } while (var);
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error" + e.getErrorCode());
            }
    }
    //Menus
    public static void printMenu() {
        System.out.println("\n\n\nWelcome to Eastside Uncommons!\nChoose your profile:");
        System.out.println("\t1. Property Manager");
        System.out.println("\t2. Tenant");
        System.out.println("\t3. Company Manager"); //next
        System.out.println("\t4. Financial Manager");//last? read only
        System.out.println("\t5. QUIT");

    }
     //Pay Rent, Add a roommate/Pet, check Payment Status, update personal data
    public static int tenantMenu(String name) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Welcome back " + name + ", " + " \nWhat would you like to do today?");
        System.out.println("\t1. Pay Rent");
        System.out.println("\t2. Check Payment Status");
        System.out.println("\t3. Update Personal data");
        System.out.println("\t4. Add a roommate");
        System.out.println("\t5. Back");
        int option = validateInt(scnr, 1, 5);
        return option;
        
    }
    
    public static void ifTenant(int tenantChoice, int id, Connection conn){
        System.out.println("You chose option" + " #"+ tenantChoice);
        Scanner scnr = new Scanner(System.in);
        boolean flag = true;
        try {
            do {

                switch (tenantChoice) {
                    case 1: //Pay Rent--> 
                            int toPay = calculateMonthsLate(id); //num of months to pay
                            int rent = 0; //query for tenant's lease ids apartments rent via nat join
                            // Prepare the statement
                                    PreparedStatement getRent = conn.prepareStatement(
                                            "SELECT Apartment.rent FROM Apartment JOIN Lease ON Apartment.apartment_number = Lease.apartment_number AND Apartment.property_id = Lease.property_id JOIN Tenant ON Lease.lease_id = Tenant.lease_id WHERE Tenant.T_ID = ?"); //get rent val for tenant's apartment
                                    getRent.setInt(1, id);

                                    // Execute the query
                                    ResultSet res = getRent.executeQuery(); //maybe try assigning it to int

                                    if (res.next()) {
                                        rent = res.getInt("Rent");
                                        System.out.println(rent);
                                        
                                    } else {
                                        System.out.println("\n\nTenant ID does not exist. Returning to the main menu.");
                                    }

                                    // Close the ResultSet and PreparedStatement
                                    res.close();
                                    getRent.close();

                            int lateFee = rent/12 * toPay;
                            int total = (rent * toPay) + lateFee;
                            int paymentAmt = 0;

                            if (toPay > 0) {
                                
                                System.out.println("You owe $" + rent * toPay + " plus a late fee of $"+ lateFee + " this totals $" + total + "\n Make a payment NOW to avoid further fees and possible eviction\n" );
                                System.out.println("How much would you like to pay today? (Max transaction amount for 1 day is $25,000): ");
                                paymentAmt = validateInt(scnr, 50, 25000);
                                //query to create payment of paymentAmt on t_id's lease_id
                                System.out.println("A total of $" + paymentAmt + " will be processed from your bank account on file. Warning: Declined payments accrue an interest fee of 0.3% of the payment amount");
                                System.out.println("\nThank you for your payment!");
                                
                            } else if (toPay == 0) {
                                System.out.println("You are up to date with payments. No need to make a payment");
                                break;
                            } else {
                                System.out.println("You are " + (-toPay) + " month(s) ahead of rent payments. No need to make a payment");
                                break;
                            }
                            //getting lease id
                            // Prepare the statement
                            int leaseId = 0;
                            PreparedStatement getLeaseIdStatement = conn.prepareStatement(
                                    "SELECT lease_id FROM Tenant WHERE t_id = ?");
                            getLeaseIdStatement.setInt(1, id);

                            // Execute the query
                            ResultSet leaseIdResult = getLeaseIdStatement.executeQuery();

                            // Check if a result is found
                            if (leaseIdResult.next()) {
                                 leaseId = leaseIdResult.getInt("lease_id");
                                
                            } else {
                                System.out.println("No lease_id found for tenant with t_id " + id);
                            }

                            // Close the ResultSet and PreparedStatement
                            leaseIdResult.close();
                            getLeaseIdStatement.close();



                            int nextPaymentId = 1;

                            PreparedStatement maxPaymentIdStatement = conn
                                    .prepareStatement("SELECT MAX(payment_id) AS max_payment_id FROM Payment");
                            ResultSet maxPaymentIdResult = maxPaymentIdStatement.executeQuery();

                            if (maxPaymentIdResult.next()) {
                                nextPaymentId = maxPaymentIdResult.getInt("max_payment_id") + 1;
                            }

                            // Close the ResultSet and PreparedStatement
                            maxPaymentIdResult.close();
                            maxPaymentIdStatement.close();

                            // Create a new payment for Monthly Rent
                            String paymentType = "Monthly Rent";

                            PreparedStatement createPaymentStatement = conn.prepareStatement(
                                    "INSERT INTO Payment (payment_id, amount, type, lease_id) VALUES (?, ?, ?, ?)");
                            createPaymentStatement.setInt(1, nextPaymentId);
                            createPaymentStatement.setInt(2, paymentAmt); // Assuming paymentAmt is the amount to be
                                                                          // paid
                            createPaymentStatement.setString(3, paymentType);
                            createPaymentStatement.setInt(4, leaseId); // Getting leaseId from tenant

                            // Execute the INSERT statement
                            createPaymentStatement.executeUpdate();

                            // Close the PreparedStatement
                            createPaymentStatement.close();

                            // Display a confirmation message
                            System.out.println(
                                    "\nPayment for Monthly Rent created successfully with payment_id: " + nextPaymentId);




                       
                        
                        break;
                    case 2: //Check Payment Status
                            //if id mod 2 = 0 --> set monthsLate = 0  month, if id mod 3 =0, monthsLate = 1  , if id mod 2 = 0 && id mod 10 = 0,  monthsLate = 2, if id mod 2 = 0 && id mod 10 = 0 && id mod 5 = 0, monthsLate = -3. If monthsLate > 0 print how many months of rent they owe (monthsLate), if monthsLate = 0, print that they're up to date with payments and if monthsLate < 0 print how many months they're ahead of rent by
                            // have to pay rent (get rent w query on lease_id) * monthsLate + lateFee rent/12
                            int monthsLate = calculateMonthsLate(id);

                            if (monthsLate > 0) {
                            System.out.println("You are " + monthsLate + " month(s) late on rent payments.");
                            } else if (monthsLate == 0) {
                            System.out.println("You are up to date with payments.");
                            } else {
                            System.out.println("You are " + (-monthsLate) + " month(s) ahead of rent payments.");
                            }
                        

                        break;
                    case 3: // Update Personal Data --> phone_num, email, Tenant tbl Bank acount- name_on_card, account_num
                            System.out.println("What would you like to update?");
                            System.out.println("\t1. Phone Number");
                            System.out.println("\t2. Email");
                            System.out.println("\t3. Bank Account");
                            
                            String dataChange = scnr.nextLine();
                                switch (dataChange) {
                                    case "1": 
                                        System.out.println("Enter new phone number (XXX-XXX-XXXX): ");
                                        String number = scnr.nextLine();
                                        PreparedStatement phone = conn
                                                .prepareStatement("update person set phone_num = ? where P_ID = ?");
                                        phone.setString(1, number);
                                        phone.setInt(2, id);
                                        phone.executeUpdate();
                                        System.out.println("Successful");
                                        //51425 - 771-904-7456
                                        break;
                                    case "2": 
                                            System.out.println("Enter new email: ");
                                        String email = scnr.nextLine();
                                        PreparedStatement emailStatement = conn
                                                .prepareStatement("update person set email = ? where P_ID = ?");
                                        emailStatement.setString(1, email);
                                        emailStatement.setInt(2, id);
                                        emailStatement.executeUpdate();
                                        System.out.println("Successful");
                                        //51425 - agober@icio.us
                                        break;

                                    case "3": 
                                            
                                            System.out.println("Enter new Name on card");
                                        String name_on_card = scnr.nextLine();
                                            PreparedStatement bankAcctName = conn
                                                .prepareStatement("update tenant set name_on_card = ? where T_ID = ?");
                                        bankAcctName.setString(1, name_on_card);
                                        bankAcctName.setInt(2, id);
                                        System.out.println("Enter new account number (XXXXXXXX): ");
                                        int acctNum = validateInt(scnr, 0, 1000000000);
                                        PreparedStatement bankAcctNum = conn
                                                .prepareStatement("update tenant set account_num = ? where T_ID = ?");
                                        bankAcctNum.setInt(1, acctNum);
                                        bankAcctNum.setInt(2, id);
                                        bankAcctName.executeUpdate();
                                        bankAcctNum.executeUpdate();
                                        System.out.println("Successful");

                                        break;
                            
                                
                                    default: System.out.println("Invalid option selected");
                                            
                                        break;
                                }
                        
                        break;
                    case 4: // Add a roommate
                                //get roommates num from prep, store in var, increment var and then push back
                                int numRoomies = 0;
                                //fetch roomie data
                                PreparedStatement roommateStatement = conn.prepareStatement(
                                            "SELECT roommates FROM Tenant"); //get num rooommates
                                    

                                    // Execute the query
                                    ResultSet rset = roommateStatement.executeQuery();

                                   if (rset.next()) {
                                        
                                        numRoomies = rset.getInt("roommates");
                                        
                                        
                                        
                                        
                                    } 
                                    System.out.println("Current # of roommates: " + numRoomies);
                                    

                                    // Close the ResultSet 
                                    rset.close();
                                    
                                System.out.println("Enter rooommates Name");
                                        String roommateInfo = scnr.nextLine();
                                        System.out.println("Enter roommate credit score, credit score must be above 685 to rent at Eastside Uncommons");
                                        int roomieScore = validateInt(scnr, 685, 800);
                                        int injectNum = ++numRoomies;
                                        
                                            PreparedStatement roomieStatement = conn
                                                .prepareStatement("update tenant set roommates = ? where T_ID = ?");
                                        roomieStatement.setInt(1, injectNum);
                                        roomieStatement.setInt(2, id);
                                        System.out.println("Roommate " + roommateInfo + " successfully added!");
                                        System.out.println("New # of roommates: " + injectNum);
                        

                        break;
                    case 5: System.out.println("Back to Main Menu....");
                            flag = false;
                            break;
                    default: System.out.println("Error: Enter a number between 1 and 5");
                    flag = false;
                    break;
                    
                }
                if (flag) {
                            System.out.println("\t1. Pay Rent");
                            System.out.println("\t2. Check Payment Status");
                            System.out.println("\t3. Update Personal data");
                            System.out.println("\t4. Add a roommate");
                            System.out.println("\t5. Back");
                            tenantChoice = validateInt(scnr, 1, 5);
                }

            } while (flag);
        } catch (SQLException e) {
            System.out.println("Error Occured, try again");
            e.printStackTrace();
            flag = false;
        }
        

    }


    private static int calculateMonthsLate(int id) {

        int monthsLate = 0;

        if (id % 2 == 0) {
            // id mod 2 = 0 --> set monthsLate = 0 month
            monthsLate = 0;
        }

        if (id % 3 == 0) {
            // id mod 3 = 0, monthsLate = 1
            monthsLate = 1;
        }

        if (id % 2 == 0 && id % 10 == 0) {
            // id mod 2 = 0 && id mod 10 = 0, monthsLate = 2
            monthsLate = 2;
        } if(id % 5 == 0){
            monthsLate = 4;
        }

        if (id % 2 == 0 && id % 10 == 0 && id % 5 == 0) {
            // id mod 2 = 0 && id mod 10 = 0 && id mod 5 = 0, monthsLate = -3
            monthsLate = -3;
        }
        return monthsLate;
    }
    public static void payRent(Connection conn, String Lease_ID ){

    }

    public static int validateInt(Scanner scnr, int min, int max){
        if (scnr.hasNextInt()) {
            int val = scnr.nextInt();
            if (val >= min && val <= max) {

                return val;
            }
        }
        do {
            System.out.println("Invalid input, Enter a number between " + min + "-" + max);
            String junk = scnr.nextLine();
            if (scnr.hasNextInt()) {
                int val = scnr.nextInt();
                if (val >= min && val <= max) {
                    return val;
                }
            }
        } while (true);
    }

    

    





}