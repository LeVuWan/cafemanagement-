package com.windy.cafemanagement.Services;

import java.util.Collections;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.windy.cafemanagement.models.CustomUserDetails;
import com.windy.cafemanagement.models.Employee;

/**
 * CustomUserDetailsService
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025 VuLQ Create
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeService employeeService;

    public CustomUserDetailsService(@Lazy EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Load user by username
     * 
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
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