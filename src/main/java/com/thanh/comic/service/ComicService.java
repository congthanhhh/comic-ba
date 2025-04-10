package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.ComicRequest;
import com.thanh.comic.dto.response.Comic.ComicResponse;
import com.thanh.comic.maper.ComicMapper;
import com.thanh.comic.repository.ComicRepository;
import com.thanh.comic.repository.GenreRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ComicService {

    ComicRepository comicRepository;
    GenreRepository genreRepository;
    CloudinaryService cloudinaryService;
    ComicMapper comicMapper;

    public ComicResponse create(ComicRequest request) throws IOException {
        String imgUrl = cloudinaryService.uploadImage(request.getFile());
        var comic = comicMapper.toComic(request);
        comic.setImageUrl(imgUrl);
        comic.setGenres(genreRepository.findAllById(request.getGenres()));
        comic = comicRepository.save(comic);
        return comicMapper.toComicResponse(comic);
    }

}
