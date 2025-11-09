package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Employee;

import jakarta.transaction.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public List<Employee> findAllByIsDeleted(Boolean isDeleted);

    public Employee save(Employee employee);

    public Optional<Employee> findById(Long id);

    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false AND " +
            "(LOWER(e.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    public List<Employee> searchByKeyword(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.isDeleted = true WHERE e.employeeId = :id")
    public void softDeleteById(@Param("id") Long id);

    public Employee findByUsername(String username);

    @Query(value = """
                SELECT e.fullname, e.address, e.phone_number, e.salary, p.name AS permission_name
                FROM employee e
                JOIN permission p ON e.permissionid = p.permissionid
                WHERE e.is_deleted = false
            """, nativeQuery = true)
    List<Object[]> getEmployeeInformation();
}
