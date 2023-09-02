package SpendWise.Utils;

import java.io.Serializable;
import java.util.Iterator;

public class Pair<T1, T2> implements Iterable<Object>, Serializable {
    private T1 first;
    private T2 second;

    public Pair(T1 firstItem, T2 secondItem) {
        this.first = firstItem;
        this.second = secondItem;
    }

    public void setFirst(T1 firstItem) {
        this.first = firstItem;
    }

    public void setSecond(T2 secondItem) {
        this.second = secondItem;
    }

    public T1 getFirst() {
        return first;
    }

    public T1 getKey() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public T2 getValue() {
        return second;
    }

    @Override
    public String toString() {
        return first.toString() + " || " + second.toString();
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < getSize();
            }

            @Override
            public Object next() {
                switch (index++) {
                    case 0:
                        return first;
                    case 1:
                        return second;
                    default:
                        return null;
                }
            }
        };
    }

    public int getSize() {
        return 2;
    }
}
