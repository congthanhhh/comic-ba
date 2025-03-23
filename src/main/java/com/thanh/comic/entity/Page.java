package com.thanh.comic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long pageId;
//    @ManyToOne
//    @JoinColumn(name = "chapter_id")
//    Chapter chapter;
    int pageNumber;
    String imageUrl;
}
