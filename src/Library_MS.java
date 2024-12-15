import java.util.Scanner;
import java.sql.*;

// Book table related activities of the program
class Book {
    // connection details to connect program with database
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASSWORD = "navindu70R#";

    public static void view_All_Books() {  //Book Viewing method

        String query = "SELECT * FROM books";

        System.out.println();// Add space line for readability
        System.out.println("--The Book Details--");
        System.out.println();// Add space line for readability

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // connet mysql database using above mentioned URL,User name and password
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query))
        {

            System.out.println("Listing all books:");
            while (resultSet.next()) { //read the table data
                int book_id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                int year_published = resultSet.getInt("year_published");

                //Display the table data
                System.out.println("Book ID: " + book_id);
                System.out.println("Title: " + title);
                System.out.println("Author: " + author);
                System.out.println("Publisher: " + publisher);
                System.out.println("Year Published: " + year_published);
                System.out.println(); // Add space line for readability
            }
        } catch (SQLException e) { //Exception Handling
            System.out.println("Error fetching books: " + e.getMessage());
        }
    }

    public static void add_Book(Scanner scanner) {
        System.out.println();// Add space line for readability
        System.out.println("--Add New Book Details--");
        System.out.println();// Add space line for readability

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.print("Enter book title: "); //Display Enter book title
            String title = scanner.nextLine(); //Get String type book title name as a input from user
            System.out.print("Enter author: "); //Display Enter author
            String author = scanner.nextLine(); //Get String type book author name as a input from user
            System.out.print("Enter publisher: "); //Display Enter publisher
            String publisher = scanner.nextLine(); //Get String type book publisher name as a input from user
            System.out.print("Enter year published: "); //Display Enter Year of published
            int year_published = scanner.nextInt(); //Get integer type published year as a input from user
            scanner.nextLine();

            String insertQuery = "INSERT INTO books (title, author, publisher, year_published) VALUES (?, ?, ?, ?)"; //Inserting collected data to database
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, title); //Insert data for title column
            preparedStatement.setString(2, author); //Insert data for author column
            preparedStatement.setString(3, publisher); //Insert data for publisher column
            preparedStatement.setInt(4, year_published); //Insert data for year published column

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) { //After collect four data end the data entering with below message
                System.out.println("\nA new book was inserted successfully!\n");
            }

            // Print all books
            String query_book = "SELECT * FROM books";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query_book);

            while (result.next()) {
                int book_id = result.getInt(1);
                title = result.getString(2);
                author = result.getString(3);
                publisher = result.getString(4);
                year_published = result.getInt(5);

                System.out.println("Book ID: " + book_id);
                System.out.println("Book title: " + title);
                System.out.println("Author of book: " + author);
                System.out.println("Publisher of book: " + publisher);
                System.out.println("Published year: " + year_published);
                System.out.println(); // Print a newline for better readability
            }

            connection.close(); // Close the database connection
            System.out.println("--Book Added Successfully--.");
        } catch (SQLException e) { //Exception handling
            System.out.println("Error: " + e);
        }
    }

    public static void update_Book(Scanner scanner) {
        System.out.println();// Add space line for readability
        System.out.println("--Update Book Details--");
        System.out.println();// Add space line for readability

        System.out.print("Enter the book ID to update: ");
        int book_id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter new book title: "); //Get details for  update the table
        String title = scanner.nextLine();
        System.out.print("Enter new author: ");
        String author = scanner.nextLine();
        System.out.print("Enter new publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter new year published: ");
        int year_published = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        String updateQuery = "UPDATE books SET title = ?, author = ?, publisher = ?, year_published = ? WHERE book_id = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");// Load the MySQL JDBC driver
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery); // Update the new data to the database
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, publisher);
            preparedStatement.setInt(4, year_published);
            preparedStatement.setInt(5, book_id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book details updated successfully");
            } else {
                System.out.println("No book found with the provided ID."); // when user entering wrong ID
            }

            connection.close(); // Close the database connection
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void delete_Book(Scanner scanner) {
        System.out.println();// Add space line for readability
        System.out.println("--Delete the Book--");
        System.out.println();// Add space line for readability
        System.out.print("Enter the book ID to delete: "); // Get the Book ID for delete that row
        int book_id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        String deleteQuery = "DELETE FROM books WHERE book_id = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");// Load the MySQL JDBC driver
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);// Create a PreparedStatement to execute the delete query with the specified book_id
            preparedStatement.setInt(1, book_id);

            int rowsDeleted = preparedStatement.executeUpdate();
            // Check if any rows were deleted
            if (rowsDeleted > 0) {   //when true

                System.out.println("Book with ID " + book_id + " deleted successfully");
            } else { //when false
                System.out.println("No book found with the provided ID.");
            }

            connection.close(); // Close the database connection
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void search_Books(Scanner scanner) {
        System.out.println();// Add space line for readability
        System.out.println("--Search the Book--");
        System.out.println("-------------------");
        System.out.println("Select search option:");  // Provide the user with search options
        System.out.println("1. Search by title");
        System.out.println("2. Search by author");
        System.out.println("3. Search by year published");
        System.out.println();// Add space line for readability
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        String search_command = ""; // Initialize search_command and search_header variables
        String search_header = "";

        switch (choice) { // Getting inputs from user to searching
            case 1:
                search_command = "SELECT * FROM books WHERE title LIKE ?";
                search_header = "title";
                break;
            case 2:
                search_command = "SELECT * FROM books WHERE author LIKE ?";
                search_header = "author";
                break;
            case 3:
                search_command = "SELECT * FROM books WHERE year_published = ?";
                search_header = "year published";
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
                return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL JDBC driver
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(search_command);

            if (choice == 1 || choice == 2) {  //check the entered result correctivity
                System.out.print("Enter the " + search_header + " to search for: ");
                String searchTerm = scanner.nextLine();
                preparedStatement.setString(1, "%" + searchTerm + "%");
            } else if (choice == 3) {
                System.out.print("Enter the " + search_header + " to search for: ");
                int searchTerm = scanner.nextInt();
                preparedStatement.setInt(1, searchTerm);
            }
            System.out.println("\n--This is the Result--\n");

            // Execute the search query and get the result
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) { //print all book's details of similar to search names
                int book_id = result.getInt(1);
                String title = result.getString(2);
                String author = result.getString(3);
                String publisher = result.getString(4);
                int year_published = result.getInt(5);

                System.out.println("Book ID: " + book_id);
                System.out.println("Book title: " + title);
                System.out.println("Author of book: " + author);
                System.out.println("Publisher of book: " + publisher);
                System.out.println("Published year: " + year_published);
                System.out.println(); // Print a newline for better readability
            }

            connection.close();// Close the database connection
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}

//Members table related activities of the program//
class Members{
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASSWORD = "navindu70R#";
    public static void add_New_Member() {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input

        System.out.println("--------------");
        System.out.println("Add New Member\n");

        //Get the member information from user
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        System.out.print("Enter member email: ");
        String email = scanner.nextLine();
        System.out.print("Enter member phone number: ");
        String phone = scanner.nextLine();

        String insertQuery = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
        // SQL query to insert a new member into the members table

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Create a PreparedStatement to execute the insert query
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println();// Add space line for readability
                System.out.println("A new member was added successfully!");
                System.out.println();// Add space line for readability
            }

            connection.close(); // Close the database connection
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }



}

//Loans table related activities of the program//
class Loans{
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASSWORD = "navindu70R#";

    public static void loan_Book() {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input

        System.out.println();// Add space line for readability
        System.out.println("--Book Borrowing--");
        System.out.println();// Add space line for readability

        System.out.print("Enter member ID: ");
        int member_id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter book ID: ");
        int book_id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Get current date for loan_date
        java.sql.Date loanDate = new java.sql.Date(System.currentTimeMillis());

        // Calculate return date (Assume one month loan period)
        java.sql.Date returnDate = new java.sql.Date(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L));

        String insertQuery = "INSERT INTO loans (book_id, member_id, loan_date, return_date) VALUES (?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, book_id);
            preparedStatement.setInt(2, member_id);
            preparedStatement.setDate(3, loanDate);
            preparedStatement.setDate(4, returnDate);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Loan recorded successfully!");
                System.out.println("--------------------------------");
                System.out.println("Loan date: " + loanDate);
                System.out.println("Return date: " + returnDate);
            } else {
                System.out.println("Failed to record loan.");
            }

            connection.close();// Close the database connection
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void return_Book() {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input

        System.out.println();// Add space line for readability
        System.out.println("--Book Returning--");
        System.out.println();// Add space line for readability
        System.out.print("Enter loan ID: ");

        int loan_id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.println("--------------------------------");
        // Get current date for return_date
        java.sql.Date returnDate = new java.sql.Date(System.currentTimeMillis());

        String updateQuery = "UPDATE loans SET return_date = ? WHERE loan_id = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDate(1, returnDate);
            preparedStatement.setInt(2, loan_id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book marked as returned successfully!");
                System.out.println("Return date: " + returnDate);
            } else {
                System.out.println("Failed to mark book as returned.");
            }

            connection.close();// Close the database connection
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}


public class Library_MS {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input

        System.out.println();// Add space line for readability
        System.out.println("---Welcome to Library Management System---");
        System.out.println();// Add space line for readability

        while (true) {
            try {
                System.out.println("Select an option");  // Display the options to the user
                System.out.println("----------------");
                System.out.println("1 - View all books");
                System.out.println("2 - Add new book");
                System.out.println("3 - Update book details");
                System.out.println("4 - Delete a book");
                System.out.println("5 - Searching book");
                System.out.println("6 - Add new member");
                System.out.println("7 - Loan a book");
                System.out.println("8 - Return a book");
                System.out.println("9 - Exit");
                System.out.println();

                System.out.print("Enter Option Number: ");
                int choice_No = scanner.nextInt(); //Get number from the user
                scanner.nextLine();

                switch (choice_No) { // Switch statement to do the different process related user input
                    case 1:
                        Book.view_All_Books(); // Call the Book class, view_All_Book method to view all books
                        break;
                    case 2:
                        Book.add_Book(scanner); // Call the Book class, add_Book method to add new books
                        break;
                    case 3:
                        Book.update_Book(scanner); // Call the Book class, update_Book method to update book details
                        break;
                    case 4:
                        Book.delete_Book(scanner);  // Call the Book class, delete_Book method to delete books
                        break;
                    case 5:
                        Book.search_Books(scanner);  // Call the Book class, search_Books method to search books
                        break;
                    case 6:
                        Members.add_New_Member();  // Call the Member class, add_New_Member method to add new member for member table
                        break;
                    case 7:
                        Loans.loan_Book();  // Call the Loan class, loan_Book method to add loan details of books
                        break;
                    case 8:
                        Loans.return_Book();  // Call the Loan class, return_Book method to add return details of books
                        break;
                    case 9:
                        System.out.println("Exiting..."); // Print an exit message and end the program
                        System.out.println("---Thank you for using Library Management System---");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again."); //Showing option panel again
                }
                System.out.println("--------------------------------");
                System.out.println();// Add space line for readability
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                scanner.nextLine();
            }

        }
    }
}















