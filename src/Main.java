import java.util.Scanner;

public class Main {
    private static PatientManager patientManager = new PatientManager();
    private static BedManager bedManager = new BedManager();
    private static  Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        
        //Print out menu
        while (running) {
            System.out.println("\n-=-=-=--==-=-=-=-=-=-= MEDICARE HOSPITAL ADMISSION SYSTEM -=-=-=-=-=-=-=-=-==-=-=-=-=-");
            System.out.println("1. Patient Management");
            System.out.println("2. Bed Management");
            System.out.println("3. Ward Reports");
            System.out.println("4. Exit");
            System.out.println("Enter choice (1-4): ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    patientManagementMenu();
                    break;

                case "2":
                    bedManagementMenu();
                    break;

                case "3":
                    reportsMenu();
                    break;

                case "4":
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 4.");

            }
        }
    }

    //Print out patient managment menu
    private static void patientManagementMenu() {
        System.out.println("\n~_~_~_~_~_~_~_ PATIENT MANAGEMENT ~_~_~_~_~_~_~_~_~_~");
        System.out.println("1. Register New Patient");
        System.out.println("2. Search Patient by ID");
        System.out.println("3. Update Patient Details");
        System.out.println("4. Delete Patient");
        System.out.println("5. Display All Patients");
        System.out.println("6. Sort Patients");
        System.out.println("ENTER CHOICE (1 -> 6): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                registerPatientHelper();
                break;

            case "2":
                System.out.print("Enter Patient ID to search: ");
                String searchId = scanner.nextLine().trim();
                Patient p = patientManager.searchPatient(searchId);
                if (p != null) {
                    System.out.println("\n^^^^^^^ PATIENT FOUND ^^^^^^^");
                    p.displayDetails();
                } else {
                    System.out.println("No patient found with ID: " + searchId);
                }
                break;

            case "3":
                updatePatientHelper();
                break;

            case "4":
                System.out.print("Enter Patient ID to delete: ");
                String deleteId = scanner.nextLine().trim();
                patientManager.deletePatient(deleteId);
                break;

            case "5":
                patientManager.displayAllPatients();
                break;

            case "6":
                sortPatientsHelper();
                break;

            default:
                System.out.println("Invalid selection.");
        }

    }

    private static void registerPatientHelper() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();

        if (patientManager.isIdDuplicate(id)) {
            System.out.println("Error: Patient ID " + id + " already exists!");
            return;
        }

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine().trim();

        System.out.print("Enter Medical Condition: ");
        String condition = scanner.nextLine().trim();

        System.out.println("--- SELECT CATEGORY ---");
        System.out.println("1. Inpatient");
        System.out.println("2. Outpatient");
        System.out.println("3. Emergency");
        System.out.println("Choice: ");
        String catChoice = scanner.nextLine().trim();

        if (catChoice.equals("1")) {
            Inpatient inpatient = new Inpatient(id, firstName, lastName,age,gender, condition, "Unassigned", "Unassigned");
            patientManager.registerPatient(inpatient);
            System.out.println("Inpatient created! Allocate a bed in Bed Management.");
        } else if (catChoice.equals("2")) {
            Patient outpatient = new Patient(id, firstName, lastName, age, gender, condition, PatientCategory.OUTPATIENT);
            patientManager.registerPatient(outpatient);
        } else if (catChoice.equals("3")) {
            Patient emergency = new Patient(id, firstName, lastName, age, gender, condition, PatientCategory.EMERGENCY);
            patientManager.registerPatient(emergency);
        } else {
            System.out.println("Invalid category selected.");
        }
    }

    private static void updatePatientHelper() {
        System.out.print("Enter patient ID to update: ");
        String id = scanner.nextLine().trim();

        Patient existing = patientManager.searchPatient(id);
        if (existing == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter New First Name (" + existing.getFirstName() + "): ");
        String fName = scanner.nextLine().trim();

        System.out.print("Enter New Last Name (" + existing.getLastName() + "): ");
        String lName = scanner.nextLine().trim();

        System.out.print("Enter New Age (" + existing.getAge() + "): ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter New Gender (" + existing.getGender() + "): ");
        String gender = scanner.nextLine().trim();

        System.out.print("Enter New Medical Condition (" + existing.getMedicalCondition() + "): ");
        String cond = scanner.nextLine().trim();

        patientManager.updatePatient(id, fName, lName, age, gender, cond);
    }

    private static void sortPatientsHelper() {
        System.out.println("1. Sort by Patient ID");
        System.out.println("2. Sort by Last Name / Surname");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            patientManager.sortById();
        } else if (choice.equals("2")) {
            patientManager.sortByLastName();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // Bed management printing
    private static void bedManagementMenu() {
        System.out.println("\n--- BED MANAGEMENT ---");
        System.out.println("1. Automatically Allocate First Available Bed");
        System.out.println("2. Manually Allocate Specific Bed");
        System.out.println("3. Release Bed (Discharge)");
        System.out.println("4. Display Complete Ward Layout (4x5)");
        System.out.println("5. Display Available Beds");
        System.out.println("6. Display Occupied Beds");
        System.out.print("Enter choice (1-6): ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": // Automatic Allocation
                System.out.print("Enter Patient ID for Automatic Bed Allocation: ");
                String autoId = scanner.nextLine().trim();
                Patient autoPatient = patientManager.searchPatient(autoId);

                if (autoPatient == null) {
                    System.out.println("Error: Patient not found!");
                } else if (!(autoPatient instanceof Inpatient)) {
                    System.out.println("Error: Only INPATIENTS can be allocated a hospital bed!");
                } else {
                    bedManager.allocateBedAuto((Inpatient) autoPatient);
                }
                break;

            case "2": // Manual Allocation
                System.out.print("Enter Patient ID to assign bed: ");
                String manualId = scanner.nextLine().trim();
                Patient manualPatient = patientManager.searchPatient(manualId);

                if (manualPatient == null) {
                    System.out.println("Error: Patient not found!");
                } else if (!(manualPatient instanceof Inpatient)) {
                    System.out.println("Error: Only INPATIENTS can be allocated a hospital bed!");
                } else {
                    bedManager.displayAvailableBeds();
                    System.out.print("Enter Bed Number to allocate (1-20): ");
                    try {
                        int bedNum = Integer.parseInt(scanner.nextLine().trim());
                        bedManager.allocateBed((Inpatient) manualPatient, bedNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Bed number must be a valid integer!");
                    }
                }
                break;

            case "3":
                System.out.print("Enter Bed Number to release (1-20): ");
                try {
                    int bedNum = Integer.parseInt(scanner.nextLine().trim());
                    bedManager.releaseBed(bedNum);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Bed number must be a valid integer!");
                }
                break;

            case "4": bedManager.displayWardLayout(); break;
            case "5": bedManager.displayAvailableBeds(); break;
            case "6": bedManager.displayOccupiedBeds(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    // Print out Ward reports and metrics 
    private static void reportsMenu() {
        System.out.println("\n_~_~_~_~_~_~ WARD REPORTS & METRICS ~_~_~_~_~_~_~_");
        int totalPatients = patientManager.getTotalPatientsCount();
        int occupiedBeds = bedManager.getOccupiedCount();
        int availableBeds = 20 - occupiedBeds;
        double occupancyPercentage = (occupiedBeds / 20.0) * 100;

        System.out.println("Total Registered Patients: " + totalPatients);
        System.out.println("Total Occupied Beds: " + occupiedBeds + " / 20");
        System.out.println("Total Available Beds: " + availableBeds + " / 20");
        System.out.printf("Ward Occupancy Rate: %.2f%%\n", occupancyPercentage);
        System.out.println("-=-=-===-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-====-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }
}
