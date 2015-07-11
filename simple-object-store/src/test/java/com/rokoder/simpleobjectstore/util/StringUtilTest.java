package com.rokoder.simpleobjectstore.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testBasic() {
        byte bytes[] = new byte[8];
        bytes[0] = 0;
        bytes[1] = 2;
        bytes[2] = 4;
        bytes[3] = 8;
        bytes[4] = 16;
        bytes[5] = 32;
        bytes[6] = 64;
        bytes[7] = (byte) 255;

        String outStr = StringUtil.byteArrayToHexString(bytes);
        Assert.assertEquals("00 02 04 08 10 20 40 ff", outStr);
    }

    @Test
    public void testNull() {
        String outStr = StringUtil.byteArrayToHexString(null);

        Assert.assertNull(outStr);
    }

    @Test
    public void testEmpty() {
        String outStr = StringUtil.byteArrayToHexString(new byte[0]);

        Assert.assertEquals(outStr, "");
    }

}
