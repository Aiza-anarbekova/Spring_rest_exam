package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.LessonRequest;
import com.example.spring_rest_exam.dto.VideoRequest;
import com.example.spring_rest_exam.dto.response.StudentResponse;
import com.example.spring_rest_exam.dto.response.VideoResponse;
import com.example.spring_rest_exam.dto.responseView.StudentResponseView;
import com.example.spring_rest_exam.dto.responseView.VideoResponseView;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Lesson;
import com.example.spring_rest_exam.model.Student;
import com.example.spring_rest_exam.model.Video;
import com.example.spring_rest_exam.repository.LessonRepository;
import com.example.spring_rest_exam.repository.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final LessonRepository lessonRepository;

    public VideoService(VideoRepository videoRepository, LessonRepository lessonRepository) {
        this.videoRepository = videoRepository;
        this.lessonRepository = lessonRepository;
    }

    public VideoResponse addVideo(VideoRequest request) {
        Video video =mapToEntity(request);
        return mapToResponse(videoRepository.save(video));
    }

    public Video mapToEntity(VideoRequest request){
        Video video = new Video();
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                ()->new NotFoundException(String.format("lesson with id %s not found",request.getLessonId())
        ));
        video.setVideoName(request.getVideoName());
        video.setLink(request.getLink());
        video.setLesson(lesson);
        return videoRepository.save(video);
    }

    public VideoResponse mapToResponse(Video video){
        return VideoResponse.builder()
                .id(video.getId())
                .videoName(video.getVideoName())
                .link(video.getLink())
                .build();
    }

    public VideoResponse getById(Long id) {
       Video video = videoRepository.findVideoById(id);
       return mapToResponse(video);
    }

    public List<Video> getVideoByLessonId(Long id) {
        return videoRepository.findVideoByLessonId(id);
    }

    public VideoResponse updateVideo(Long id,VideoRequest request) {
        Video video1 = videoRepository.findById(id).orElseThrow(
                ()->new NotFoundException(String.format("video with id - %s not found",id))
        );
        Video video = update(video1,request);
        return mapToResponse(videoRepository.save(video));
    }

    public Video update(Video video , VideoRequest request){
        video.setVideoName(request.getVideoName());
        video.setLink(request.getLink());
        return videoRepository.save(video);
    }

    public VideoResponse deleteVideo(Long id) {
        Video video = videoRepository.findVideoById(id);
        video.setLesson(null);
        videoRepository.deleteById(id);
        return mapToResponse(video);
    }
    public List<Video> search(String name, Pageable pageable){
        String text = name== null?"":name;
        return videoRepository.searchByVideoName(text.toUpperCase(),pageable);
    }

    public List<VideoResponse> getAll(List<Video> videos){
        List<VideoResponse> responses = new ArrayList<>();
        for (Video v:videos) {
            responses.add(mapToResponse(v));
        }
        return responses;
    }

    public VideoResponseView pagination(String text, int size, int page){
        Pageable pageable = PageRequest.of(page-1,size);
        VideoResponseView videoResponseView = new VideoResponseView();
        videoResponseView.setResponses(getAll(search(text.toUpperCase(),pageable)));
        return videoResponseView;
    }
}
