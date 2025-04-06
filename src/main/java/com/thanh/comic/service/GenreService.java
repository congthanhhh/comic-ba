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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public GenreResponse updateGenre(Long genreId,GenreRequest request) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_EXISTED));
        genre.setName(request.getName());
        genre.setDescription(request.getDescription());
        return genreMapper.toGenreResponse(genreRepository.save(genre));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteGenre(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_EXISTED));
        genreRepository.delete(genre);
    }

    public GenreResponse getGenre(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_EXISTED));
        return genreMapper.toGenreResponse(genre);
    }

    public List<GenreResponse> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toGenreResponse)
                .toList();
    }
}
