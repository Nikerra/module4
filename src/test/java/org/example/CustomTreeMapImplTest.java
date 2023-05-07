package org.example;

import static org.junit.Assert.*;

import junit.framework.Assert;
import junit.framework.TestCase;
import java.util.Comparator;

public class CustomTreeMapImplTest extends TestCase {
    Comparator<Integer> comparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    };
    CustomTreeMapImpl<Integer, String> customTreeMap = new CustomTreeMapImpl<>(comparator);

    public void testGet() {
        Assert.assertEquals(null,customTreeMap.get(10));
        customTreeMap.put(15, "value=15");
        assertEquals("value=15", customTreeMap.get(15));
    }

    public void testPut() {
        Assert.assertEquals(null, customTreeMap.put(15, "value=15"));
        Assert.assertEquals("value=15", customTreeMap.put(15, "value=19"));
    }

    public void testRemove() {
        Assert.assertEquals(null, customTreeMap.remove(15));
        customTreeMap.put(15, "value=15");
        Assert.assertEquals("value=15", customTreeMap.remove(15));
    }

    public void testContainsKey() {
        Assert.assertEquals(false, customTreeMap.containsKey(10));
        customTreeMap.put(15, "value=15");
        Assert.assertEquals(true, customTreeMap.containsKey(15));
    }

    public void testContainsValue() {
        Assert.assertEquals(false, customTreeMap.containsValue("value=10"));
        customTreeMap.put(15, "value=15");
        Assert.assertEquals(true, customTreeMap.containsValue("value=15"));
    }

    public void testKeys() {
        Assert.assertEquals("[]",customTreeMap.toString(customTreeMap.keys()));
        customTreeMap.put(15, "value=15");
        Assert.assertEquals("[15]",customTreeMap.toString(customTreeMap.keys()));
    }

    public void testValues() {
        Assert.assertEquals("[]",customTreeMap.toString(customTreeMap.values()));
        customTreeMap.put(15, "value=15");
        Assert.assertEquals("[value=15]",customTreeMap.toString(customTreeMap.values()));
    }
}