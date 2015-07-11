package com.rokoder.simpleobjectstore.serializer;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ObjectJavaSerializerTest {

    private static class TestC implements Serializable {
        private final int varInt;
        private final float varFloat;
        private final String varString;
        private final BigInteger varBigInteger;
        private final BigDecimal varBigDecimal;
        private final Date varDate;
        private final LocalDate varLocalDate;
        private final LocalDateTime varLocalDateTime;
        private final DateTime varDateTime;

        private TestC(int varInt, float varFloat, String varString, BigInteger varBigInteger, BigDecimal varBigDecimal, Date varDate, LocalDate varLocalDate, LocalDateTime varLocalDateTime, DateTime varDateTime) {
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

    @Test
    public void testBasic() {
        ObjectJavaSerializer os = new ObjectJavaSerializer();

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestC tc = new TestC(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt, dt);
        byte[] bytes = os.toBytes(tc);

        TestC t1 = os.fromBytes(bytes, TestC.class);

        Assert.assertEquals(tc.getVarInt(), t1.getVarInt());
        Assert.assertEquals(tc.getVarFloat(), t1.getVarFloat(), 0.0001);
        Assert.assertEquals(tc.getVarString(), t1.getVarString());
        Assert.assertEquals(tc.getVarBigInteger(), t1.getVarBigInteger());
        Assert.assertEquals(tc.getVarBigDecimal(), t1.getVarBigDecimal());
        Assert.assertEquals(tc.getVarDate(), t1.getVarDate());
        Assert.assertEquals(tc.getVarLocalDate(), t1.getVarLocalDate());
        Assert.assertEquals(tc.getVarLocalDateTime(), t1.getVarLocalDateTime());
        Assert.assertEquals(tc.getVarDateTime(), t1.getVarDateTime());

    }
}