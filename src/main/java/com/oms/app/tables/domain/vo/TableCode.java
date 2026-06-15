package com.oms.app.tables.domain.vo;

import com.oms.app.tables.domain.exception.TableDomainExceptions;
import jakarta.persistence.Embeddable;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public record TableCode(String value) {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[A-Z0-9_-]+$");

    public TableCode {

        Objects.requireNonNull(value, "tableCode must not be null");

        String normalized = value.trim().toUpperCase(Locale.ROOT);

        if (normalized.isEmpty()) throw TableDomainExceptions.blankTableCode();

        if (normalized.length() > 5) throw TableDomainExceptions.tableCodeTooLong(5);

        if (!VALID_PATTERN.matcher(normalized).matches()) throw TableDomainExceptions.invalidTableCodeFormat(normalized);

        value = normalized;
    }

    public static TableCode of(String code) {
        return new TableCode(code);
    }
}
