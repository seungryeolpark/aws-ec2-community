package com.example.demo.api;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.enums.DeleteStatus;
import com.example.demo.service.etc.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UploadAPI {

    @Value("${file.save}")
    private String savePath;

    @Value("${file.upload}")
    private String uploadPath;

    private final ImageService imageService;

    @PostMapping("/post/upload")
    public String upload(
//            @RequestParam Map<String, Object> map,
            @RequestParam String id,
            MultipartHttpServletRequest request) throws IOException {
//        CsrfToken token = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
        List<MultipartFile> fileList = request.getFiles("upload");
        log.info("fileList={}, id={}", fileList, id);

        String fileSavePath = null;
        String fileUploadPath = null;

        for (MultipartFile mf : fileList) {
            if (fileList.get(0).getSize() > 0) {
                String originalFileName = mf.getOriginalFilename();

                String ext = StringUtils.getFilenameExtension(originalFileName);

                String newFileName = "img_" + UUID.randomUUID() + "." + ext;

                fileSavePath = savePath + newFileName;
                fileUploadPath = uploadPath + newFileName;

                File file = new File(fileSavePath);

                mf.transferTo(file);
            }
        }
//        String fileName = "http://13.124.173.100:8080" + fileUploadPath;
        String fileName = fileUploadPath;

        BoardDto.image imageDto = BoardDto.image.builder()
                .id(id)
                .url(fileName)
                .deleteStatus(DeleteStatus.Deleted)
                .build();

        imageService.save(imageDto);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uploaded", true);
        jsonObject.put("url", fileName);

        String response = jsonObject.toString();
        log.info("response={}", response);

        return response;
    }
}

