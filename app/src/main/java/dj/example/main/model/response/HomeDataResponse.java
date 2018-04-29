package dj.example.main.model.response;

import java.util.List;

/**
 * Created by CSC on 4/29/2018.
 */

public class HomeDataResponse {

    public static class HomeResponse {

        private List<HomeData> data;

        public List<HomeData> getData() {
            return data;
        }

        public static class HomeData {
            public String _id;
            public String title;
            public List<Emojis> emojis;

            public static class Emojis {
                public String _id;
                public String title;
                public String img;
                public String tags;
            }
        }
    }

    private HomeResponse response;

    public HomeResponse getResponse() {
        return response;
    }
}
