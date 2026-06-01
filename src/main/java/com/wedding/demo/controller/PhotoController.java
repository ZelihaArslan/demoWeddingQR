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

@Controller
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/")
    public String index(Model model) {
        List<String> photos = photoService.getAllPhotos();
        model.addAttribute("photos", photos);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Lütfen bir dosya seçin.");
            return "redirect:/";
        }

        try {
            photoService.uploadPhoto(file);
            redirectAttributes.addFlashAttribute("message", "Fotoğraf başarıyla yüklendi!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Yükleme sırasında bir hata oluştu: " + e.getMessage());
        }

        return "redirect:/";
    }
}
