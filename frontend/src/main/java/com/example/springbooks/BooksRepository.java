package com.example.springbooks;

import org.springframework.data.jpa.repository.JpaRepository;

interface BooksRepository extends JpaRepository<Books, Long> {
    
}
