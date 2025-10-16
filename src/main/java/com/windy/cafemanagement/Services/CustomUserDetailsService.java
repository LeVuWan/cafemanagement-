package com.windy.cafemanagement.Services;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.windy.cafemanagement.models.Employee;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeService employeeService;

    public CustomUserDetailsService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = this.employeeService.findEmployeeByUsername(username);

        if (employee == null) {
            System.out.println("Không tìm thấy user");
            throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
        }

        return new User(employee.getUsername(), employee.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getPermission().getName())));
    }

}
