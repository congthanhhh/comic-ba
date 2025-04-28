package com.thanh.comic.service;

import com.thanh.comic.contanst.PredefinedStatusComic;
import com.thanh.comic.dto.request.Comic.ComicRequest;
import com.thanh.comic.dto.request.Comic.ComicUpdateRequest;
import com.thanh.comic.dto.response.Comic.ComicResponse;
import com.thanh.comic.dto.response.Comic.PaginatedResponse;
import com.thanh.comic.entity.Comic;
import com.thanh.comic.entity.Genre;
import com.thanh.comic.exception.AppException;
import com.thanh.comic.exception.ErrorCode;
import com.thanh.comic.exception.ResourceNotFoundException;
import com.thanh.comic.maper.ComicMapper;
import com.thanh.comic.repository.ComicRepository;
import com.thanh.comic.repository.GenreRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class ComicService {
    ComicRepository comicRepository;
    ComicMapper comicMapper;
    CloudinaryService cloudinaryService;
    GenreRepository genreRepository;

    @Transactional
    public ComicResponse createComic(ComicRequest request) throws IOException {
        try {
            Comic comic = comicMapper.toComic(request);
            comic.setStatus(PredefinedStatusComic.ON_GOING);
            comic.setIsActive(true);
            
            // Upload image to Cloudinary
            if (request.getFile() != null && !request.getFile().isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(request.getFile(), "comic_web/");
                comic.setImageUrl(imageUrl);
            }
            
            // Set genres
            if (request.getGenres() != null && !request.getGenres().isEmpty()) {
                List<Genre> genres = genreRepository.findAllById(request.getGenres());
                comic.setGenres(genres);
            }

            Comic savedComic = comicRepository.save(comic);
            return comicMapper.toComicResponse(savedComic);
        } catch (DataAccessException e) {
            log.error("Database error when creating comic: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        } catch (IOException e) {
            log.error("IO error when creating comic: {}", e.getMessage());
            throw e; 
        }
    }

    public ComicResponse getComicById(String id) {
        Comic comic = findComicById(id);
        return comicMapper.toComicResponse(comic);
    }
    
    public List<ComicResponse> getAllComics() {
        try {
            return comicRepository.findAll().stream()
                    .filter(Comic::getIsActive)
                    .map(comicMapper::toComicResponse)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Database error when fetching comics: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public PaginatedResponse<ComicResponse> getComicsPaginated(int page, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Comic> comicPage = comicRepository.findActiveComicsOrderByLatestChapter(pageable);

            List<ComicResponse> comicResponses = comicPage.getContent().stream()
                    .map(comicMapper::toComicResponse)
                    .toList();
                    
            return PaginatedResponse.<ComicResponse>builder()
                    .content(comicResponses)
                    .totalPages(comicPage.getTotalPages())
                    .totalElements(comicPage.getTotalElements())
                    .currentPage(page)
                    .pageSize(pageSize)
                    .build();
        } catch (DataAccessException e) {
            log.error("Database error when fetching paginated comics: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    
    @Transactional
    public ComicResponse updateComic(String id, ComicUpdateRequest request) throws IOException {
        try {
            Comic comic = findComicById(id);
            
            comicMapper.updateComicFromRequest(request, comic);

            if (request.getFile() != null && !request.getFile().isEmpty()) {
                String oldImageUrl = comic.getImageUrl();

                String newImageUrl = cloudinaryService.uploadImage(request.getFile(), "comic_web/");
                comic.setImageUrl(newImageUrl);

                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    cloudinaryService.deleteImage(oldImageUrl);
                }
            }
            
            // Update genres if provided
            if (request.getGenres() != null && !request.getGenres().isEmpty()) {
                List<Genre> genres = genreRepository.findAllById(request.getGenres());
                if (genres.size() != request.getGenres().size()) {
                    throw new AppException(ErrorCode.GENRE_NOT_EXISTED);
                }
                comic.setGenres(genres);
            }

            Comic updatedComic = comicRepository.save(comic);
            return comicMapper.toComicResponse(updatedComic);
        } catch (DataAccessException e) {
            log.error("Database error when updating comic: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    
    public void updateActive(String id) {
            Comic comic = findComicById(id);
            comic.setIsActive(false);
            comicRepository.save(comic);
    }

    @Transactional
    public void deleteComic(String id) {
        try {
            Comic comic = findComicById(id);

            // Delete image from Cloudinary if it exists
            if (comic.getImageUrl() != null && !comic.getImageUrl().isEmpty()) {
                cloudinaryService.deleteImage(comic.getImageUrl());
            }

            comicRepository.delete(comic);
        } catch (DataAccessException e) {
            log.error("Database error when deleting comic: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    
    private Comic findComicById(String id) {
        return comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic not found with id: " + id));
    }
}
