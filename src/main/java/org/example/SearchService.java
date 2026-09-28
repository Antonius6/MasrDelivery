package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SearchService {

    public List<Restaurant> search(List<Restaurant> restaurantList, RestaurantSearchCriteria criteria) {
        return restaurantList.stream()
                .filter(r -> !criteria.isOpenOnly() || r.isOpen())
                .filter(r -> criteria.getDistrict() == null || r.getDistrict() == criteria.getDistrict())
                .filter(r -> criteria.getCuisine() == null ||
                        r.getCuisines().stream().anyMatch(c -> c.equalsIgnoreCase(criteria.getCuisine())))
                .filter(r -> criteria.getMinRating() == null || r.getRating() >= criteria.getMinRating())
                .filter(r -> criteria.getPrice() == null ||
                        r.getMenu().values().stream().anyMatch(item -> item.getPrice() <= criteria.getPrice()))
                .filter(r -> criteria.getTextQuery() == null ||
                        r.getName().toLowerCase().contains(criteria.getTextQuery()) ||
                        r.getCuisines().stream().anyMatch(c -> c.toLowerCase().contains(criteria.getTextQuery())))
                .sorted(Comparator.comparing(Restaurant::getRating).reversed()
                        .thenComparing(Restaurant::getName))
                .collect(Collectors.toList());
    }
    public Set<String> getAllDistinctCuisines(List<Restaurant> restaurantList) {
        return restaurantList.stream()
                .flatMap(r -> r.getCuisines().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }



}

