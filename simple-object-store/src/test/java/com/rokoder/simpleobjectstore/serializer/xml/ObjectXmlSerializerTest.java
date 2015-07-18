package com.rokoder.simpleobjectstore.serializer.xml;

import com.rokoder.simpleobjectstore.TestModel;
import com.thoughtworks.xstream.XStream;
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

        TestModel actTM = os.fromBytes(bytes, TestModel.class);

        Assert.assertEquals(tc.getVarInt(), actTM.getVarInt());
        Assert.assertEquals(tc.getVarFloat(), actTM.getVarFloat(), 0.0001);
        Assert.assertEquals(tc.getVarString(), actTM.getVarString());
        Assert.assertEquals(tc.getVarBigInteger(), actTM.getVarBigInteger());
        Assert.assertEquals(tc.getVarBigDecimal(), actTM.getVarBigDecimal());
        Assert.assertEquals(tc.getVarDate(), actTM.getVarDate());
        Assert.assertEquals(tc.getVarLocalDate(), actTM.getVarLocalDate());
        Assert.assertEquals(tc.getVarLocalDateTime(), actTM.getVarLocalDateTime());
        Assert.assertEquals(tc.getVarDateTime(), actTM.getVarDateTime());

    }

    @Test
    public void testConstructorWithXStream() {
        XStream xStream = new XStream();
        ObjectXmlSerializer os = new ObjectXmlSerializer(xStream, false);

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt,
                dt);
        byte[] bytes = os.toBytes(tc);

        TestModel actTM = os.fromBytes(bytes, TestModel.class);

        Assert.assertEquals(tc.getVarInt(), actTM.getVarInt());
        Assert.assertEquals(tc.getVarFloat(), actTM.getVarFloat(), 0.0001);
        Assert.assertEquals(tc.getVarString(), actTM.getVarString());
        Assert.assertEquals(tc.getVarBigInteger(), actTM.getVarBigInteger());
        Assert.assertEquals(tc.getVarBigDecimal(), actTM.getVarBigDecimal());
        Assert.assertEquals(tc.getVarDate(), actTM.getVarDate());
        Assert.assertEquals(tc.getVarLocalDate(), actTM.getVarLocalDate());
        Assert.assertEquals(tc.getVarLocalDateTime(), actTM.getVarLocalDateTime());
        Assert.assertEquals(tc.getVarDateTime(), actTM.getVarDateTime());

    }


    @Test
    public void testCompactFalse() {
        ObjectXmlSerializer os = new ObjectXmlSerializer(false);

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt,
                dt);
        byte[] bytes = os.toBytes(tc);

        TestModel actTM = os.fromBytes(bytes, TestModel.class);

        Assert.assertEquals(tc.getVarInt(), actTM.getVarInt());
        Assert.assertEquals(tc.getVarFloat(), actTM.getVarFloat(), 0.0001);
        Assert.assertEquals(tc.getVarString(), actTM.getVarString());
        Assert.assertEquals(tc.getVarBigInteger(), actTM.getVarBigInteger());
        Assert.assertEquals(tc.getVarBigDecimal(), actTM.getVarBigDecimal());
        Assert.assertEquals(tc.getVarDate(), actTM.getVarDate());
        Assert.assertEquals(tc.getVarLocalDate(), actTM.getVarLocalDate());
        Assert.assertEquals(tc.getVarLocalDateTime(), actTM.getVarLocalDateTime());
        Assert.assertEquals(tc.getVarDateTime(), actTM.getVarDateTime());

    }

    @Test
    public void testFromBytesWithNull() {
        ObjectXmlSerializer os = new ObjectXmlSerializer();

        TestModel t1 = os.fromBytes(null, TestModel.class);

        Assert.assertNull(t1);
    }

    @Test
    public void testFromBytesWithEmpty() {
        ObjectXmlSerializer os = new ObjectXmlSerializer();

        TestModel t1 = os.fromBytes(new byte[0], TestModel.class);

        Assert.assertNull(t1);
    }

}
