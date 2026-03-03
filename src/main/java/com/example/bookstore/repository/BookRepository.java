package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    //QUERY METHOD
    boolean existsByTitleAndAuthorId(String title, Long authorId);

    @Query(value = """
            SELECT *
            FROM books
            WHERE price > (SELECT AVG(price) FROM books)
            """, nativeQuery = true)
    List<Book> findBooksAboveAveragePrice();

    @Query("SELECT COALESCE(AVG(b.price), 0) FROM Book b")
    BigDecimal findAveragePrice();

    @Modifying
    @Query(value = """
            UPDATE books
            SET price = price * :factor
            WHERE editorial = :editorial
            """, nativeQuery = true)
    int updatePriceByEditorial(@Param("editorial") String editorial,
                               @Param("factor") BigDecimal factor);
}
