package com.example.springbooks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BooksRepository extends JpaRepository<Book, Long> {
    Book findByisbn(String isbn);
}
