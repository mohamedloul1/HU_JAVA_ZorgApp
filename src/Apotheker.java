public class Apotheker extends Users {
    public Apotheker(int userId, String UserName) {
        super(userId, UserName);
    }
    static final int addMedication= 1;
    static final int updateMedication = 2;
    static final int ViewPatientList= 3;
    static final int EditPatientData= 4;
    static final int SearchBypatientNumber= 5;
    static final int Exit= 6;


    public static void apothekerStartMenu(){
        System.out.println("Jij bent als" + Colors.YELLOW_BOLD+" Apotheker "+Colors.RESET + "ingelogd");

    }


}
