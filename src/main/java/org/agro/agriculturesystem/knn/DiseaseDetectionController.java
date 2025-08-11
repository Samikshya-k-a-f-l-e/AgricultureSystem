package org.agro.agriculturesystem.knn;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // For development only, restrict in production
public class DiseaseDetectionController {

    private DiseaseDetectionService diseaseDetectionService;
    
    // Constructor injection
    public DiseaseDetectionController(DiseaseDetectionService diseaseDetectionService) {
        this.diseaseDetectionService = diseaseDetectionService;
    }

    @PostMapping(value = "/detect-disease", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DiseaseDetectionResult> detectDisease(@RequestParam("image") MultipartFile imageFile) {
        try {
            DiseaseDetectionResult result = diseaseDetectionService.detectDisease(imageFile);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}