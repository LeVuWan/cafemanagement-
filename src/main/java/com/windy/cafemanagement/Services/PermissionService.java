package com.windy.cafemanagement.Services;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import com.windy.cafemanagement.models.Permission;
import com.windy.cafemanagement.repositories.PermissionRepository;

/**
 * PermissionService
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
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    /**
     * get list permission by isDeleted = false
     * 
     * @return List<Permission>
     * @throws EntityNotFoundException, DataAccessException
     */
    public List<Permission> getAllPermissionsService() throws DataAccessException {
        return permissionRepository.findAllByIsDeleted(false);
    }

    /**
     * get permission by id
     * 
     * @param id
     * @return LPermission
     * @throws EntityNotFoundException, DataAccessException
     */
    public Permission findByPermissionById(Long id) throws EntityNotFoundException, DataAccessException {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền với ID: " + id));
    }
}
