package WILDFIRE;
import java.util.Scanner;

public class driver {

    static int inUse(Wildfire[] arr){
        int total_size_of_arr = arr.length;
        int number_of_slots_inUse = 0;
        for(int i = 0; i<total_size_of_arr; i ++){
            if(arr[i] == null)
                continue;
            number_of_slots_inUse++;
        }
        return number_of_slots_inUse;
    }

    static boolean ampleSpace(Wildfire[] arr, int num_we_want_to_add){
        int in_use = inUse(arr);
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

    static boolean fireAlreadyExists(Wildfire[] arr, long fireID){
        for(Wildfire wildfire : arr){
            if(wildfire != null && wildfire.get_fireID() == fireID){
                return true;
            }
        }
        return false;
    }

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
                                double areaBurned = scanner.nextDouble();
                                scanner.nextLine();
                                if(areaBurned<0){
                                    System.out.println("Invalid negative value entered for area burned, converting to positive value.");
                                    areaBurned = areaBurned * -1;
                                }

                                System.out.print("Enter the containment percentage: ");
                                double containmentPercentage = scanner.nextDouble();
                                scanner.nextLine();
                                if(containmentPercentage<0){
                                    System.out.println("Invalid negative value entered for containment percentage, rounding to 0%.");
                                    containmentPercentage = 0;
                                }
                                if(containmentPercentage>100){
                                    System.out.print("Invalid positive value over 100 entered for containment percentage, converting to positive value. Converting to 100%");
                                    containmentPercentage = 100;
                                }

                                System.out.print("Enter the risk level (Low/Moderate/High/Extreme): ");
                                String riskLevel = scanner.nextLine();
                                riskLevel = riskLevel.toLowerCase();

                                while(!riskLevel.equalsIgnoreCase("Low")&&!riskLevel.equalsIgnoreCase("Moderate")&&!riskLevel.equalsIgnoreCase("High")&&!riskLevel.equalsIgnoreCase("Extreme")){
                                    System.out.print("Invalid input, enter the risk level (Low/Moderate/High/Extreme): ");
                                    riskLevel = scanner.nextLine();
                                }

                                System.out.print("Is evacuation required? (true/false): ");
                                boolean evacuationRequired = scanner.nextBoolean();
                                scanner.nextLine();

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
                        System.out.println("Displaying all wildfires:");
                        for(Wildfire wf : WildfireDatabase){
                            if(wf != null){
                                System.out.println(wf);
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Exiting the system. Goodbye!");
                        scanner.close();
                        return;
                    case 4:
                        System.out.println("Total number of wildfires created: " + Wildfire.get_numberOfWildFires());
                        break;
                    case 5:
                        System.out.println("Exiting the system. Goodbye!");
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
