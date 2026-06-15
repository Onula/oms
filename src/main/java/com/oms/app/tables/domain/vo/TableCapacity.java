package com.oms.app.tables.domain.vo;

import com.oms.app.tables.domain.exception.TableDomainExceptions;
import jakarta.persistence.Embeddable;

@Embeddable
public record TableCapacity( int value ) {

    public static final int MAX_SIZE = 30;
    public static final int MIN_SIZE = 1;

    public TableCapacity {
        if (value < MIN_SIZE || value > MAX_SIZE) {
            throw TableDomainExceptions.invalidCapacity(MIN_SIZE,MAX_SIZE);
        }
    }

    public static TableCapacity of(int value) {
        return new TableCapacity(value);
    }
}
