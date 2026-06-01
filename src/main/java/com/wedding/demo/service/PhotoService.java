package com.wedding.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PhotoService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadPhoto(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    public List<String> getAllPhotos() {
        List<String> photoUrls = new ArrayList<>();
        try {
            // Cloudinary API'sini kullanarak yüklenen resimleri getiriyoruz.
            // Not: Çok fazla resim varsa pagination gerekebilir.
            Map result = cloudinary.api().resources(ObjectUtils.asMap("type", "upload", "max_results", 500));
            List<Map> resources = (List<Map>) result.get("resources");
            
            if (resources != null) {
                for (Map resource : resources) {
                    photoUrls.add(resource.get("url").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoUrls;
    }
}
