package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findByPriceLessThanOrderByPrice(double maxPrice);
    List<Product> findByPriceGreaterThanEqualOrderByPrice(double minPrice);
    List<Product> findByPriceBetweenOrderByPrice(double minPrice, double maxPrice);
    // END
}
