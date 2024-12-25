package com.example.lead.management.system.services;

import com.example.lead.management.system.models.Restaurant;
import com.example.lead.management.system.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Page<Restaurant> findAll(PageRequest pageRequest) {
        return restaurantRepository.findAll(pageRequest);
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }

    public void update(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public Page<Restaurant> findByNameContaining(String query, PageRequest pageRequest) {
        System.out.println("debugger2");
        return restaurantRepository.findAllByNameContaining(query, pageRequest);
    }
}
