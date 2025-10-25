package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.Responses.TableInforRes;
import com.windy.cafemanagement.models.TableEntity;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    List<TableEntity> findAllByIsDeleted(Boolean isDeleted);

    Optional<TableEntity> findById(Long id);

    @Query("""
                SELECT new com.windy.cafemanagement.Responses.TableInforRes(t.tableId, t.tableName)
                FROM TableEntity t
                WHERE (t.isDeleted IS NULL OR t.isDeleted = false)
                  AND t.tableId <> :excludedTableId
            """)
    List<TableInforRes> findAllActiveExcept(@Param("excludedTableId") Long excludedTableId);
}
