package com.rokoder.simpleobjectstore;

import com.rokoder.simpleobjectstore.dao.DefaultObjectStoreDao;
import com.rokoder.simpleobjectstore.serializer.ObjectJavaSerializer;
import com.rokoder.simpleobjectstore.serializer.ObjectSerializer;
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
        ObjectSerializer os = new ObjectJavaSerializer();

        return new ObjectStoreImpl("test-object-store", new DefaultObjectStoreDao(ds, "test_object_store"), os, true);
    }

    @Test
    public void testBasic() {
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
    }
}
