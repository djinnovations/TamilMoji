package dj.example.main.model.response;

/**
 * Created by CSC on 4/29/2018.
 */

public class SearchDataResponse {

    public static class SearchResponse{

        public static class SearchData{
            public String _id;
            public String title;
            public String img;
            public String tags;
        }
    }

    private SearchDataResponse.SearchResponse response;

    public SearchDataResponse.SearchResponse getResponse() {
        return response;
    }
}
