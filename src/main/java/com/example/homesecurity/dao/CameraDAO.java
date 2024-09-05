package com.example.homesecurity.dao;

import com.example.homesecurity.entity.Camera;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CameraDAO {
    void save(Camera sensor);

    List<Camera> findAll();

    List<Camera> findByName(String name);


    void updateCamera(Camera camera);

    void deleteCameraById(Long id);
}
