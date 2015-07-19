package com.rokoder.simpleobjectstore.serializer.json;

import com.google.gson.Gson;
import com.rokoder.simpleobjectstore.TestModel;
import com.rokoder.simpleobjectstore.serializer.ObjectSerializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ObjectJsonSerializerTest {

    @Test
    public void testBasic() {
        ObjectJsonSerializer os = new ObjectJsonSerializer(false);

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt,
                dt);

        byte[] bytes = os.toBytes(tc);

        Assert.assertNotNull(bytes);
        Assert.assertNotEquals(0, bytes.length);

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
    public void testConstructorWithGson() {
        Gson gson = new Gson();

        ObjectJsonSerializer os = new ObjectJsonSerializer(gson);

        byte[] bytes = os.toBytes("Hello");

        Assert.assertNotNull(bytes);
        Assert.assertNotEquals(0, bytes.length);

        String actStr = os.fromBytes(bytes, String.class);

        Assert.assertEquals("Hello", actStr);

    }

    @Test
    public void testFromBytesWithNull() {
        ObjectSerializer os = new ObjectJsonSerializer();

        TestModel t1 = os.fromBytes(null, TestModel.class);

        Assert.assertNull(t1);
    }

    @Test
    public void testFromBytesWithEmpty() {
        ObjectSerializer os = new ObjectJsonSerializer();

        TestModel t1 = os.fromBytes(new byte[0], TestModel.class);

        Assert.assertNull(t1);
    }

}
