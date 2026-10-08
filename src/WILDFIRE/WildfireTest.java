package WILDFIRE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WildfireTest {
    private static int checks;

    public static void main(String[] args) throws Exception {
        testWildfireModel();
        System.out.println("[PASS] Wildfire model: constructors, counter, getters, setters, toString, equals");

        testDriverHelpers();
        System.out.println("[PASS] Driver helpers: occupancy, capacity, duplicate IDs, choice input, display, country search");

        testDriverCli();
        System.out.println("[PASS] CLI workflow: registration, validation, authentication, all update fields, searches, exit");

        testPasswordLockout();
        System.out.println("[PASS] CLI security: repeated failed registration passwords trigger lockout");

        System.out.println();
        System.out.println("Feature coverage: 4/4 feature groups passed (100%).");
        System.out.println("Assertions passed: " + checks);
        System.out.println("Note: This is feature coverage, not instrumented statement/branch coverage.");
    }

    private static void testWildfireModel() {
        int startingCount = Wildfire.get_numberOfWildFires();
        Wildfire defaults = new Wildfire();
        check(Wildfire.get_numberOfWildFires() == startingCount + 1, "Default constructor increments the counter");
        check(defaults.get_fireID() == -1, "Default fire ID");
        check("N/A".equals(defaults.get_fireName()), "Default fire name");
        check("N/A".equals(defaults.get_country()), "Default country");
        check("N/A".equals(defaults.get_region()), "Default region");
        check(defaults.get_areaBurned() == -1.0, "Default area burned");
        check(defaults.get_containmentPercentage() == -1.0, "Default containment");
        check("N/A".equals(defaults.get_riskLevel()), "Default risk level");
        check(!defaults.get_evacuationRequired(), "Default evacuation status");
        check(defaults.toString().contains("fireID: -1"), "Default toString output");

        Wildfire fire = new Wildfire(77L, "Test Fire", "USA", "Oregon", 12.5, 45.0, "high", true);
        check(Wildfire.get_numberOfWildFires() == startingCount + 2, "Full constructor increments the counter");
        check(fire.get_fireID() == 77L, "Full constructor fire ID");
        check("Test Fire".equals(fire.get_fireName()), "Full constructor fire name");
        check("USA".equals(fire.get_country()), "Full constructor country");
        check("Oregon".equals(fire.get_region()), "Full constructor region");
        check(fire.get_areaBurned() == 12.5, "Full constructor area burned");
        check(fire.get_containmentPercentage() == 45.0, "Full constructor containment");
        check("high".equals(fire.get_riskLevel()), "Full constructor risk level");
        check(fire.get_evacuationRequired(), "Full constructor evacuation status");

        fire.set_fireName("Updated Fire");
        fire.set_country("Canada");
        fire.set_region("Alberta");
        fire.set_areaBurned(20.0);
        fire.set_containmentPercentage(80.0);
        fire.set_riskLevel("moderate");
        fire.set_evacuationRequired(false);
        check("Updated Fire".equals(fire.get_fireName()), "Fire name setter");
        check("Canada".equals(fire.get_country()), "Country setter");
        check("Alberta".equals(fire.get_region()), "Region setter");
        check(fire.get_areaBurned() == 20.0, "Area setter");
        check(fire.get_containmentPercentage() == 80.0, "Containment setter");
        check("moderate".equals(fire.get_riskLevel()), "Risk setter");
        check(!fire.get_evacuationRequired(), "Evacuation setter");

        Wildfire equalByFields = new Wildfire(77L, "Shared", "USA", "Oregon", 12.5, 45.0, "high", true);
        Wildfire sameReferences = new Wildfire(77L, "Shared", "USA", "Oregon", 12.5, 45.0, "high", true);
        check(sameReferences.equals(equalByFields), "equals with matching field values and shared string references");
        check(sameReferences.equals(sameReferences), "equals identity case");
        check(!sameReferences.equals(null), "equals null case");
        check(!sameReferences.equals("not a wildfire"), "equals different-class case");
        check(!sameReferences.equals(new Wildfire(78L, "Shared", "USA", "Oregon", 12.5, 45.0, "high", true)),
                "equals detects a different fire ID");
        check(sameReferences.toString().contains("evacuationRequired: true"), "Full toString output");
    }

    private static void testDriverHelpers() throws Exception {
        Wildfire first = new Wildfire(10L, "First", "USA", "North", 5.0, 20.0, "low", false);
        Wildfire second = new Wildfire(20L, "Second", "Canada", "South", 7.0, 70.0, "high", true);
        Wildfire[] records = {first, null, second};
        check(driver.inUse(records) == 2, "inUse counts non-null database entries");
        check(driver.ampleSpace(records, 1), "ampleSpace permits an insertion that fits");
        check(!driver.ampleSpace(records, 2), "ampleSpace rejects an insertion exceeding remaining capacity");
        check(!driver.ampleSpace(new Wildfire[] {first}, 1), "ampleSpace rejects a full database");
        check(driver.fireAlreadyExists(records, 20L), "Duplicate ID is found");
        check(!driver.fireAlreadyExists(records, 99L), "Unknown ID is not found");

        String choiceOutput = captureOutput(() -> {
            try (Scanner scanner = new Scanner("bad\n9\n3\n")) {
                int choice = driver.readChoice(scanner, "Choice: ", 1, 5);
                check(choice == 3, "readChoice retries invalid text and out-of-range values");
            }
        });
        check(occurrences(choiceOutput, "Invalid choice.") == 2, "readChoice reports both invalid choices");

        String displayOutput = captureOutput(() -> driver.displayWildfire(0, first));
        check(displayOutput.contains("Fire ID: 10"), "displayWildfire prints the record ID");
        check(displayOutput.contains("Risk Level: Low"), "displayWildfire formats the risk level");

        String countryOutput = captureOutput(() -> {
            check(driver.findWildfiresByCountry(records, "uSa") == 1, "Country search ignores case");
            check(driver.findWildfiresByCountry(records, "Missing") == 0, "Country search returns zero for no matches");
        });
        check(countryOutput.contains("First"), "Country search displays matching records");
    }

    private static void testDriverCli() throws Exception {
        String input = String.join("\n",
                "2", "0",
                "1", "wrong", "fire2026", "1",
                "101", "Pine Fire", "USA", "California", "bad", "-12", "x", "101", "35",
                "urgent", "High", "maybe", "true",
                "1", "fire2026", "2",
                "1", "fire2026", "1",
                "101", "202", "Cedar Fire", "Canada", "Alberta", "60", "85", "Moderate", "false",
                "1", "fire2026", "1",
                "2", "wrong", "wrong", "wrong",
                "2", "fire2026", "not-a-number", "101",
                "0", "no", "1", "Pine Updated",
                "2", "United States",
                "3", "West",
                "4", "-50",
                "5", "120",
                "6", "unknown", "Extreme",
                "7", "maybe", "false",
                "8",
                "3", " United States ",
                "3", "Mexico",
                "4", "bad", "101", "100",
                "4", "0",
                "5", "");
        String output = runDriver(input);

        check(output.contains("Welcome to the Wildfire Management System!"), "CLI welcome/menu");
        check(occurrences(output, "Main Menu:") >= 8,
                "Main menu is reprinted after an out-of-range numeric selection");
        check(output.contains("That fire ID already exists."), "Registration rejects duplicate fire IDs");
        check(output.contains("converting to positive value"), "Registration normalizes negative burned area");
        check(output.contains("Invalid input. Enter a containment percentage between 0 and 100:"),
                "Registration rejects invalid containment inputs");
        check(output.contains("Invalid input, enter the risk level"), "Registration validates risk levels");
        check(output.contains("Invalid input. Enter true or false:"), "Registration validates evacuation input");
        check(output.contains("requested amount of insertion exceeds the DB Limits"),
                "Registration reports insufficient remaining capacity");
        check(output.contains("DB is at Max capacity"), "Registration reports a full database");
        check(output.contains("Incorrect password. Access denied."), "Registration and update authentication reject wrong passwords");
        check(output.contains("Invalid Fire ID. Please enter a number:"), "Update validates fire ID input");
        check(output.contains("Update Menu:"), "Update menu is displayed");
        check(output.contains("Invalid choice. Please enter a number from 1 to 8."),
                "Update menu retries invalid selections");
        check(output.contains("Invalid negative value entered for area burned"), "Update normalizes negative area");
        check(output.contains("converting to 100%"), "Update clamps containment above 100");
        check(output.contains("Invalid input, enter the risk level"), "Update validates risk levels");
        check(output.contains("United States"), "Country search trims input and finds a matching record");
        check(output.contains("No Wildfires were found in Mexico."), "Country search reports no matches");
        check(output.contains("Invalid percentage. Please enter a value from 0.0 to 100.0."),
                "Containment search validates its threshold");
        check(output.contains("No Wildfires were found with a containment percentage at or below 0.0%"),
                "Containment search reports no matching records");
        check(output.contains("Thank you for using the Global Wildfire Monitoring System."),
                "CLI exits cleanly");
        check(output.contains("Fire Name: Pine Updated"), "CLI update changes fire name");
        check(output.contains("Country: United States"), "CLI update changes country");
        check(output.contains("Region: West"), "CLI update changes region");
        check(output.contains("Area Burned: 50.0 hectares"), "CLI update changes area");
        check(output.contains("Containment Percentage: 100.0 %"), "CLI update changes containment");
        check(output.contains("Risk Level: Extreme"), "CLI update changes risk level");
        check(output.contains("Evacuation Required: false"), "CLI update changes evacuation status");
    }

    private static void testPasswordLockout() throws Exception {
        String input = String.join("\n",
                "0",
                "1", "bad", "bad", "bad",
                "1", "bad", "bad", "bad",
                "1", "bad", "bad", "bad");
        String output = runDriver(input);
        check(output.contains("Multiple unauthorized access attempts detected"),
                "Nine failed password attempts trigger the lockout warning");
    }

    private static String runDriver(String input) throws Exception {
        return captureOutput(() -> {
            InputStream originalIn = System.in;
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            try {
                driver.main(new String[0]);
            } finally {
                System.setIn(originalIn);
            }
        });
    }

    private static String captureOutput(ThrowingRunnable action) throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream captured = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(captured);
            action.run();
        } finally {
            System.setOut(originalOut);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }

    private static int occurrences(String text, String fragment) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(fragment, index)) >= 0) {
            count++;
            index += fragment.length();
        }
        return count;
    }

    private static void check(boolean condition, String description) {
        checks++;
        if (!condition) {
            throw new AssertionError("Test failed: " + description);
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
