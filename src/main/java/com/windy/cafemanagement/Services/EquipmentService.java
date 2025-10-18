package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.configs.SecurityUtil;
import com.windy.cafemanagement.dto.EquipmentDto;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.Equipment;
import com.windy.cafemanagement.models.ImportOrder;
import com.windy.cafemanagement.repositories.EmployeeRepository;
import com.windy.cafemanagement.repositories.EquipmentRepository;
import com.windy.cafemanagement.repositories.ImportOrderRepository;

import jakarta.transaction.Transactional;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final ImportOrderRepository importOrderRepository;
    private final EmployeeRepository employeeRepository;

    public EquipmentService(EquipmentRepository equipmentRepository, ImportOrderRepository importOrderRepository,
            EmployeeRepository employeeRepository) {
        this.equipmentRepository = equipmentRepository;
        this.importOrderRepository = importOrderRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Equipment> getAllEquipmentsService() {
        return equipmentRepository.findAllByIsDeleted(false);
    }

    @Transactional
    public Equipment createEquipmentWithImportOrder(EquipmentDto dto) {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName(dto.getEquipmentName());
        equipment.setQuantity(dto.getQuantity());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setUnitPrice(dto.getUnitPrice());
        equipment.setIsDeleted(false);

        Equipment savedEquipment = equipmentRepository.save(equipment);
        String username = SecurityUtil.getSessionUser();
        Employee user = employeeRepository.findByUsername(username);

        ImportOrder importOrder = new ImportOrder();
        importOrder.setEquipment(savedEquipment);
        importOrder.setImportDate(dto.getPurchaseDate());
        importOrder.setQuantity(dto.getQuantity());
        importOrder.setTotalAmount(dto.getQuantity() * dto.getUnitPrice());
        importOrder.setProduct(null);
        importOrder.setEmployee(user);
        importOrderRepository.save(importOrder);

        return savedEquipment;
    }

}
