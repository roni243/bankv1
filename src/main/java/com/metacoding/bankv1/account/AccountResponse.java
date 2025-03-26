package com.metacoding.bankv1.account;

import com.metacoding.bankv1.user.User;
import lombok.Data;

import java.util.List;

public class AccountResponse {

    // {{#models.histories}}
    //{{createdAt}}
    //{{withdrawNumber}}
    // {{/models.histories}}

    // 화면이랑 똑같이 생겼다!
    @Data
    public static class DetailDTO {
        private String fullname; // 세션의값
        private int number;
        private int balance;
        private List<HistoryDTO> histories;

        public DetailDTO(User sessionUser, Account account, List<HistoryDTO> histories) {
            this.fullname = fullname;
            this.number = number;
            this.balance = balance;
            this.histories = histories;
        }
    }

    // 두번째 쿼리
    @Data
    public static class HistoryDTO {
        private String createdAt;
        private int withdrawNumber;
        private int depositNumber;
        private int amount;
        private int balance;
        private String type;
    }
}