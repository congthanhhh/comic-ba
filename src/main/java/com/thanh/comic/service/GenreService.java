package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.GenreRequest;
import com.thanh.comic.dto.response.Comic.GenreResponse;
import com.thanh.comic.entity.Genre;
import com.thanh.comic.exception.AppException;
import com.thanh.comic.exception.ErrorCode;
import com.thanh.comic.maper.GenreMapper;
import com.thanh.comic.repository.GenreRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class GenreService {

    GenreRepository genreRepository;
    GenreMapper genreMapper;

    public GenreResponse createGenre(GenreRequest request) {
        if(genreRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.USER_EXITED);
        }
        Genre genre = genreMapper.toGenre(request);
        return genreMapper.toGenreResponse(genreRepository.save(genre));
    }
}
