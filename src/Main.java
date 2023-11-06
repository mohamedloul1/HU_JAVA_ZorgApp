import java.security.PublicKey;

public class Main {


    public static void main(String[] args) {

        AuthenticationServer authenticationServer = new AuthenticationServer();
        Users users = authenticationServer.login("HuisArts");
        if(users !=null){
            if (users.hasType("admin")){
                System.out.println("Access granted to admin functionality.");
            }else if (users.hasType("HuisArts")){
                System.out.println("Jij bent als" + Colors.YELLOW_BOLD+" HuisArts "+Colors.RESET + "ingelogd");
                Administration.UserChecker(users);
            }else if (users.hasType("Apotheker")){
                System.out.println("Jij bent als" + Colors.YELLOW_BOLD+" Apotheker "+Colors.RESET + "ingelogd");
                Administration.UserChecker(users);
            }else if (users.hasType("Fysiotherapeut")){
                System.out.println("Jij bent als" + Colors.YELLOW_BOLD+" Fysiotherapeut "+Colors.RESET + "ingelogd");
                Administration.UserChecker(users);
            }else if (users.hasType("Tandart")){
                System.out.println("Jij bent als" + Colors.YELLOW_BOLD+" Tandart "+Colors.RESET + "ingelogd");
                Administration.UserChecker(users);
            }else {
                System.out.println("Access denied. User doesn't have necessary roles.");
            }
        }else {
            System.out.println(Colors.RED+"Invalid credentials. Access denied."+Colors.RESET);
        }
    }
}
