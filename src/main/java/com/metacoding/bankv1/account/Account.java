package com.metacoding.bankv1.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "account_tb")
@Entity
public class Account {

    @Id // id 없다 auto increment 안됨
    private Integer number;
    private String password;
    private Integer balance; // 잔액
    private Integer userId; //FK
    private Timestamp createdAt;
}
