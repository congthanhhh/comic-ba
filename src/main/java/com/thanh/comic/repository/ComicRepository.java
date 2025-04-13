package com.thanh.comic.repository;


import com.thanh.comic.entity.Comic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, String> {

}
