package com.example.bookstore.mapper;

import com.example.bookstore.dto.request.BookRequest;
import com.example.bookstore.dto.response.BookResponse;
import com.example.bookstore.dto.response.BookReportResponse;
import com.example.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(target = "authorName", expression = """
             java(book.getAuthor().getFirstName()
                  + " " + book.getAuthor().getLastName())
             """)
    @Mapping(target = "editorial", expression = """
             java(book.getEditorial().name())
             """)
    BookResponse toResponse(Book book);

    @Mapping(target = "authorName", expression = """
             java(book.getAuthor().getFirstName()
                  + " " + book.getAuthor().getLastName())
             """)
    @Mapping(target = "editorial", expression = """
             java(book.getEditorial().name())
             """)
    @Mapping(target = "averagePrice", ignore = true)
    BookReportResponse toReportResponse(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    Book toEntity(BookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    void updateEntityFromRequest(BookRequest request,
                                  @MappingTarget Book book);
}
