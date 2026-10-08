package WILDFIRE;
import java.util.Scanner;

public class driver {

    // Check whether the requested insertion count can fit without exceeding the array capacity.
    static boolean ampleSpace(Wildfire[] arr, int num_we_want_to_add){
        int in_use = Wildfire.get_numberOfWildFires();
        int relative_space = in_use;
        int new_in_use = in_use + num_we_want_to_add;
        int length_of_arr = arr.length;
        int max_to_add = length_of_arr - relative_space;

        if(in_use == length_of_arr){
            System.out.println("The DB is at Max capacity. No more insertions");
            return false;
        }
        if(new_in_use <= length_of_arr){
            System.out.println("The DB space permits adding this number of Wildfires. Ample space is not guaranteed after transaction.");
            return true;
        }
        else{                
            System.out.println("The requested amount of insertion exceeds the DB Limits. The DB can only handle: "+max_to_add+" insertions.");
            return false; 
        }
    }

    // Search the database to see whether this fire ID already exists.
    static boolean fireAlreadyExists(Wildfire[] arr, long fireID){
        for(Wildfire wildfire : arr){
            if(wildfire != null && wildfire.get_fireID() == fireID){
                return true;
            }
        }
        return false;   
    }

    // Keep prompting until the user enters a valid numeric choice within the allowed range.
    static int readChoice(Scanner scanner, String prompt, int min, int max){
        while(true){
            System.out.print(prompt);
            String input = scanner.nextLine();
            try{
                int choice = Integer.parseInt(input);
                if(choice >= min && choice <= max){
                    return choice;
                }
            }
            catch(NumberFormatException e){
                // Continue prompting until a valid numeric choice is entered.
            }
            System.out.println("Invalid choice. Please enter a number from " + min + " to " + max + ".");
        }
    }

    // Find and print all wildfire records that belong to a specific country.
    static int findWildfiresByCountry(Wildfire[] database, String country){
        int matches = 0;
        for(int i = 0; i < database.length; i++){
            Wildfire wildfire = database[i];
            if(wildfire != null && wildfire.get_country().equalsIgnoreCase(country)){
                System.out.println(wildfire);
                matches++;
            }
        }
        return matches;
    }

    // Main application loop manages the user interface and all wildfire operations.
    public static void main (String argsp[]){
        Scanner scanner = new Scanner(System.in);
        String main_interface = 
                "Main Menu:\n" +
                "1. Register new Wildfires (password required)\n" +
                "2. Update an existing Wildfire (password required)\n" +
                "3. Display Wildfires by country\n" +
                "4. Display Wildfires by containment percentage\n" +
                "5. Exit\n" +
                "Please enter your choice: ";
        String welcome_message = "Welcome to the Wildfire Management System!";
        String prompt_message = "Please enter the number of wildfires to create: ";
        String password = "fire2026";
        int consecutive_Incorrect_Attempts = 0;
        
        System.out.println(welcome_message);
        System.out.print(prompt_message);

        int numberOfWildfires = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Wildfire[] WildfireDatabase = new Wildfire[numberOfWildfires];
        while(true){
            System.out.print(main_interface);
            int choice = scanner.nextInt();
            scanner.nextLine();
            if(choice < 1 || choice > 5){
                continue;
            }
            else{
                try{
                switch(choice){
                    case 1:
                        // Verify password before allowing wildfire creation.
                        String passwordInput = "Enter a password to create a new wildfire: ";
                        System.out.print(passwordInput);
                        String userPassword = scanner.nextLine();
                        
                        if(!userPassword.equals(password)){
                            consecutive_Incorrect_Attempts++;
                            if(consecutive_Incorrect_Attempts == 9){
                                throw new SecurityException("WARNING: Multiple unauthorized access attempts detected. Program terminating immediately!.");
                            }
                            for(int i = 0; i < 2; i++){

                                System.out.println("Incorrect password. Access denied.");
                                System.out.print(passwordInput);

                                userPassword = scanner.nextLine();
                                if(userPassword.equals(password)){
                                    break;
                                }
                                consecutive_Incorrect_Attempts++;
                                if(consecutive_Incorrect_Attempts == 9){
                                    throw new SecurityException("WARNING: Multiple unauthorized access attempts detected. Program terminating immediately!.");
                                }
                                if(i == 1){
                                    System.out.println("Incorrect password. Access denied.");
                                    throw new SecurityException("Access denied due to incorrect password.");
                                }
                            }
                        }
                        consecutive_Incorrect_Attempts = 0;

                        // Ask for the number of wildfire records to add and then collect each one.
                        String successful_message = "Access granted. How many wildfires would you like to create: ";
                        System.out.print(successful_message);
                        int wildfiresToCreate = scanner.nextInt();
                        scanner.nextLine(); 

                        if(ampleSpace(WildfireDatabase,wildfiresToCreate)){
                            for(int i = 0; i<wildfiresToCreate; i++){
                                System.out.print("Enter the fire ID: ");
                                long fireID = scanner.nextLong();
                                scanner.nextLine();
                                while(fireAlreadyExists(WildfireDatabase, fireID)){
                                    System.out.println("That fire ID already exists. Please enter a different ID.");
                                    System.out.print("Enter the fire ID: ");
                                    fireID = scanner.nextLong();
                                    scanner.nextLine();
                                }

                                System.out.print("Enter the fire name: ");
                                String fireName = scanner.nextLine();

                                System.out.print("Enter the country: ");
                                String country = scanner.nextLine();

                                System.out.print("Enter the region: ");
                                String region = scanner.nextLine();

                                System.out.print("Enter the area burned: ");
                                double areaBurned;
                                while(true){
                                    String areaBurnedInput = scanner.nextLine();
                                    try{
                                        areaBurned = Double.parseDouble(areaBurnedInput);
                                        if(!Double.isNaN(areaBurned)||areaBurned<0){
                                            break;
                                        }
                                    }
                                    catch(NumberFormatException e){
                                        // Continue prompting until the value is a valid number.
                                    }
                                    System.out.print("Invalid input. Enter the area burned: ");
                                }
                                System.out.print("Enter the containment percentage: ");
                                double containmentPercentage;
                                while(true){
                                    String containmentInput = scanner.nextLine();
                                    try{
                                        containmentPercentage = Double.parseDouble(containmentInput);
                                        if(!Double.isNaN(containmentPercentage) && containmentPercentage >= 0 && containmentPercentage <= 100){
                                            break;
                                        }
                                    }
                                    catch(NumberFormatException e){
                                        // Continue prompting until the value is a valid number.
                                    }
                                    System.out.print("Invalid input. Enter a containment percentage between 0 and 100: ");
                                }

                                System.out.print("Enter the risk level (Low/Moderate/High/Extreme): ");
                                String riskLevel = scanner.nextLine();

                                while(!riskLevel.equalsIgnoreCase("Low")&&!riskLevel.equalsIgnoreCase("Moderate")&&!riskLevel.equalsIgnoreCase("High")&&!riskLevel.equalsIgnoreCase("Extreme")){
                                    System.out.print("Invalid input, enter the risk level (Low/Moderate/High/Extreme): ");
                                    riskLevel = scanner.nextLine();
                                }

                                System.out.print("Is evacuation required? (true/false): ");
                                boolean evacuationRequired;
                                while(true){
                                    String evacuationInput = scanner.nextLine();
                                    if(evacuationInput.equalsIgnoreCase("true")){
                                        evacuationRequired = true;
                                        break;
                                    }
                                    if(evacuationInput.equalsIgnoreCase("false")){
                                        evacuationRequired = false;
                                        break;
                                    }
                                    System.out.print("Invalid input. Enter true or false: ");
                                }
                                
                                //Find a spot to insert into db
                                Wildfire wildfire = new Wildfire(fireID, fireName, country, region,
                                        areaBurned, containmentPercentage, riskLevel, evacuationRequired);
                                int emptySlot = 0;
                                while(WildfireDatabase[emptySlot] != null){
                                    emptySlot++;
                                }
                                WildfireDatabase[emptySlot] = wildfire;
                            }
                        }
                        else{
                            
                        }

                        
                        break;
                    case 2:
                        // Confirm access before updating any existing wildfire record.
                        boolean accessGranted = false;
                        for(int attempt = 1; attempt <= 3; attempt++){
                            System.out.print("Enter a password to update an existing wildfire: ");
                            String updatePassword = scanner.nextLine();
                            if(updatePassword.equals(password)){
                                accessGranted = true;
                                break;
                            }
                            System.out.println("Incorrect password. Access denied.");
                        }
                        if(!accessGranted){
                            break;
                        }

                        boolean returnToMainMenu = false;
                        while(!returnToMainMenu){
                            System.out.print("Enter the fire ID of the Wildfire to update: ");
                            long fireID;
                            while(true){
                                String fireIDInput = scanner.nextLine();
                                try{
                                    fireID = Long.parseLong(fireIDInput);
                                    break;
                                }
                                catch(NumberFormatException e){
                                    System.out.print("Invalid Fire ID. Please enter a number: ");
                                }
                            }

                            int wildfireIndex = -1;
                            
                            for(int i = 0; i < WildfireDatabase.length; i++){
                                if(WildfireDatabase[i] != null && WildfireDatabase[i].get_fireID() == fireID){
                                    wildfireIndex = i;
                                    break;
                                }
                            }

                            if(wildfireIndex == -1){
                                System.out.println("No Wildfire was found with Fire ID " + fireID + ".");
                                int missingFireChoice = readChoice(scanner,
                                        "1. Enter another Fire ID\n2. Return to the main menu\nPlease enter your choice: ",
                                        1, 2);
                                if(missingFireChoice == 2){
                                    returnToMainMenu = true;
                                }
                                continue;
                            }

                            Wildfire wildfire = WildfireDatabase[wildfireIndex];
                            System.out.println(wildfire);
                            boolean updating = true;
                            while(updating){
                                int updateChoice = readChoice(scanner,
                                        "Update Menu:\n" +
                                        "1. Fire Name\n" +
                                        "2. Country\n" +
                                        "3. Region\n" +
                                        "4. Area Burned\n" +
                                        "5. Containment Percentage\n" +
                                        "6. Risk Level\n" +
                                        "7. Evacuation Required\n" +
                                        "8. Return to the main menu\n" +
                                        "Please enter your choice: ",
                                        1, 8);
                                switch(updateChoice){
                                    case 1:
                                        System.out.print("Enter the new fire name: ");
                                        wildfire.set_fireName(scanner.nextLine());
                                        break;
                                    case 2:
                                        System.out.print("Enter the new country: ");
                                        wildfire.set_country(scanner.nextLine());
                                        break;
                                    case 3:
                                        System.out.print("Enter the new region: ");
                                        wildfire.set_region(scanner.nextLine());
                                        break;
                                    case 4:
                                        System.out.print("Enter the new area burned: ");
                                        double areaBurned;
                                        while(true){
                                            String areaBurnedInput = scanner.nextLine();
                                            try{
                                                areaBurned = Double.parseDouble(areaBurnedInput);
                                                break;
                                            }
                                            catch(NumberFormatException e){
                                                System.out.print("Invalid input. Enter the new area burned: ");
                                            }
                                        }
                                        if(areaBurned < 0){
                                            System.out.println("Invalid negative value entered for area burned, converting to positive value.");
                                            areaBurned = areaBurned * -1;
                                        }
                                        wildfire.set_areaBurned(areaBurned);
                                        break;
                                    case 5:
                                        System.out.print("Enter the new containment percentage: ");
                                        double containmentPercentage;
                                        while(true){
                                            String containmentInput = scanner.nextLine();
                                            try{
                                                containmentPercentage = Double.parseDouble(containmentInput);
                                                break;
                                            }
                                            catch(NumberFormatException e){
                                                System.out.print("Invalid input. Enter the new containment percentage: ");
                                            }
                                        }
                                        if(containmentPercentage < 0){
                                            System.out.println("Invalid negative value entered for containment percentage, rounding to 0%.");
                                            containmentPercentage = 0;
                                        }
                                        if(containmentPercentage > 100){
                                            System.out.println("Invalid positive value over 100 entered for containment percentage, converting to 100%.");
                                            containmentPercentage = 100;
                                        }
                                        wildfire.set_containmentPercentage(containmentPercentage);
                                        break;
                                    case 6:
                                        System.out.print("Enter the new risk level (Low/Moderate/High/Extreme): ");
                                        String riskLevel = scanner.nextLine().toLowerCase();
                                        while(!riskLevel.equals("low") && !riskLevel.equals("moderate")
                                                && !riskLevel.equals("high") && !riskLevel.equals("extreme")){
                                            System.out.print("Invalid input, enter the risk level (Low/Moderate/High/Extreme): ");
                                            riskLevel = scanner.nextLine().toLowerCase();
                                        }
                                        wildfire.set_riskLevel(riskLevel);
                                        break;
                                    case 7:
                                        System.out.print("Is evacuation required? (true/false): ");
                                        String evacuationInput = scanner.nextLine();
                                        while(!evacuationInput.equalsIgnoreCase("true")
                                                && !evacuationInput.equalsIgnoreCase("false")){
                                            System.out.print("Invalid input. Enter true or false: ");
                                            evacuationInput = scanner.nextLine();
                                        }
                                        wildfire.set_evacuationRequired(Boolean.parseBoolean(evacuationInput));
                                        break;
                                    case 8:
                                        updating = false;
                                        returnToMainMenu = true;
                                        continue;
                                }
                                System.out.println(wildfire);
                            }
                        }
                        break;
                    case 3:
                        // Search the database by country and print any matching wildfires.
                        System.out.print("Enter a country name: ");
                        String country = scanner.nextLine().trim();
                        if(findWildfiresByCountry(WildfireDatabase, country) == 0){
                            System.out.println("No Wildfires were found in " + country + ".");
                        }
                        break;
                    case 4:
                        // Display all wildfire records whose containment level is at or below the chosen threshold.
                        double maximumContainment;
                        while(true){
                            System.out.print("Enter a maximum containment percentage (0.0-100.0): ");
                            String containmentInput = scanner.nextLine();
                            try{
                                maximumContainment = Double.parseDouble(containmentInput);
                                if(maximumContainment >= 0.0 && maximumContainment <= 100.0){
                                    break;
                                }
                            }
                            catch(NumberFormatException e){
                                // Continue prompting for a valid percentage.
                            }
                            System.out.println("Invalid percentage. Please enter a value from 0.0 to 100.0.");
                        }
                        int matchingWildfires = 0;
                        for(int i = 0; i < WildfireDatabase.length; i++){
                            Wildfire wildfire = WildfireDatabase[i];
                            if(wildfire != null && wildfire.get_containmentPercentage() <= maximumContainment){
                                System.out.println(wildfire);
                                matchingWildfires++;
                            }
                        }
                        if(matchingWildfires == 0){
                            System.out.println("No Wildfires were found with a containment percentage at or below "
                                    + maximumContainment + "%.");
                        }
                        break;
                    case 5:
                        System.out.println("Thank you for using the Global Wildfire Monitoring System.");
                        scanner.close();
                        return; 
                }
            }
            catch (SecurityException e){
                if(consecutive_Incorrect_Attempts >= 9){
                    System.out.println("WARNING: Multiple unauthorized access attempts detected. Program terminating immediately!.");
                    scanner.close();
                    return;
                }
                else{
                    System.out.println(e.getMessage());
                }
            }

        }
        }

}
}
