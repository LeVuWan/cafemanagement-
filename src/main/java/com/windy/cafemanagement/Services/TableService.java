package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.models.TableEntity;
import com.windy.cafemanagement.repositories.TableRepository;

@Service
public class TableService {
    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<TableEntity> getAllTableService() {
        return tableRepository.findAllByIsDeleted(false);
    }
}
