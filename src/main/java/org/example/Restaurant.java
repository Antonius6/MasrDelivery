package org.example;

import java.util.*;

public class Restaurant {

    private final String id;
    private static  int count = 43240;
    private final String name;
    private District district;
    private RestaurantStatus status;
    private double rating;
    private Set<String> cuisines = new HashSet<>();
    private Map<String, MenuItem> menu = new LinkedHashMap<>();
    Restaurant( String name, District district , RestaurantStatus status) {
        this.name = name;
        this.district = district;
        this.status = status;
        this.id = "RES-" + count++;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public static int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public District getDistrict() {
        return district;
    }

    public RestaurantStatus getStatus() {
        return status;
    }

    public double getRating() {
        return rating;
    }

    public Set<String> getCuisines() {
        return Collections.unmodifiableSet(cuisines);
    }

    public Map<String, MenuItem> getMenu() {
        return Collections.unmodifiableMap(menu);
    }

    public boolean isOpen(){
        return this.status ==  RestaurantStatus.OPEN;
    }

    public void setRating(double rating) {
        if(rating < 0.0 || rating >5.0){
            throw  new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }
        this.rating = rating;
    }

    public void addMenuItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("MenuItem cannot be null");
        }
        this.menu.put(item.getItemId(), item);
    }

    public void removeMenuItem(String itemId) {
        this.menu.remove(itemId);
    }

    public void addCuisine(String cuisine) {
        if(cuisine == null || cuisine.isBlank()){
            throw new IllegalArgumentException("Cuisine cannot be null or blank");
        }
        this.cuisines.add(cuisine.toUpperCase().trim());
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        String status = isOpen() ? "OPEN" : "CLOSED";
        return name + " | Cuisines:" +cuisines + " | Rating: " + rating + " | Status: " + status;
    }
}
