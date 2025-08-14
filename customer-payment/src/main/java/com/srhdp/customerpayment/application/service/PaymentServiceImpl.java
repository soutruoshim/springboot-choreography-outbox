package com.srhdp.customerpayment.application.service;

import com.srhdp.choreographycommon.common.events.payment.PaymentStatus;
import com.srhdp.choreographycommon.common.util.DuplicateEventValidator;
import com.srhdp.customerpayment.application.entity.Customer;
import com.srhdp.customerpayment.application.entity.CustomerPayment;
import com.srhdp.customerpayment.application.mapper.EntityDtoMapper;
import com.srhdp.customerpayment.application.repository.CustomerRepository;
import com.srhdp.customerpayment.application.repository.PaymentRepository;
import com.srhdp.customerpayment.common.dto.PaymentDto;
import com.srhdp.customerpayment.common.dto.PaymentProcessRequest;
import com.srhdp.customerpayment.common.exception.CustomerNotFoundException;
import com.srhdp.customerpayment.common.exception.InsufficientBalanceException;
import com.srhdp.customerpayment.common.service.PaymentService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private static final Mono<Customer> CUSTOMER_NOT_FOUND = Mono.error(new CustomerNotFoundException());
    private static final Mono<Customer> INSUFFICIENT_BALANCE = Mono.error(new InsufficientBalanceException());
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Mono<PaymentDto> process(PaymentProcessRequest request) {
        return DuplicateEventValidator.validate(
                        this.paymentRepository.existsByOrderId(request.orderId()),
                        this.customerRepository.findById(request.customerId())
                )
                .switchIfEmpty(CUSTOMER_NOT_FOUND)
                .filter(c -> c.getBalance() >= request.amount())
                .switchIfEmpty(INSUFFICIENT_BALANCE)
                .flatMap(c -> this.deductPayment(c, request))
                .doOnNext(dto -> log.info("payment processed for {}", dto.orderId()));
    }

    private Mono<PaymentDto> deductPayment(Customer customer, PaymentProcessRequest request) {
        var customerPayment = EntityDtoMapper.toCustomerPayment(request);
        customer.setBalance(customer.getBalance() - request.amount());
        customerPayment.setStatus(PaymentStatus.DEDUCTED);
        return this.customerRepository.save(customer)
                .then(this.paymentRepository.save(customerPayment))
                .map(EntityDtoMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<PaymentDto> refund(UUID orderId) {
        return this.paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.DEDUCTED)
                .zipWhen(cp -> this.customerRepository.findById(cp.getCustomerId()))
                .flatMap(t -> this.refundPayment(t.getT1(), t.getT2()))
                .doOnNext(dto -> log.info("refunded amount {} for {}", dto.amount(), dto.orderId()));
    }

    private Mono<PaymentDto> refundPayment(CustomerPayment customerPayment, Customer customer) {
        customer.setBalance(customer.getBalance() + customerPayment.getAmount());
        customerPayment.setStatus(PaymentStatus.REFUNDED);
        return this.customerRepository.save(customer)
                .then(this.paymentRepository.save(customerPayment))
                .map(EntityDtoMapper::toDto);
    }

}
