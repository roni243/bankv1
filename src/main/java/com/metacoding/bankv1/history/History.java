package com.metacoding.bankv1.history;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "history_tb")
@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer withdrawNumber; //1111 (FK) 중복가능 인덱스 안탐
    private Integer depositNumber; //2222 (FK)
    private Integer amount; //100원
    private Integer withdrawBalance; // 900원 (그 시점의 잔액, 현재잔액 아니고 이체되는 시점의 잔액)
    private Timestamp createdAt;

}
