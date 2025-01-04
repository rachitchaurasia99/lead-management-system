package com.example.lead.management.system.mapper;

import com.example.lead.management.system.dtos.LeadDto;
import com.example.lead.management.system.dtos.OrderDto;
import com.example.lead.management.system.dtos.UserDto;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.Order;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.services.LeadService;
import com.example.lead.management.system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        return new OrderDto(
                order.getId(),
                order.getLead().getId(),
                order.getUser().getId(),
                order.getDescription(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getOrderNumber()
        );
    }

    public static Order toEntity(OrderDto orderDto, UserService userService, LeadService leadService) {
        if (orderDto == null) {
            return null;
        }

        Lead lead = new Lead();
        LeadDto leadDto = orderDto.getLeadDto(leadService);
                lead.setId(leadDto.getId());
                lead.setName(leadDto.getName());
                lead.setAddress(leadDto.getAddress());
                lead.setDescription(leadDto.getDescription());
                lead.setStatus(leadDto.getStatus());


        User user = getUserDto(orderDto, userService);

        Order order = new Order();
        order.setId(orderDto.getId());
        order.setLead(lead);
        order.setUser(user);
        order.setDescription(orderDto.getDescription());
        order.setCreatedAt(orderDto.getCreatedAt());
        order.setStatus(orderDto.getStatus());
        order.setOrderNumber(orderDto.getOrderNumber());

        return order;
    }

    private static User getUserDto(OrderDto orderDto, UserService userService) {
        User user = new User();
        UserDto userDto = orderDto.getUserDto(userService);
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setTimeZone(userDto.getTimeZone());
        user.setRole(userDto.getRole());
        user.setJoinDate(userDto.getJoinDate());
        return user;
    }
}
