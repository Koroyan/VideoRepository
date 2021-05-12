package com.example.accessingdatajpa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@EnableJpaRepositories
public interface VideoRepository extends CrudRepository<Video, Long> {
     List<Video> findAll();
     List<Video> findByvName(String vName);
     Optional<Video> findById(Long id);
     void deleteById(Long aLong);
     @Transactional
     @Modifying
     @Query("delete from Video v where v.vName = ?1")
     void deleteByvName(String vName);

     default void updateVideoById(Long id, Video video){
          Video v=new Video();
          if (findById(id).isPresent()) {
                v = findById(id).get();
          }
          v.setvName(video.getvName());
          v.setvDescription(video.getvDescription());
          v.setvUsage(video.getvUsage());
          save(v);
     }
}
