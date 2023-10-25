import java.util.ArrayList;
import java.util.List;

public class AuthenticationServer {
    private List<Users> user;

    public AuthenticationServer(){
        user = new ArrayList<>();
        Users admin = new Users(1,"admin");
        admin.addType("admin");
        user.add(admin);
        Users huisArts = new Users(2,"HuisArts");
        huisArts.addType("HuisArts");
        user.add(huisArts);
    }
    public Users login(String username){
        for(Users users : user){
            if (users.userName.equals(username)){
                return users;
            }
        }
        return null;
    }
}

