package pl.lazyteam.pricebreaker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lazyteam.pricebreaker.entity.DefaultUserFlags;
import pl.lazyteam.pricebreaker.entity.ProductFlags;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ProductFlagsDao extends JpaRepository<ProductFlags, Long>
{
}
