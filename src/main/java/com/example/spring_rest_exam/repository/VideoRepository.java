package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.model.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    List<Video> findVideoByLessonId(Long id);
    Video findVideoById(Long id);

    @Query("select v from Video  v where upper(v.videoName) like  concat('%',:text,'%') ")
    List<Video> searchByVideoName(String text, Pageable pageable);

}
