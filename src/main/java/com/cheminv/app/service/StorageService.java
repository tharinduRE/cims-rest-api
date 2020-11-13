package com.cheminv.app.service;

import io.github.jhipster.security.RandomUtil;
import liquibase.util.file.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private static final Path UPLOAD_FOLDER = Paths.get("./uploads");

    private static final Path USER_AVATAR_FOLDER = Paths.get(UPLOAD_FOLDER +"/user-avatar");

    private static final Path REPORTS_FOLDER = Paths.get(UPLOAD_FOLDER +"/reports");

    public void init() throws IOException {
        if(!Files.exists(UPLOAD_FOLDER)){
            Files.createDirectories(UPLOAD_FOLDER);
        }
        if(!Files.exists(USER_AVATAR_FOLDER)){
            Files.createDirectories(USER_AVATAR_FOLDER);
        }
    }

    public String saveUserAvatar(MultipartFile file) throws Exception {
        try {
            init();
            String fileName = "user-image-"+ RandomUtil.generateRandomAlphanumericString()
                +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
            Path dfile = USER_AVATAR_FOLDER.resolve(fileName);
            BufferedImage ResizedImage = resizeImage(ImageIO.read(file.getInputStream()),512,512);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(ResizedImage,"png", os);
            InputStream imageInputStream = new ByteArrayInputStream(os.toByteArray());
            Files.copy(imageInputStream, dfile, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new Exception("File Cannot be Uploaded");
        }

    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public Resource loadUserAvatar(String fileName) throws Exception {
        try {
            Path file = USER_AVATAR_FOLDER.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new Exception("Could not find file");
            }
        }
        catch (Exception e) {
            throw new Exception("Could not download file");
        }
    }

}
