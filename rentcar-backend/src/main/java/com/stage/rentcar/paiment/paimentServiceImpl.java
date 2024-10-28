package com.stage.rentcar.paiment;


import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.stage.rentcar.reservation.Reservation;
import com.stage.rentcar.reservation.ReservationRepository;
import com.stage.rentcar.reservation.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class paimentServiceImpl implements PaimentService {
   //paypal
    @Autowired
    private APIContext apiContext;

    @Override
    public Payment createPayment(
            Double total ,
            String currency,
            String method,
            String intent ,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency),"%.2f",total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

       //payment infos to send to paypal
        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment() ;
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);

    }

//execute the payment when we confirm the payment when we click on confirm btn
   @Override
    public Payment executePayment (
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution= new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext,paymentExecution);
    }






}
