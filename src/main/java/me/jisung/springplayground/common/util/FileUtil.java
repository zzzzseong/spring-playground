package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Slf4j(topic = "FileUtil")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    /**
     * Retrieves a file from the static resource directory.
     *
     * @param path the path to the static resource
     * @return the File object, or null if an error occurs
     */
    public static File get(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return resource.getFile();
        } catch (IOException e) {
            log.error("Failed to retrieve static resource file: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Converts a File to a MultipartFile.
     *
     * @param file the File to convert
     * @return the converted MultipartFile, or null if an error occurs
     */
    public static MultipartFile toMultipartFile(File file) {
        try {
            final String originalFilename = file.getName();
            final String contentType = Files.probeContentType(file.toPath());
            final byte[] content = Files.readAllBytes(file.toPath());

            return new SimpleMultipartFile(originalFilename, originalFilename, contentType, content);
        } catch (IOException e) {
            log.error("Failed to convert file to MultipartFile: {}", e.getMessage());
            return null;
        }
    }
}

class SimpleMultipartFile implements MultipartFile {

    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] content;

    public SimpleMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.content = content;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        Files.write(dest.toPath(), content);
    }
}
