
-- AUTORES
INSERT INTO authors (first_name, last_name, nationality) VALUES ('Robert', 'Martin', 'American');
INSERT INTO authors (first_name, last_name, nationality) VALUES ('Joshua', 'Bloch', 'American');
INSERT INTO authors (first_name, last_name, nationality) VALUES ('Martin', 'Fowler', 'British');
INSERT INTO authors (first_name, last_name, nationality) VALUES ('Eric', 'Evans', 'American');
INSERT INTO authors (first_name, last_name, nationality) VALUES ('Kathy', 'Sierra', 'American');

-- LIBROS
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Clean Code', 39.99, 'https://images.example.com/clean-code.jpg', 15, 'PENGUIN', 1);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Clean Architecture', 44.99, 'https://images.example.com/clean-architecture.jpg', 10, 'PENGUIN', 1);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('The Clean Coder', 34.99, 'https://images.example.com/clean-coder.jpg', 8, 'OREILLY', 1);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Effective Java', 49.99, 'https://images.example.com/effective-java.jpg', 20, 'ADDISON_WESLEY', 2);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Java Puzzlers', 29.99, 'https://images.example.com/java-puzzlers.jpg', 5, 'ADDISON_WESLEY', 2);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Refactoring', 54.99, 'https://images.example.com/refactoring.jpg', 12, 'OREILLY', 3);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Patterns of Enterprise Application Architecture', 59.99, 'https://images.example.com/patterns-enterprise.jpg', 7, 'MANNING', 3);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Domain-Driven Design', 64.99, 'https://images.example.com/ddd.jpg', 9, 'ADDISON_WESLEY', 4);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Head First Java', 35.99, 'https://images.example.com/head-first-java.jpg', 25, 'OREILLY', 5);
INSERT INTO books (title, price, image_url, stock, editorial, author_id) VALUES ('Head First Design Patterns', 42.99, 'https://images.example.com/head-first-patterns.jpg', 18, 'OREILLY', 5);
