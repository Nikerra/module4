package org.example;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        /**
         * Ручной прогон программы
         */
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
        CustomTreeMapImpl<Integer, String> customTreeMap = new CustomTreeMapImpl<>(comparator);
        System.out.println("customTreeMap.isEmpty()=" + customTreeMap.isEmpty());
        System.out.println(customTreeMap.put(40, "Value1"));
        System.out.println(customTreeMap.put(15, "Value2"));
        System.out.println(customTreeMap.put(15, "Value15"));
        System.out.println(customTreeMap.put(19, "Value19"));
        System.out.println("customTreeMap.isEmpty()=" + customTreeMap.isEmpty());
        System.out.println("size=" + customTreeMap.size());
        System.out.println("toString()=" + customTreeMap);
        System.out.println("key=" + customTreeMap.toString(customTreeMap.keys()));
        System.out.println("value=" + customTreeMap.toString(customTreeMap.values()));
        System.out.println("get=10:" + customTreeMap.get(10));
        System.out.println("get=15:" + customTreeMap.get(15));
        System.out.println("remove=" + customTreeMap.remove(15));
        System.out.println("toString()=" + customTreeMap);
        System.out.println("containsKey=10:" + customTreeMap.containsKey(10));
        System.out.println("containsKey=15:" + customTreeMap.containsKey(19));
        System.out.println("containsValue=Value10:" + customTreeMap.containsValue("Value10"));
        System.out.println("containsValue=Value19:" + customTreeMap.containsValue("Value19"));

    }
}