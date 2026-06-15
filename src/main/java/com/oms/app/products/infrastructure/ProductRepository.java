package com.oms.app.products.infrastructure;

import com.oms.app.products.domain.Product;
import com.oms.app.products.domain.vo.ProductId;
import com.oms.app.products.domain.vo.ProductName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, ProductId> {
    boolean existsByName(ProductName name);
}
