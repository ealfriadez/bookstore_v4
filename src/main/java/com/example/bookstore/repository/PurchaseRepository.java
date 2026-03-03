package com.example.bookstore.repository;

import com.example.bookstore.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByCustomerEmailOrderByPurchaseDateDesc(String email);

    @Query(value = "SELECT * FROM fn_sales_report_by_book(:dateFrom, :dateTo)", nativeQuery = true)
    List<Object[]> getSalesReportByBook(@Param("dateFrom") LocalDateTime dateFrom,
                                        @Param("dateTo") LocalDateTime dateTo);

    @Query(value = "SELECT * FROM fn_sales_summary(:dateFrom, :dateTo)", nativeQuery = true)
    List<Object[]> getSalesSummary(@Param("dateFrom") LocalDateTime dateFrom,
                                   @Param("dateTo") LocalDateTime dateTo);
}
