package com.example.lead.management.system.services;

import com.example.lead.management.system.dtos.UserDto;
import com.example.lead.management.system.exceptions.UserHasAssociatedLeadsException;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.repositories.UserRepository;
import com.example.lead.management.system.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void update(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        userDto.setRole(user.getRole().name());
        User updatedUser = UserMapper.toEntity(userDto);
        updatedUser.setId(user.getId());
        updatedUser.setPassword(user.getPassword());
        userRepository.save(updatedUser);
    }

    public List<User> sortByTopPerformer() {
        return nonAdminUsers()
                .sorted(Comparator.comparingLong(User::getSuccessfulConversions).reversed())
                .toList();
    }

    public List<User> sortByBottomPerformer() {
        return nonAdminUsers()
                .sorted(Comparator.comparingLong(User::getSuccessfulConversions))
                .toList();
    }

    private Stream<User> nonAdminUsers() {
        return findAll().stream().filter(user -> !user.getRole().equals(User.Role.ADMIN));
    }

    public void delete(User user) {
        if (!user.getLeads().isEmpty()) {
            throw new UserHasAssociatedLeadsException("Cannot delete user with associated leads.");
        }

        userRepository.delete(user);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public Optional<User> currentUser(){
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) currentUser).getUsername();
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }
}
