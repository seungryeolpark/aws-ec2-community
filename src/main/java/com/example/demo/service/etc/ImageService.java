package com.example.demo.service.etc;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Image;
import com.example.demo.entity.enums.DeleteStatus;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageService {
    @Value("${file.save}")
    private String savePath;

    private final ImageRepository imageRepository;

    @Transactional
    public Long save(BoardDto.image imageDto) {
        Image image = Image.builder()
                .uuid(imageDto.getId())
                .url(imageDto.getUrl())
                .deleteStatus(imageDto.getDeleteStatus())
                .build();

        imageRepository.save(image);

        return image.getId();
    }

    public List<Image> findListByUUID(String uuid) {
        return imageRepository.getImageList(uuid);
    }

    public List<Image> findListByStatus(Enum<DeleteStatus> status) {
        return imageRepository.getImageListByStatus(status);
    }

    @Transactional
    public void imageListSetBoard(Board board, List<Image> imageList) {
        for (Image image : imageList) {
            if (image.getDeleteStatus() == DeleteStatus.Stored || board.getContent().contains(image.getUrl())) {
                image.setDeleteStatus(DeleteStatus.Stored);
                image.setBoard(board);
            } else {
                imageRepository.delete(image);
                String[] urlArr = image.getUrl().split("/");
                String filename = urlArr[urlArr.length-1];

                File file = new File(savePath + filename);

                if ( file.exists() ) {
                    file.delete();
                }
            }
        }
    }

    @Transactional
    public void imageListSetDeleted(List<Image> imageList) {
        for (Image image : imageList) {
            if (image.getDeleteStatus() == DeleteStatus.Stored) {
                image.setDeleteStatus(DeleteStatus.Deleted);
            }
        }
    }

    @Transactional
    public void deleteImageListByStatus() {
        imageRepository.deleteImageListByStatus();
    }
}
