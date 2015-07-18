package com.rokoder.simpleobjectstore;

import com.rokoder.simpleobjectstore.dao.DefaultObjectStoreDao;
import com.rokoder.simpleobjectstore.serializer.ObjectSerializer;
import com.rokoder.simpleobjectstore.serializer.xml.ObjectXmlSerializer;
import com.rokoder.simpleobjectstore.util.PooledDataSourceFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ObjectStoreImplTest {

    private ObjectStore createObjectStore() {
        DataSource ds = PooledDataSourceFactory.createDefaultInMemoryDataSourceUsingDerby();
        ObjectSerializer os = new ObjectXmlSerializer();

        return new ObjectStoreImpl("test-object-store", new DefaultObjectStoreDao(ds, "test_object_store"), os, true);
    }

    @Test
    public void testConstructorWithoutCreateSchema() {
        DataSource ds = PooledDataSourceFactory.createDefaultInMemoryDataSourceUsingDerby();

        ObjectSerializer os = new ObjectXmlSerializer();

        DefaultObjectStoreDao doso = new DefaultObjectStoreDao(ds, "test_object_store");
        doso.createTableSchema();

        ObjectStoreImpl objStore = new ObjectStoreImpl("test-object-store", doso, os);

        Assert.assertEquals("test-object-store", objStore.getName());
        Assert.assertEquals(36, objStore.getId().length());

        objStore.put("1", "Hello");

        Assert.assertEquals("Hello", objStore.get("1", String.class));
    }

    @Test
    public void testBasic() {
        ObjectStore os = createObjectStore();

        Assert.assertEquals("test-object-store", os.getName());
        Assert.assertEquals(36, os.getId().length());

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt,
                dt);

        os.put("1", tc);
        TestModel actTM = os.get("1", TestModel.class);

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
    public void testPut() throws InterruptedException {
        ObjectStore os = createObjectStore();

        os.put("1", "Hello");
        Assert.assertEquals("Hello", os.get("1", String.class));

    }

    @Test
    public void testPutWithExpiry() throws InterruptedException {
        ObjectStore os = createObjectStore();

        os.put("1", "Hello", new LocalDateTime().plusSeconds(1));
        Assert.assertEquals("Hello", os.get("1", String.class));

        os.put("2", "Hello World", null);
        Assert.assertEquals("Hello World", os.get("2", String.class));

        Thread.sleep(1000);

        Assert.assertNull(os.get("1", TestModel.class));
        Assert.assertEquals("Hello World", os.get("2", String.class));
    }

    @Test
    public void testPutOrUpdate() throws InterruptedException {
        ObjectStore os = createObjectStore();

        os.putOrUpdate("1", "Hello");

        Assert.assertEquals("Hello", os.get("1", String.class));

        os.putOrUpdate("1", "Hello World");

        Assert.assertEquals("Hello World", os.get("1", String.class));

        os.putOrUpdate("1", "Hello");

        Assert.assertEquals("Hello", os.get("1", String.class));

        os.putOrUpdate("1", "Hello World");

        Assert.assertEquals("Hello World", os.get("1", String.class));

    }

    @Test
    public void testPutOrUpdateWithExpiry() throws InterruptedException {
        ObjectStore os = createObjectStore();

        os.putOrUpdate("1", "Hello", new LocalDateTime().plusSeconds(1));

        Assert.assertEquals("Hello", os.get("1", String.class));

        os.putOrUpdate("1", "Hello World", new LocalDateTime().plusSeconds(1));

        Assert.assertEquals("Hello World", os.get("1", String.class));

        os.putOrUpdate("2", "Hello2", null);

        Assert.assertEquals("Hello2", os.get("2", String.class));

        os.putOrUpdate("2", "Hello2 World", null);

        Assert.assertEquals("Hello2 World", os.get("2", String.class));

        Thread.sleep(1000);

        Assert.assertNull(os.get("1", String.class));
        Assert.assertEquals("Hello2 World", os.get("2", String.class));
    }


    @Test
    public void testDelete() {
        ObjectStore os = createObjectStore();

        LocalDate ld = new LocalDate();
        LocalDateTime ldt = new LocalDateTime();
        Date d = ldt.toDate();
        DateTime dt = new DateTime();

        TestModel tc = new TestModel(10, 9.5f, "Hello", BigInteger.valueOf(10L), BigDecimal.valueOf(9.5), d, ld, ldt,
                dt);

        os.put("1", tc);
        TestModel actTM = os.get("1", TestModel.class);

        Assert.assertEquals(tc.getVarInt(), actTM.getVarInt());
        Assert.assertEquals(tc.getVarFloat(), actTM.getVarFloat(), 0.0001);
        Assert.assertEquals(tc.getVarString(), actTM.getVarString());
        Assert.assertEquals(tc.getVarBigInteger(), actTM.getVarBigInteger());
        Assert.assertEquals(tc.getVarBigDecimal(), actTM.getVarBigDecimal());
        Assert.assertEquals(tc.getVarDate(), actTM.getVarDate());
        Assert.assertEquals(tc.getVarLocalDate(), actTM.getVarLocalDate());
        Assert.assertEquals(tc.getVarLocalDateTime(), actTM.getVarLocalDateTime());
        Assert.assertEquals(tc.getVarDateTime(), actTM.getVarDateTime());

        os.delete("1");

        Assert.assertNull(os.get("1", TestModel.class));
    }

}
