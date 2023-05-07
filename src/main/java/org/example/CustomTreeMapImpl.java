package org.example;

import java.sql.ClientInfoStatus;
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

    private transient Entry<K, V> root;
    private final Comparator<K> comparator;
    private  int size = 0;
    private  int modCount = 0;
    private  EntrySet entrySet;

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
        Entry<K,V> entry = getEntry(key);
        return (entry == null ? null : entry.value);
    }

    /**
     * Add value by key.
     *
     * @return previous value or null
     */
    @Override
    public V put(K key, V value) {
        Entry<K,V> tempRoot = root;
        if (tempRoot == null) {
            addEntryToEmptyMap(key, value);
            return null;
        }
        int cmpValue;
        Entry<K,V> entry;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                entry = tempRoot;
                cmpValue = cpr.compare(key, tempRoot.key);
                if (cmpValue < 0)
                    tempRoot = tempRoot.left;
                else if (cmpValue > 0)
                    tempRoot = tempRoot.right;
                else {
                    V oldValue = tempRoot.value;
                    tempRoot.value = value;
                    return oldValue;
                }
            } while (tempRoot != null);
        } else {
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                entry = tempRoot;
                cmpValue = k.compareTo(tempRoot.key);
                if (cmpValue < 0)
                    tempRoot = tempRoot.left;
                else if (cmpValue > 0)
                    tempRoot = tempRoot.right;
                else {
                    V oldValue = tempRoot.value;
                    tempRoot.value = value;
                    return oldValue;
                }
            } while (tempRoot != null);
        }
        addEntry(key, value, entry, cmpValue < 0);
        return null;
    }

    /**
     * Remove item by key.
     *
     * @param key - key
     * @return deleted item or null if item with key doesn't exists
     */
    @Override
    public V remove(K key) {
        Entry<K,V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        V oldValue = entry.value;

        modCount++;
        size--;
        if (entry.left != null && entry.right != null) {
            Entry<K,V> heirEntry = heir(entry);
            entry.key = heirEntry.key;
            entry.value = heirEntry.value;
            entry = heirEntry;
        }
        Entry<K,V> replacement = (entry.left != null ? entry.left : entry.right);

        if (replacement != null) {
            replacement.parent = entry.parent;
            if (entry.parent == null) {
                root = replacement;
            } else if (entry == entry.parent.left) {
                entry.parent.left = replacement;
            } else {
                entry.parent.right = replacement;
            }
            entry.left = entry.right = entry.parent = null;
        } else if (entry.parent == null) {
            root = null;
        } else {
            if (entry.parent != null) {
                if (entry == entry.parent.left) {
                    entry.parent.left = null;
                } else if (entry == entry.parent.right) {
                    entry.parent.right = null;
                }
                entry.parent = null;
            }
        }
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
        for (Entry<K,V> entry = getFirstEntry(); entry != null; entry = heir(entry))
            if (valEquals(value, entry.value)) {
                return true;
            }
        return false;
    }

    /**
     * Get all keys.
     * Get content in format '[ key1, ..., keyN ] or [ ] if empty.
     */
    @Override
    public Object[] keys() {
        Set<Map.Entry<K,V>> entrySet;
        entrySet = entrySet();
        Object[] arrayKey = new Object[entrySet.size()];
        int i = 0;
        for (Map.Entry<K, V> array: entrySet) {
            arrayKey[i++] = array.getKey();
        }
        return arrayKey;
    }

    /**
     * Get all values.
     * Get content in format '[ value1, ..., valueN ] or [ ] if empty.
     */
    @Override
    public Object[] values() {
        Set<Map.Entry<K,V>> entrySet;
        entrySet = entrySet();
        Object[] arrayValue = new Object[entrySet.size()];
        int i = 0;
        for (Map.Entry<K, V> array: entrySet) {
            arrayValue[i++] = array.getValue();
        }
        return arrayValue;
    }


    /**
     * SUPPORT METHOD
     */

    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;
        Entry(K key, V value, Entry<K,V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
        }
        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
        public String toString() {
            return key + "=" + value;
        }
    }



    private Entry<K,V> getEntry(Object key) {
        Comparable<? super K> tempKey = (Comparable<? super K>) key;
        Entry<K,V> parentEntry = root;
        while (parentEntry != null) {
            int cmpValue = tempKey.compareTo(parentEntry.key);
            if (cmpValue < 0) {
                parentEntry = parentEntry.left;
            } else if (cmpValue > 0) {
                parentEntry = parentEntry.right;
            } else {
                return parentEntry;
            }
        }
        return null;
    }

    private void addEntry(K key, V value, Entry<K, V> parent, boolean addToLeft) {
        Entry<K,V> entry = new Entry<>(key, value, parent);
        if (addToLeft) {
            parent.left = entry;
        } else {
            parent.right = entry;
        }
        size++;
        modCount++;
    }

    private void addEntryToEmptyMap(K key, V value) {
        compare(key, key);
        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
    }

    private int compare(Object key1, Object key2) {
        return comparator==null ? ((Comparable<? super K>)key1).compareTo((K)key2)
                : comparator.compare((K)key1, (K)key2);
    }

    private <K,V> Entry<K,V> heir(Entry<K,V> tempEntry) {
        if (tempEntry == null)
            return null;
        else if (tempEntry.right != null) {
            Entry<K,V> parentEntry = tempEntry.right;
            while (parentEntry.left != null)
                parentEntry = parentEntry.left;
            return parentEntry;
        } else {
            Entry<K,V> parentEntry = tempEntry.parent;
            Entry<K,V> childEntry = tempEntry;
            while (parentEntry != null && childEntry == parentEntry.right) {
                childEntry = parentEntry;
                parentEntry = parentEntry.parent;
            }
            return parentEntry;
        }
    }

    private boolean valEquals(Object o1, Object o2) {
        return (o1 == null ? o2 == null : o1.equals(o2));
    }

    private  Entry<K,V> getFirstEntry() {
        Entry<K,V> tempParent = root;
        if (tempParent != null)
            while (tempParent.left != null) {
                tempParent = tempParent.left;
            }
        return tempParent;
    }


    /**
     * Copy SET Method
     */

    class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        public Iterator<Map.Entry<K,V>> iterator() {
            return new EntryIterator(getFirstEntry());
        }
        public int size() {
            return size;
        }
    }

    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K,V> next;
        Entry<K,V> lastReturned;
        int expectedModCount;

        PrivateEntryIterator(Entry<K,V> first) {
            expectedModCount = modCount;
            lastReturned = null;
            next = first;
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Entry<K,V> nextEntry() {
            Entry<K,V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            next = heir(e);
            lastReturned = e;
            return e;
        }
    }

    final class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>> {
        EntryIterator(Entry<K,V> first) {
            super(first);
        }
        public Map.Entry<K,V> next() {
            return nextEntry();
        }
    }
    public String toString() {
        Iterator<Map.Entry<K,V>> i = entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Map.Entry<K,V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key   == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }

    public Set<Map.Entry<K,V>> entrySet() {
        EntrySet es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet());
    }


    /**
     * Copt ARRAYS Method
     */
    public static String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

}