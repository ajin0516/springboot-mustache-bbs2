package com.mustache.bbs2.controller;

import com.mustache.bbs2.domain.entity.Article;
import com.mustache.bbs2.domain.dto.ArticleDto;
import com.mustache.bbs2.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articles")
//@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    //    findAll()한 결과를 list로 넘김
    @GetMapping("/list")
    public String list(Model model){
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "list";
    }

    @GetMapping("")
    public String index(){
        return "redirect:/list";
    }

    @GetMapping("/new")
    public String createArticlePage() {
        return "articles/new";
    }

    @GetMapping("/{id}")
    public String selectSingle(@PathVariable Long id, Model model){
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if(!optionalArticle.isEmpty()) {
            model.addAttribute("article", optionalArticle.get());
            return "articles/show";
        }else {
            return "articles/error";
        }
    }

    @PostMapping("")
    public String createArticle(ArticleDto articleDto){
        log.info(articleDto.getTitle());
        Article savedArticle = articleRepository.save(articleDto.toEntity());
        log.info("generatedId:{}", savedArticle.getId());

        return String.format("redirect:/articles/%d",
                savedArticle.getId());
    }
}
