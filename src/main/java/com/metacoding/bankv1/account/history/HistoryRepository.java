package com.metacoding.bankv1.account.history;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor // final을 생성자로 만들어준다.
@Repository
public class HistoryRepository {
    private final EntityManager em;

    public void save(int withdrawNumber, int depositNumber, int amount, int withdrawBalance) {
        Query query = em.createNativeQuery("insert into history_tb(withdraw_number, deposit_number, amount, withdraw_balance, created_at) values (?, ?, ?, ?, now())");
        //변경하려면 서비스에서변경, 멱등, 들어간게 그대로, 변수명 정해져 있다.
        query.setParameter(1, withdrawNumber);
        query.setParameter(2, depositNumber);
        query.setParameter(3, amount);
        query.setParameter(4, withdrawBalance);
        query.executeUpdate();
    }
}
