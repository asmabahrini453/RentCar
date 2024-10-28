package com.stage.rentcar.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {
    //to read a file path
    public static byte[] readFileFromLocation(String fileUrl){
        if(StringUtils.isBlank(fileUrl)){ //we have no file url
            return null ;
        }
        try{
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        }catch (IOException e){
            log.warn("no file found in the path " + fileUrl);
        }
        return null;
    }
}