package com.example.bookstore.service;

import com.example.bookstore.dto.request.PurchaseItemRequest;
import com.example.bookstore.dto.request.PurchaseRequest;
import com.example.bookstore.dto.response.PurchaseResponse;
import com.example.bookstore.dto.response.SalesReportByBookResponse;
import com.example.bookstore.dto.response.SalesSummaryResponse;
import com.example.bookstore.exception.InsufficientStockException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.mapper.PurchaseMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Purchase;
import com.example.bookstore.model.PurchaseItem;
import com.example.bookstore.model.PurchaseStatus;
import com.example.bookstore.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final BookService bookService;
    private final PurchaseMapper purchaseMapper;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           BookService bookService,
                           PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.bookService = bookService;
        this.purchaseMapper = purchaseMapper;
    }

    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        Purchase purchase = new Purchase();
        purchase.setCustomerName(request.customerName());
        purchase.setCustomerEmail(request.customerEmail());
        purchase.setStatus(PurchaseStatus.COMPLETED);

        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseItemRequest itemReq : request.items()) {
            Book book = bookService.findBookOrThrow(itemReq.bookId());

            if (book.getStock() < itemReq.quantity()) {
                throw new InsufficientStockException(
                        "Stock insuficiente para '%s'. Disponible: %d, solicitado: %d"
                                .formatted(book.getTitle(), book.getStock(), itemReq.quantity()));
            }

            book.setStock(book.getStock() - itemReq.quantity());

            BigDecimal subtotal = book.getPrice()
                    .multiply(BigDecimal.valueOf(itemReq.quantity()));

            PurchaseItem item = new PurchaseItem();
            item.setBook(book);
            item.setQuantity(itemReq.quantity());
            item.setUnitPrice(book.getPrice());
            item.setSubtotal(subtotal);
            item.setPurchase(purchase);

            purchase.getItems().add(item);
            total = total.add(subtotal);
        }

        purchase.setTotal(total);
        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponse> findAll() {
        return purchaseRepository.findAll().stream()
                .map(purchaseMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PurchaseResponse findById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compra no encontrada con ID: %d".formatted(id)));
        return purchaseMapper.toResponse(purchase);
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponse> findByCustomerEmail(String email) {
        return purchaseRepository.findByCustomerEmailOrderByPurchaseDateDesc(email)
                .stream()
                .map(purchaseMapper::toResponse)
                .toList();
    }

    @Transactional
    public PurchaseResponse cancelPurchase(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compra no encontrada con ID: %d".formatted(id)));

        if (purchase.getStatus() == PurchaseStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "La compra con ID: %d ya esta cancelada".formatted(id));
        }

        for (PurchaseItem item : purchase.getItems()) {
            Book book = item.getBook();
            book.setStock(book.getStock() + item.getQuantity());
        }

        purchase.setStatus(PurchaseStatus.CANCELLED);
        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    @Transactional(readOnly = true)
    public List<SalesReportByBookResponse> getSalesReportByBook(LocalDate from, LocalDate to) {
        LocalDateTime dateFrom = from != null ? from.atStartOfDay() : null;
        LocalDateTime dateTo = to != null ? to.atTime(23, 59, 59) : null;

        return purchaseRepository.getSalesReportByBook(dateFrom, dateTo).stream()
                .map(row -> new SalesReportByBookResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        ((Number) row[2]).longValue(),
                        (BigDecimal) row[3],
                        ((Number) row[4]).longValue()
                )).toList();
    }

    @Transactional(readOnly = true)
    public SalesSummaryResponse getSalesSummary(LocalDate from, LocalDate to) {
        LocalDateTime dateFrom = from != null ? from.atStartOfDay() : null;
        LocalDateTime dateTo = to != null ? to.atTime(23, 59, 59) : null;

        List<Object[]> result = purchaseRepository.getSalesSummary(dateFrom, dateTo);
        if (result.isEmpty()) {
            return new SalesSummaryResponse(BigDecimal.ZERO, 0L, BigDecimal.ZERO, null, null, null);
        }
        Object[] row = result.getFirst();
        return new SalesSummaryResponse(
                (BigDecimal) row[0],
                ((Number) row[1]).longValue(),
                (BigDecimal) row[2],
                row[3] != null ? ((Number) row[3]).longValue() : null,
                (String) row[4],
                row[5] != null ? ((Number) row[5]).longValue() : null
        );
    }
}
