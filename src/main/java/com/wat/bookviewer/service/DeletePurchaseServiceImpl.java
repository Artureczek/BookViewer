package com.wat.bookviewer.service;

import java.time.temporal.ChronoUnit;
import com.wat.bookviewer.domain.Purchase;
import com.wat.bookviewer.domain.User;
import com.wat.bookviewer.repository.PurchaseRepository;
import com.wat.bookviewer.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by akielbiewski on 24.11.2016.
 */
@Transactional
@Component
public class DeletePurchaseServiceImpl implements DeletePurchaseService {

    @Inject
    PurchaseRepository purchaseRepository;

    @Inject
    MailService mailService;

    @Inject
    UserRepository userRepository;

    @Override
    @Scheduled(cron="0 0 0 * * ?")
    //@Scheduled(fixedRate = 4000)
    public void checkIfPurchaseIsPastTermOrDayBefore() {

        List<Purchase> purchaseList = purchaseRepository.findAll();
        purchaseList.stream().forEach(e->{


            LocalDate term = e.getDate().plusDays(e.getValue().longValue());
            long daysBetween = DAYS.between(LocalDate.now(),e.getDate().plusDays(e.getValue().longValue()));

            if(term.isBefore(LocalDate.now())){
                e.setStatus("Nieaktywny");
            }
            else if(term.isAfter(LocalDate.now()) && daysBetween == 1){
                Optional<User> user = userRepository.findOneById(e.getUserId());
                mailService.sendPurchaseNearingLimitEmail(user.get() ,"http://myhost:28080/");
            }


        });

    }
}
