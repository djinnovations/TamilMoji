package dj.example.main.utils;

/**
 * Created by DJphy on 28-09-2016.
 */
public class URLHelper {

    public static final String END_POINT = "http://34.217.125.68/tamilmoji/";
    public static final String API_KEY_NASA = "vtRoSiuO7Ze134C859OhPy8AqLOXxIvDmNfVmHOU";


    private static URLHelper ourInstance;

    public static URLHelper getInstance(){
        if (ourInstance == null){
            ourInstance = new URLHelper();
        }
        return ourInstance;
    }

    private URLHelper(){

    }

    public static void clearInstance(){
        ourInstance = null;
    }

    public String getHomeAPI() {
        return END_POINT+"home.php";
    }

    public String getSearchAPI(String txt) {
        return END_POINT+"explore.php?query="+txt;
    }

}
