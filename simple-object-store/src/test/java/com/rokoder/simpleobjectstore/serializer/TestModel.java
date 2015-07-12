package com.rokoder.simpleobjectstore.serializer;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TestModel implements Serializable {
    private final int varInt;
    private final float varFloat;
    private final String varString;
    private final BigInteger varBigInteger;
    private final BigDecimal varBigDecimal;
    private final Date varDate;
    private final LocalDate varLocalDate;
    private final LocalDateTime varLocalDateTime;
    private final DateTime varDateTime;

    public TestModel(int varInt, float varFloat, String varString, BigInteger varBigInteger, BigDecimal varBigDecimal, Date varDate, LocalDate varLocalDate, LocalDateTime varLocalDateTime, DateTime varDateTime) {
        this.varInt = varInt;
        this.varFloat = varFloat;
        this.varString = varString;
        this.varBigInteger = varBigInteger;
        this.varBigDecimal = varBigDecimal;
        this.varDate = varDate;
        this.varLocalDate = varLocalDate;
        this.varLocalDateTime = varLocalDateTime;
        this.varDateTime = varDateTime;
    }

    public int getVarInt() {
        return varInt;
    }

    public float getVarFloat() {
        return varFloat;
    }

    public String getVarString() {
        return varString;
    }

    public BigInteger getVarBigInteger() {
        return varBigInteger;
    }

    public BigDecimal getVarBigDecimal() {
        return varBigDecimal;
    }

    public Date getVarDate() {
        return varDate;
    }

    public LocalDate getVarLocalDate() {
        return varLocalDate;
    }

    public LocalDateTime getVarLocalDateTime() {
        return varLocalDateTime;
    }

    public DateTime getVarDateTime() {
        return varDateTime;
    }

}
