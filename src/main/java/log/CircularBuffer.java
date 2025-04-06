package log;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * Кольцевой буфер фиксированного размера
 * для хранения элементов по принципу FIFO (первый пришел - первый вышел).
 * При заполнении буфера новые элементы перезаписывают самые старые.
 */
public class CircularBuffer<T> implements Iterable<T> {
    private final Object[] buffer;
    private int start = 0;
    private int end = 0;
    private int size = 0;
    private final int capacity;
    /**
     * Создает новый кольцевой буфер заданной емкости
     */
    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }
    /**
     * Добавляет элемент в буфер. Если буфер заполнен,
     * самый старый элемент перезаписывается.
     */
    public synchronized void add(T item) {
        if (size < capacity) {
            buffer[end] = item;
            end = (end + 1) % capacity;
            size++;
        } else {
            buffer[end] = item;
            start = (start + 1) % capacity;
            end = (end + 1) % capacity;
        }
    }
    /**
     * Возвращает элемент по указанному индексу.
     */
    public synchronized T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (T) buffer[(start + index) % capacity];
    }
    /**
     * Возвращает текущее количество элементов в буфере.
     */
    public synchronized int size() {
        return size;
    }
    /**
     * Возвращает последовательность элементов из указанного диапазона.
     */
    public synchronized Iterable<T> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= size) {
            return new ArrayList<>();
        }
        ArrayList<T> entries = new ArrayList<>();
        for (int i = startFrom; i < startFrom + count && i < size; i++) {
            entries.add(get(i));
        }
        return entries;
    }
    /**
     * Возвращает итератор для последовательного перебора элементов буфера.
     */
    @Override
    public synchronized Iterator<T> iterator() {
        return new Iterator<T>() {
            private int current = start;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public T next() {
                T item = (T) buffer[current];
                current = (current + 1) % capacity;
                count++;
                return item;
            }
        };
    }
}
