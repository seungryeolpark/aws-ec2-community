package com.example.demo.repository.custom;

import com.example.demo.entity.Image;
import com.example.demo.entity.enums.DeleteStatus;
import com.example.demo.entity.enums.Role;

import java.util.List;

public interface ImageRepositoryCustom {

    List<Image> getImageList(String uuid);

    List<Image> getImageListByStatus(Enum<DeleteStatus> status);

    void deleteImageListByStatus();
}
