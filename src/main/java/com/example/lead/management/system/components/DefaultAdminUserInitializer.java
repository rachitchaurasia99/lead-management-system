package com.example.lead.management.system.components;

import com.example.lead.management.system.models.User;
import com.example.lead.management.system.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DefaultAdminUserInitializer implements CommandLineRunner {

    private final UserService userService;

    public DefaultAdminUserInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.findByEmail("admin@example.com").isEmpty()) {
            User adminUser = new User();
            adminUser.setFirstName("Default");
            adminUser.setLastName("Admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword("admin_password");
            adminUser.setRole(User.Role.ADMIN);
            adminUser.setTimeZone("UTC");
            adminUser.setJoinDate(new Date());

            userService.save(adminUser);
            System.out.println("Default admin user created!");
        } else {
            System.out.println("Admin user already exists!");
        }
    }
}

