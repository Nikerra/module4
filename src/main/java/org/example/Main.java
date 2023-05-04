package org.example;

import java.util.Comparator;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        };
        CustomTreeMapImpl<Integer, String> customTreeMap = new CustomTreeMapImpl<>(comparator);
        System.out.println("customTreeMap.isEmpty()=" + customTreeMap.isEmpty());
        customTreeMap.put(40, "Value1");
        customTreeMap.put(15, "Value1");
        customTreeMap.put(19, "Value1");
        System.out.println("customTreeMap.isEmpty()=" + customTreeMap.isEmpty());
        System.out.println("size=" + customTreeMap.size());
        System.out.println("toString()=" + customTreeMap);
        System.out.println("customTreeMapKey=" + customTreeMap.keys());
        System.out.println("customTreeMapValues=" + customTreeMap.values());
        }
    }
