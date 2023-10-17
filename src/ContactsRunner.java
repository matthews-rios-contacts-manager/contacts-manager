import util.Input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class ContactsRunner {

    public static void main(String[] args) throws IOException {

        //Program entry point that includes a throw for all generic IO exceptions while handling.
        //Variable created to control the do while loop and determines if the user continues or not.
        boolean confirm;

        //Encapsulates the entire program and continues as long as the boolean value is true, which is controlled by the user.
        do {

            //Prints the program main menu.
            System.out.println("**************************************");
            System.out.println("        ~ Contacts Manager ~          ");
            System.out.println("**************************************");
            System.out.println("1. View Contacts");
            System.out.println("2. Add New Contact");
            System.out.println("3. Search a Contact By Name");
            System.out.println("4. Delete Existing Contact");
            System.out.println("5. Exit\n");
            System.out.println("Enter an option (1, 2, 3, 4 or 5): ");

            //Format the file path by using the Path object.
            String currentDirectory = System.getProperty("user.dir");
            String directory = currentDirectory + "/src/data";
            String fileName = "contacts.txt";
            Path filePath = Paths.get(directory, fileName);

            //Used to read and store user input.
            Input input = new Input();
            int initialInput = input.getInt();


            //The list represents the existing contact data and reads all lines from the contact.txt file.
            List<String> data = Files.readAllLines(filePath);
            List<String> newData = Files.readAllLines((filePath));

            List<String> lines;

            //Switch statement used to handle user choice.
            switch (initialInput) {
                case 1:
                    System.out.println("**************************************");
                    System.out.println("          ~ View Contacts ~           ");
                    System.out.println("**************************************");
                    System.out.printf("\n%-20s | %-12s |\n", "Name", "Phone number");
                    System.out.println("--------------------------------");
                    for (long i = 0; i < data.size(); i++) {
                        System.out.println(data.get((int) i));
                    }
                    break;

                case 2:
                    //Prompts the user for full name and formatted number with hyphens that appends the new contact to txt file.
                    //Returns an error if the number is not between 7-10 digits.
                    System.out.println("**************************************");
                    System.out.println("         ~ Add New Contacts ~         ");
                    System.out.println("**************************************");
                    System.out.println("Enter Contact First Name: ");
                    String contactFirstNameInput = input.getString();
                    System.out.println("Enter Contact Last Name: ");
                    String contactLastNameInput = input.getString();
                    String formattedNames = contactFirstNameInput + " " + contactLastNameInput;

                    // Check if there's already a contact with the same name.
                    boolean contactExists = false;
                    for (String line : Files.readAllLines(filePath)) {
                        if (line.startsWith(formattedNames)) {
                            contactExists = true;
                            break;
                        }
                    }

                    if (contactExists) {
                        System.out.println("There's already a contact named " + formattedNames + ". Do you want to add the new contact anyway? (Y/N)");
                        String answer = input.getString();
                        if (answer.equalsIgnoreCase("n")) {
                            System.out.println("Contact not saved.");
                            break;
                        }
                    }

                    System.out.println("Enter Contact Phone Number");
                    String contactPhoneNumber = String.valueOf(input.getLong());

                    Contact newContact = new Contact(contactFirstNameInput, contactLastNameInput, contactPhoneNumber);
                    String firstName = newContact.getFirstName();
                    String lastName = newContact.getLastName();
                    String number = newContact.getNumber();

                    String formattedName = firstName + " " + lastName;
                    String formattedPhoneNumber = null;

                    if(number.length() == 10) {
                        formattedPhoneNumber = number.substring(0, 3) + "-"  + number.substring(3, 6) + "-" + number.substring(6);
                    } else if(number.length() == 7) {
                        formattedPhoneNumber = number.substring(0, 3) + "-"  + number.substring(3);
                    }else{
                        System.out.println("Please enter a 7 to 10 digit phone number");
                    }

                    // Pad the name and phone number strings with spaces to align them under the headers
                    long namePadding = 20 - formattedName.length();
                    long phonePadding = 12 - formattedPhoneNumber.length();
                    formattedName += " ".repeat((int) Math.max(0, namePadding));
                    formattedPhoneNumber = " ".repeat((int) Math.max(0, phonePadding)) + formattedPhoneNumber;

                    // Format the contact details in a tabular format
                    String formattedLine = String.format("%s | %s |", formattedName, formattedPhoneNumber);
                    lines = Arrays.asList(formattedLine);

                    System.out.println("contact added");
                    System.out.println("You entered: " + formattedLine);

                    //Adds in the data without overwriting
                    Files.write(filePath, lines, StandardOpenOption.APPEND);
                    break;

                //Case-insensitive contact search.
                case 3:
                    System.out.println("**************************************");
                    System.out.println("  ~ Search a Contact By Name ~        ");
                    System.out.println("**************************************");
                    System.out.println("Enter the Contact Name: ");
                    String searchParameter = input.getString();
                    String correctname = null;

                    for (String name : data) {
                        if (name.toLowerCase().contains(searchParameter.toLowerCase())) {
                            correctname = name;
                        }
                        else {
                            correctname = "No Contact Found.";
                        }
                    }
                    System.out.println(correctname);
                    break;

                //Deletes contact, updates txt file and informs user once complete.
                case 4:
                    System.out.println("**************************************");
                    System.out.println("   ~ Delete Existing Contact ~        ");
                    System.out.println("**************************************");
                    System.out.println("Enter the Name of the Contact You want to DELETE: ");
                    String deleteParameter = input.getString();

                    for (String name : data) {
                        if (name.toLowerCase().contains(deleteParameter.toLowerCase())) {
                            newData.remove(name);
                            System.out.println("contact deleted");
                            System.out.println("You deleted: " + name);
                        }
                    }
                    //Create a new file if it does not exist.
                    Files.write(filePath, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
                    Files.write(filePath, newData, StandardOpenOption.APPEND);
                    break;

                case 5:
                    break;
            }
            confirm = input.yesNo();
        } while(confirm);
        System.out.println("""
                 ____             __   __            _          _              _\s
                / ___|  ___  ___  \\ \\ / /__  _   _  | |    __ _| |_ ___ _ __  | |
                \\___ \\ / _ \\/ _ \\  \\ V / _ \\| | | | | |   / _` | __/ _ \\ '__| | |
                 ___) |  __/  __/   | | (_) | |_| | | |__| (_| | ||  __/ |    |_|
                |____/ \\___|\\___|   |_|\\___/ \\__,_| |_____\\__,_|\\__\\___|_|    (_)""");
    }

}

// TODO: Create exception for InputMismatchException if inserting the wrong name when searching.
// TODO: Create exception for InputMismatchException if inserting the wrong character or string on the main menu.