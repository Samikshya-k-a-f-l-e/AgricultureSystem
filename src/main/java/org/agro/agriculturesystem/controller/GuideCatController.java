package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import org.agro.agriculturesystem.dto.GuideCatDTO;
import org.agro.agriculturesystem.model.FarmGuide;
import org.agro.agriculturesystem.model.GuideCategory;
import org.agro.agriculturesystem.repository.FarmGuideRepo;
import org.agro.agriculturesystem.service.FarmGuideService;
import org.agro.agriculturesystem.service.GuideCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GuideCatController {

    @Autowired
    private GuideCategoryService guideCategoryService;

    @PostMapping("/addGuideCategory")
    public String addGuideCatgory(@ModelAttribute("guide") GuideCatDTO guideDTO) {
        try{
            guideCategoryService.saveGuideCat(guideDTO);
            return "redirect:/adminGuides";
        }catch (Exception e){
            return "redirect:/adminGuides?error=" + e.getMessage();
        }
    }

    @Autowired
    public GuideCatController(GuideCategoryService categoryService) {
        this.guideCategoryService = categoryService;
    }

    @Autowired
    private FarmGuideService farmGuideService;

    @Autowired
    private FarmGuideRepo farmGuideRepo;

    @GetMapping("adminGuides")
    public String adminGuides(Model model, HttpSession session,
    @RequestParam(defaultValue ="1") int page, @RequestParam(defaultValue ="7") int size) {
        List<GuideCategory> categories = guideCategoryService.getAllCategories()
                .stream()
                .sorted(Comparator.comparing(GuideCategory::getStatus).reversed().thenComparing(GuideCategory::getName))
//                .sorted(Comparator.comparing(GuideCategory::getName))
                .collect(Collectors.toList());
        model.addAttribute("categories", categories);

        // Get paginated guides
        Page<FarmGuide> guidePage = farmGuideService.getPaginatedFarmGuides(page, size);
        model.addAttribute("guides", guidePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", guidePage.getTotalPages());
        model.addAttribute("totalItems", guidePage.getTotalElements());

        List<FarmGuide> featuredGuides = farmGuideService.getAllFeatured();
        model.addAttribute("featuredGuides", featuredGuides);

        if (session.getAttribute("userId") == null || session.getAttribute("role") == null) {
            return "admin/login";
        }
        return "admin/guides";
    }

    @DeleteMapping("/deleteGuideCategory/{id}")
    public ResponseEntity<String> deleteGuideCategory(@PathVariable int id) {
        try {
            guideCategoryService.deleteGuideCategory(id);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getGuideCategory/{id}")
    @ResponseBody
    public GuideCategory getGuideCategoryById(@PathVariable int id) {
        return guideCategoryService.getCategoryById(id);
    }

    @PostMapping("/updateGuideCategory/{id}")
    public String updateCategory(@PathVariable int id, @ModelAttribute("guide") GuideCatDTO guideDTO) {
        try{
            guideCategoryService.updateGuideCategory(id, guideDTO);
            return "redirect:/adminGuides";
        }catch (Exception e){
            return "redirect:/adminGuides?error=" + e.getMessage();

        }

    }

}
