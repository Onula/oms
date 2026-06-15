package com.oms.app.reservations.domain.vo;

import java.util.regex.Pattern;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.invalidCustomerPhone;

public record ReservationCustomerPhone(String value) {
    private static final int MIN_DIGITS = 7;
    private static final int MAX_DIGITS = 20;
    private static final Pattern VALID_PATTERN =
            Pattern.compile("^\\+?[0-9\\s\\-().]+$");

    private static final String VALID_PATTERN_EXAMPLE = "+30 691 234 5678";

    public ReservationCustomerPhone {
        String normalized = value == null ? null : value.trim();

        long digitCount = normalized == null
                ? 0
                : normalized.chars().filter(Character::isDigit).count();

        if (normalized == null
                || normalized.isEmpty()
                || digitCount < MIN_DIGITS
                || digitCount > MAX_DIGITS
                || !VALID_PATTERN.matcher(normalized).matches())
        {
            throw invalidCustomerPhone(MIN_DIGITS, MAX_DIGITS, VALID_PATTERN_EXAMPLE);
        }

        value = normalized;
    }

    public static ReservationCustomerPhone of(String value) {
        return new ReservationCustomerPhone(value);
    }
}
