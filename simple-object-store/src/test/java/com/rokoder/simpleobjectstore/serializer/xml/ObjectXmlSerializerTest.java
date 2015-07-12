package com.rokoder.simpleobjectstore.serializer.xml;

import com.rokoder.simpleobjectstore.serializer.TestModel;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ObjectXmlSerializerTest {

    @Test
    public void testBasic() {
        ObjectXmlSerializer os = new ObjectXmlSerializer();

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
    public void testCompactFalse() {
        ObjectXmlSerializer os = new ObjectXmlSerializer(false);

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt, dt);
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

}
