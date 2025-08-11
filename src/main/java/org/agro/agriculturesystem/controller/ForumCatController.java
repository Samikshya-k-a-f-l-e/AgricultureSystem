package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import org.agro.agriculturesystem.dto.ForumCatDTO;
import org.agro.agriculturesystem.model.ForumCategory;
import org.agro.agriculturesystem.model.ForumTopics;
import org.agro.agriculturesystem.service.ForumCatService;
import org.agro.agriculturesystem.service.ForumTopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ForumCatController {

    @Autowired
    private ForumCatService forumCatService;

    @Autowired
    private ForumTopicsService forumTopicsService;

    @PostMapping("/addForumCategory")
    public String addForumCategory(@ModelAttribute("forum") ForumCatDTO forumCatDTO) {
        try{
            forumCatService.addForumCat(forumCatDTO);
            return "redirect:/forums";
        }catch (Exception e) {
            return "redirect:/addForumCategory?error=" + e.getMessage();
        }

    }

    @GetMapping("/forums")
    public String forums(
            HttpSession session,
            Model model,
            @PageableDefault(size = 7, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        if (session.getAttribute("userId") == null || session.getAttribute("role") == null) {
            return "admin/login";
        }

        // Get categories
        List<ForumCategory> forumCategories = forumCatService.getAllCategories();
        model.addAttribute("fcategories", forumCategories);

        // Get paginated topics
        Page<ForumTopics> forumTopicsPage = forumTopicsService.getAllTopics(pageable);
        model.addAttribute("topicsPage", forumTopicsPage);

        return "admin/forums";
    }

    @DeleteMapping("/deleteForumCategory/{id}")
    public ResponseEntity<String> deleteForumCategory(@PathVariable int id) {
        try {
            forumCatService.deleteForumCategory(id);
            return ResponseEntity.ok("Forum category deleted successfully");
        } catch (DataIntegrityViolationException dive) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete category: it has related forum topics.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getForumCategory/{id}")
    @ResponseBody
    public ForumCategory getForumCategoryById(@PathVariable int id) {
        return forumCatService.getCategoryById(id);
    }

    @PostMapping("/updateForumCategory/{id}")
    public String updateForumCategory(@PathVariable int id, @ModelAttribute("forum") ForumCatDTO forumCatDTO) {
        try{
            forumCatService.updateForumCategory(id, forumCatDTO);
            return "redirect:/forums";
        }catch (Exception e) {
            return "redirect:/addForumCategory?error=" + e.getMessage();

        }

    }
}