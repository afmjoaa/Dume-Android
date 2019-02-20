package io.dume.dume.util;

public interface Pred<T extends Object> {
    boolean keep(T item);
}
