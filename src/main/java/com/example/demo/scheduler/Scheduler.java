package com.example.demo.scheduler;

import com.example.demo.entity.Image;
import com.example.demo.entity.enums.DeleteStatus;
import com.example.demo.service.etc.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    @Value("${file.save}")
    private String savePath;

    final int day = 24 * 60 * 60 * 1000;

    private final ImageService imageService;

    @Scheduled(fixedDelay = day)
    public void deleteGarbageImage() {

        List<Image> imageList = imageService.findListByStatus(DeleteStatus.Deleted);

        for (Image image : imageList) {
            log.info("imageId={}", image.getId());

            String[] urlArr = image.getUrl().split("/");
            String filename = urlArr[urlArr.length-1];

            File file = new File(savePath + filename);

            if ( file.exists() ) {
                file.delete();
            }
        }

        imageService.deleteImageListByStatus();
    }
}
