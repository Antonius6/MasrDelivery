package org.example;

public class RestaurantSearchCriteria {

    private final District district;
    private final String cuisine;
    private final Double price;
    private final String textQuery;
    private final boolean openOnly;




    private final Double minRating;


    private RestaurantSearchCriteria(Builder builder) {
        this.district = builder.district;
        this.cuisine = builder.cuisine;
        this.price = builder.price;
        this.openOnly = builder.openOnly;
        this.minRating = builder.minRating;
        this.textQuery = builder.textQuery;
    }

   public static class Builder {
        private District district;
        private String cuisine;
        private Double price;
        private String textQuery;
        private boolean openOnly = false;
        private Double minRating;


        public Builder district(District district) {
            this.district = district;
            return this;
        }


        public Builder cuisine(String cuisine) {
            if (cuisine != null && !cuisine.isBlank()) {
                this.cuisine = cuisine.toUpperCase().trim();
            }
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder textQuery(String textQuery) {
            if (textQuery != null && !textQuery.isBlank()) {
                this.textQuery = textQuery.toLowerCase().trim();
            }
            return this;
        }

        public Builder openOnly(boolean openOnly) {
            this.openOnly = openOnly;
            return this;
        }
        public Builder minRating(Double minRating) {
            if(minRating>5.0 || minRating<0.0){
                throw new IllegalArgumentException("minRating must be between 0.0 and 5.0");
            }
           this.minRating = minRating;
           return this;
        }

        public RestaurantSearchCriteria build() {
           return new RestaurantSearchCriteria(this);
        }
    }

    public District getDistrict() {
        return district;
    }

    public String getCuisine() {
        return cuisine;
    }

    public Double getPrice() {
        return price;
    }


    public boolean isOpenOnly() {
        return openOnly;
    }
    public Double getMinRating() {
        return minRating;
    }

    public String getTextQuery() {
        return textQuery;
    }
}
