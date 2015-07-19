Simple Object Store
===================

Simple object store to be used to store and retrieve objects. Most common use case when you can't change the under line
RDBMS databse.

Motivation
----------

During one of my projects, I need to have noSql like interface to store schema less objects. But unfortunately I cannot
change the underline database that I was using. Since I am using RDBMS, I implemented this simple object store library
which can store Java objects in a RDBMS. I needed to store separate object stores in separate tables.

Features
--------

 * Support object storage and retrieval.
 * Out of box support for various object storage serializers like XML, Java Serialization, Json etc.
 * Zip serialzer which can be combined with other serailizers to enable compression.
 * Out of box XML convertors for joda-time objects.
 * Support for object expiry based on time. Every object can have its own expiry time set.

Basic Usage
-----------

```java

DataSource ds = createDataSource(); // Your data base data source. You can create it as you pls
ObjectSerializer os = new ObjectXmlSerializer();

DefaultObjectStoreDao doso = new DefaultObjectStoreDao(ds, "test_object_store");
doso.createTableSchema(); // If you are creating schema outside. Refer to schema creating.

ObjectStoreImpl objStore = new ObjectStoreImpl("test-object-store", doso, os);
objStore.put("1", "Hello");

String value = objStore.get("1", String.class);

```

Manual Schema Creation
----------------------

If you are creating schema manually then you can below sql statement as reference.

```sql

CREATE TABLE test_object_store (create_time TIMESTAMP, update_time TIMESTAMP, expire_time TIMESTAMP, key_str VARCHAR(200), value_obj BLOB);
// INDEX CREATION
CREATE INDEX test_object_store_kes_str ON test_object_store (key_str);

```

Compression
-----------

@ObjectZipSerializer@ can be used to enable compression. We can use any object serialzer with it.

```java

DataSource ds = createDataSource(); // Your data base data source. You can create it as you pls
ObjectSerializer xmlOS = new ObjectXmlSerializer(); // We are using xml serialzer but you can use any.
ObjectZipSerializer zipOS = new ObjectZipSerializer(xmlOS);

DefaultObjectStoreDao doso = new DefaultObjectStoreDao(ds, "test_object_store");
doso.createTableSchema(); // If you are creating schema outside. Refer to schema creating.

ObjectStoreImpl objStore = new ObjectStoreImpl("test-object-store", doso, zipOS);
objStore.put("1", "Hello");

String value = objStore.get("1", String.class);

```

Available Object Serializers
----------------------------

* ObjectJsonSerializer
* ObjectXmlSerializer
* ObjectJavaSerializer
* ObjectZipSerializer
