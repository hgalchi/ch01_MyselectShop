package com.sparta.myselectshop.shop.repository;

import com.sparta.myselectshop.shop.entity.Folder;
import com.sparta.myselectshop.shop.entity.Product;
import com.sparta.myselectshop.shop.entity.ProductFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
    Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);



}
