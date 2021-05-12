package com.example.accessingdatajpa.controller;

import com.example.accessingdatajpa.pojo.VideoModel;
import com.example.accessingdatajpa.repository.Video;
import com.example.accessingdatajpa.repository.VideoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;

@RestController
public class VideoController {


    VideoRepository videoRepository;

    @Autowired
    public VideoController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @GetMapping("/play-video/{id}")
    public ResponseEntity<ByteArrayResource> allVideos(@PathVariable String id) {
        Optional<Video> video = videoRepository.findById(Long.parseLong(id));
        return video.map(value -> ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(new ByteArrayResource(value.getvUsage()))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("getAll/video")
    public ResponseEntity<List<VideoModel>> getAll() {
        List<Video> videos = videoRepository.findAll();
        if (videos == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(getVideoModels(videos));
        }

    }

    private List<VideoModel> getVideoModels(List<Video> videos) {
        List<VideoModel> videoModels = new ArrayList<VideoModel>();
        for (Video video : videos) {
            System.out.println(video.getvName());
            String url = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/play-video/")
                    .path(String.valueOf(video.getId()))
                    .toUriString();
            videoModels.add(new VideoModel(video.getvName(), video.getvDescription(), url));
        }
        return videoModels;
    }

    @GetMapping("get/video/name/{name}")
    public ResponseEntity<List<VideoModel>> byName(@PathVariable String name) {
        List<Video> videos = videoRepository.findByvName(name);
        if (videos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(getVideoModels(videos));
        }
    }

    @GetMapping("get/video/id/{id}")
    public ResponseEntity<List<VideoModel>> byId(@PathVariable String id) {
        Optional<Video> video = videoRepository.findById(Long.parseLong(id));
        return video.map(value -> ResponseEntity.ok(getVideoModels(Collections.singletonList(value)))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "add/video", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}
            , produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public void put(@RequestParam("video") String video, @RequestParam("usage") MultipartFile videoFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Video v = objectMapper.readValue(video, Video.class);
        System.out.println("add " + Arrays.toString(videoFile.getBytes()));
        byte[] usage = videoFile.getBytes();
        videoRepository.save(new Video(v.getvName(), v.getvDescription(), usage));
    }

    @DeleteMapping("delete/video/id/{id}")
    public void deleteById(@PathVariable String id) {
        videoRepository.deleteById(Long.parseLong(id));
    }

    @DeleteMapping("delete/video/name/{name}")
    public void deleteByName(@PathVariable String name) {
        videoRepository.deleteByvName(name);
    }

    @PostMapping(value = "update/video/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}
            , produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public void post(@RequestParam("video") String video, @RequestParam("usage") MultipartFile videoFile, @PathVariable String id) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Video v = objectMapper.readValue(video, Video.class);
        System.out.println("add " + Arrays.toString(videoFile.getBytes()));
        byte[] usage = videoFile.getBytes();
        Video newVideo = new Video(v.getvName(), v.getvDescription(), usage);
        videoRepository.updateVideoById(Long.parseLong(id), newVideo);
    }

}
