package pl.lazyteam.pricebreaker.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.entity.VerificationToken;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * DAO that represnts user tokens.
 */
@Transactional
@Repository
public interface TokenDao extends CrudRepository<VerificationToken, Integer>
{
    VerificationToken findVerificationTokenByToken(String token);

    VerificationToken findVerificationTokenByUser(User user);


    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

}
