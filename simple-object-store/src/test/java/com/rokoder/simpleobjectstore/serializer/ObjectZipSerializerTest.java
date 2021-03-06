package com.rokoder.simpleobjectstore.serializer;

import com.rokoder.simpleobjectstore.TestModel;
import com.rokoder.simpleobjectstore.serializer.xml.ObjectXmlSerializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ObjectZipSerializerTest {

    @Test
    public void testBasicWithXml() {
        ObjectZipSerializer os = new ObjectZipSerializer(new ObjectXmlSerializer());

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt,
                dt);
        byte[] bytes = os.toBytes(tc);

        Assert.assertNotNull(bytes);
        Assert.assertNotEquals(0, bytes.length);

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
    public void testBasicWithXmlCompactFalse() {
        ObjectZipSerializer os = new ObjectZipSerializer(new ObjectXmlSerializer(false));

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
    public void testBasicWithJava() {
        ObjectZipSerializer os = new ObjectZipSerializer(new ObjectJavaSerializer());

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
        ObjectZipSerializer os = new ObjectZipSerializer(new ObjectJavaSerializer());

        TestModel t1 = os.fromBytes(null, TestModel.class);

        Assert.assertNull(t1);
    }

    @Test
    public void testFromBytesWithEmpty() {
        ObjectZipSerializer os = new ObjectZipSerializer(new ObjectJavaSerializer());

        TestModel t1 = os.fromBytes(new byte[0], TestModel.class);
        Assert.assertNull(t1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromBytesException() {
        ObjectSerializer mockObjSerializer = Mockito.mock(ObjectSerializer.class);
        Mockito.when(mockObjSerializer.toBytes(Mockito.any())).thenReturn(new byte[10]);

        ObjectZipSerializer os = new ObjectZipSerializer(mockObjSerializer);

        try {
            os.fromBytes(new byte[10], String.class);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unable to load bytes becasue of I/O Exception to object type=class java.lang.String",
                    e.getMessage());
            throw e;
        }

    }
}
