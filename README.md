# Zorg-App Handleiding

Welkom bij de handleiding voor de Zorg-App. Deze handleiding helpt je bij het opzetten en starten van de console-applicatie vanuit de repository, inclusief het instellen van de SQL Server database en het importeren van de database data.

## Voorwaarden

Zorg ervoor dat je de volgende software op je systeem hebt geïnstalleerd:

- Git
- IntelliJ IDEA (Ultimate Edition voor volledige ondersteuning van Java en databases)
- Java Development Kit (JDK)
- Apache Maven
- SQL Server

## Stappen om de applicatie te starten

### 1. Clone de repository

1. Open IntelliJ IDEA.
2. Selecteer **Get from VCS** in het welkomstscherm.
3. Plak de repository URL en klik op **Clone**.

### 2. Open het project

1. Nadat de repository is gekloond, opent IntelliJ IDEA automatisch het project.
2. IntelliJ zal je mogelijk vragen om aanvullende configuraties zoals de JDK en Maven. Zorg ervoor dat deze correct zijn ingesteld.

### 3. Installeer de afhankelijkheden

1. Open de Terminal binnen IntelliJ IDEA (onderaan het scherm).
2. Voer de volgende opdracht uit om de nodige Maven-pakketten te installeren:

    ```bash
    mvn clean install
    ```

### 4. Database instellen

1. Maak een nieuwe SQL Server database aan.
2. Importeer de database data vanuit de `Database` map die zich in de repository bevindt:
   - Open SQL Server Management Studio (SSMS).
   - Verbind met jouw SQL Server.
   - Klik met de rechtermuisknop op **Databases** en selecteer **Restore Database...**.
   - Selecteer **Device** en voeg het backupbestand toe uit de `Database` map.
   - Volg de stappen om de database te herstellen.

### 5. Configureer de databaseverbinding

Je hebt al een klasse `DatabaseConnector` die de verbinding met de database beheert. Zorg ervoor dat deze correct in je project is geplaatst. De klasse ziet er als volgt uit:

```java 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static Connection connection;

    private static final String connectionUrl = "jdbc:sqlserver://localhost:1434;databaseName=ZorgConsoleApp;user=sa;password=0992280051;trustServerCertificate=true";

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(connectionUrl);
        }
        return connection;
    }
}

### 6. Start de applicatie
Ga naar de Run configuratie in IntelliJ IDEA.
Selecteer de hoofdklasse van de applicatie (bijvoorbeeld com.example.zorgapp.Main).
Klik op Run.
De console-applicatie zou nu moeten starten en je zou output in de terminal moeten zien.

### 7. Toegang tot de applicatie
De applicatie wordt bestuurd via de console, volg de instructies die worden weergegeven in de terminal.
