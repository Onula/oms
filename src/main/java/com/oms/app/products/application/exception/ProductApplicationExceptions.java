package com.oms.app.products.application.exception;

import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class ProductApplicationExceptions {

    private static final ExceptionCode PRODUCT_NOT_FOUND = ExceptionCode.of("products.not_found");
    private static final ExceptionCode PRODUCT_NAME_ALREADY_EXISTS = ExceptionCode.of("products.name.already_exists");

    public static ResourceNotFoundException productNotFound(UUID productId) {
        return new ResourceNotFoundException(
                PRODUCT_NOT_FOUND,
                productId
        );

    }


    public static BusinessConflictException productNameAlreadyExistsException(String name) {
        return new BusinessConflictException(
                PRODUCT_NAME_ALREADY_EXISTS,
                name
        );
    }


}
