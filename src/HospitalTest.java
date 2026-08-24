import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HospitalTest {

    private PatientManager patientManager;
    private BedManager bedManager;

    @BeforeEach
    public void setUp() {
        // Initializes fresh instances before every test run
        patientManager = new PatientManager();
        bedManager = new BedManager();
    }

    @Test
    public void testRegisterPatientSuccess() {
        Patient p = new Patient("P001", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);

        assertTrue(patientManager.registerPatient(p));
        assertEquals(1, patientManager.getTotalPatientsCount());
        assertNotNull(patientManager.searchPatient("P001"));
    }

    @Test
    public void testRegisterDuplicatePatientFails() {
        Patient p1 = new Patient("P001", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P001", "Jane", "Smith", 25, "Female", "Fever", PatientCategory.OUTPATIENT);

        assertTrue(patientManager.registerPatient(p1));
        assertFalse(patientManager.registerPatient(p2), "Registering duplicate ID should return false");
        assertEquals(1, patientManager.getTotalPatientsCount());
    }

    @Test
    public void testSearchPatientNotFound() {
        assertNull(patientManager.searchPatient("INVALID_ID"));
    }

    @Test
    public void testUpdatePatientDetails() {
        Patient p = new Patient("P002", "Mark", "Twain", 45, "Male", "Cough", PatientCategory.OUTPATIENT);
        patientManager.registerPatient(p);

        boolean updated = patientManager.updatePatient("P002", "Marcus", "Twain", 46, "Male", "Recovered");
        assertTrue(updated);

        Patient updatedPatient = patientManager.searchPatient("P002");
        assertEquals("Marcus", updatedPatient.getFirstName());
        assertEquals(46, updatedPatient.getAge());
        assertEquals("Recovered", updatedPatient.getMedicalCondition());
    }

    @Test
    public void testDeletePatient() {
        Patient p = new Patient("P003", "Sarah", "Connor", 29, "Female", "Observation", PatientCategory.EMERGENCY);
        patientManager.registerPatient(p);

        assertTrue(patientManager.deletePatient("P003"));
        assertNull(patientManager.searchPatient("P003"));
        assertEquals(0, patientManager.getTotalPatientsCount());
    }

    @Test
    public void testSortPatientsById() {
        Patient p1 = new Patient("P003", "Charlie", "Brown", 10, "Male", "Asthma", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P001", "Alice", "Wonder", 22, "Female", "Allergy", PatientCategory.OUTPATIENT);
        Patient p3 = new Patient("P002", "Bob", "Builder", 35, "Male", "Fracture", PatientCategory.OUTPATIENT);

        patientManager.registerPatient(p1);
        patientManager.registerPatient(p2);
        patientManager.registerPatient(p3);

        patientManager.sortById();

        assertEquals("P001", patientManager.getPatientList().get(0).getPatientId());
        assertEquals("P002", patientManager.getPatientList().get(1).getPatientId());
        assertEquals("P003", patientManager.getPatientList().get(2).getPatientId());
    }

    @Test
    public void testSortPatientsByLastName() {
        Patient p1 = new Patient("P001", "John", "Zebra", 40, "Male", "Checkup", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P002", "Anna", "Apple", 25, "Female", "Checkup", PatientCategory.OUTPATIENT);

        patientManager.registerPatient(p1);
        patientManager.registerPatient(p2);

        patientManager.sortByLastName();

        assertEquals("Apple", patientManager.getPatientList().get(0).getLastName());
        assertEquals("Zebra", patientManager.getPatientList().get(1).getLastName());
    }

    // ==========================================
    // BED MANAGER TESTS
    // ==========================================

    @Test
    public void testManualBedAllocationSuccess() {
        Inpatient inpatient = new Inpatient("P004", "David", "Miller", 50, "Male", "Surgery", "Unassigned", "Unassigned");

        // Allocate Bed 5 (Row 0, Col 4 -> B05)
        assertTrue(bedManager.allocateBed(inpatient, 5));
        assertEquals("B05", inpatient.getBedNumber());
        assertEquals("General Ward", inpatient.getWardNumber());
        assertEquals(1, bedManager.getOccupiedCount());
    }

    @Test
    public void testManualBedAllocationAlreadyOccupied() {
        Inpatient p1 = new Inpatient("P005", "Tom", "Hardy", 38, "Male", "Cardiac", "Unassigned", "Unassigned");
        Inpatient p2 = new Inpatient("P006", "Emma", "Stone", 30, "Female", "Migraine", "Unassigned", "Unassigned");

        bedManager.allocateBed(p1, 1);

        // Attempting to assign p2 to bed 1 should fail
        assertFalse(bedManager.allocateBed(p2, 1));
        assertEquals(1, bedManager.getOccupiedCount());
    }

    @Test
    public void testAutoBedAllocationSuccess() {
        Inpatient inpatient = new Inpatient("P010", "Alex", "Taylor", 35, "Female", "Observation", "Unassigned", "Unassigned");

        // First bed available in an empty ward is Bed 1 (B01)
        assertTrue(bedManager.allocateBedAuto(inpatient));
        assertEquals("B01", inpatient.getBedNumber());
        assertEquals(1, bedManager.getOccupiedCount());
    }

    @Test
    public void testAutoBedAllocationSequential() {
        Inpatient p1 = new Inpatient("P011", "Sam", "Kerr", 28, "Female", "Recovery", "Unassigned", "Unassigned");
        Inpatient p2 = new Inpatient("P012", "Jordan", "Lee", 31, "Male", "Surgery", "Unassigned", "Unassigned");

        bedManager.allocateBedAuto(p1); // Assigned B01
        bedManager.allocateBedAuto(p2); // Assigned B02

        assertEquals("B01", p1.getBedNumber());
        assertEquals("B02", p2.getBedNumber());
        assertEquals(2, bedManager.getOccupiedCount());
    }

    @Test
    public void testAutoBedAllocationWardFull() {
        // Fill all 20 beds first
        for (int i = 1; i <= 20; i++) {
            Inpatient p = new Inpatient("P" + i, "Patient" + i, "Test", 30, "Male", "Condition", "Unassigned", "Unassigned");
            bedManager.allocateBed(p, i);
        }

        assertEquals(20, bedManager.getOccupiedCount());

        // Attempt automatic allocation on a full ward
        Inpatient extraPatient = new Inpatient("P21", "Overflow", "Patient", 40, "Female", "Emergency", "Unassigned", "Unassigned");
        assertFalse(bedManager.allocateBedAuto(extraPatient));
    }

    @Test
    public void testReleaseBedSuccess() {
        Inpatient inpatient = new Inpatient("P007", "Chris", "Evans", 42, "Male", "Recovery", "Unassigned", "Unassigned");
        bedManager.allocateBed(inpatient, 3);

        assertTrue(bedManager.releaseBed(3));
        assertEquals("Unassigned", inpatient.getBedNumber());
        assertEquals(0, bedManager.getOccupiedCount());
    }

    @Test
    public void testReleaseBedAlreadyEmpty() {
        // Bed 10 is empty by default
        assertFalse(bedManager.releaseBed(10), "Releasing an empty bed should return false");
    }
}