package com.sparta.myselectshop.shop.controller;

import com.sparta.myselectshop.shop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.shop.dto.ProductRequestDto;
import com.sparta.myselectshop.shop.dto.ProductResponseDto;
import com.sparta.myselectshop.auth.filter.UserDetailsImpl;
import com.sparta.myselectshop.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto RequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.createProduct(RequestDto,userDetails.getUser());
    }

    // 관심 상품 희망 최저가 등록하기
    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        // 응답 보내기
        return productService.updateProduct(id, requestDto);
    }

    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(

            @RequestParam ("page") int page,
            @RequestParam ("size") int size,
            @RequestParam ("sortBy") String sortBy,
            @RequestParam ("isAsc") boolean isAsc,


            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return productService.getProducts(userDetails.getUser(),
                page-1,size,sortBy,isAsc);
    }

    @PostMapping("/products/{productId}/folder")
    public void addFolder(
            @PathVariable Long productId,
            @RequestParam Long folderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        productService.addFolder(productId,folderId,userDetails.getUser());

    }


    @GetMapping("/folders/{folderId}/products")
    public Page<ProductResponseDto> getProductsInFolder(
           @PathVariable Long folderId,
           @RequestParam ("page") int page,
           @RequestParam ("size") int size,
           @RequestParam ("sortBy") String sortBy,
           @RequestParam ("isAsc") boolean isAsc,
           @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return productService.getProductsInFolder(
                folderId,
                page-1,
                size,
                sortBy,
                isAsc,
                userDetails.getUser()
        );

    }

}
