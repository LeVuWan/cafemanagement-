package com.windy.cafemanagement.Services;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.windy.cafemanagement.configs.SecurityUtil;
import com.windy.cafemanagement.dto.EquipmentDto;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.Equipment;
import com.windy.cafemanagement.models.ImportOrder;
import com.windy.cafemanagement.repositories.EmployeeRepository;
import com.windy.cafemanagement.repositories.EquipmentRepository;
import com.windy.cafemanagement.repositories.ImportOrderRepository;

/**
 * EquipmentService
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

    /**
     * get all equipments by isDeleted = false
     * 
     * @return List<Equipment>
     * @throws DataAccessException
     */
    public List<Equipment> getAllEquipmentsService() throws DataAccessException {
        return equipmentRepository.findAllByIsDeleted(false);
    }

    /**
     * get equipment by id
     * 
     * @param emquepmentId
     * @return Equipment
     * @throws DataAccessException
     * @throws EntityNotFoundException
     */
    public Equipment getEquipmentById(Long emquepmentId)
            throws DataAccessException, EntityNotFoundException, SecurityException {
        return equipmentRepository.findById(emquepmentId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thiết bị với ID: " + emquepmentId));
    }

    /**
     * create equipment with import order
     * 
     * @param dto
     * @return Equipment
     * @throws DataAccessException
     * @throws EntityNotFoundException
     */
    @Transactional
    public Equipment createEquipmentWithImportOrder(EquipmentDto dto)
            throws EntityNotFoundException, Exception, DataAccessException, SecurityException {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName(dto.getEquipmentName());
        equipment.setQuantity(dto.getQuantity());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setUnitPrice(dto.getUnitPrice());
        equipment.setIsDeleted(false);

        Equipment savedEquipment = equipmentRepository.save(equipment);
        String username = SecurityUtil.getSessionUser();

        if (username == null) {
            throw new SecurityException("Không tìm thấy người dùng trong phiên làm việc.");
        }

        Employee user = employeeRepository.findByUsername(username);

        if (user == null) {
            throw new EntityNotFoundException("Không tìm thấy nhân viên với username: " + username);
        }

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

    /**
     * create equipment with import order
     * 
     * @param dto
     * @return Equipment
     * @throws DataAccessException
     * @throws EntityNotFoundException
     */
    @Transactional
    public Equipment updateEquipmentWithImportOrder(EquipmentDto dto) throws EntityNotFoundException, Exception {
        if (dto.getEquipmentId() == null) {
            throw new IllegalArgumentException("Thiếu ID thiết bị cần cập nhật");
        }

        // 1. Tìm và cập nhật Equipment
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị với ID: " + dto.getEquipmentId()));

        equipment.setEquipmentName(dto.getEquipmentName());
        equipment.setQuantity(dto.getQuantity());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setUnitPrice(dto.getUnitPrice());

        Equipment updatedEquipment = equipmentRepository.save(equipment);

        List<ImportOrder> importOrders = importOrderRepository.findByEquipment(updatedEquipment);

        if (importOrders.isEmpty()) {
            return updatedEquipment;
        }
        for (ImportOrder importOrder : importOrders) {
            importOrder.setImportDate(dto.getPurchaseDate());
            importOrder.setQuantity(dto.getQuantity());
            importOrder.setTotalAmount(dto.getQuantity() * dto.getUnitPrice());

            importOrderRepository.save(importOrder);
        }

        return updatedEquipment;
    }

    /**
     * soft delete equipment
     * 
     * @param equipmentId
     * @throws DataAccessException
     * @throws EntityNotFoundException
     */
    @Transactional
    public void softDeleteEquipment(Long equipmentId) throws EntityNotFoundException, DataAccessException {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thiết bị với ID: " + equipmentId));

        if (Boolean.TRUE.equals(equipment.getIsDeleted())) {
            throw new EntityNotFoundException("Thiết bị này đã bị xóa trước đó");
        }

        equipment.setIsDeleted(true);
        equipmentRepository.save(equipment);

        List<ImportOrder> importOrders = importOrderRepository.findByEquipment(equipment);

        if(importOrders.isEmpty()) {
            return;
        }

        for (ImportOrder importOrder : importOrders) {
            importOrder.setIsDeleted(true);
            importOrderRepository.save(importOrder);
        }
    }
}
