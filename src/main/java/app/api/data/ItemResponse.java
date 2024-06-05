package app.api.data;

import java.util.List;

public class ItemResponse {

    public Integer status;
    public String message;
    public ItemPayload payload;

    public static class ItemPayload {

        public Integer count;
        public List<Item2> rows;

        public static class Item2 {
            public Integer id;
            public String name;
            String description;
            Integer price;
            Boolean isInStock;
            Integer categoryId;
            String categoryName;
            String image;
        }
    }
}


