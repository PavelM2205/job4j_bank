package ru.job4j.job4j.bank.repository;

import ru.job4j.job4j.bank.model.Id;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Store<T extends Id> {
    protected Map<Integer, T> store = new ConcurrentHashMap<>();
    protected AtomicInteger idGenerator = new AtomicInteger(0);

    public T saveOrUpdate(T model) {
        if (model.getId() == 0) {
            model.setId(idGenerator.incrementAndGet());
            return store.put(model.getId(), model);
        }
        return store.computeIfPresent(model.getId(), (key, value) -> model);
    }

    public Optional<T> delete(int id) {
        return Optional.ofNullable(store.remove(id));
    }

    public List<T> findAll() {
        return new CopyOnWriteArrayList<>(store.values());
    }

    public Optional<T> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }
}

