package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import org.agro.agriculturesystem.dto.FarmGuideDTO;
import org.agro.agriculturesystem.model.FarmGuide;
import org.agro.agriculturesystem.repository.FarmGuideRepo;
import org.agro.agriculturesystem.service.FarmGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.web.bind.annotation.*;


@Controller
public class FarmGuideController {

    @Autowired
    private FarmGuideService farmGuideService;
    @Autowired
    private FarmGuideRepo farmGuideRepo;

    @PostMapping(value = "/createFarmGuide")
    public String createFarmGuide(@ModelAttribute("farmGuide") FarmGuideDTO farmGuideDTO, HttpSession session) {
        try{
            Integer userId = (Integer) session.getAttribute("userId");

            farmGuideService.addFarmGuide(farmGuideDTO, userId);
            return "redirect:/adminGuides";
        }catch (Exception e){
            return "redirect:/adminGuides?error=" + e.getMessage();
        }
    }

    // DELETE category
    @DeleteMapping("/deleteFarmGuide/{id}")
    public ResponseEntity<String> deleteFarmGuide(@PathVariable int id) {
        try {
            farmGuideService.deleteFarmGuide(id);
            return ResponseEntity.ok("Guide deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getFarmGuide/{id}")
    @ResponseBody
    public FarmGuide getFarmGuideById(@PathVariable int id) {
        return farmGuideService.getFarmGuideById(id);
    }

    @PostMapping("/updateFarmGuide/{id}")
    public String updateFarmGuideById(@PathVariable int id, @ModelAttribute("guide") FarmGuideDTO farmGuideDTO, HttpSession session) {
        try{
            Integer userId = (Integer) session.getAttribute("userId");
            farmGuideService.updateFarmGuide(farmGuideDTO, id, userId);
            return "redirect:/adminGuides";
        }catch (Exception e){
            return "redirect:/adminGuides?error=" + e.getMessage();
        }

    }

    @GetMapping("/guides/{slug}")
    public String viewGuideBySlug(@PathVariable String slug, Model model) {
        FarmGuide guide = farmGuideService.getFarmGuideBySlug(slug);
        model.addAttribute("guide", guide);
        return "admin/readAGuide";
    }

    @PostMapping("/farm-guides/{id}/remove-featured")
    @ResponseBody
    public ResponseEntity<String> removeFromFeatured(@PathVariable int id) {
        FarmGuide guide = farmGuideService.getFarmGuideById(id);
        if (guide == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guide not found");
        }

        guide.setFeatured(false);
        guide.setFeaturedPosition(0);
        farmGuideService.save(guide);

        return ResponseEntity.ok("Removed from featured");
    }

    @PostMapping("/farm-guides/{id}/move-up")
    @ResponseBody
    public ResponseEntity<String> moveUp(@PathVariable int id) {
        FarmGuide guide = farmGuideService.getFarmGuideById(id);
        if (guide == null) {
            return ResponseEntity.badRequest().body("Guide not found");
        }

        int pos = guide.getFeaturedPosition();
        if (pos <= 1) {
            return ResponseEntity.ok("Already at top");
        }

        guide.setFeaturedPosition(pos - 1);
        farmGuideService.save(guide);
        return ResponseEntity.ok("Moved up to position " + guide.getFeaturedPosition());
    }

    @PostMapping("/farm-guides/{id}/move-down")
    @ResponseBody
    public ResponseEntity<String> moveDown(@PathVariable int id) {
        FarmGuide guide = farmGuideService.getFarmGuideById(id);
        if (guide == null) {
            return ResponseEntity.badRequest().body("Guide not found");
        }

        int pos = guide.getFeaturedPosition();
        int total = farmGuideService.getAllFeatured().size();
        if (pos >= total) {
            return ResponseEntity.ok("Already at bottom");
        }

        guide.setFeaturedPosition(pos + 1);
        farmGuideService.save(guide);
        return ResponseEntity.ok("Moved down to position " + guide.getFeaturedPosition());
    }

}