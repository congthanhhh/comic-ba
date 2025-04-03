package com.thanh.comic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int chapterNumber;
    String title;
    LocalDateTime releaseDate;
    String summary;
    int viewCount;

    @ManyToOne
    @JoinColumn(name = "comic_id")
    Comic comic;

    @OneToMany(mappedBy = "chapter")
    List<Page> pages;
}
