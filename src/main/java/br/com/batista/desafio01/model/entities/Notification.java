package br.com.batista.desafio01.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "Notification", uniqueConstraints = {})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pknotification")
    @SequenceGenerator( name = "seq_pknotification", sequenceName = "seqpknotification", allocationSize = 1)
    @Column(name = "idNotification")
    private long idNotification;

    @NotEmpty
    @Column(name = "title")
    private String title;

    @NotEmpty
    @Column(name = "message")
    private String message;

    @Column(name = "createDate")
    @NotNull
    Date createDate;

    @NotEmpty
    @Column(name = "userEmail")
    private String userEmail;

    @ManyToOne
    @JoinColumn(name="transaction", nullable = false)
    private Transaction transaction;

    @Column(name = "isSent")
    private boolean isSent = false;

    public Notification() {

    }

    public long getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(long idNotification) {
        this.idNotification = idNotification;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isSent() {
        return isSent;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
