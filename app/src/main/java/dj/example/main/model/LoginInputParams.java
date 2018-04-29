package dj.example.main.model;

/**
 * Created by DJphy on 26-01-2017.
 */

public class LoginInputParams {

    private String email=  "";
    private String uid;
    private String oauth_access_token;
    private String name = "";
    private String provider;
    private String image;
    private String resource_class;
    private String location;
    private String gender;
    private String birthday;
    //private List<String> roles;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOauth_access_token() {
        return oauth_access_token;
    }

    public void setOauth_access_token(String oauth_access_token) {
        this.oauth_access_token = oauth_access_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getResource_class() {
        return resource_class;
    }

    public void setResource_class(String resource_class) {
        this.resource_class = resource_class;
    }

    /*public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }*/
}
