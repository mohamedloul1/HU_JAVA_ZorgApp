import java.util.Scanner;

public class Administration {
    public static void UserChecker(Users users) {
        String userType = "";
        if (users != null) {
            if (users.hasType("Apotheker")) {
                userType = "Apotheker";
            } else if (users.hasType("HuisArts"))
            {
                userType = "HuisArts";
            }else if (users.hasType("Fysiotherapeut"))
            {
                userType = "Fysiotherapeut";
            }
            else if (users.hasType("Tandart"))
            {
                userType = "Tandart";
            }
        }

        switch (userType){
            case "Apotheker":
                Apotheker.apothekerStartMenu();
                break;
            case "HuisArts":
                HuisArts.huisArtsStartMenu();
                break;
            case "Fysiotherapeut":
                System.out.println("Fysiotherapeut");
                break;
            case "Tandart":
                System.out.println("Tandart");
                break;
            default:
                System.out.println("Fout");
                break;
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
