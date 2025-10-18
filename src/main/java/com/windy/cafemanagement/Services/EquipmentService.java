package com.windy.cafemanagement.Services;

import java.util.List;
import java.util.Optional;

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

    public Equipment getEquipmentById(Long emquepmentId) {
        return equipmentRepository.findById(emquepmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị với ID: " + emquepmentId));
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

    @Transactional
    public Equipment updateEquipmentWithImportOrder(EquipmentDto dto) {
        if (dto.getEquipmentId() == null) {
            throw new IllegalArgumentException("Thiếu ID thiết bị cần cập nhật");
        }

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị với ID: " + dto.getEquipmentId()));

        equipment.setEquipmentName(dto.getEquipmentName());
        equipment.setQuantity(dto.getQuantity());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setUnitPrice(dto.getUnitPrice());

        Equipment updatedEquipment = equipmentRepository.save(equipment);

        Optional<ImportOrder> importOrderOpt = importOrderRepository.findByEquipment(updatedEquipment);
        if (importOrderOpt.isPresent()) {
            ImportOrder importOrder = importOrderOpt.get();
            importOrder.setImportDate(dto.getPurchaseDate());
            importOrder.setQuantity(dto.getQuantity());
            importOrder.setTotalAmount(dto.getQuantity() * dto.getUnitPrice());
            importOrderRepository.save(importOrder);
        }

        return updatedEquipment;
    }

    @Transactional
    public void softDeleteEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị với ID: " + equipmentId));

        if (Boolean.TRUE.equals(equipment.getIsDeleted())) {
            throw new RuntimeException("Thiết bị này đã bị xóa trước đó");
        }

        equipment.setIsDeleted(true);
        equipmentRepository.save(equipment);

        importOrderRepository.findByEquipment(equipment)
                .ifPresent(importOrderRepository::delete);
    }
}
