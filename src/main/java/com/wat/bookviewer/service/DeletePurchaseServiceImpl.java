package com.wat.bookviewer.service;

import com.wat.bookviewer.domain.Purchase;
import com.wat.bookviewer.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by akielbiewski on 24.11.2016.
 */
@Transactional
@Service
public class DeletePurchaseServiceImpl implements DeletePurchaseService {

    @Inject
    PurchaseRepository purchaseRepository;

    @Override
    public void deletePurchase() {

        purchaseRepository.deleteAll();


    }
}
