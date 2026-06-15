package com.oms.app.products.application.create_product;


import com.oms.app.products.domain.Product;
import com.oms.app.products.infrastructure.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.oms.app.products.application.exception.ProductApplicationExceptions.productNameAlreadyExistsException;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateProductHandler {

    private final ProductRepository productRepository;

    public void handle(CreateProductCommand cmd) {

        if( productRepository.existsByName( cmd.name() )){
            throw productNameAlreadyExistsException( cmd.name().value());
        }

        Product product = Product.create( cmd.name(), cmd.price(), cmd.category() );

        productRepository.save(product);
    }
}
