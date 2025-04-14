package com.thanh.comic.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thanh.comic.exception.AppException;
import com.thanh.comic.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {

    Cloudinary cloudinary;
    List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    public String uploadImage(MultipartFile file, String folder)
            throws IOException {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
        }
        
        String[] fileNameParts = getFileName(file.getOriginalFilename());
        if (fileNameParts.length < 2) {
            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
        }
        
        String extension = fileNameParts[1].toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
        }
        
        String publicValue = folder + generatePublicValue(file.getOriginalFilename());
        File fileUpload = null;
        
        try {
            fileUpload = convert(file);
            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
            return cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));
        } catch (IOException e) {
            log.error("IO Exception during file upload: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_PROCESSING_ERROR);
        } finally {
            if (fileUpload != null) {
                cleanDisk(fileUpload);
            }
        }
    }

    public void deleteImage(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) {
            log.warn("Attempted to delete null or empty image URL");
            return;
        }
        
        try {
            // Extract the public ID from the Cloudinary URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Successfully deleted image with public ID: {}", publicId);
            } else {
                log.warn("Could not extract public ID from URL: {}", imageUrl);
            }
        } catch (IOException e) {
            log.error("Error deleting image from Cloudinary: {}", e.getMessage());
            // We don't want to throw an exception here as it might interrupt the main flow
            // Just log the error instead
        }
    }
    
    private String extractPublicIdFromUrl(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) {
            return null;
        }
        
        try {
            // Typical Cloudinary URL format: https://res.cloudinary.com/cloud_name/image/upload/v1234567890/comic_web/uuid_filename.extension
            // We need to extract the "comic_web/uuid_filename" part
            String[] urlParts = imageUrl.split("/");
            int length = urlParts.length;
            
            if (length < 2) {
                return null;
            }

            // Get the last part (filename with extension)
            String fileWithExtension = urlParts[length - 1];
            // Get the version part or folder name
            String versionOrFolder = urlParts[length - 2];
            
            // Check if the version part starts with 'v'
            if (versionOrFolder.startsWith("v")) {
                // If there's a version number, we need to get the folder from the part before that
                String folder = urlParts[length - 3];
                String fileName = fileWithExtension.substring(0, fileWithExtension.lastIndexOf('.'));
                return folder + "/" + fileName;
            } else {
                // No version number, use the folder and filename
                String fileName = fileWithExtension.substring(0, fileWithExtension.lastIndexOf('.'));
                return versionOrFolder + "/" + fileName;
            }
        } catch (Exception e) {
            log.error("Error extracting public ID from URL: {}", e.getMessage());
            return null;
        }
    }

    private File convert(MultipartFile file) throws IOException {
        String[] fileNameParts = getFileName(file.getOriginalFilename());
        String fileName = generatePublicValue(file.getOriginalFilename());
        String extension = fileNameParts[1];
        
        File convFile = new File(StringUtils.join(fileName, ".", extension));
        
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
            return convFile;
        } catch (IOException e) {
            log.error("Error converting file: {}", e.getMessage());
            throw new IOException("Failed to convert uploaded file", e);
        }
    }

    private void cleanDisk(File file) {
        try {
            log.info("Cleaning up temporary file: {}", file.toPath());
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error cleaning up temporary file: {}", e.getMessage());
        }
    }

    public String generatePublicValue(String originalName) {
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        if (originalName == null) {
            return new String[]{"unknown", "jpg"};
        }
        return originalName.split("\\.");
    }
}
