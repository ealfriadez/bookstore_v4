package com.example.bookstore.mapper;

import com.example.bookstore.dto.request.AuthorRequest;
import com.example.bookstore.dto.response.AuthorResponse;
import com.example.bookstore.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorResponse toResponse(Author author);

    @Mapping(target = "id", ignore = true)
    Author toEntity(AuthorRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(AuthorRequest request,
                                  @MappingTarget Author author);
}
