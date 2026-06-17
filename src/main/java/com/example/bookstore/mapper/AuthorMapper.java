package com.example.bookstore.mapper;

import com.example.bookstore.dto.request.AuthorRequest;
import com.example.bookstore.dto.response.AuthorResponse;
import com.example.bookstore.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    Author toEntity(AuthorRequest request);

    AuthorResponse toResponse(Author author);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(AuthorRequest request,
                                  @MappingTarget Author author);

    List<AuthorResponse> toResponseList(List<Author> authors);
}
