package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.Permission;
import com.windy.cafemanagement.repositories.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getAllPermissionsService() {
        return permissionRepository.findAllByIsDeleted(false);
    }

    public Permission findByPermissionById(Long id) {
        return permissionRepository.findByPermissionId(id);
    }
}
