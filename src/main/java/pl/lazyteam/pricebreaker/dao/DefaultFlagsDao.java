package pl.lazyteam.pricebreaker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lazyteam.pricebreaker.entity.DefaultUserFlags;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface DefaultFlagsDao extends JpaRepository<DefaultUserFlags, Integer>
{
}
