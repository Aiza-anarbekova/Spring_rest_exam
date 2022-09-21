package com.example.spring_rest_exam.controllers;

import com.example.spring_rest_exam.dto.VideoRequest;
import com.example.spring_rest_exam.dto.response.VideoResponse;
import com.example.spring_rest_exam.dto.responseView.VideoResponseView;
import com.example.spring_rest_exam.model.Video;
import com.example.spring_rest_exam.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/video")
@PreAuthorize("hasAuthority('ADMIN')")
public class VideoController {
    private final VideoService videoService;
    @Autowired
    public VideoController(VideoService videoRepository) {
        this.videoService = videoRepository;
    }


    @PostMapping
    public VideoResponse saveLesson(@RequestBody VideoRequest request){
        return videoService.addVideo(request);
    }

    @GetMapping("{id}")
    public VideoResponse getById(@PathVariable Long id){
        return videoService.deleteVideo(id);
    }

    @PutMapping("/{id}")
    public VideoResponse updateVideo(@PathVariable Long id,
                                     @RequestBody VideoRequest request){
        return videoService.updateVideo(id, request);
    }

    @DeleteMapping("/{id}")
    public VideoResponse delete(@PathVariable("id") Long id){
        return videoService.deleteVideo(id);
    }

    @GetMapping
    public VideoResponseView pagination(@RequestParam(name = "text",required = false) String text,
                                        @RequestParam int page,
                                        @RequestParam int size){
        return videoService.pagination(text, page, size);
    }

}
