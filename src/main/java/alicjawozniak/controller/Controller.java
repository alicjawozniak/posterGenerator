package alicjawozniak.controller;

import alicjawozniak.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;


@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private GenerateService generateService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/generate")
    @ResponseBody
    public ResponseEntity<Resource> generate(Model model, @RequestParam String text) {
        File file = generateService.generateImage(text);
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .body(resource);
    }
}
