package com.windy.cafemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.windy.cafemanagement.models.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
