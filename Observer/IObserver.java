package Observer;

public interface IObserver<T> {
    T update(Integer id, Double price);
}

