package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.MenuItemReview;
import edu.ucsb.cs156.example.entities.UCSBDate;
import edu.ucsb.cs156.example.repositories.MenuItemReviewRepository;


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

@WebMvcTest(controllers = MenuItemReviewController.class)

@Import(TestConfig.class)

public class MenuItemReviewControllerTests extends ControllerTestCase {
        @MockBean
        MenuItemReviewRepository menuItemReviewRepository;

        @MockBean
        UserRepository userRepository;

        //Tests for GET
        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/menu_item_review/all"))
                                .andExpect(status().is(403)); // logged out users can't get all
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/menu_item_review/all"))
                                .andExpect(status().is(200)); // logged
        }


        //Tests for POST
        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/menu_item_review/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/menu_item_review/post"))
                                .andExpect(status().is(403)); // only admins can post
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_menuitemreview() throws Exception {

                // arrange
                LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                MenuItemReview menuItemReview1 = MenuItemReview.builder()
                                .itemID(Long.valueOf(1))
                                .reviewerEmail("a@ucsb.edu")
                                .stars(4)
                                .comments("good")
                                .dateReviewed(ldt1)
                                .build();

                LocalDateTime ldt2 = LocalDateTime.parse("2022-03-11T00:00:00");

                MenuItemReview menuItemReview2 = MenuItemReview.builder()
                                .itemID(Long.valueOf(2))
                                .reviewerEmail("b@ucsb.edu")
                                .stars(3)
                                .comments("nice")
                                .dateReviewed(ldt2)
                                .build();
                ArrayList<MenuItemReview> expectedReviews = new ArrayList<>();
                expectedReviews.addAll(Arrays.asList(menuItemReview1, menuItemReview2));

                when(menuItemReviewRepository.findAll()).thenReturn(expectedReviews);

                // act
                MvcResult response = mockMvc.perform(get("/api/menu_item_review/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(menuItemReviewRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedReviews);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_menuitemreview() throws Exception {
                // arrange

                LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                MenuItemReview menuItemReview1 = MenuItemReview.builder()
                                .itemID(Long.valueOf(1))
                                .reviewerEmail("a@ucsb.edu")
                                .stars(4)
                                .comments("good")
                                .dateReviewed(ldt1)
                                .build();

                when(menuItemReviewRepository.save(eq(menuItemReview1))).thenReturn(menuItemReview1);

                // act
                MvcResult response = mockMvc.perform(
                    post("/api/menu_item_review/post?itemID=1&reviewerEmail=a@ucsb.edu&stars=4&comments=good&dateReviewed=2022-01-03T00:00:00")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(menuItemReviewRepository, times(1)).save(menuItemReview1);
                String expectedJson = mapper.writeValueAsString(menuItemReview1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

}
