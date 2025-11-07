package com.windy.cafemanagement.Services;

import java.util.Collections;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.windy.cafemanagement.models.CustomUserDetails;
import com.windy.cafemanagement.models.Employee;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(@Lazy EmployeeService employeeService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findEmployeeByUsername(username);

        if (employee == null) {
            throw new UsernameNotFoundException("Không tìm thấy user: " + username);
        }

        return new CustomUserDetails(
                employee.getUsername(),
                employee.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + employee.getPermission().getName().toUpperCase())),
                employee.getAvatar());
    }
}