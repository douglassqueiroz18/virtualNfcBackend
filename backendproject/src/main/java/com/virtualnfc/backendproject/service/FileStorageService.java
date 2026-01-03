package com.virtualnfc.backendproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile; // IMPORTANTE
import java.io.IOException; // IMPORTANTE
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
@Service
public class FileStorageService {

    public String saveFile(MultipartFile file, String prefix, Long id) {

        try {
            String os = System.getProperty("os.name").toLowerCase();
            String uploadPath;

            if (os.contains("win")) {
                uploadPath = System.getProperty("user.home")
                        + File.separator + "Documents"
                        + File.separator + "viva-nfc"
                        + File.separator + "arquivos";
            } else {
                uploadPath = "/var/www/viva-nfc/arquivos";
            }

            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = prefix + "_" + id + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadPath, fileName);

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/" + fileName;

        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar arquivo no servidor", e);
        }
    }
}
