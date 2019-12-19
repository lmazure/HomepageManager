package utils;


@FunctionalInterface
public interface QuadriConsumer<T,U, V, W> {

    void accept(T t, U u, V v, W w);
}
