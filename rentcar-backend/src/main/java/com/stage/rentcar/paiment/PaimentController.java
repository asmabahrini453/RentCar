package com.stage.rentcar.paiment;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.stage.rentcar.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("paiment")
@RequiredArgsConstructor
@Slf4j
public class PaimentController {

    @Autowired
    private final PaimentService paimentService;

    @Autowired
    private final ReservationRepository reservationRepository;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<PaimentResponse> createPaiment(
            @RequestParam("montantTotal") Double montantTotal,
            @RequestParam("currency") String currency) {
        try {
            // Define the cancel and success URLs
            String cancelUrl = "http://localhost:4201/reservation/pay/cancel";
            String successUrl = "http://localhost:4201/reservation/pay/success";

            // Create the PayPal payment with the given montantTotal and currency
            Payment payment = paimentService.createPayment(
                    montantTotal,
                    currency,  // Use the currency passed as a parameter
                    "paypal",
                    "sale",
                    "description de paiement",
                    cancelUrl,
                    successUrl
            );

            // Find the approval URL
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    PaimentResponse response = new PaimentResponse();
                    response.setApprovalUrl(links.getHref());
                    response.setCancelUrl(cancelUrl);
                    response.setSuccessUrl(successUrl);
                    return ResponseEntity.ok(response);
                }
            }

        } catch (PayPalRESTException e) {
            log.error("Error creating payment: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/success")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<String> paimentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ){
        try {
            Payment payment = paimentService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok("Payment successful");
            }

        } catch (PayPalRESTException e) {
            log.error("Error executing payment: ", e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> paimentCancel(){
        return ResponseEntity.ok("Payment cancelled");
    }

    @GetMapping("/error")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> paimentError(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during payment");
    }
}
