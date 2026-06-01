package com.wedding.demo.controller;

import com.wedding.demo.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/")
    public String index(Model model) {
        List<Map<String, String>> mediaList = photoService.getAllMedia();
        model.addAttribute("mediaList", mediaList);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttributes) {
        if (files == null || files.length == 0 || files[0].isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Lütfen en az bir dosya seçin.");
            return "redirect:/";
        }

        int successCount = 0;
        int errorCount = 0;

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    photoService.uploadFile(file);
                    successCount++;
                } catch (IOException e) {
                    e.printStackTrace();
                    errorCount++;
                }
            }
        }

        if (errorCount == 0) {
            redirectAttributes.addFlashAttribute("message", successCount + " dosya başarıyla yüklendi!");
        } else {
            redirectAttributes.addFlashAttribute("message", successCount + " dosya yüklendi, " + errorCount + " dosyada hata oluştu.");
        }

        return "redirect:/";
    }
}
