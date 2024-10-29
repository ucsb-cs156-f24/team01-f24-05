package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.Article; 
import edu.ucsb.cs156.example.entities.UCSBDate;
import edu.ucsb.cs156.example.repositories.ArticleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDateTime;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ArticlesController.class)
@Import(TestConfig.class)
public class ArticlesControllerTests extends ControllerTestCase{

    @MockBean
    ArticleRepository articleRepository;

    @MockBean
    UserRepository userRepository;

    // Authorization tests for /api/articles/admin/all

    @Test
    public void logged_out_users_cannot_get_all() throws Exception {
            mockMvc.perform(get("/api/articles/all"))
                            .andExpect(status().is(403)); // logged out users can't get all
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void logged_in_users_can_get_all() throws Exception {
            mockMvc.perform(get("/api/articles/all"))
                            .andExpect(status().is(200)); // logged
    }


    // Authorization tests for /api/articles/post
        // (Perhaps should also have these for put and delete)

    @Test
    public void logged_out_users_cannot_post() throws Exception {
            mockMvc.perform(post("/api/articles/post"))
                            .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void logged_in_regular_users_cannot_post() throws Exception {
            mockMvc.perform(post("/api/articles/post"))
                            .andExpect(status().is(403)); // only admins can post
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void logged_in_user_can_get_all_articles() throws Exception {

            // arrange
            LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

            Article article1 = Article.builder()
                            .email("ben@gmail.com")
                            .dateAdded(ldt1)
                            .url("https://google.com")
                            .title("Ben")
                            .explanation("benny")
                            .build();

            LocalDateTime ldt2 = LocalDateTime.parse("2022-03-11T00:00:00");

            Article article2 = Article.builder()
                            .email("josh@gmail.com")
                            .dateAdded(ldt2)
                            .url("https://google.com")
                            .title("Josh")
                            .explanation("jiddy")
                            .build();

            ArrayList<Article> expectedArticles = new ArrayList<>();
            expectedArticles.addAll(Arrays.asList(article1, article2));

            when(articleRepository.findAll()).thenReturn(expectedArticles);

            // act
            MvcResult response = mockMvc.perform(get("/api/articles/all"))
                            .andExpect(status().isOk()).andReturn();

            // assert

            verify(articleRepository, times(1)).findAll();
            String expectedJson = mapper.writeValueAsString(expectedArticles);
            String responseString = response.getResponse().getContentAsString();
            assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void an_admin_user_can_post_a_new_article() throws Exception {
            // arrange

            LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

            Article article1 = Article.builder()
                            .email("ben@gmail.com")
                            .dateAdded(ldt1)
                            .url("https://google.com")
                            .title("Ben")
                            .explanation("thing")
                            .build();

            when(articleRepository.save(eq(article1))).thenReturn(article1);

            // act
            MvcResult response = mockMvc.perform(
                            post("/api/articles/post?email=ben@gmail.com&url=https://google.com&dateAdded=2022-01-03T00:00:00&title=Ben&explanation=thing")
                                            .with(csrf()))
                            .andExpect(status().isOk()).andReturn();

            // assert
            verify(articleRepository, times(1)).save(article1);
            String expectedJson = mapper.writeValueAsString(article1);
            String responseString = response.getResponse().getContentAsString();
            assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void admin_can_delete_a_article() throws Exception {
            // arrange

            LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

            Article article1 = Article.builder()
                            .email("ben@gmail.com")
                            .dateAdded(ldt1)
                            .url("https://google.com")
                            .title("Ben")
                            .explanation("benny")
                            .build();

            when(articleRepository.findById(eq(1L))).thenReturn(Optional.of(article1));

            // act
            MvcResult response = mockMvc.perform(
                            delete("/api/articles?id=1")
                                            .with(csrf()))
                            .andExpect(status().isOk()).andReturn();

            // assert
            verify(articleRepository, times(1)).findById(1L);
            verify(articleRepository, times(1)).delete(any());

            Map<String, Object> json = responseToJson(response);
            assertEquals("Article with id 1 deleted", json.get("message"));
    }

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void admin_tries_to_delete_non_existant_article_and_gets_right_error_message()
                    throws Exception {
            // arrange

            when(articleRepository.findById(eq(15L))).thenReturn(Optional.empty());

            // act
            MvcResult response = mockMvc.perform(
                            delete("/api/articles?id=15")
                                            .with(csrf()))
                            .andExpect(status().isNotFound()).andReturn();

            // assert
            verify(articleRepository, times(1)).findById(15L);
            Map<String, Object> json = responseToJson(response);
            assertEquals("Article with id 15 not found", json.get("message"));
    }
    
}