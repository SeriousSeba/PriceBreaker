package pl.lazyteam.pricebreaker.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lazyteam.pricebreaker.entity.User;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface UserDao extends CrudRepository<User, Integer>
{
    List<User> findAll();

    List<User> findByUsername(String username);

    List<User> findByEmail(String email);

    void deleteByUsername(String username);
}
