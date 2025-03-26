package com.metacoding.bankv1.account;

import com.metacoding.bankv1.account.history.HistoryRepository;
import com.metacoding.bankv1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final HistoryRepository historyRepository;


    @Transactional
    public void 계좌생성(AccountRequest.SaveDTO saveDTO, int userId) {
        accountRepository.save(saveDTO.getNumber(), saveDTO.getPassword(), saveDTO.getBalance(), userId);
    }

    public List<Account> 나의계좌목록(Integer userId) {
        return accountRepository.findAllByUserId(userId);
    }

    @Transactional
    public void 계좌이체(AccountRequest.TransferDTO transferDTO, int userId) {
        //1. 출금 계좌 조회, 없으면 RunTimeException
        Account withdrawAccount = accountRepository.findByNumber(transferDTO.getWithdrawNumber());
        if (withdrawAccount == null) throw new RuntimeException("출금계좌가 존재하지 않습니다");

        //2. 입금 계좌 조회, 없으면 RunTimeException
        Account depositAccount = accountRepository.findByNumber(transferDTO.getDepositNumber());
        if (depositAccount == null) throw new RuntimeException("입금계좌가 존재하지 않습니다");

        //3. 출금 계좌의 잔액 조회 - 출금계좌잔액이 이체금액보다 작다
        if (withdrawAccount.getBalance() < transferDTO.getAmount()) {
            throw new RuntimeException("출금 계좌의 잔액 : " + withdrawAccount.getBalance() + ", 이체하려는 금액 : " + transferDTO.getAmount());
        }

        //4. 출금 비밀번호 확인해서 동일한지 체크 - 비밀번호 동일하지 않으면 터트림
        // 출금 비밀번호 확인해서 동일한지 체크
        // 출금 계좌와 로그인한 유저가 동일 인물인지 권한 체크 (출금계좌 주인이 맞는지 확인 - 로그인한 유저가)
        if (!(withdrawAccount.getPassword().equals(transferDTO.getWithdrawPassword()))) {
            throw new RuntimeException("출금 계좌 비밀번호가 틀렸습니다");
        }

        //5. 출금 계좌의 잔액 조회
        if (!withdrawAccount.getUserId().equals(userId)) {
            throw new RuntimeException("출금 계좌의 권한이 없습니다");
        }


        //6. Account Update 입금계좌 (핵심로직) - 위에서 둘 다 조회됨
        int withdrawBalance = withdrawAccount.getBalance();
        withdrawBalance = withdrawBalance + transferDTO.getAmount();
        accountRepository.updateByNumber(withdrawBalance, withdrawAccount.getPassword(), withdrawAccount.getNumber());
        //dto꺼 아님, 위험함 - 자기것

        int depositBalance = depositAccount.getBalance();
        depositBalance = depositBalance + transferDTO.getAmount();
        accountRepository.updateByNumber(depositBalance, depositAccount.getPassword(), depositAccount.getNumber());

        //7. History Save (핵심로직) - 신뢰할 수 있는 데이터 위에서 검증
        historyRepository.save(transferDTO.getWithdrawNumber(), transferDTO.getDepositNumber(), transferDTO.getAmount(), withdrawBalance, depositBalance);


    }

    public void 계좌상세보기(int number, String type, User sessionUser) {
        //1. 계좌 존재 확인
        Account account = accountRepository.findByNumber(number);
        if (account == null) throw new RuntimeException("계좌가 존재하지 않습니다");

        //2. 계좌 주인 확인
        if (!(account.getUserId().equals(sessionUser.getId()))) {
            throw new RuntimeException("계좌의 권한이 없습니다.");
        }

        //3. 조회해서 주면 됨
        AccountResponse.DetailDTO responseDTO = new AccountResponse.DetailDTO(
                sessionUser,
                account,
                null
        );

    }
}
