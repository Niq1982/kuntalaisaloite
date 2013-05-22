package fi.om.municipalityinitiative.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageFinder {
    List<FileImageFinder.FileJson>  getImages();

    @Cacheable("infoTextImages")
    FileImageFinder.FileInfo getFile(String fileName, String version) throws IOException;

    void validateAndSaveFile(MultipartFile multipartFile) throws IOException;
}
