package Observer;

public interface IObservable<T, E> {
    void add(T obj);
    void remove(T obj);
    E inform(Integer id, Double price);
}
