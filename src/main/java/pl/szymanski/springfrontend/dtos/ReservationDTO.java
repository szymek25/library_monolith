package pl.szymanski.springfrontend.dtos;

import java.sql.Date;
import java.sql.Timestamp;
import pl.szymanski.springfrontend.enums.ReservationStatus;
import pl.szymanski.springfrontend.model.Reservation;

public class ReservationDTO {

  private int id;

  private String userEmail;

  private String bookTitle;

  private String bookInventoryNumber;

  private ReservationStatus status;

  private Timestamp reservationDate;

  private boolean currentEmployeeHasPermissionToBook;

  private String departmentName;

  private Integer queueNo;

  private Date predictedTimeForCollect;

  public ReservationDTO(final Reservation reservation) {
    this.id = reservation.getReservationId();
    this.userEmail = reservation.getUserEmail();
    this.bookInventoryNumber = reservation.getBookInventoryNumber();
    this.bookTitle = reservation.getBookTitle();
    this.reservationDate = reservation.getReservationDate();
    this.status = reservation.getStatus();
    this.departmentName = reservation.getDepartmentName();
    this.queueNo = reservation.getQueueNo();
    this.predictedTimeForCollect = reservation.getPredictedTimeForCollect();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public String getBookInventoryNumber() {
    return bookInventoryNumber;
  }

  public void setBookInventoryNumber(String bookInventoryNumber) {
    this.bookInventoryNumber = bookInventoryNumber;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public boolean isCurrentEmployeeHasPermissionToBook() {
    return currentEmployeeHasPermissionToBook;
  }

  public void setCurrentEmployeeHasPermissionToBook(boolean currentEmployeeHasPermissionToBook) {
    this.currentEmployeeHasPermissionToBook = currentEmployeeHasPermissionToBook;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
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
}
