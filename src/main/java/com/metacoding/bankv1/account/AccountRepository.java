package com.metacoding.bankv1.account;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AccountRepository {
    private final EntityManager em;

    // 재사용 하려고
    public void updateByNumber(int balance, String password, int number) {
        Query query = em.createNativeQuery("update account_tb set balance = ?, password = ? where number = ?"); // 변경할 수 있는 것 다 적는게 편하다
        query.setParameter(1, balance);
        query.setParameter(2, password);
        query.setParameter(3, number);
        query.executeUpdate();
    }

    // 유저아이디 - 세션 - 컨트롤러
    public void save(Integer number, String password, Integer balance, int userId) {
        Query query = em.createNativeQuery("insert into account_tb(number, password, balance, user_id, created_at) values (?, ?, ?, ?, now())");
        query.setParameter(1, number);
        query.setParameter(2, password);
        query.setParameter(3, balance);
        query.setParameter(4, userId);
        query.executeUpdate();
    }

    public List<Account> findAllByUserId(Integer userId) {
        Query query = em.createNativeQuery("select * from account_tb where user_id = ? order by created_at desc", Account.class);
        query.setParameter(1, userId);
        return query.getResultList();

    }

    //number-계좌번호 - 단건조회
    public Account findByNumber(Integer number) {
        Query query = em.createNativeQuery("select * from account_tb where number = ?", Account.class);
        query.setParameter(1, number);

        try {
            return (Account) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }


}
