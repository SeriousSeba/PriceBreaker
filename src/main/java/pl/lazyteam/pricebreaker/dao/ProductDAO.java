package pl.lazyteam.pricebreaker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

import javax.transaction.Transactional;

/**
 * DAO that represnts products.
 */
@Transactional
@Repository
public interface ProductDAO extends JpaRepository<ProductInfo, Long> {
}
