package com.example.bookstore.mapper;

import com.example.bookstore.dto.response.PurchaseItemResponse;
import com.example.bookstore.dto.response.PurchaseResponse;
import com.example.bookstore.model.Purchase;
import com.example.bookstore.model.PurchaseItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(target = "items", source = "items")
    @Mapping(target = "status", expression = "java(purchase.getStatus().name())")
    PurchaseResponse toResponse(Purchase purchase);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    PurchaseItemResponse toItemResponse(PurchaseItem item);

    List<PurchaseItemResponse> toItemResponseList(List<PurchaseItem> items);
}
