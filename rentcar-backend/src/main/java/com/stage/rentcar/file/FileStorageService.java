package com.stage.rentcar.file;

import com.stage.rentcar.agence.Agence;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j // to log smthng
public class FileStorageService {
    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;
    public String saveFile(@NonNull MultipartFile sourceFile,
                         @NonNull  Integer userId) {
        final String fileUploadSubPath="users" + File.separator+ userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile sourceFile, @NonNull String fileUploadSubPath) {
        // the path exp: ./uploads/users/{userId}
        final String finalUploadPath = Paths.get(fileUploadPath, fileUploadSubPath).toString();
        //target folder
        File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()){
            boolean  folderCreated = targetFolder.mkdirs(); //mkdirs ta5talif 3ala mkdir inha it creates folders and sub-folders recursively
            if(!folderCreated){
                log.warn("failed to create the target folder");
                return  null ;
            }
        }
        //extract the file extension : jpg/ png ..
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        //to create a new name for the path exp : ./uploads/users/1/23453224677.jpg
        //cuurentTimeMillis() : to avoid special chars and spaces and to avoid uploading the same file twice
        String targetFilePath = Paths.get(finalUploadPath, System.currentTimeMillis() + "." + fileExtension).toString();
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("file saved to "+ targetFilePath);
            return targetFilePath.replace(File.separatorChar, '/');
        }catch (IOException e){
            log.error("file is not saved", e);
        }
        return null;

    }

    private String getFileExtension(String fileName) {
        if(fileName==null || fileName.isEmpty()){
            return "";
        }
        //return extension->jpg : kim a file name exp :smthng.jpg
        int lastDotIndex = fileName.lastIndexOf("."); // -> return .jpg
        //if file name doesn't have an extension
        if(lastDotIndex == -1){
            return "";
        }
        return fileName.substring(lastDotIndex+1).toLowerCase(); // +1 5ater n7ib na7i "." -> returns only jpg
    }
}
