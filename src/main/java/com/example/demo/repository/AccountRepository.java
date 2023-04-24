package com.example.demo.repository;

import com.example.demo.domain.model.Account;
import com.example.demo.request.admin.GetAccountsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmailAndUsernameNotNull(String email);

    Boolean existsByCardIdAndUsernameNotNull(String cardId);

    Account getAccountByEmailAndUsername(String email, String username);

    @Query(value = "SELECT * FROM account WHERE role_id = 5 AND `address` LIKE CONCAT('%',:location,'%')", nativeQuery = true)
    List<Account> findShop(String location);


    @Query(value = "SELECT * FROM account WHERE `username` IS NOT NULL AND (name LIKE CONCAT('%',:#{#re.keyword},'%') OR `username` LIKE CONCAT('%',:#{#re.keyword},'%') OR `email` LIKE CONCAT('%',:#{#re.keyword},'%'))", nativeQuery = true)
    List<Account> getByUsernameNotNull(GetAccountsRequest re);

    Account getByIdAndIsBLockFalse(Long id);

    Account getByEmail(String email);

    Account getByCardId(String cardId);
}
