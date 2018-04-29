package dj.example.main.model;

/**
 * Created by DJphy on 01-08-2017.
 */

public class UserInfo {

    public String uniqueId;
    public String name;
    public String emailId;
    public String profileImgUrl;
    public String birthday;
    public String gender;
    public String location;
    private Rewards rewards;

    public Rewards getRewards() {
        return rewards;
    }

    public static class Rewards{
        public int item1;
        public int item2;
        public int item3;
    }
}



