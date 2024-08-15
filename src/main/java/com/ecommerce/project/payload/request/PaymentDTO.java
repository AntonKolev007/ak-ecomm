package com.ecommerce.project.payload.request;

import java.io.Serializable;
import java.util.Objects;

public class PaymentDTO implements Serializable {
    private Long paymentId;
    private String paymentMethod;
    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    public PaymentDTO() {
    }

    public PaymentDTO(Long paymentId,
                      String paymentMethod,
                      String pgPaymentId,
                      String pgStatus,
                      String pgResponseMessage,
                      String pgName) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPgPaymentId() {
        return pgPaymentId;
    }

    public void setPgPaymentId(String pgPaymentId) {
        this.pgPaymentId = pgPaymentId;
    }

    public String getPgStatus() {
        return pgStatus;
    }

    public void setPgStatus(String pgStatus) {
        this.pgStatus = pgStatus;
    }

    public String getPgResponseMessage() {
        return pgResponseMessage;
    }

    public void setPgResponseMessage(String pgResponseMessage) {
        this.pgResponseMessage = pgResponseMessage;
    }

    public String getPgName() {
        return pgName;
    }

    public void setPgName(String pgName) {
        this.pgName = pgName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDTO that = (PaymentDTO) o;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(pgPaymentId, that.pgPaymentId) && Objects.equals(pgStatus, that.pgStatus) && Objects.equals(pgResponseMessage, that.pgResponseMessage) && Objects.equals(pgName, that.pgName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
    }
}
