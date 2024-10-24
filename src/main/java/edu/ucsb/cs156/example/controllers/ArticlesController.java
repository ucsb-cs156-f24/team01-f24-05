// package edu.ucsb.cs156.example.controllers;

// import edu.ucsb.cs156.example.entities.Article;
// import edu.ucsb.cs156.example.entities.UCSBDate;
// import edu.ucsb.cs156.example.errors.EntityNotFoundException;
// import edu.ucsb.cs156.example.repositories.ArticleRepository;
// import edu.ucsb.cs156.example.repositories.UCSBDateRepository;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import lombok.extern.slf4j.Slf4j;

// import com.fasterxml.jackson.core.JsonProcessingException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import jakarta.validation.Valid;

// import java.time.LocalDateTime;

// @Tag(name = "Articles")
// @RequestMapping("/api/articles")
// @RestController
// @Slf4j
// public class ArticlesController {

//     @Autowired
//     ArticleRepository articleRepository;
    

//     @Operation(summary= "List all ucsb dates")
//     @PreAuthorize("hasRole('ROLE_USER')")
//     @GetMapping("/all")
//     public Iterable<Article> allUCSBDates() {
//         Iterable<Article> articles1 = articleRepository.findAll();
//         return articles1;
//     }


//      /**
//      * Create a new article
//      * 
//      * @param title  the title of the article
//      * @param url     the url of the article 
//      * @param explanation the explanation of the article 
//      * @param email the email associated with the article 
//      * @param localDateTime the date 
//      * @return the saved article
//      */
//     @Operation(summary= "Create a new article")
//     @PreAuthorize("hasRole('ROLE_ADMIN')")
//     @PostMapping("/post")
//     public Article postArticle(
//             @Parameter(name="title") @RequestParam String title,
//             @Parameter(name="url") @RequestParam String url,
//             @Parameter(name="explanation") @RequestParam String explanation,
//             @Parameter(name="email") @RequestParam String email,
//             @Parameter(name="date (in iso format, e.g. YYYY-mm-ddTHH:MM:SS; see https://en.wikipedia.org/wiki/ISO_8601)") @RequestParam("localDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime)
//             throws JsonProcessingException {

//         // For an explanation of @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//         // See: https://www.baeldung.com/spring-date-parameters

//         log.info("localDateTime={}", localDateTime);

//         Article article = new Article();
//         article.setTitle(title);
//         article.setUrl(url);
//         article.setExplanation(explanation);
//         article.setEmail(email);
//         article.setLocalDateTime(localDateTime);

//         Article savedArticle = articleRepository.save(article);

//         return savedArticle;
//     }
// }
