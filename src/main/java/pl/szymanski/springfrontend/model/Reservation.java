package pl.szymanski.springfrontend.model;

import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import pl.szymanski.springfrontend.enums.ReservationStatus;

@Entity
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int reservationId;

  private String userEmail;

  private int bookEntryId;

  private String bookInventoryNumber;

  private String bookTitle;

  private String departmentName;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  private Timestamp reservationDate;

  private Integer queueNo;

  private Date predictedTimeForCollect;

  private Timestamp timeFromReservationIsReadyForCollect;

  public int getReservationId() {
    return reservationId;
  }

  public void setReservationId(int reservationId) {
    this.reservationId = reservationId;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public int getBookEntryId() {
    return bookEntryId;
  }

  public void setBookEntryId(int bookEntryId) {
    this.bookEntryId = bookEntryId;
  }

  public String getBookInventoryNumber() {
    return bookInventoryNumber;
  }

  public void setBookInventoryNumber(String bookInventoryNumber) {
    this.bookInventoryNumber = bookInventoryNumber;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public Timestamp getReservationDate() {
    return reservationDate;
  }

  public void setReservationDate(Timestamp reservationDate) {
    this.reservationDate = reservationDate;
  }

  public Integer getQueueNo() {
    return queueNo;
  }

  public void setQueueNo(Integer queueNo) {
    this.queueNo = queueNo;
  }

  public Date getPredictedTimeForCollect() {
    return predictedTimeForCollect;
  }

  public void setPredictedTimeForCollect(Date predictedTimeForCollect) {
    this.predictedTimeForCollect = predictedTimeForCollect;
  }

  public Timestamp getTimeFromReservationIsReadyForCollect() {
    return timeFromReservationIsReadyForCollect;
  }

  public void setTimeFromReservationIsReadyForCollect(
      Timestamp timeFromReservationIsReadyForCollect) {
    this.timeFromReservationIsReadyForCollect = timeFromReservationIsReadyForCollect;
  }
}
