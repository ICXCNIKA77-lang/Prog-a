public class BedManager {
    private Inpatient[][] ward = new Inpatient[4][5];

    private int getRow(int bedNum) {
        return (bedNum - 1) / 5;
    }

    private int getCol(int bedNum) {
        return (bedNum - 1) % 5;
    }

    private String formatBedCode(int number) {
        return String.format("B%02d", number);
    }

    public int findNextAvailableBed() {
        int bedCounter = 1;
        for (int i = 0; i < ward.length; i++) {
            for (int j = 0; j < ward[i].length; j++) {
                if (ward[i][j] == null) {
                    return bedCounter;
                }
                bedCounter++;
            }
        }
        return -1;
    }

    // Allocate to specific bed number
    public boolean allocateBed(Inpatient patient, int bedNumber) {
        if (bedNumber < 1 || bedNumber > 20) {
            System.out.println("Error: Bed number must be between 1 and 20.");
            return false;
        }

        int row = getRow(bedNumber);
        int col = getCol(bedNumber);

        if (ward[row][col] != null) {
            System.out.println("Error: Bed " + formatBedCode(bedNumber) + " is already occupied!");
            return false;
        }

        ward[row][col] = patient;
        patient.setBedNumber(formatBedCode(bedNumber));
        patient.setWardNumber("General Ward");
        System.out.println("Success: Patient " + patient.getFirstName() + " " + patient.getLastName()
                + " allocated to Bed " + formatBedCode(bedNumber) + ".");
        return true;
    }

    // Automatic allocation
    public boolean allocateBedAuto(Inpatient patient) {
        for (int bedNum = 1; bedNum <= 20; bedNum++) {
            int row = getRow(bedNum);
            int col = getCol(bedNum);

            if (ward[row][col] == null) {
                System.out.println("Auto-allocating Bed " + formatBedCode(bedNum) + "...");
                return allocateBed(patient, bedNum);
            }
        }

        System.out.println("Error: Ward is FULL! No beds available for automatic allocation.");
        return false;
    }

    // Overloaded auto-allocate helper
    public boolean allocateBed(Inpatient patient) {
        int nextAvailableBed = findNextAvailableBed();

        if (nextAvailableBed == -1) {
            System.out.println("ERROR: Cannot allocate bed. The ward is FULL!");
            return false;
        }
        return allocateBed(patient, nextAvailableBed);
    }

    public boolean releaseBed(int bedNumber) {
        if (bedNumber < 1 || bedNumber > 20) {
            System.out.println("Error: Bed number must be between 1 and 20.");
            return false;
        }

        int row = getRow(bedNumber);
        int col = getCol(bedNumber);

        // Return early if empty to prevent NullPointerException
        if (ward[row][col] == null) {
            System.out.println("Error: Bed " + formatBedCode(bedNumber) + " is already empty!");
            return false;
        }

        System.out.println("Success: Released bed " + formatBedCode(bedNumber)
                + " (Previously assigned to " + ward[row][col].getFirstName() + ").");

        ward[row][col].setBedNumber("Unassigned");
        ward[row][col].setWardNumber("Unassigned");
        ward[row][col] = null;
        return true;
    }

    public void displayWardLayout() {
        System.out.println("\n--- COMPLETE WARD BED LAYOUT (4x5) ---");
        int bedCounter = 1;

        for (int i = 0; i < ward.length; i++) {
            for (int j = 0; j < ward[i].length; j++) {
                String bedCode = formatBedCode(bedCounter);
                if (ward[i][j] == null) {
                    System.out.print("[" + bedCode + ": Available] ");
                } else {
                    System.out.print("[" + bedCode + ": " + ward[i][j].getPatientId() + "]");
                }
                bedCounter++;
            }
            System.out.println();
        }
        System.out.println();
    }

    public void displayAvailableBeds() {
        System.out.println("\n--- AVAILABLE BEDS ---");
        boolean foundAny = false;
        int bedCounter = 1;

        for (int i = 0; i < ward.length; i++) {
            for (int j = 0; j < ward[i].length; j++) {
                if (ward[i][j] == null) {
                    System.out.print(formatBedCode(bedCounter) + " ");
                    foundAny = true;
                }
                bedCounter++;
            }
        }

        if (!foundAny) {
            System.out.print("No available beds. Ward is FULL!");
        }
        System.out.println();
    }

    public void displayOccupiedBeds() {
        System.out.println("\n--- OCCUPIED BEDS ---");
        boolean foundAny = false;
        int bedCounter = 1;

        for (int i = 0; i < ward.length; i++) {
            for (int j = 0; j < ward[i].length; j++) {
                if (ward[i][j] != null) {
                    System.out.println(formatBedCode(bedCounter) + " -> Occupied by: "
                            + ward[i][j].getFirstName() + " " + ward[i][j].getLastName()
                            + " (" + ward[i][j].getPatientId() + ")");
                    foundAny = true;
                }
                bedCounter++;
            }
        }
        if (!foundAny) {
            System.out.println("No beds are currently occupied.");
        }
    }

    public int getOccupiedCount() {
        int count = 0;
        for (int i = 0; i < ward.length; i++) {
            for (int j = 0; j < ward[i].length; j++) {
                if (ward[i][j] != null) {
                    count++;
                }
            }
        }
        return count;
    }
}
