import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class PatientController {
    AuthenticationServer authenticationServer = new AuthenticationServer();
    Users users = authenticationServer.login("Fysiotherapeut");
    private Connection connection;

    public PatientController (){
        try {
            connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fout bij het tot stand brengen van de databaseverbinding." +
                    " Zorg ervoor dat de database actief is en controleer uw databaseconfiguratie.");
        }
    }

    List<Patient>patintList=new ArrayList<>();
    Menus menus = new Menus();

    public  void retrievePatient() {
        try (Statement statement = connection.createStatement()) {
//            String sqlQuery = "SELECT * FROM Patient";
            String sqlQuery = "SELECT Medications.MedicationID, Medications.MedicationName, Medications.Dosage, Patient.PatientID, Patient.FirstName, Patient.LastName, Patient.DateOfBirth, Patient.PatientHeight, Patient.PatienWeight " +
                    "FROM Medications " +
                    "INNER JOIN Patient ON Medications.PatientID = Patient.PatientID";

            ResultSet resultSet = statement.executeQuery(sqlQuery);

            if (!resultSet.isBeforeFirst()) {
                System.out.println("\u001B[31mDe tabel [Patient] is leeg\u001B[0m");
            }else {
                while (resultSet.next()) {
                    Patient patient = new Patient(
                            resultSet.getInt("PatientID"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getDate("DateOfBirth").toLocalDate(),
                            resultSet.getDouble("PatientHeight"),
                            resultSet.getDouble("PatienWeight"));
                    Medication medication = new Medication(
                            resultSet.getInt("MedicationID"),
                            resultSet.getString("MedicationName"),
                            resultSet.getString("Dosage"),
                            resultSet.getInt("PatientID"));
                    patient.setMedication(medication);
                    patintList.add(patient);
                }
            }

        } catch (SQLException e) {
            System.err.println("Er is een SQL-fout opgetreden bij het ophalen van patiëntgegevens:" +e);
            e.printStackTrace();
        }
    }

    public void displayPatientList( ) {
        this.retrievePatient();
        try {
            for (Patient patient : patintList) {
                double height = patient.getPatientHeight();
                double weight = patient.getPatientWeight();
                LocalDate newDateOfBirth = patient.getDateOfBirth();
                int age = calcAgeInYears(newDateOfBirth);
                double bmi = calculateBMI(height, weight);
                Medication medication = patient.getMedication();

                System.out.format("------------------------ Patiëntnummer:" + Colors.PURPLE+"%d"+ Colors.RESET+ "------------------------ %n" , patient.getPatientID());
                System.out.format("Voornaam:" + Colors.PURPLE+ "%s"+Colors.RESET+"%n", patient.getFirstName());
                System.out.format("Achternaam:" + Colors.PURPLE + "%s"+Colors.RESET+" %n", patient.getLastName());
                System.out.format("Geboortedatum:"+Colors.PURPLE +"%s"+Colors.RESET+"%n" , patient.getDateOfBirth());
                System.out.format("Hoogte:" + Colors.PURPLE+ "%s"+Colors.RESET+"%n", patient.getPatientHeight());
                System.out.format("Gewicht:"+Colors.PURPLE+"%s"+Colors.RESET+"%n", patient.getPatientWeight());
                System.out.format("BMI is:"+Colors.PURPLE+"%s"+Colors.RESET+"kg/m² %n", bmi);
                System.out.format("Leeftijd:"+Colors.PURPLE+"%s"+Colors.RESET+"%n", age);
                System.out.format("Medicatie ID:"+Colors.PURPLE+ "%d%n"+Colors.RESET, medication.getMedicationID());
                System.out.format("Medicatie naam:"+Colors.PURPLE+ "%s%n"+Colors.RESET, medication.getMedicationName());
                System.out.format("Dosering:"+Colors.PURPLE+" %s%n"+Colors.RESET, medication.getDosage());
            }
            Administration.BackToTheMainMenu(users);


        } catch (Exception e) {
            System.err.println("Er is een fout opgetreden bij het ophalen van patiëntgegevens: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void displayPatientListWithoutbackFunction( ) {
        this.retrievePatient();
        try {
            for (Patient patient : patintList) {
                double height = patient.getPatientHeight();
                double weight = patient.getPatientWeight();
                LocalDate newDateOfBirth = patient.getDateOfBirth();
                int age = calcAgeInYears(newDateOfBirth);
                double bmi = calculateBMI(height, weight);
                Medication medication = patient.getMedication();

                System.out.format("------------------------ Patiëntnummer:" + Colors.PURPLE+"%d"+ Colors.RESET+ "------------------------ %n" , patient.getPatientID());
                System.out.format("Voornaam:" + Colors.PURPLE+ "%s"+Colors.RESET+"%n", patient.getFirstName());
                System.out.format("Achternaam:" + Colors.PURPLE + "%s"+Colors.RESET+" %n", patient.getLastName());
                System.out.format("Geboortedatum:"+Colors.PURPLE +"%s"+Colors.RESET+"%n" , patient.getDateOfBirth());
                System.out.format("Hoogte:" + Colors.PURPLE+ "%s"+Colors.RESET+"%n", patient.getPatientHeight());
                System.out.format("Gewicht:"+Colors.PURPLE+"%s"+Colors.RESET+"%n", patient.getPatientWeight());
                System.out.format("BMI is:"+Colors.PURPLE+"%s"+Colors.RESET+"kg/m² %n", bmi);
                System.out.format("Leeftijd:"+Colors.PURPLE+"%s"+Colors.RESET+"%n", age);
                System.out.format("Medicatie nummer:"+Colors.PURPLE+ "%d%n"+Colors.RESET, medication.getMedicationID());
                System.out.format("Medicatie naam:"+Colors.PURPLE+ "%s%n"+Colors.RESET, medication.getMedicationName());
                System.out.format("Dosering:"+Colors.PURPLE+" %s%n"+Colors.RESET, medication.getDosage());
            }
        } catch (Exception e) {
            System.err.println("Er is een fout opgetreden bij het ophalen van patiëntgegevens: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public double calculateBMI(double height, double weight) {
        double heightInMeters = height / 100.0;
        double bmi = weight / (heightInMeters * heightInMeters);
        return Math.round(bmi * 100.0) / 100.0;
    }

    public int calcAgeInYears(LocalDate newDateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(newDateOfBirth, currentDate);
        return period.getYears();
    }

    public void insertPatient() {
        Scanner userInput = new Scanner(System.in);
        var runner = true;

        try {
                do {
                    System.out.print("Voer de voornaam van de nieuwe patient in: ");
                    String firstName = userInput.nextLine();
                    System.out.print("Voer de achternaam van de nieuwe patient  in: ");
                    String lastName = userInput.nextLine();
//                    System.out.print("Voer de geboortedatum van de nieuwe patient in (dd-MM-yyyy): ");
                    String dateOfBirth = "";
                    Date date = null;
                    while (date == null) {
                        System.out.print("Voer de geboortedatum van de nieuwe patient in (dd-MM-yyyy): ");
                        dateOfBirth = userInput.nextLine();
                        try {
                            date = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfBirth);
                        } catch (ParseException e) {
                            System.out.println("\u001B[31m Ongeldige datumindeling. Voer de datum in als dd-MM-yyyy.\u001B[0m");
                        }
                    }
                    double patientHeight = 0.0;
                    while (patientHeight <= 0) {
                        System.out.print("Voer de hoogte van de nieuwe patient in: ");
                        try {
                            patientHeight = Double.parseDouble(userInput.nextLine());
                            if (patientHeight <= 0) {
                                System.out.println("Hoogte moet groter zijn dan 0.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31m Ongeldige invoer voor hoogte. Voer een geldig getal in.\u001B[0m");
                        }
                    }
                    double patientWeight = 0.0;
                    while (patientWeight <= 0) {
                        System.out.print("Voer de gewicht van de nieuwe patient in: ");
                        try {
                            patientWeight = Double.parseDouble(userInput.nextLine());
                            if (patientWeight <= 0) {
                                System.out.println("Gewicht moet groter zijn dan 0.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31m Ongeldige invoer voor gewicht. Voer een geldig getal in.\u001B[0m");
                        }
                    }
                    String sqlInsert = "INSERT INTO Patient (FirstName," +
                            " LastName," +
                            " DateOfBirth," +
                            " PatientHeight," +
                            " PatienWeight)" +
                            " VALUES (?,?,?,?,?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
                        preparedStatement.setString(1, firstName);
                        preparedStatement.setString(2, lastName);
                        preparedStatement.setDate(3, new java.sql.Date(date.getTime()));
                        preparedStatement.setDouble(4, patientHeight);
                        preparedStatement.setDouble(5, patientWeight);
                        int affectedRows = preparedStatement.executeUpdate();

                        if (affectedRows > 0) {
                            System.out.println("Nieuwe patient succesvol toegevoegd 👇👇 \n " + "Voornaam: " +"\u001B[32m"+ firstName +"\u001B[0m \n" +
                                    "Achternamm: " +"\u001B[32m"+ lastName + "\u001B[0m \n" + "Geboortedatum: "+"\u001B[32m" + dateOfBirth + "\u001B[0m \n" +
                                    "Hoogte: " +"\u001B[32m"+ patientHeight + "\u001B[0m \n" +
                                    "Gewicht: " + "\u001B[32m" + patientWeight+ "\u001B[0m");
                        } else {
                            System.out.println("\u001B[31m Er is iets fout gegaan bij het toevoegen van de rol.\u001B[0m");
                        }
                    }
                    // Vraag of de gebruiker nog een rol wil toevoegen
                    System.out.print("\u001B[34m Wil je nog een andere rol toevoegen? (ja/nee): \u001B[0m");
                    String input = userInput.nextLine().toLowerCase();
                    if (!input.equals("ja")) {
                        runner = false;
                        System.out.println("\u001B[33mTerug naar Patiënten menu\u001B[0m");
                        menus.patientMenu();                    }
                } while (runner) ;

        }catch (Exception e) {
                e.printStackTrace();
        } finally {
            userInput.close();
        }
    }
    public void updatePatient() {
        Scanner userInput = new Scanner(System.in);
        var runner = true;
        try {
            this.displayPatientListWithoutbackFunction();
            do {
                System.out.print("Voer het ID van de patiënt in die je wilt bijwerken: ");
                try {
                    int patientId = Integer.parseInt(userInput.nextLine());
                    // Voer hier de rest van je code uit voor het bijwerken van de patiënt
                    String sqlSelect = "SELECT * FROM Patient WHERE PatientID = ?";
                    try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {
                        selectStatement.setInt(1, patientId);
                        ResultSet resultSet = selectStatement.executeQuery();

                        if (resultSet.next()) {
                            // Patiënt gevonden, haal de bestaande gegevens op
                            String currentFirstName = resultSet.getString("FirstName");
                            String currentLastName = resultSet.getString("LastName");
                            Date currentDateOfBirth = resultSet.getDate("DateOfBirth");
                            double currentPatientHeight = resultSet.getDouble("PatientHeight");
                            double currentPatientWeight = resultSet.getDouble("PatienWeight");

                            System.out.println("------------------------------------------------------");
                            System.out.println(Colors.BLUE+"Huidige gegevens van de patiënt:"+Colors.RESET);
                            System.out.println("Voornaam: "+Colors.PURPLE+ currentFirstName +Colors.RESET);
                            System.out.println("Achternaam: "+Colors.PURPLE+ currentLastName +Colors.RESET);
                            System.out.println("Geboortedatum: "+Colors.PURPLE+ currentDateOfBirth +Colors.RESET);
                            System.out.println("Hoogte: " +Colors.PURPLE+ currentPatientHeight +Colors.RESET);
                            System.out.println("Gewicht: "+Colors.PURPLE+ currentPatientWeight +Colors.RESET);

                            // Vraag om nieuwe gegevens voor de patiënt
                            System.out.print("Voer de nieuwe voornaam in (druk op Enter om niet bij te werken): ");
                            String newFirstName = userInput.nextLine();
                            if (!newFirstName.isEmpty()) {
                                currentFirstName = newFirstName;
                            }

                            System.out.print("Voer de nieuwe achternaam in (druk op Enter om niet bij te werken): ");
                            String newLastName = userInput.nextLine();
                            if (!newLastName.isEmpty()) {
                                currentLastName = newLastName;
                            }

                            System.out.print("Voer de nieuwe geboortedatum in (dd-MM-yyyy, druk op Enter om niet bij te werken): ");
                            String newDateOfBirth = userInput.nextLine();
                            if (!newDateOfBirth.isEmpty()) {
                                try {
                                    currentDateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(newDateOfBirth);
                                } catch (ParseException e) {
                                    System.out.println("\u001B[31m Ongeldige datumindeling. De geboortedatum is niet bijgewerkt.\u001B[0m");
                                }
                            }
                            System.out.print("Voer de nieuwe hoogte in (druk op Enter om niet bij te werken): ");
                            String newHeight = userInput.nextLine();
                            if (!newHeight.isEmpty()) {
                                try {
                                    currentPatientHeight = Double.parseDouble(newHeight);
                                } catch (NumberFormatException e) {
                                    System.out.println("\u001B[31m Ongeldige invoer voor hoogte. De hoogte is niet bijgewerkt.\u001B[0m");
                                }
                            }
                            System.out.print("Voer het nieuwe gewicht in (druk op Enter om niet bij te werken): ");
                            String newWeight = userInput.nextLine();
                            if (!newWeight.isEmpty()) {
                                try {
                                    currentPatientWeight = Double.parseDouble(newWeight);
                                } catch (NumberFormatException e) {
                                    System.out.println("\u001B[31m Ongeldige invoer voor gewicht. Het gewicht is niet bijgewerkt.\u001B[0m");
                                }
                            }
                            String sqlUpdate = "UPDATE Patient SET FirstName=?, LastName=?, DateOfBirth=?, PatientHeight=?, PatienWeight=? WHERE PatientID=?";
                            try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
                                updateStatement.setString(1, currentFirstName);
                                updateStatement.setString(2, currentLastName);
                                updateStatement.setDate(3, new java.sql.Date(currentDateOfBirth.getTime()));
                                updateStatement.setDouble(4, currentPatientHeight);
                                updateStatement.setDouble(5, currentPatientWeight);
                                updateStatement.setInt(6, patientId);

                                int affectedRows = updateStatement.executeUpdate();

                                if (affectedRows > 0) {
                                    System.out.println(Colors.GREEN+"Patiëntgegevens zijn succesvol bijgewerkt."+Colors.RESET);
                                } else {
                                    System.out.println("\u001B[31m Er is iets fout gegaan bij het bijwerken van de patiëntgegevens.\u001B[0m");
                                }
                            }
                            System.out.println("------------------------------------------------- \n");
                            System.out.print("Wil je nog een andere patiënt bijwerken?"+"(\u001B[32mja\u001B[0m"+ "/" +"\u001B[31mnee\u001B[0m):");
                            String input = userInput.nextLine().toLowerCase();
                            if (!input.equals("ja")) {
                                runner = false;
                                Administration.BackToTheMainMenu(users);
                                }
                        } else {
                            System.out.println("\u001B[31m Patiënt met opgegeven ID niet gevonden.\u001B[0m");
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
    public void searchPatientById() {
        Scanner userInput = new Scanner(System.in);
        var runner = true;

        try {
            while (runner) {
                System.out.print("Voer het ID van de patiënt in die je wilt zoeken: ");
                int patientId = Integer.parseInt(userInput.nextLine());

                // Check of de patiënt bestaat voordat je gaat zoeken
//                String sqlSelect = "SELECT * FROM Patient WHERE PatientID = ?";
                String sqlSelect = "SELECT Medications.MedicationID, Medications.MedicationName, Medications.Dosage, Patient.PatientID, Patient.FirstName, Patient.LastName, Patient.DateOfBirth, Patient.PatientHeight, Patient.PatienWeight " +
                        "FROM Medications " +
                        "INNER JOIN Patient ON Medications.PatientID = Patient.PatientID " +
                        "WHERE Patient.PatientID = ?";

                try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {
                    selectStatement.setInt(1, patientId);
                    ResultSet resultSet = selectStatement.executeQuery();

                    if (resultSet.next()) {
                        int currentPatientID = resultSet.getInt("PatientID");
                        String currentFirstName = resultSet.getString("FirstName");
                        String currentLastName = resultSet.getString("LastName");
                        Date currentDateOfBirth = resultSet.getDate("DateOfBirth");
                        double currentPatientHeight = resultSet.getDouble("PatientHeight");
                        double currentPatientWeight = resultSet.getDouble("PatienWeight");
                        String currentMedicationName = resultSet.getString("medicationName");
                        String currentDosage=resultSet.getString("dosage");


                        System.out.println("\u001B[32mPatiënt gevonden:\u001B[0m");
                        System.out.println("Voornaam: " + "\u001B[34m" + currentFirstName + "\u001B[0m");
                        System.out.println("Achternaam: " + "\u001B[34m" + currentLastName + "\u001B[0m");
                        System.out.println("Geboortedatum: " + "\u001B[34m" + currentDateOfBirth + "\u001B[0m");
                        System.out.println("Hoogte: " + "\u001B[34m" + currentPatientHeight + "\u001B[0m");
                        System.out.println("Gewicht: " + "\u001B[34m" + currentPatientWeight + "\u001B[0m");
                        System.out.println("Medicatie: " + "\u001B[34m" + currentMedicationName + "\u001B[0m");
                        System.out.println("Dosage: " + "\u001B[34m" + currentDosage + "\u001B[0m");

                    } else {
                        System.out.println("\u001B[31mPatiënt met opgegeven ID niet gevonden.\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mOngeldige invoer: Voer een geldig geheel getal in. \u001B[0m");
                }
                System.out.print("Wil je nog een andere patiënt Zoken?"+"(\u001B[32mja\u001B[0m"+ "/" +"\u001B[31mnee\u001B[0m):");
                String input = userInput.nextLine().toLowerCase();
                if (!input.equals("ja")) {
                    runner = false;
                    Administration.BackToTheMainMenu(users);
                }
                else {
                    runner = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userInput.close();
        }
    }

    public void searchPatientByName() {
        Scanner userInput = new Scanner(System.in);
        try {
            do {
                System.out.print("Voer de voornaam  van de patiënt in die je wilt zoeken: ");
                String patientName = userInput.nextLine();

                String sqlSelect = "SELECT * FROM Patient WHERE FirstName = ?";
                try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {
                    selectStatement.setString(1, patientName);
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        int patientID = resultSet.getInt("PatientId");
                        String currentFirstName = resultSet.getString("FirstName");
                        String currentLastName = resultSet.getString("LastName");
                        Date currentDateOfBirth = resultSet.getDate("DateOfBirth");
                        double currentPatientHeight = resultSet.getDouble("PatientHeight");
                        double currentPatientWeight = resultSet.getDouble("PatienWeight");

                        System.out.println("\u001B[32mPatiënt gevonden:\u001B[0m");
                        System.out.println("Voornaam: " + "\u001B[34m" + currentFirstName + "\u001B[0m");
                        System.out.println("Achternaam: " + "\u001B[34m" + currentLastName + "\u001B[0m");
                        System.out.println("Geboortedatum: " + "\u001B[34m" + currentDateOfBirth + "\u001B[0m");
                        System.out.println("Hoogte: " + "\u001B[34m" + currentPatientHeight + "\u001B[0m");
                        System.out.println("Gewicht: " + "\u001B[34m" + currentPatientWeight + "\u001B[0m");
                    } else {
                        System.out.println("\u001B[31mPatiënt met opgegeven ID niet gevonden.\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mOngeldige invoer: Voer een geldig geheel getal in. \u001B[0m");
                }
                System.out.print("Wil je nog een andere patiënt zoeken?"+"(\u001B[32mja\u001B[0m"+ "/" +"\u001B[31mnee\u001B[0m):");
                String input = userInput.nextLine().toLowerCase();
                if (!input.equals("ja")) {
                    break;
                }

            }while (true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userInput.close();
        }
    }
    public void deletePatient() {
        Menus menus = new Menus();
        Scanner userInput = new Scanner(System.in);
        var runner = true;
        try {
            do {
                System.out.print("Voer het ID van de patiënt in die je wilt verwijderen: ");
                try {
                    int patientId = Integer.parseInt(userInput.nextLine());
                    boolean isDeleted = deletePatientById(patientId);

                    if (isDeleted) {
                        System.out.println("\u001B[32mPatiënt succesvol verwijderd.\u001B[0m");

                        System.out.print("Wil je nog een andere patiënt verwijderen?" + "(\u001B[32mja\u001B[0m" + "/" + "\u001B[31mnee\u001B[0m):");
                        String input = userInput.nextLine().toLowerCase();
                        if (!input.equals("ja")) {
                            System.out.println("\u001B[33mTerug naar Patiënten menu\u001B[0m");
                            runner = false;
                            menus.patientMenu();
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
    public boolean deletePatientById(int patientId) {
        Menus menus = new Menus();
        try {
            // Check of de patiënt bestaat voordat je gaat verwijderen
            String sqlSelect = "SELECT * FROM Patient WHERE PatientID = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {
                selectStatement.setInt(1, patientId);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    // Bevestiging vragen voor verwijderen
                    Scanner userInput = new Scanner(System.in);
                    System.out.print("Wil je deze patiënt verwijderen?" + "(\u001B[32mja\u001B[0m" + "/" + "\u001B[31mnee\u001B[0m):");
                    String confirmation = userInput.nextLine().toLowerCase();

                    if (confirmation.equals("ja")) {
                        // Verwijder de patiënt
                        String sqlDelete = "DELETE FROM Patient WHERE PatientID=?";
                        try (PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete)) {
                            deleteStatement.setInt(1, patientId);
                            int affectedRows = deleteStatement.executeUpdate();
                            return affectedRows > 0;
                        }
                    } else {
                        System.out.println("\u001B[32mVerwijdering geannuleerd.\u001B[0m");
                        System.out.println("\u001B[33mTerug naar hoofdmenu\u001B[0m");
                        menus.mainMenu();
                    }
                } else {
                    System.out.println("\u001B[31m Patiënt met opgegeven ID niet gevonden.\u001B[0m");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


//    public void deletePatient() {
//        Scanner userInput = new Scanner(System.in);
//        try {
//            do {
//                System.out.print("Voer het ID van de patiënt in die je wilt verwijderen: ");
//                try {
//                    int patientId = Integer.parseInt(userInput.nextLine());
//
//                    // Check of de patiënt bestaat voordat je gaat verwijderen
//                    String sqlSelect = "SELECT * FROM Patient WHERE PatientID = ?";
//                    try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {
//                        selectStatement.setInt(1, patientId);
//                        ResultSet resultSet = selectStatement.executeQuery();
//
//                        if (resultSet.next()) {
//                            String currentFirstName = resultSet.getString("FirstName");
//                            String currentLastName = resultSet.getString("LastName");
//                            Date currentDateOfBirth = resultSet.getDate("DateOfBirth");
//                            double currentPatientHeight = resultSet.getDouble("PatientHeight");
//                            double currentPatientWeight = resultSet.getDouble("PatienWeight");
//
//                            System.out.println("\u001B[32mPatiënt gevonden:\u001B[0m");
//                            System.out.println("Voornaam: "+ "\u001B[34m" + currentFirstName + "\u001B[0m");
//                            System.out.println("Achternaam: "+ "\u001B[34m" + currentLastName + "\u001B[0m");
//                            System.out.println("Geboortedatum: "+ "\u001B[34m" + currentDateOfBirth + "\u001B[0m");
//                            System.out.println("Hoogte: " + "\u001B[34m"+ currentPatientHeight + "\u001B[0m");
//                            System.out.println("Gewicht: "+ "\u001B[34m" + currentPatientWeight + "\u001B[0m");
//
//                            // Bevestiging vragen voor verwijderen
//                            System.out.print("Wil je deze patiënt verwijderen?"+"(\u001B[32mja\u001B[0m"+ "/" +"\u001B[31mnee\u001B[0m):");
//                            String confirmation = userInput.nextLine().toLowerCase();
//
//                            if (confirmation.equals("ja")) {
//                                // Verwijder de patiënt
//                                String sqlDelete = "DELETE FROM Patient WHERE PatientID=?";
//                                try (PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete)) {
//                                    deleteStatement.setInt(1, patientId);
//                                    int affectedRows = deleteStatement.executeUpdate();
//
//                                    if (affectedRows > 0) {
//                                        System.out.println("\u001B[32mPatiënt succesvol verwijderd.\u001B[0m");
//                                    } else {
//                                        System.out.println("\u001B[31m Er is iets fout gegaan bij het verwijderen van de patiënt.\u001B[0m");
//                                    }
//                                }
//                            } else {
//                                System.out.println("\u001B[32mVerwijdering geannuleerd.\u001B[0m");
//                            }
//                        } else {
//                            System.out.println("\u001B[31m Patiënt met opgegeven ID niet gevonden.\u001B[0m");
//                        }
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("\u001B[31m Ongeldige invoer: Voer een geldig geheel getal in. \u001B[0m");
//                }
//
//                System.out.print("Wil je nog een andere patiënt verwijderen?"+"(\u001B[32mja\u001B[0m"+ "/" +"\u001B[31mnee\u001B[0m):");
//                String input = userInput.nextLine().toLowerCase();
//                if (!input.equals("ja")) {
//                    break;
//                }
//            } while (true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            userInput.close();
//        }
//    }


//    private void calcAge(Patient patient){
//        LocalDate currentDate = LocalDate.now();
//        LocalDate dateOfBirth = patient.getDateOfBirth();
//        Period age = Period.between(dateOfBirth, currentDate);
//        System.out.format("Age: %s%n",age.getYears());
//    }











}
