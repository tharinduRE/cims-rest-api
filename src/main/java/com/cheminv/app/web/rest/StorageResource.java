package com.cheminv.app.web.rest;

import com.cheminv.app.service.StorageService;
import io.swagger.annotations.Api;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimetypesFileTypeMap;
import java.io.InputStream;

@RestController
@RequestMapping("/static")
public class StorageResource {

    private final StorageService storageService;

    public StorageResource(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(path = "/account/avatar/{fileName}")
    public ResponseEntity<byte[]> getUserAvatar(@PathVariable String fileName) throws Exception {
        Resource resource =  storageService.loadUserAvatar(fileName);
        InputStream in = resource.getInputStream();
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+resource.getFilename())
            .header(HttpHeaders.CONTENT_TYPE,new MimetypesFileTypeMap().getContentType(resource.getFilename()))
            .body(IOUtils.toByteArray(in));
    }

}
