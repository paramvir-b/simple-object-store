package com.rokoder.simpleobjectstore.serializer;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

public class ObjectJavaSerializerTest {

    @Test
    public void testBasic() {
        ObjectJavaSerializer os = new ObjectJavaSerializer();

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt,
                dt);
        byte[] bytes = os.toBytes(tc);

        TestModel t1 = os.fromBytes(bytes, TestModel.class);

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

    @Test
    public void testFromBytesWithNull() {
        ObjectJavaSerializer os = new ObjectJavaSerializer();

        TestModel t1 = os.fromBytes(null, TestModel.class);

        Assert.assertNull(t1);
    }

    @Test
    public void testFromBytesWithEmpty() {
        ObjectJavaSerializer os = new ObjectJavaSerializer();

        TestModel t1 = os.fromBytes(new byte[0], TestModel.class);
        Assert.assertNull(t1);

    }

    @Test(expected = IllegalStateException.class)
    public void testFromBytesWithLessBytes() {
        ObjectJavaSerializer os = new ObjectJavaSerializer();

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt,
                dt);
        byte[] bytes = os.toBytes(tc);
        // Creating imcomplete bytes to generate exception
        byte[] copyBytes = Arrays.copyOf(bytes, 10);

        try {
            os.fromBytes(copyBytes, TestModel.class);
        } catch (IllegalStateException e) {
            Assert.assertEquals(
                    "Unable to load bytes becasue of I/O Exception to object type=class com.rokoder.simpleobjectstore.serializer.TestModel",
                    e.getMessage());
            throw e;
        }

    }

}