public class Inpatient extends Patient {
    private String wardNumber;
    private String bedNumber;

    public Inpatient(String patientId, String firstname, String lastName, int age, String gender, String medicalCondition, String wardNumber, String bedNumber) {
        super(patientId, firstname, lastName, age, gender, medicalCondition, PatientCategory.INPATIENT);
        this.wardNumber = wardNumber;
        this.bedNumber = bedNumber;
    }

    public String getWardNumber() {return wardNumber;}
    public void setWardNumber(String wardNumber ) {
        this.wardNumber = wardNumber;
    }

    public String getBedNumber() {return bedNumber;}
    public void setBedNumber( String bedNumber ) {
        this.bedNumber = bedNumber;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Ward Number: " + wardNumber);
        System.out.println("Bed Number: " + bedNumber);
    }
}
