package com.poly.sof309.service;

import com.poly.sof309.exception.DuplicateProductException;
import com.poly.sof309.exception.ValidationException;
import com.poly.sof309.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/** Nghiệp vụ CRUD Sản phẩm (lưu trong bộ nhớ). */
@Service
public class ProductService {
    private final Map<Long, Product> store = new ConcurrentSkipListMap<>();
    private final AtomicLong seq = new AtomicLong();

    public ProductService() {
        add("Laptop Dell XPS", 25000000, 10);
        add("Chuột Logitech", 350000, 50);
    }

    public List<String> validate(String name, double price, int quantity) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.isBlank()) errors.add("Tên sản phẩm không được để trống");
        if (price <= 0) errors.add("Giá bán phải lớn hơn 0");
        if (quantity < 0) errors.add("Số lượng phải lớn hơn hoặc bằng 0");
        return errors;
    }

    public synchronized Product add(String name, double price, int quantity) {
        checkAndGuard(null, name, price, quantity);
        Product p = new Product(seq.incrementAndGet(), name.trim(), price, quantity);
        store.put(p.getId(), p);
        return p;
    }

    public synchronized Product update(long id, String name, double price, int quantity) {
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm có id = " + id);
        }
        checkAndGuard(id, name, price, quantity);
        Product p = new Product(id, name.trim(), price, quantity);
        store.put(id, p);
        return p;
    }

    public synchronized void delete(long id) {
        if (store.remove(id) == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm có id = " + id);
        }
    }

    public List<Product> findAll() { return new ArrayList<>(store.values()); }

    public Product findById(long id) { return store.get(id); }

    private void checkAndGuard(Long selfId, String name, double price, int quantity) {
        List<String> errors = validate(name, price, quantity);
        if (!errors.isEmpty()) throw new ValidationException(errors);
        boolean dup = store.values().stream().anyMatch(p ->
                p.getName().equalsIgnoreCase(name.trim()) && (selfId == null || p.getId() != selfId));
        if (dup) throw new DuplicateProductException("Tên sản phẩm đã tồn tại");
    }
}
