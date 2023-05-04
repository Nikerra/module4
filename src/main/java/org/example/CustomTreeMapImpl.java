package org.example;

import java.util.*;

/**
 * # Module 4, task 2. Java Collections Framework Part III
 *
 * ## Задание 1
 *
 * 1. Реализовать класс `CustomTreeMapImpl<K,V>`, который представляет отображение на основе бинарного дерева поиска.
 * 2. Класс `CustomTreeMapImpl` (необходимо создать самостоятельно) должен реализовывать интерфейс `CustomTreeMap<K,V>` и может хранить объекты любого типа
 *
 * ### Конструкторы
 *
 * `CustomTreeMapImpl(Comparator<K> comparator)` - ссылка на `CustomTreeMap<K,V>`: #ref
 *
 * ### Критерии приемки
 *
 * 1. Каждый публичный метод класса `CustomTreeMapImpl` должен быть покрыт unit-тестом.
 * 2. !!! Вносить правки в интерфейс `CustomTreeMap<K,V>` нельзя.
 */
public class CustomTreeMapImpl<K, V> implements CustomTreeMap<K, V> {

    private CustomTreeMapImpl.Entry<K, V> root;
    private final Comparator<K> comparator;
    private int size;
    private int modCount;


    static final class Entry<K, V> {
        K key;
        V value;

        @Override
        public String toString() {
            return "{"+ key + "=" + value + '}';
        }

        CustomTreeMapImpl.Entry<K, V> left;
        CustomTreeMapImpl.Entry<K, V> right;
        CustomTreeMapImpl.Entry<K, V> parent;

        Entry(K key, V value, CustomTreeMapImpl.Entry<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    public CustomTreeMapImpl(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    /**
     *  Get ize of map.
     *
     * @return - size of map
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Check map is empty.
     *
     * @return - boolean value
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get item by key.
     *
     * @param key - key
     */
    @Override
    public V get(Object key) {
        Entry<K,V> p = getEntry(key);
        return (p==null ? null : p.value);
    }

    /**
     * Add value by key.
     *
     * @return previous value or null
     */
    @Override
    public V put(K key, V value) {
        return put(key, value, true);
    }

    /**
     * Remove item by key.
     *
     * @param key - key
     * @return deleted item or null if item with key doesn't exists
     */
    @Override
    public V remove(K key) {
        Entry<K,V> p = getEntry(key);
        if (p == null)
            return null;

        V oldValue = p.value;
        deleteEntry(p);
        return oldValue;
    }


    /**
     * Checks if item exists by key.
     *
     * @param key - key
     * @return true or false
     */
    @Override
    public boolean containsKey(K key) {
        return getEntry(key) != null;
    }

    /**
     * Checks if item exists by value.
     *
     * @param value - item
     * @return true or false
     */
    @Override
    public boolean containsValue(V value) {
        for (Entry<K,V> e = getFirstEntry(); e != null; e = successor(e))
            if (valEquals(value, e.value))
                return true;
        return false;
    }

    @Override
    public Object[] keys() {
        return new Object[0];
    }

    @Override
    public Object[] values() {
        return new Object[0];
    }


    /**
     * SUPPORT METHOD
     */

    private Entry<K,V> getEntry(Object key) {
        if (comparator != null)
            return getEntryUsingComparator(key);
        Objects.requireNonNull(key);
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }

    private Entry<K,V> getEntryUsingComparator(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K) key;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            Entry<K,V> p = root;
            while (p != null) {
                int cmp = cpr.compare(k, p.key);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }

    private V put(K key, V value, boolean replaceOld) {
        Entry<K,V> t = root;
        if (t == null) {
            addEntryToEmptyMap(key, value);
            return null;
        }
        int cmp;
        Entry<K,V> parent;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else {
                    V oldValue = t.value;
                    if (replaceOld || oldValue == null) {
                        t.value = value;
                    }
                    return oldValue;
                }
            } while (t != null);
        } else {
            Objects.requireNonNull(key);
            @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else {
                    V oldValue = t.value;
                    if (replaceOld || oldValue == null) {
                        t.value = value;
                    }
                    return oldValue;
                }
            } while (t != null);
        }
        addEntry(key, value, parent, cmp < 0);
        return null;
    }

    private void addEntry(K key, V value, Entry<K, V> parent, boolean addToLeft) {
        Entry<K,V> e = new Entry<>(key, value, parent);
        if (addToLeft)
            parent.left = e;
        else
            parent.right = e;
        size++;
        modCount++;
    }

    private void addEntryToEmptyMap(K key, V value) {
        compare(key, key); // type (and possibly null) check
        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
    }

    private int compare(Object k1, Object k2) {
        return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
                : comparator.compare((K)k1, (K)k2);
    }


    private void deleteEntry(Entry<K,V> p) {
        modCount++;
        size--;
        if (p.left != null && p.right != null) {
            Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }
        Entry<K,V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;
            p.left = p.right = p.parent = null;

        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    private <K,V> Entry<K,V> successor(Entry<K,V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Entry<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private boolean valEquals(Object o1, Object o2) {
        return (o1==null ? o2==null : o1.equals(o2));
    }
    private  Entry<K,V> getFirstEntry() {
        Entry<K,V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }
}