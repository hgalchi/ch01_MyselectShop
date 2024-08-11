package com.sparta.myselectshop.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.auth.controller.UserController;
import com.sparta.myselectshop.auth.entity.User;
import com.sparta.myselectshop.auth.entity.UserRoleEnum;
import com.sparta.myselectshop.auth.filter.UserDetailsImpl;
import com.sparta.myselectshop.auth.service.UserService;
import com.sparta.myselectshop.mockFilter.MockSpringSecurityFilter;
import com.sparta.myselectshop.shop.dto.ProductRequestDto;
import com.sparta.myselectshop.shop.entity.Product;
import com.sparta.myselectshop.shop.service.FolderService;
import com.sparta.myselectshop.shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = {ProductController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfiguration.class
                )
        }
)
class ProductControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        String username = "wywudi";
        String password = "wywudi";
        String email = "wywudi@email.com";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(username, password, email, role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());

    }

    @Test
    @DisplayName("신규 관신상품 등록")
    void test1() throws Exception {
        //given
        this.mockUserSetup();
        String title = "Apple <b>아이폰</b> 14 프로 256GB [자급제]";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_3456175/34561756621.20220929142551.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=34561756621";
        int lPrice = 959000;

        ProductRequestDto reqDto = new ProductRequestDto(
                title,
                imageUrl,
                linkUrl,
                lPrice
        );

        String postInfo = objectMapper.writeValueAsString(reqDto);

        //when-then
        mvc.perform(post("/api/products")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("관심 상품 희망 최저가 등록")
    void test2() throws Exception {
        //given
        Long id = 1L;
        ProductRequestDto dto = new ProductRequestDto("title", "image", "link", 3000);
        String putInfo = objectMapper.writeValueAsString(dto);
        this.mockUserSetup();

        //when-then
        mvc.perform(put("/api/products/{id}", id)
                        .content(putInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 목록 조회")
    void test3() throws Exception {
        //give
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("size", "10");
        params.add("sortBy", "title");
        params.add("isAsc", "true");
        this.mockUserSetup();

        //when-then
        mvc.perform(get("/api/products")
                        .params(params)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("폴더에 상품 추가")
    void test4() throws Exception {
        //given
        Long productId = 1L;
        String folderId = "2";
        this.mockUserSetup();

        //when-then
        mvc.perform(post("/api/products/{productId}/folder", productId)
                        .param("folderId", folderId)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());

    }
}