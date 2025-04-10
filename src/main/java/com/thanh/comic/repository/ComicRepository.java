package com.thanh.comic.repository;


import com.thanh.comic.entity.Comic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComicRepository extends JpaRepository<Comic, String> {
}
