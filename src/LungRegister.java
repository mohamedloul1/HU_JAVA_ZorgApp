import java.sql.*;
import java.util.Scanner;

public class LungRegister {
    AuthenticationServer authenticationServer = new AuthenticationServer();
    Users users = authenticationServer.login("Apotheker");

    private Connection connection;

    public LungRegister(){
        try {
            connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fout bij het tot stand brengen van de databaseverbinding." +
                    " Zorg ervoor dat de database actief is en controleer uw databaseconfiguratie.");
        }
    }

    public void insertLungRegister() {
        PatientController patientController = new PatientController();
        Scanner userInput = new Scanner(System.in);
        boolean runner = true;

        try {
            do {
                try {
                    patientController.displayPatientListWithoutbackFunction();
                    System.out.println("\n");
                    System.out.print("------------------------------------------------------------- \n");
                    System.out.print("Selecteer de patiënt aan wie u Longinhoud registreren wilt toevoegen: \n");
                    int patientID = Integer.parseInt(userInput.nextLine());
                    System.out.print("Voer de registreren van de patient in: ");
                    String registreren = userInput.nextLine();
                    String sqlInsert = "INSERT INTO LungRegister (Register, PatientID) VALUES (?,?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                        preparedStatement.setString(1, registreren);
                        preparedStatement.setInt(2, patientID);
                        int affectedRows = preparedStatement.executeUpdate();

                        if (affectedRows > 0) {
                            ResultSet rs = preparedStatement.getGeneratedKeys();
                            if (rs.next()) {
                                int lungID = rs.getInt(1);
                                System.out.println("\n"+"Nieuwe medicatie succesvol toegevoegd 👇👇\n"+
                                        "LungNummer: " + Colors.PURPLE + lungID + Colors.RESET + "\n"+
                                        "Longinhoud registreren: " + Colors.PURPLE + registreren + Colors.RESET + "\n"+
                                        "Patiëntnummer: " + Colors.PURPLE + patientID + Colors.RESET);
                            }
                        } else {
                            System.out.println(Colors.RED+"Er is iets fout gegaan bij het toevoegen van de Longinhoud registreren."+Colors.RESET);
                        }
                    }
                    // Vraag of de gebruiker nog een rol wil toevoegen
                    System.out.print("Wil je nog een andere Longinhoud registreren toevoegen?:"+"(\u001B[32mja\u001B[0m"+ "/" +"\u001B[31mnee\u001B[0m):");
                    String input = userInput.nextLine().toLowerCase();
                    if (input.equals("ja")) {
                        System.out.println("Volg de stappen");
                    }
                    else {
                        runner = false;
                        Administration.BackToTheMainMenu(users);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31m Ongeldige invoer voor Patiëntnummer. Voer een geldig getal in.\u001B[0m");
                }
            } while (runner) ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userInput.close();
        }
    }
}
