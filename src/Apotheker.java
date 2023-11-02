import java.util.Scanner;

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
        MedicationController medicationController = new MedicationController();
        PatientController patientController = new PatientController();
        Scanner userInput = new Scanner(System.in);
        boolean runner = true;
        while (runner){
            try {
                System.out.println("\u001B[34mselecteer uw keuze om door te gaan\u001B[0m \n" +
                        "\n" +
                        "[" + Colors.BLUE + "1" + Colors.RESET + "]" + "medicatie toevoegen \n"+
                        "[" + Colors.BLUE + "2" + Colors.RESET + "]" + "Medicatie bijwerken \n" +
                        "[" + Colors.BLUE + "3" + Colors.RESET + "]" + "Bekijk de patiëntenlijst \n" +
                        "[" + Colors.BLUE + "4" + Colors.RESET + "]" + "Patiëntgegevens bewerken \n" +
                        "[" + Colors.BLUE + "5" + Colors.RESET + "]" + "Zoek op patiëntnummer \n" +
                        "[" + Colors.BLUE + "6" + Colors.RESET + "]" + "Exit");
                int choice = Integer.parseInt(userInput.nextLine());
                switch (choice) {
                    case addMedication:
                        medicationController.insertMedications();
                        runner = false;
                        break;
                    case updateMedication:
                        medicationController.updateMedication();
                        runner = false;
                        break;
                    case ViewPatientList:
                        patientController.displayPatientList();
                        runner = false;
                        break;
                    case EditPatientData:
                        patientController.updatePatient();
                        runner = false;
                        break;
                    case SearchBypatientNumber:
                        patientController.searchPatientById();
                        runner = false;
                        break;
                    case Exit:
                        runner = false;
                        ProgrammaExit.programmaExit();
                        break;
                    default:
                        break;
                }
            }catch(NumberFormatException e){
                System.err.println("\u001B[31mOngeldige invoer. Voer een geldig getal in.\u001B[0m");
            }
        }
    }
    static final int BackToTheMainMenu = 1;
    public static void BackToTheMainMenu() {
        Scanner userInput = new Scanner(System.in);
        boolean runner = true;
        while (runner) {
            try {
                System.out.println("Voer het getal 1 om terug te gaan naar main menu");
                int choice = Integer.parseInt(userInput.nextLine());
                if (choice==BackToTheMainMenu) {
                    runner = false;
                    apothekerStartMenu();
                } else {
                    System.out.println(Colors.RED+"Onjuist getal ingevoerd probeer nog een keer"+Colors.RESET);
                    runner = true;

                }
            } catch (NumberFormatException e) {
                System.err.println("\u001B[31mOngeldige invoer. Voer een geldig getal in.\u001B[0m");
            }
        }
    }




}
