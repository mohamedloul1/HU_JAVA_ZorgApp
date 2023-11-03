import java.util.Scanner;

public class Administration {



    public static void UserChecker(Users users) {
        if (users != null) {
            if (users.hasType("Apotheker")) {
                Apotheker.apothekerStartMenu();
            } else if (users.hasType("HuisArts")) {
                HuisArts.huisArtsStartMenu();
            } else if (users.hasType("Fysiotherapeut")) {
                System.out.println("Fysiotherapeut");
            } else if (users.hasType("Tandart")) {
                System.out.println("Tandart");
            } else {
                System.out.println("Fout");
            }
        } else {
            System.out.println("Gebruiker is null");
        }
    }
    static final int BackToTheMainMenu = 1;

    public static void BackToTheMainMenu(Users users) {
        Scanner userInput = new Scanner(System.in);
        boolean runner = true;
        while (runner) {
            try {
                System.out.println("Voer het getal 1 om terug te gaan naar main menu");
                int choice = Integer.parseInt(userInput.nextLine());
                if (choice==BackToTheMainMenu) {
                    runner = false;
                    UserChecker(users);
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
