import com.sun.jdi.connect.Connector;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MedicationController {

    private Connection connection;

    public  MedicationController (){
        try {
            connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fout bij het tot stand brengen van de databaseverbinding." +
                    " Zorg ervoor dat de database actief is en controleer uw databaseconfiguratie.");
        }
    }
List<Medication> medicationsList = new ArrayList<>();
//    public void getAllMedications() {
//        try (Statement statement = connection.createStatement()) {
//            String sqlQuery = "SELECT * FROM Medications";
//            ResultSet resultSet = statement.executeQuery(sqlQuery);
//
//            if (!resultSet.isBeforeFirst()) {
//                System.out.println("\u001B[31mDe tabel [Medications] is leeg\u001B[0m");
//            }else {
//                while (resultSet.next()) {
//                    Medication medication = new Medication(resultSet.getInt("MedicationID"),
//                            resultSet.getString("MedicationName"),
//                            resultSet.getString("Dosage"),
//                            resultSet.getInt("PatientID"));
//                    medicationsList.add(medication);
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Er is een SQL-fout opgetreden bij het ophalen van patiëntgegevens:");
//            e.printStackTrace();
//        }
//    }

    public void getAllMedicationsWithNames() {
        Menus menus = new Menus();
        try (Statement statement = connection.createStatement()) {
            String sqlQuery = "SELECT M.*, P.FirstName, P.LastName " +
                    "FROM Medications M " +
                    "INNER JOIN Patient P ON M.PatientID = P.PatientID";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            if (!resultSet.isBeforeFirst()) {
                System.out.println("\u001B[31mDe tabel [Medications] is leeg\u001B[0m");
            } else {
                while (resultSet.next()) {
                    Medication medication = new Medication(resultSet.getInt("MedicationID"),
                            resultSet.getString("MedicationName"),
                            resultSet.getString("Dosage"),
                            resultSet.getInt("PatientID"));

                    String patientName = resultSet.getString("FirstName");
                    String patientLastName = resultSet.getString("LastName");

                    System.out.format("--------------------Medicatie Nummer:"+Colors.PURPLE+"%d" + Colors.RESET +"--------------------%n"+
                                      "Voornaam patiënt:"+Colors.PURPLE+"%s %n" +Colors.RESET+
                                      "Achternaam patiënt:"+Colors.PURPLE+"%s %n"+Colors.RESET+
                                      "Patiëntnummer:"+Colors.PURPLE +"%s %n" + Colors.RESET +
                                      "Medicatie naam:"+ Colors.PURPLE+ "%s %n" + Colors.RESET +
                                      "Dosering:"+ Colors.PURPLE+"%s %n" + Colors.RESET ,

                            medication.getMedicationID(),
                            patientName,
                            patientLastName,
                            medication.getPatientID(),
                            medication.getMedicationName(),
                            medication.getDosage()
                    );
                    medicationsList.add(medication);
                }
            }
        } catch (SQLException e) {
            System.err.println("Er is een SQL-fout opgetreden bij het ophalen van patiëntgegevens:");
            e.printStackTrace();
        }
    }


    public void insertMedications() {
        AuthenticationServer authenticationServer = new AuthenticationServer();
        PatientController patientController =new PatientController();
        Menus menus = new Menus();
        Scanner userInput = new Scanner(System.in);
        boolean runner = true;

        try {
            do {
                try {
                    patientController.displayPatientListWithoutbackFunction();
                    System.out.println("\n");
                    System.out.print("------------------------------------------------------------- \n");
                    System.out.print("Selecteer de patiënt aan wie u medicatie wilt toevoegen: \n");
                    int patientID = Integer.parseInt(userInput.nextLine());
                    System.out.print("Voer de naam van de nieuwe medicatie in: ");
                    String medicationName = userInput.nextLine();
                    System.out.print("Voer de dosering van de medicatie in: ");
                    String dosage = userInput.nextLine();
                    String sqlInsert = "INSERT INTO Medications (MedicationName," +
                            " Dosage," +
                            " PatientID)" +
                            " VALUES (?,?,?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
                        preparedStatement.setString(1, medicationName);
                        preparedStatement.setString(2, dosage);
                        preparedStatement.setInt(3, patientID);
                        int affectedRows = preparedStatement.executeUpdate();

                        if (affectedRows > 0) {
                            System.out.println("\n"+"Nieuwe medicatie succesvol toegevoegd 👇👇\n"+
                                    "Medicatie Naam: " + Colors.PURPLE + medicationName + Colors.RESET + "\n"+
                                    "Dosering: " + Colors.PURPLE + dosage + Colors.RESET+ "\n"+
                                    "Patiëntnummer: " + Colors.PURPLE + patientID + Colors.RESET);
                        } else {
                            System.out.println(Colors.RED+"Er is iets fout gegaan bij het toevoegen van de medicatie."+Colors.RESET);
                        }
                    }
                    // Vraag of de gebruiker nog een rol wil toevoegen
                    System.out.print("Wil je nog een andere medicatie toevoegen?:"+"(\u001B[32mja\u001B[0m"+ "/" +"\u001B[31mnee\u001B[0m):");
                    String input = userInput.nextLine().toLowerCase();
                    if (input.equals("ja")) {
                        System.out.println("Volg de stappen");
                    }
                    else {
                        runner = false;

                        System.out.println("je gaat terug naar hoofdmenu");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31m Ongeldige invoer voor Patiëntnummer. Voer een geldig getal in.\u001B[0m");
                }
            } while (runner) ;
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            userInput.close();
        }
    }
    public void deleteMedications() {
        PatientController patientController=new PatientController();
        Menus menus = new Menus();
        Scanner userInput = new Scanner(System.in);
        var runner = true;
        try {
            this.getAllMedicationsWithNames();
            do {
                System.out.print("Voer het ID van de medicatie in die je wilt verwijderen: ");
                try {
                    int medicationId = Integer.parseInt(userInput.nextLine());
                    boolean isDeleted = deleteMedicationIdById(medicationId);

                    if (isDeleted) {
                        System.out.println("\u001B[32mMedicatie succesvol verwijderd.\u001B[0m");

                        System.out.print("Wil je nog een andere Medicatie verwijderen?" + "(\u001B[32mja\u001B[0m" + "/" + "\u001B[31mnee\u001B[0m):");
                        String input = userInput.nextLine().toLowerCase();
                        if (!input.equals("ja")) {
                            System.out.println("\u001B[33mTerug naar hoofdmenu\u001B[0m");
                            runner = false;
                            menus.medicationsMenu();
                        }
                    } else {
                        System.out.println("\u001B[32mPropeer nog een keer\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31m Ongeldige invoer: Voer een geldig geheel getal in. \u001B[0m");
                }

            } while (runner);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userInput.close();
        }
    }
    public boolean deleteMedicationIdById(int medicationId) {
        Menus menus = new Menus();
        try {

            String sqlSelect = "SELECT * FROM Medications WHERE MedicationID = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {
                selectStatement.setInt(1, medicationId);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    // Bevestiging vragen voor verwijderen
                    Scanner userInput = new Scanner(System.in);
                    System.out.print("Wil je deze Medicatie verwijderen?" + "(\u001B[32mja\u001B[0m" + "/" + "\u001B[31mnee\u001B[0m):");
                    String confirmation = userInput.nextLine().toLowerCase();

                    if (confirmation.equals("ja")) {
                        // Verwijder de patiënt
                        String sqlDelete = "DELETE FROM Medications WHERE MedicationID=?";
                        try (PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete)) {
                            deleteStatement.setInt(1, medicationId);
                            int affectedRows = deleteStatement.executeUpdate();
                            return affectedRows > 0;
                        }
                    } else {
                        System.out.println("\u001B[32mVerwijdering geannuleerd.\u001B[0m");
                        System.out.println("\u001B[33mTerug naar hoofdmenu\u001B[0m");
                        menus.medicationsMenu();
                    }
                } else {
                    System.out.println("\u001B[31m Medicatie met opgegeven ID niet gevonden.\u001B[0m");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateMedication() {
        PatientController patientController = new PatientController();
        Menus menus = new Menus();
        Scanner userInput = new Scanner(System.in);
        var runner = true;
        try {
            patientController.displayPatientListWithoutbackFunction();
            do {
                System.out.print("Voer het nummer van de medicatie in die je wilt bijwerken: ");
                try {
                    int MedicationID = Integer.parseInt(userInput.nextLine());
                    // Voer hier de rest van je code uit voor het bijwerken van de patiënt
                    String sqlSelect = "SELECT * FROM Medications WHERE MedicationID = ?";
                    try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {
                        selectStatement.setInt(1, MedicationID);
                        ResultSet resultSet = selectStatement.executeQuery();

                        if (resultSet.next()) {
                            String currentMedicationName = resultSet.getString("MedicationName");
                            String currentDosage = resultSet.getString("Dosage");
                            int currentPatientID = resultSet.getInt("PatientID");

                            System.out.println("\n");
                            System.out.println("------------Huidige medicatie gegevens van de patiëntnummer:" + Colors.PURPLE+ currentPatientID + Colors.RESET+"------------");
                            System.out.println("MedicationName: "+ Colors.PURPLE + currentMedicationName + Colors.RESET);
                            System.out.println("Dosage: "+ Colors.PURPLE + currentDosage + Colors.RESET);
                            System.out.println("Patientnummer: "+ Colors.PURPLE + currentPatientID + Colors.RESET);

                            // Vraag om nieuwe gegevens voor de patiënt
                            System.out.print("Voer de nieuwe medicatienaam in (druk op Enter om niet bij te werken): ");
                            String newMedicationName = userInput.nextLine();
                            if (!newMedicationName.isEmpty()) {
                                currentMedicationName = newMedicationName;
                            }

                            System.out.print("Voer de nieuwe dosering in (druk op Enter om niet bij te werken): ");
                            String newDosage = userInput.nextLine();
                            if (!newDosage.isEmpty()) {
                                currentDosage = newDosage;
                            }
                            System.out.print("Voer de nieuwe Patinetnummer in (druk op Enter om niet bij te werken): ");
                            String newPatientID = userInput.nextLine();
                            if (!newPatientID.isEmpty()) {
                                try {
                                    currentPatientID = Integer.parseInt(newPatientID);
                                } catch (NumberFormatException e) {
                                    System.out.println("\u001B[31m Ongeldige invoer voor Patientnummer. De new Patientnummer is niet bijgewerkt.\u001B[0m");
                                }
                            }
                            String sqlUpdate = "UPDATE Medications SET MedicationName=?, Dosage=?, PatientID=? WHERE MedicationID=?";
                            try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
                                updateStatement.setString(1, currentMedicationName);
                                updateStatement.setString(2, currentDosage);
                                updateStatement.setInt(3, currentPatientID);
                                updateStatement.setInt(4, MedicationID);
                                int affectedRows = updateStatement.executeUpdate();
                                if (affectedRows > 0) {
                                    System.out.println("\u001B[32mMedicatie is succesvol bijgewerkt.\u001B[0m");
                                } else {
                                    System.out.println("\u001B[31m Er is iets fout gegaan bij het bijwerken van de Medicatie.\u001B[0m");
                                }
                            }
                            System.out.print("Wil je nog een andere Medicatie bijwerken"+"(\u001B[32mja\u001B[0m" + "/" + "\u001B[31mnee\u001B[0m):");
                            String input = userInput.nextLine().toLowerCase();
                            if (!input.equals("ja")) {
                                runner = false;
                                System.out.println("je gaat terug naar hoofdmenu");                            }
                        } else {
                            System.out.println("\u001B[31m Medicatie met opgegeven ID niet gevonden.\u001B[0m");
                        }
                    }
                } catch(NumberFormatException e){
                    System.out.println("\u001B[31m Ongeldige invoer: Voer een geldig geheel getal in.\u001B[0m");
                }
            }while (runner);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userInput.close();
        }
    }






}
