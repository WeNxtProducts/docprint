package com.wenxt.docprint.dms.dto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {

//    public String saveFileUpload(byte[] byteArry, String tranId, String genType) throws IOException {
//        // Ensure byteArry is not null
//        if (byteArry == null) {
//            throw new IllegalArgumentException("byteArry cannot be null");
//        }
//        
//        // Ensure tranId is not null or empty
//        if (tranId == null || tranId.trim().isEmpty()) {
//            throw new IllegalArgumentException("tranId cannot be null or empty");
//        }
//        
//        // Sanitize genType to avoid invalid characters in filename
//        if (genType != null) {
//            genType = genType.replaceAll("[^a-zA-Z0-9.-]", "_");
//        }
//        
//        // Prepare filename
//        String filename = tranId + (genType != null ? "." + genType : ".bin");
//        
//        // Define file path
//        File file = new File("d://documentemal/" + filename);
//        
//        // Create parent directories if they do not exist
//        file.getParentFile().mkdirs();
//        
//        // Write bytes to file
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            fos.write(byteArry);
//        }
//        
//        return filename;
//    }
}

