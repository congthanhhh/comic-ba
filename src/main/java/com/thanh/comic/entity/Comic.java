package com.thanh.comic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String imageUrl;
    String status;
    String description;
    int viewCount;
    String ageRating;
    Boolean isDeleted = true;

    @OneToMany(mappedBy = "comic")
    List<Chapter> chapters;

    @ManyToMany
    @JoinTable(name = "comic_genres", joinColumns = @JoinColumn(name = "comic_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    List<Genre> genres;
}
