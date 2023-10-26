import java.util.ArrayList;
import java.util.List;

public class Users {
    int userId;
    String userName;
    List<String> userType;

public Users(int userId, String UserName){
    this.userId = userId;
    this.userName=UserName;
    this.userType= new ArrayList<>();

}

public int getUserId(){return userId;}

    public String getUserName(){return userName;}

public void addType(String type){
    userType.add(type);
}

public boolean hasType(String type){
    return userType.contains(type);
}
}
