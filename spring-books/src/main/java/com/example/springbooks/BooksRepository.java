package com.example.springbooks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BooksRepository extends JpaRepository<Book, Long> {
    Book findByisbn(String isbn);
    Book findByBookID(Long id);
}
