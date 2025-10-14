package com.windy.cafemanagement.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class UploadService {

    public UploadService() {
    }

    public String uploadImage(MultipartFile file, String target) {
        if (file == null || file.isEmpty()) {
            return null;
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
            return "/assets/images/" + target + "/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
