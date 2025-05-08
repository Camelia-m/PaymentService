package org.psp.payment.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.psp.payment.dto.RouterResponse;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String channel;
    private String paymentNetwork;
    private String merchantId;
    private String transactionType;
    private BigDecimal amount;
    private String traceNumber;
    private String encryptedCardNumber;
    private LocalDateTime dateTime;
    private String transformError;
    private String authenticationError;
    private String routingError;
    private String responseCode;
    private String responseErrorMessage;
    private boolean isRequest;

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void updateRoutingInfo(Transaction transaction, RouterResponse routerResponse) {
        if(routerResponse.hasError()){
            transaction.setRoutingError(routerResponse.getErrorMessage());
        }else {
            transaction.setPaymentNetwork(routerResponse.getPaymentNetwork());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transaction that = (Transaction) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
