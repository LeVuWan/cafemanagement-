package com.windy.cafemanagement.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * UploadService
 *
 * Version 1.0
 *
 * Date: 11-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 */
@Service
public class UploadService {

    public UploadService() {
    }

    /**
     * upload image to target folder under src/main/resources/static/assets/img
     * 
     * @param file
     * @param target
     * @return relative URL path to saved image (e.g.,
     *         /assets/img/{target}/{filename})
     * @throws NullPointerException when file or target is null/empty
     */
    public String uploadImage(MultipartFile file, String target) {
        if (file == null) {
            throw new NullPointerException("file not found");
        }

        if (file.isEmpty()) {
            throw new NullPointerException("file is empty");
        }

        if (target == null || target.trim().isEmpty()) {
            throw new NullPointerException("target not found");
        }

        try {
            // Đọc dữ liệu từ file
            byte[] bytes = file.getBytes();

            // Xác định thư mục gốc trong project
            String rootPath = new File("src/main/resources/static/assets/img/" + target)
                    .getAbsolutePath();

            // Tạo thư mục nếu chưa tồn tại
            File dir = new File(rootPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Tạo tên file duy nhất
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            // Tạo file đích
            File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);

            // Ghi dữ liệu xuống ổ đĩa
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(bytes);
            }

            // Trả về đường dẫn URL tương đối để lưu DB
            return "/assets/img/" + target + "/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
