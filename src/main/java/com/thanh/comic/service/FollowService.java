package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.FollowRequest;
import com.thanh.comic.dto.response.Comic.FollowResponse;
import com.thanh.comic.entity.Follow;
import com.thanh.comic.maper.FollowMapper;
import com.thanh.comic.repository.ComicRepository;
import com.thanh.comic.repository.FollowRepository;
import com.thanh.comic.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowService {

    FollowRepository followRepository;
    ComicRepository comicRepository;
    UserRepository userRepository;
    FollowMapper followMapper;

    public FollowResponse createFollow(FollowRequest request) {
        Follow follow = new Follow();
        follow.setComic(comicRepository.findById(request.getComicId())
                .orElseThrow(() -> new RuntimeException("Comic not found")));
        follow.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        return followMapper.toFollowResponse(followRepository.save(follow));
    }

    public List<FollowResponse> getAll() {
        return followRepository.findAll().stream()
                .map(followMapper::toFollowResponse)
                .toList();
    }

    public List<FollowResponse> getAllByUserId(String userId) {
        return followRepository.findAllByUserId(userId).stream()
                .map(followMapper::toFollowResponse)
                .toList();
    }




}
