package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    private static final Tika tika = new Tika();

    private static final String CONTENT_TYPE_DEFAULT = MediaType.APPLICATION_OCTET_STREAM_VALUE;

    public static boolean isEmpty(MultipartFile file) throws IOException {
        return file == null || file.isEmpty();
    }

    /**
     * Detects the MIME type of the content provided by the given InputStream.
     *
     * @param is the InputStream containing the content to be analyzed
     * @return the detected MIME type as a String (e.g., "image/png", "application/pdf")
     * @throws IOException if an I/O error occurs while reading the InputStream
     */
    public static String getMimeType(InputStream is) throws IOException {
        return tika.detect(is);
    }

    /**
     * Loads a file from the classpath and converts it into a {@link MultipartFile}.
     * <p>
     * This method is useful for scenarios where static resources (e.g., images or templates)
     * are bundled within the application and need to be reused as MultipartFile objects,
     * such as for file upload simulation or external API calls.
     * </p>
     *
     * @param path the relative path to the file within the classpath (e.g., "images/logo.png")
     * @return a {@code MultipartFile} representing the file, or {@code null} if the file cannot be found or read
     */
    public static MultipartFile getMultipartFileFromClassPath(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            String filename = resource.getFilename();
            byte[] content = resource.getInputStream().readAllBytes();
            String contentType = Optional.ofNullable(Files.probeContentType(Paths.get(filename)))
                    .orElse(CONTENT_TYPE_DEFAULT);

            return new SimpleMultipartFile(filename, filename, contentType ,content);
        } catch (IOException e) {
            log.error("Failed to load resource [{}] from classpath as MultipartFile: {}", path, e.getMessage());
            return null;
        }
    }


    static class SimpleMultipartFile implements MultipartFile {

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

        @Override public String getName() { return name; }
        @Override public String getOriginalFilename() { return originalFilename; }
        @Override public String getContentType() { return contentType; }
        @Override public boolean isEmpty() { return content == null || content.length == 0; }
        @Override public long getSize() { return content.length; }
        @Override public byte[] getBytes() { return content; }
        @Override public InputStream getInputStream() { return new ByteArrayInputStream(content); }
        @Override public void transferTo(File dest) throws IOException { Files.write(dest.toPath(), content); }
    }
}
