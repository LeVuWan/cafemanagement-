package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import com.windy.cafemanagement.models.Employee;

/**
 * EmployeeRepository interface
 *
 * Version 1.0
 *
 * Date: 11-10-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 11-10-2025 VuLQ Create
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * get all employees by isDeleted
     * 
     * @param isDeleted
     * @return List<Employee>
     */
    public List<Employee> findAllByIsDeleted(Boolean isDeleted);

    /**
     * get data for Job Details page
     * 
     * @param employee
     * @return Employee
     */
    public Employee save(Employee employee);

    /**
     * get employee by id
     * 
     * @param id
     * @return Employee
     */
    public Optional<Employee> findById(Long id);

    /**
     * get employee by keyword and isDeleted
     * 
     * @param keyword
     * @return List<Employee>
     */
    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false AND " +
            "(LOWER(e.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    public List<Employee> searchByKeyword(@Param("keyword") String keyword);

    /**
     * soft delete employee by id
     * 
     * @param id
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.isDeleted = true WHERE e.employeeId = :id")
    public Employee softDeleteById(@Param("id") Long id);

    /**
     * get employee by username
     * 
     * @param username
     * @return Employee
     */
    public Employee findByUsername(String username);

    /**
     * geting employee information with permission name
     * 
     * @return List<Object[]>
     */
    @Query(value = """
                SELECT e.fullname, e.address, e.phone_number, e.salary, p.name AS permission_name
                FROM employee e
                JOIN permission p ON e.permissionid = p.permissionid
                WHERE e.is_deleted = false
            """, nativeQuery = true)
    List<Object[]> getEmployeeInformation();

}
