package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> cmp;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        cmp = c;
    }
    
    
    public T max() {
        return max(cmp);
    };
    
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        int maxItemIndex = 0;
        for (int i = 0; i < this.size(); i++) {
            int compVal = c.compare(this.get(i), this.get(maxItemIndex));
            if (compVal > 0) {
                maxItemIndex = i;
            }
        }
        return this.get(maxItemIndex);
    }
}
