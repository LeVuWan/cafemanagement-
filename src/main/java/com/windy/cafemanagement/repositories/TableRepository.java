package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.Responses.TableInforRes;
import com.windy.cafemanagement.models.TableEntity;

/**
 * TableRepository interface
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
public interface TableRepository extends JpaRepository<TableEntity, Long> {

  /**
   * get all table by isDeleted false
   * 
   * @param isDeleted
   * 
   * @return List<TableEntity>
   */
  List<TableEntity> findAllByIsDeleted(Boolean isDeleted);

  /**
   * get all table by isDeleted false
   * 
   * @param tableId
   * @param invoiceId
   * @return Optional<TableBookingDetail>
   */
  Optional<TableEntity> findById(Long id);

  /**
   * get all active tables except the excluded table id
   * 
   * @param excludedTableId
   * @return List<TableInforRes>
   */
  @Query("""
          SELECT new com.windy.cafemanagement.Responses.TableInforRes(t.tableId, t.tableName)
          FROM TableEntity t
          WHERE (t.isDeleted IS NULL OR t.isDeleted = false)
            AND t.tableId <> :excludedTableId
      """)
  List<TableInforRes> findAllActiveExcept(@Param("excludedTableId") Long excludedTableId);
}
