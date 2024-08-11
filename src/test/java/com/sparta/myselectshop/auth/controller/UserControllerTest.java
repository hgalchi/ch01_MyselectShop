package com.sparta.myselectshop.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.auth.service.UserService;
import com.sparta.myselectshop.mockFilter.MockSpringSecurityFilter;
import com.sparta.myselectshop.shop.service.FolderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        controllers={UserController.class}
        ,excludeFilters={
                @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = WebSecurityConfiguration.class
                )
}
)
class UserControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    FolderService folderService;

    @MockBean
    UserService userService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }




    @Test
    @DisplayName("로그인 page")
    void test1() throws Exception {
        //when-then
        mvc.perform(get("/api/user/login-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 요청 처리")
    void test2() throws Exception {
        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();

        signupRequestForm.add("username", "wywudi");
        signupRequestForm.add("password", "wywudi");
        signupRequestForm.add("email", "wywudi@email.com");
        signupRequestForm.add("admin", "false");

        //when-then
        mvc.perform(post("/api/user/signup")
                        .params(signupRequestForm)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/api/user/login-page"))
                .andDo(print());

    }
}