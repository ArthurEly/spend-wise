package SpendWise.Utils;

import java.io.Serializable;
import java.util.Iterator;

public class Triple<T1, T2, T3> implements Iterable<Object>, Serializable {
    private T1 first;
    private T2 second;
    private T3 third;

    public Triple(T1 firstItem, T2 secondItem, T3 thirdItem) {
        this.first = firstItem;
        this.second = secondItem;
        this.third = thirdItem;
    }

    public void setFirst(T1 firstItem) {
        this.first = firstItem;
    }

    public void setSecond(T2 secondItem) {
        this.second = secondItem;
    }

    public void setThird(T3 thirdItem) {
        this.third = thirdItem;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public T3 getThird() {
        return third;
    }

    @Override
    public String toString() {
        return first.toString() + " || " + second.toString() + " || " + third.toString();
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
                    case 2:
                        return third;
                    default:
                        return null;
                }
            }
        };
    }

    public int getSize() {
        return 3;
    }
}
