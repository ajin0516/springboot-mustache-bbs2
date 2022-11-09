package com.mustache.bbs2.controller;

import com.mustache.bbs2.domain.entity.Article;
import com.mustache.bbs2.domain.dto.ArticleDto;
import com.mustache.bbs2.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "articles/list";
    }

    @GetMapping("")
    public String index(){
        return "redirect:/articles/list";
    }

    @GetMapping("/new")
    public String createArticlePage() {
        return "articles/new";
    }


    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, ArticleDto articleDto, Model model){
        log.info("title:{} content:{}", articleDto.getTitle(), articleDto.getContent());
        Article article = articleRepository.save(articleDto.toEntity());
        // Duplicated Entry에러가 안나는 이유 : update쿼리 실행
        // id가 있는 경우: update   id가 없는 경우: insert
        model.addAttribute("article", article);
        return String.format("redirect:/articles/%d", article.getId());
    }

    // addAttribute view객체를 name이름으로 추가
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
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if(!optionalArticle.isEmpty()) {
            model.addAttribute("article", optionalArticle.get());
            return "articles/edit";
        }else {
            model.addAttribute("message", String.format("%d가 없습니다",id));
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
