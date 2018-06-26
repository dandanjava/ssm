package com.gbq.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by gbqzh on 2018/4/3.
 */
@Controller()
@RequestMapping("cropperController")
public class CropperController {
    @RequestMapping("upload")
    public void upload(@RequestParam("croppedImage") MultipartFile file) throws IOException {
        String filePath = "D:\\BianCheng\\test-file";
        String fileName = file.getOriginalFilename();
        filePath = filePath + File.separator + fileName;
        File file1 = new File(filePath);
        file.transferTo(file1);
    }
}
