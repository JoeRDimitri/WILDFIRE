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
                            String num_wildfires = "Please enter number of wildfires would you like to create: ";
                            System.out.print(successful_message);
                            int wildfiresToCreate_again = scanner.nextInt();
                            scanner.nextLine(); 
                            for(int i = 0; i<wildfiresToCreate_again; i++)
                                int empty_index = findFirstEmptyIndex(WildfireDatabase);

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
