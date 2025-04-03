package com.thanh.comic.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/page")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PageController {
}
