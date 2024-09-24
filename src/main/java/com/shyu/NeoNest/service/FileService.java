package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Transactional
@Service
public class FileService {

    @Value("${spring.file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    // 파일을 삭제하는 로직
    public void deleteFile(String storeFileName) {
        if (storeFileName == null || storeFileName.isEmpty()) {
            return;  // 삭제할 파일이 없으면 바로 종료
        }

        Path filePath = Paths.get(getFullPath(storeFileName));
        try {
            Files.deleteIfExists(filePath);  // 파일이 존재하면 삭제
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 오류 발생: " + storeFileName, e);
        }
    }

    // 파일명을 UUID로 변경하여 저장
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 파일 확장자를 추출하는 메서드
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
