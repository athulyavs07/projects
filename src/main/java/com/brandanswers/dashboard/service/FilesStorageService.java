package com.brandanswers.dashboard.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.brandanswers.dashboard.funson.BaseDataAccessFunson;


public class FilesStorageService extends BaseDataAccessFunson {
	    public static String save(MultipartFile file) {
	    	String docFolder= propertyHelper.getDocFolder();
	        try {
	        	byte[] bytes = file.getBytes();
	            Path copyLocation = Paths
	                .get(docFolder+StringUtils.cleanPath(file.getOriginalFilename()));
	           
	            Files.write(copyLocation, bytes);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return docFolder+file.getOriginalFilename();
	    }
}
