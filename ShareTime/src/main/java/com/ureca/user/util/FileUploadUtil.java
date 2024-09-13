// FileUploadUtil.java의 첫 번째 줄
package com.ureca.user.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUploadUtil {

    // 파일 업로드 기본 경로를 설정
    @Value("${file.upload-dir}")
    private String uploadDir; // application.properties의 설정 값 사용

    // 파일 저장 메서드
    public String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        // uploads 폴더 경로를 생성합니다.
        File uploadDirectory = new File(uploadDir);

        // 폴더가 존재하지 않으면 생성합니다.
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();  // 디렉토리를 생성합니다.
        }

        // 파일을 절대 경로의 uploads 폴더에 저장합니다.
        String filePath = uploadDirectory + File.separator + fileName;
        File file = new File(filePath);
        multipartFile.transferTo(file);

        return filePath;  // 저장된 파일의 경로를 반환합니다.
    }
}