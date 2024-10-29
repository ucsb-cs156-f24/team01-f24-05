package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBOrganizations;
import edu.ucsb.cs156.example.repositories.UCSBOrganizationsRepository;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBOrganizationsController.class)
@Import(TestConfig.class)

public class UCSBOrganizationsControllerTests extends ControllerTestCase {

        @MockBean
        UCSBOrganizationsRepository ucsbOrganizationsRepository;

        @MockBean
        UserRepository userRepository;

        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsborganizations/all"))
                                .andExpect(status().is(403)); // logged out users can't get all
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsborganizations/all"))
                                .andExpect(status().is(200)); // logged
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_ucsbOrganizations() throws Exception {

                // arrange

                UCSBOrganizations carrillo = UCSBOrganizations.builder()
                                .orgCode("test")
                                .orgTranslationShort("test")
                                .orgTranslation("test")
                                .inactive(false)
                                .build();

                UCSBOrganizations dlg = UCSBOrganizations.builder()
                                .orgCode("test")
                                .orgTranslationShort("test")
                                .orgTranslation("test")
                                .inactive(false)
                                .build();

                ArrayList<UCSBOrganizations> expectedCommons = new ArrayList<>();
                expectedCommons.addAll(Arrays.asList(carrillo, dlg));

                when(ucsbOrganizationsRepository.findAll()).thenReturn(expectedCommons);

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsborganizations/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbOrganizationsRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedCommons);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsborganizations/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsborganizations/post"))
                                .andExpect(status().is(403)); // only admins can post
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_commons() throws Exception {
                // arrange

                UCSBOrganizations test = UCSBOrganizations.builder()
                                .orgCode("test")
                                .orgTranslationShort("test")
                                .orgTranslation("test")
                                .inactive(true)
                                .build();

                when(ucsbOrganizationsRepository.save(eq(test))).thenReturn(test);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/ucsborganizations/post?orgCode=test&orgTranslationShort=test&orgTranslation=test&inactive=true")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbOrganizationsRepository, times(1)).save(test);
                String expectedJson = mapper.writeValueAsString(test);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }
}