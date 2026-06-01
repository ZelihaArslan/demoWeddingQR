package com.wedding.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    @Autowired
    private Cloudinary cloudinary;

    public void uploadFile(MultipartFile file) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("resource_type", "auto"); // Cloudinary'nin dosya türünü otomatik algılamasını sağlar (resim, video vb.)
        
        // En güvenli ve en uyumlu yöntem olan getBytes() metoduna geri dönüyoruz.
        cloudinary.uploader().upload(file.getBytes(), params);
    }

    public List<Map<String, String>> getAllMedia() {
        List<Map<String, String>> mediaList = new ArrayList<>();
        try {
            // Cloudinary API'sini kullanarak hem resimleri hem de videoları getiriyoruz.
            Map imageResult = cloudinary.api().resources(ObjectUtils.asMap("resource_type", "image", "max_results", 500));
            List<Map> imageResources = (List<Map>) imageResult.get("resources");
            if (imageResources != null) {
                for (Map resource : imageResources) {
                    Map<String, String> media = new HashMap<>();
                    media.put("url", resource.get("secure_url").toString());
                    media.put("resource_type", "image");
                    mediaList.add(media);
                }
            }
            
            Map videoResult = cloudinary.api().resources(ObjectUtils.asMap("resource_type", "video", "max_results", 500));
            List<Map> videoResources = (List<Map>) videoResult.get("resources");
             if (videoResources != null) {
                for (Map resource : videoResources) {
                    Map<String, String> media = new HashMap<>();
                    media.put("url", resource.get("secure_url").toString());
                    media.put("resource_type", "video");
                    mediaList.add(media);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaList;
    }
}
