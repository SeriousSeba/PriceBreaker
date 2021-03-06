package pl.lazyteam.pricebreaker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lazyteam.pricebreaker.entity.User;

import javax.transaction.Transactional;
import java.util.List;

/**
 * DAO that represnts user. It alows to find, add and delete users.
 */
@Transactional
@Repository
public interface UserDao extends JpaRepository<User, Integer>
{
    List<User> findAll();

    List<User> findByUsername(String username);

    List<User> findByEmail(String email);

    void deleteByUsername(String username);
}
