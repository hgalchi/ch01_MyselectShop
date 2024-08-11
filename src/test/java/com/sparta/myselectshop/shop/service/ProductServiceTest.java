package com.sparta.myselectshop.shop.service;

import com.sparta.myselectshop.auth.entity.User;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.shop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.shop.dto.ProductRequestDto;
import com.sparta.myselectshop.shop.dto.ProductResponseDto;
import com.sparta.myselectshop.shop.entity.Product;
import com.sparta.myselectshop.shop.repository.FolderRepository;
import com.sparta.myselectshop.shop.repository.ProductFolderRepository;
import com.sparta.myselectshop.shop.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    FolderRepository folderRepository;

    @Mock
    ProductFolderRepository productFolderRepository;

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 이상으로 변경")
    void test1() {
        //given
        Long productId = 100L;
        int myprice = ProductService.MIN_MY_PRICE + 3_000_000;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        ProductService productService = new ProductService(productRepository, folderRepository, productFolderRepository);
        User user = new User();
        ProductRequestDto productRequestDto = new ProductRequestDto("title", "image", "link", 3000);
        Product product = new Product(productRequestDto, user);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        //when
        ProductResponseDto result = productService.updateProduct(productId, requestMyPriceDto);

        //then
        assertEquals(myprice, result.getMyprice());
    }


    @Test
    @DisplayName("관심 상품 희망가 - 최저가 미만으로 변경")
    void test2() {
        //given
        Long productId = 100L;
        int myprice = ProductService.MIN_MY_PRICE - 50;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        ProductService productService = new ProductService(productRepository, folderRepository, productFolderRepository);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productService.updateProduct(productId, requestMyPriceDto));


        //then
        assertEquals("유효하지 않은 관심 가격입니다. 최소" + ProductService.MIN_MY_PRICE + "이상으로 설정해 주세요."
                , exception.getMessage());


    }

}