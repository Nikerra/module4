package org.example;

import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {

        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
        CustomTreeMapImpl<Integer, String> customTreeMap = new CustomTreeMapImpl<>(comparator);
        System.out.println("customTreeMap.isEmpty()=" + customTreeMap.isEmpty());
        customTreeMap.put(40, "Value1");
        customTreeMap.put(15, "Value2");
        customTreeMap.put(19, "Value3");
        System.out.println("customTreeMap.isEmpty()=" + customTreeMap.isEmpty());
        System.out.println("size=" + customTreeMap.size());
        System.out.println("toString()=" + customTreeMap);
        System.out.println("key=" + Arrays.toString(customTreeMap.keys()));
        System.out.println("value=" + Arrays.toString(customTreeMap.values()));
    }
}