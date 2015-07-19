package com.rokoder.simpleobjectstore.dao;

import com.rokoder.simpleobjectstore.util.TestHelper;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;

public abstract class DefaultObjectStoreDaoTest {

    public abstract DataSource createDataSource();

    @Test
    public void testObjectStoreColsConstructor() {
        TestHelper.testPrivateVoidConstructor(ObjectStoreDao.ObjectStoreCols.class);
    }

    @Test
    public void testBasic() {
        DataSource dataSource = createDataSource();

        ObjectStoreDao osd = new DefaultObjectStoreDao(dataSource, "object_store");

        osd.createTableSchema();

        osd.insert("1", "Hello".getBytes(), null);

        String value = new String(osd.fetchBytes("1", new LocalDateTime()));

        Assert.assertEquals("Hello", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullDataSource() {
        try {
            new DefaultObjectStoreDao(null, "object_store");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("DataSource cannot be null", e.getMessage());
            throw e;
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithSpaceInTableNameNull() {
        try {
            DataSource dataSource = createDataSource();
            new DefaultObjectStoreDao(dataSource, null);

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Table name is null", e.getMessage());
            throw e;
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithSpaceInTableNameEmpty() {
        try {
            DataSource dataSource = createDataSource();
            new DefaultObjectStoreDao(dataSource, "");

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Table name is empty", e.getMessage());
            throw e;
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithSpaceInTableName() {
        try {
            DataSource dataSource = createDataSource();
            new DefaultObjectStoreDao(dataSource, "object store");

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Table name cannot contain space. tableName='object store'", e.getMessage());
            throw e;
        }

    }

    @Test
    public void testInsertWithoutExpiry() {
        DataSource dataSource = createDataSource();

        ObjectStoreDao osd = new DefaultObjectStoreDao(dataSource, "object_store");

        osd.createTableSchema();

        osd.insert("1", "Hello".getBytes(), null);

        String value = new String(osd.fetchBytes("1", new LocalDateTime()));

        Assert.assertEquals("Hello", value);
    }

    @Test
    public void testInsertWithExpiry() throws InterruptedException {
        DataSource dataSource = createDataSource();

        ObjectStoreDao osd = new DefaultObjectStoreDao(dataSource, "object_store");

        osd.createTableSchema();

        osd.insert("1", "Hello".getBytes(), new LocalDateTime().plusSeconds(1));

        String value = new String(osd.fetchBytes("1", new LocalDateTime()));

        Assert.assertEquals("Hello", value);

        Thread.sleep(1000);

        Assert.assertNull(osd.fetchBytes("1", new LocalDateTime()));
    }


    @Test
    public void testUpdateWithNullExpireTime() {
        DataSource dataSource = createDataSource();

        ObjectStoreDao osd = new DefaultObjectStoreDao(dataSource, "object_store");

        osd.createTableSchema();

        osd.insert("1", "Hello".getBytes(), null);

        String value;

        value = new String(osd.fetchBytes("1", new LocalDateTime()));
        Assert.assertEquals("Hello", value);

        osd.update("1", "Hello_New".getBytes(), null);
        value = new String(osd.fetchBytes("1", new LocalDateTime()));
        Assert.assertEquals("Hello_New", value);
    }

    @Test
    public void testUpdateWithExpireTime() throws InterruptedException {
        DataSource dataSource = createDataSource();

        ObjectStoreDao osd = new DefaultObjectStoreDao(dataSource, "object_store");

        osd.createTableSchema();

        osd.insert("1", "Hello".getBytes(), new LocalDateTime().plusSeconds(1));

        String value;

        value = new String(osd.fetchBytes("1", new LocalDateTime()));
        Assert.assertEquals("Hello", value);

        osd.update("1", "Hello_New".getBytes(), new LocalDateTime().plusSeconds(1));
        value = new String(osd.fetchBytes("1", new LocalDateTime()));
        Assert.assertEquals("Hello_New", value);

        Thread.sleep(1000);

        Assert.assertNull(osd.fetchBytes("1", new LocalDateTime()));
    }

    @Test
    public void testInsertOrUpdate() {
        DataSource dataSource = createDataSource();

        ObjectStoreDao osd = new DefaultObjectStoreDao(dataSource, "object_store");

        osd.createTableSchema();

        osd.insertOrUpdate("1", "Hello".getBytes(), null);

        String value;

        value = new String(osd.fetchBytes("1", new LocalDateTime()));
        Assert.assertEquals("Hello", value);

        osd.insertOrUpdate("1", "Hello_New".getBytes(), null);
        value = new String(osd.fetchBytes("1", new LocalDateTime()));
        Assert.assertEquals("Hello_New", value);
    }

    @Test
    public void testDelete() {
        DataSource dataSource = createDataSource();

        ObjectStoreDao osd = new DefaultObjectStoreDao(dataSource, "object_store");

        osd.createTableSchema();

        osd.insert("1", "Hello".getBytes(), null);

        String value;

        value = new String(osd.fetchBytes("1", new LocalDateTime()));
        Assert.assertEquals("Hello", value);

        osd.delete("1");
        Assert.assertNull(osd.fetchBytes("1", new LocalDateTime()));
    }

    @Test
    public void testDeleteByExpireTime() throws InterruptedException {
        DataSource dataSource = createDataSource();

        ObjectStoreDao osd = new DefaultObjectStoreDao(dataSource, "object_store");

        osd.createTableSchema();

        osd.insert("1", "Hello".getBytes(), new LocalDateTime().plusSeconds(1));

        String value;

        value = new String(osd.fetchBytes("1", new LocalDateTime()));
        Assert.assertEquals("Hello", value);

        osd.deleteByExpireTime(new LocalDateTime().plusSeconds(1));

        Assert.assertNull(osd.fetchBytes("1", new LocalDateTime()));
    }

}
