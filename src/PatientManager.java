import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PatientManager {
    private ArrayList<Patient> patientList = new ArrayList<>();

    public boolean isIdDuplicate(String patientId) {
        for (Patient p : patientList) {
            if (p.getPatientId().equalsIgnoreCase(patientId)) {
                return true;
            }
        }
        return false;
    }

    public boolean registerPatient(Patient patient) {
        if (isIdDuplicate(patient.getPatientId())) {
            System.out.println("Error: Patient with ID " + patient.getPatientId() + " already exists!");
            return false;
        }

        patientList.add(patient);
            System.out.println("Success: Patient " + patient.getFirstName() + " " + patient.getLastName() + " registered!");
            return true;
    }

    public Patient searchPatient(String patientId) {
        for (Patient p : patientList) {
            if (p.getPatientId().equalsIgnoreCase(patientId)) {
                return p;
            }
        }
        return null;
    }

    public boolean updatePatient(String patientId, String newFirstName, String newLastName, int newAge, String newGender, String newCondition) {
        Patient p = searchPatient(patientId);
        if (p == null) {
            System.out.println("Error: Patient with ID " + patientId + " not found.");
            return false;
        }

        p.setFirstName(newFirstName);
        p.setLastName(newLastName);
        p.setAge(newAge);
        p.setGender(newGender);
        p.setMedicalCondition(newCondition);
        System.out.println("Success: Details updated for patient ID " + patientId);
        return true;
    }

    public boolean deletePatient(String patientId) {
        Patient p = searchPatient(patientId);
        if (p == null) {
            System.out.println("Error: Patient with ID " + patientId + " not found.");
            return false;
        }

        patientList.remove(p);
        System.out.println("Success: Patient ID " + patientId + " removed from the system.");
        return true;
    }

    public void displayAllPatients() {
        if (patientList.isEmpty()) {
            System.out.println("No patients currently registered in the system.");
            return;
        }

        System.out.println("\n--- ALL REGISTERED PATIENTS ---");
        for (Patient p : patientList) {
            p.displayDetails();
            System.out.println("-------------------------------------");
        }
    }

    public void sortById() {
        Collections.sort(patientList, Comparator.comparing(Patient::getPatientId));
        System.out.println("Success: Patients sorted alphabetically by Patient ID.");
    }

    public void sortByLastName() {
        Collections.sort(patientList, Comparator.comparing(Patient::getLastName));
        System.out.println("Success: Patients sorted alphabetically by Last Name.");
    }

    public int getTotalPatientsCount() {
        return patientList.size();
    }

    public ArrayList<Patient> getPatientList() {
        return patientList;
    }


}
