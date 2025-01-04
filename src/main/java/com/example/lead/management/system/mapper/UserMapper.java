package com.example.lead.management.system.mapper;
import com.example.lead.management.system.dtos.UserDto;
import com.example.lead.management.system.models.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getTimeZone(),
                user.getRole().name(),
                user.getJoinDate(),
                user.getLeads(),
                user.getPassword(),
                user.getMatchingPassword()
        );
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setTimeZone(userDto.getTimeZone());
        user.setRole(User.Role.valueOf(userDto.getRole()));
        user.setJoinDate(userDto.getJoinDate());
        user.setPassword(userDto.getPassword());
        user.setMatchingPassword(userDto.getMatchingPassword());
        user.setJoinDate(userDto.getJoinDate());
        return user;
    }
}
