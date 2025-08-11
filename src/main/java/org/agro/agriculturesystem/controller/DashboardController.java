package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import org.agro.agriculturesystem.dto.MarketDataDTO;
import org.agro.agriculturesystem.model.*;
import org.agro.agriculturesystem.repository.FarmGuideRepo;
import org.agro.agriculturesystem.repository.ForumTopicsRepo;
import org.agro.agriculturesystem.repository.UserRepository;
import org.agro.agriculturesystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    @Autowired
    private GuideCategoryService guideCategoryService;

    @Autowired
    private ForumCatService forumCatService;

    @Autowired
    private FarmGuideRepo farmGuideRepo;

    @Autowired
    private FarmGuideService farmGuideService;

    @Autowired
    private ForumReplyService forumReplyService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private  MarketDataService marketDataService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/guides")
    public String guides(Model model) {

        List<GuideCategory> filteredCategories = guideCategoryService.getAllCategories().stream()
                .filter(c -> "on".equalsIgnoreCase(c.getStatus()))
                .sorted(Comparator.comparing(GuideCategory::getName))
                .filter(c -> c.getGuidesCount() >= 1)
                .collect(Collectors.toList());
        model.addAttribute("categories", filteredCategories);

        List<FarmGuide> featuredGuides = farmGuideRepo.findByFeaturedTrue().stream()
                .filter(c -> "published".equalsIgnoreCase(String.valueOf(c.getStatus())))
                .collect(Collectors.toList());
        model.addAttribute("featuredGuides", featuredGuides);

        Map<String, List<FarmGuide>> guidesByCategory = farmGuideService.getAllFarmGuides()
                .stream()
                .filter(g -> "published".equalsIgnoreCase(String.valueOf(g.getStatus())))
                .collect(Collectors.groupingBy(FarmGuide::getCategory));

        Map<String, List<String>> subcategoriesByCategory = new HashMap<>();

        guidesByCategory.forEach((category, guides) -> {
            List<String> subcategories = guides.stream()
                    .map(FarmGuide::getSubcategory)
                    .filter(Objects::nonNull)
                    .filter(sub -> !sub.trim().isEmpty())
                    .distinct()
                    .collect(Collectors.toList());
            subcategoriesByCategory.put(category, subcategories);
        });

        model.addAttribute("guidesByCategory", guidesByCategory);
        model.addAttribute("subcategoriesByCategory", subcategoriesByCategory);


        return "guides";
    }

    @GetMapping("/guide/{slug}")
    public String readGuide(@PathVariable String slug, Model model) {
        FarmGuide guide = farmGuideService.getFarmGuideBySlug(slug);
        model.addAttribute("guide", guide);
        return "readGuide";
    }


    @GetMapping("/machinery")
    public String farmMachinery(){
        return "farm-machinery";
    }

    @GetMapping("/weatherForecast")
    public String weatherForecast(){
        return "weather-forecast";
    }

    @GetMapping("/plantDisease")
    public String plantDisease(){
        return "plantDisease";
    }

    @GetMapping("/marketForecast")
    public String marketForecast(){
        return "market-forecast";
    }

    @Autowired
    private ForumTopicsService forumTopicsService;

    @GetMapping("/forum")
    public String forum(HttpSession session, Model model,  @PageableDefault(size = 7, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        if (session.getAttribute("userId") == null) {
            return "login";
        }
        List<ForumCategory> forumCategories = forumCatService.getAllCategories();
        model.addAttribute("fcategories", forumCategories);

        Page<ForumTopics> forumTopicsPage = forumTopicsService.getAllTopics(pageable);
        model.addAttribute("topicsPage", forumTopicsPage);

        long totalRegularUsers = userService.getTotalRegularUsers();
        model.addAttribute("totalUsers", totalRegularUsers);

        long totalTopics = forumTopicsService.getTotalTopics();
        model.addAttribute("totalTopics", totalTopics);

        return "forum";
    }


    @GetMapping("/forum/{slug}")
    public String readforumTopic(@PathVariable String slug, Model model, HttpSession session, @RequestParam(defaultValue = "0") int page){
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        ForumTopics forumTopic = forumTopicsService.getTopicBySlug(slug);
        model.addAttribute("forumTopic", forumTopic);

        Map<String, Long> topicReactionCounts = reactionService.getReactionTypeCountsForTopic(forumTopic.getId());
        model.addAttribute("topicReactionCounts", topicReactionCounts);

        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").ascending());
        Page<ForumReply> replies = forumReplyService.getRepliesByTopicId(forumTopic.getId(), pageable);

        model.addAttribute("repliesPage", replies);
        model.addAttribute("currentPage", page);

        Map<Long, Map<String, Long>> replyReactionCounts = new HashMap<>();
        for (ForumReply reply : replies.getContent()) {
            replyReactionCounts.put(reply.getId(), reactionService.getReactionTypeCountsForReply(reply.getId()));
        }
        model.addAttribute("replyReactionCounts", replyReactionCounts);

        return "forum-topic";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(session.getAttribute("userId").toString());

        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);

        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("settings")
    public String settings(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(session.getAttribute("userId").toString());

        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);

        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "settings";
    }

    // for admin

    @GetMapping("/adminLogin")
    public String showAdminLoginPage() {
        return "admin/login";
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/adminDashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null || !"admin".equals(session.getAttribute("role"))) {
            return "admin/login";
        }

        List<User> users = userRepository.findAll();
        model.addAttribute("userList", users);

        List<FarmGuide> guides = farmGuideService.getAllFarmGuides();
        model.addAttribute("guides", guides);

        long totalTopics = forumTopicsService.getTotalTopics();
        model.addAttribute("totalTopics", totalTopics);

        return "admin/dashboard";
    }

    @GetMapping("marketData")
    public String marketData(HttpSession session,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "4") int size,
                             Model model) {
        if (session.getAttribute("userId") == null || session.getAttribute("role") == null) {
            return "admin/login";
        }

        // Fixed: Now correctly using the paginated method
        Page<MarketData> marketDataPage = marketDataService.getAllMarketData(PageRequest.of(page, size));

        // Convert to DTOs for the view
        List<MarketDataDTO> marketDataDTOs = marketDataPage.getContent().stream()
                .map(marketDataService::convertEntityToDto)
                .collect(Collectors.toList());

        model.addAttribute("marketDataList", marketDataDTOs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", marketDataPage.getTotalElements());
        model.addAttribute("totalPages", marketDataPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/market-data";
    }

    @GetMapping("adminMachinery")
    public String adminMachinery(HttpSession session) {
        if (session.getAttribute("userId") == null || session.getAttribute("role") == null) {
            return "admin/login";
        }
        return "admin/machinery";
    }

    @GetMapping("adminSettings")
    public String adminSettings(HttpSession session) {
        if (session.getAttribute("userId") == null || session.getAttribute("role") == null) {
            return "admin/login";
        }
        return "admin/settings";
    }

    @GetMapping("/readAGuide")
    public String readAGuide() {
        return "admin/readAGuide";
    }
}
