package com.oms.app;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExceptionHandlingSmokeTest {

    @Test
    void problemDetailCanRepresentApiError() {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );

        problem.setProperty("code", "request.validation.failed");

        assertEquals(400, problem.getStatus());
        assertEquals("Request validation failed", problem.getDetail());
        assertEquals("request.validation.failed", problem.getProperties().get("code"));
        assertNotNull(problem.getProperties());
    }
}