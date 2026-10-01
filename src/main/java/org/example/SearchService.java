package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchService {

    public List<Restaurant> search(List<Restaurant> restaurantList, Predicate<Restaurant> predicate) {
        return restaurantList.stream()
                .filter(predicate)
                .sorted(Comparator.comparing(Restaurant::getRating).reversed()
                        .thenComparing(Restaurant::getName))
                .collect(Collectors.toList());
    }

    public List<Restaurant> search(List<Restaurant> restaurantList, RestaurantSearchCriteria criteria) {
        return search(restaurantList, criteria.toPredicate());
    }
    public Set<String> getAllDistinctCuisines(List<Restaurant> restaurantList) {
        return restaurantList.stream()
                .flatMap(r -> r.getCuisines().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }



}