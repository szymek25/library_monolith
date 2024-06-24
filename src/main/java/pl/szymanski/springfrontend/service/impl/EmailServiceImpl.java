package pl.szymanski.springfrontend.service.impl;

import java.time.LocalDate;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.model.BookOrder;
import pl.szymanski.springfrontend.model.Reservation;
import pl.szymanski.springfrontend.service.EmailService;
import pl.szymanski.springfrontend.service.MessageService;

@Service
public class EmailServiceImpl implements EmailService {

  private static Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Autowired
  private JavaMailSender emailSender;

  @Autowired
  private MessageService messageService;

  @Override
  public void notifyFirstUserFromReservationQueueAboutReadyToCollect(
      final Reservation reservation) {
    final String subject = messageService
        .getMessageForCurrentLocale("email.reservations.readyForCollect.subject");
    sendEmail(reservation.getUserEmail(), subject, createBodyForReadyForCollectEmail(reservation));
  }

  @Override
  public void notifyAboutChangePredictedDateForCollect(final Reservation reservation) {
    final String subject = messageService
        .getMessageForCurrentLocale("email.reservations.changePredictedDate.subject");
    sendEmail(reservation.getUserEmail(), subject,
        createBodyForChangePredictedDateForCollect(reservation));
  }

  @Override
  public void cancelReservationNotification(final Reservation reservation) {
    final String subject = messageService
        .getMessageForCurrentLocale("email.reservations.cancelReservation.subject");
    sendEmail(reservation.getUserEmail(), subject, createBodyForCancelReservation(reservation));
  }

  @Override
  public void notifyAboutOrderIsReadyForCollect(final BookOrder bookOrder) {
    final String subject = messageService
        .getMessageForCurrentLocale("email.bookOrder.orderReady.subject");
    sendEmail(bookOrder.getUserEmail(), subject, createBodyForOrderReadyForCollectEmail(bookOrder));
  }

  @Override
  public void cancelOrderNotification(BookOrder bookOrder) {
    final String subject = messageService
        .getMessageForCurrentLocale("email.bookOrder.cancelOrder.subject");
    sendEmail(bookOrder.getUserEmail(), subject, createBodyForCancelOrderEmail(bookOrder));

  }

  private String createBodyForCancelOrderEmail(final BookOrder bookOrder) {
    final StringBuilder body = new StringBuilder();
    body.append("<html>");
    body.append("<body>");
    body.append("<h3>");
    body.append(
        messageService.getMessageForCurrentLocale("email.bookOrder.cancelOrder.header"));
    body.append("</h3>");
    body.append("<div>");
    body.append(
        "<p>" + messageService
            .getMessageForCurrentLocale("email.bookOrder.cancelOrder.info")
            + "</p>");

    body.append("<p>" + messageService
        .getMessageForCurrentLocale("email.bookOrder.cancelOrder.details.info")
        + "</p>");
    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.bookOrder.cancelOrder.details.bookTitle"));
    body.append(" " + bookOrder.getBookTitle());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.bookOrder.cancelOrder.details.bookInventoryNumber"));
    body.append(" " + bookOrder.getBookInventoryNumber());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.bookOrder.cancelOrder.details.departmentName"));
    body.append(" " + bookOrder.getDepartmentName());
    body.append("</p>");

    body.append(
        "<b>" + messageService
            .getMessageForCurrentLocale("email.bookOrder.cancelOrder.end")
            + "</b>");

    body.append("</div>");
    body.append("</body>");
    body.append("</html>");

    return body.toString();

  }

  private String createBodyForOrderReadyForCollectEmail(final BookOrder bookOrder) {
    final StringBuilder body = new StringBuilder();
    body.append("<html>");
    body.append("<body>");
    body.append("<h3>");
    body.append(
        messageService.getMessageForCurrentLocale("email.bookOrder.orderReady.header"));
    body.append("</h3>");

    body.append("<div>");
    body.append(
        "<p>" + messageService.getMessageForCurrentLocale("email.bookOrder.orderReady.info")
            + "</p>");
    body.append("<p>" + messageService
        .getMessageForCurrentLocale("email.bookOrder.orderReady.details.info") + "</p>");
    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.bookOrder.orderReady.details.bookTitle"));
    body.append(" " + bookOrder.getBookTitle());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.bookOrder.orderReady.details.inventoryNumber"));
    body.append(" " + bookOrder.getBookInventoryNumber());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.bookOrder.orderReady.details.departmentName"));
    body.append(" " + bookOrder.getDepartmentName());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.bookOrder.orderReady.details.timeForCollect"));
    final LocalDate date = LocalDate.now();
    body.append(" " + date.plusDays(ApplicationConstants.DAYS_FOR_COLLECT_BOOK_AFTER_ORDER));
    body.append("</p>");

    body.append("<p>" + messageService
        .getMessageForCurrentLocale("email.bookOrder.orderReady.summary") + "</p>");

    body.append(
        "<b>" + messageService.getMessageForCurrentLocale("email.bookOrder.orderReady.end")
            + "</b>");

    body.append("</div>");
    body.append("</body>");
    body.append("</html>");
    return body.toString();
  }

  protected String createBodyForCancelReservation(final Reservation reservation) {
    final StringBuilder body = new StringBuilder();
    body.append("<html>");
    body.append("<body>");
    body.append("<h3>");
    body.append(
        messageService.getMessageForCurrentLocale("email.reservations.cancelReservation.header"));
    body.append("</h3>");
    body.append("<div>");
    body.append(
        "<p>" + messageService
            .getMessageForCurrentLocale("email.reservations.cancelReservation.info")
            + "</p>");

    body.append("<p>" + messageService
        .getMessageForCurrentLocale("email.reservations.cancelReservation.details.info")
        + "</p>");
    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.reservations.cancelReservation.details.bookTitle"));
    body.append(" " + reservation.getBookTitle());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.reservations.cancelReservation.details.bookInventoryNumber"));
    body.append(" " + reservation.getBookInventoryNumber());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.reservations.cancelReservation.details.departmentName"));
    body.append(" " + reservation.getDepartmentName());
    body.append("</p>");

    body.append(
        "<b>" + messageService
            .getMessageForCurrentLocale("email.reservations.cancelReservation.end")
            + "</b>");

    body.append("</div>");
    body.append("</body>");
    body.append("</html>");

    return body.toString();
  }

  protected String createBodyForChangePredictedDateForCollect(final Reservation reservation) {
    final StringBuilder body = new StringBuilder();
    body.append("<html>");
    body.append("<body>");
    body.append("<h3>");
    body.append(
        messageService.getMessageForCurrentLocale("email.reservations.changePredictedDate.header"));
    body.append("</h3>");
    body.append("<div>");
    body.append(
        "<p>" + messageService
            .getMessageForCurrentLocale("email.reservations.changePredictedDate.info")
            + "</p>");

    body.append("<p>" + messageService
        .getMessageForCurrentLocale("email.reservations.changePredictedDate.details.info")
        + "</p>");
    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.reservations.changePredictedDate.details.bookTitle"));
    body.append(" " + reservation.getBookTitle());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.reservations.changePredictedDate.details.bookInventoryNumber"));
    body.append(" " + reservation.getBookInventoryNumber());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.reservations.changePredictedDate.details.departmentName"));
    body.append(" " + reservation.getDepartmentName());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.reservations.changePredictedDate.details.predictedDate"));
    body.append(" " + reservation.getPredictedTimeForCollect());
    body.append("</p>");

    body.append(
        "<b>" + messageService
            .getMessageForCurrentLocale("email.reservations.changePredictedDate.end")
            + "</b>");

    body.append("</div>");
    body.append("</body>");
    body.append("</html>");

    return body.toString();
  }

  protected String createBodyForReadyForCollectEmail(final Reservation reservation) {
    final StringBuilder body = new StringBuilder();
    body.append("<html>");
    body.append("<body>");
    body.append("<h3>");
    body.append(
        messageService.getMessageForCurrentLocale("email.reservations.readyForCollect.header"));
    body.append("</h3>");

    body.append("<div>");
    body.append(
        "<p>" + messageService.getMessageForCurrentLocale("email.reservations.readyForCollect.info")
            + "</p>");
    body.append("<p>" + messageService
        .getMessageForCurrentLocale("email.reservations.readyForCollect.details.info") + "</p>");
    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.reservations.readyForCollect.details.bookTitle"));
    body.append(" " + reservation.getBookTitle());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale(
            "email.reservations.readyForCollect.details.bookInventoryNumber"));
    body.append(" " + reservation.getBookInventoryNumber());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.reservations.readyForCollect.details.departmentName"));
    body.append(" " + reservation.getDepartmentName());
    body.append("</p>");

    body.append("<p>");
    body.append(messageService
        .getMessageForCurrentLocale("email.reservations.readyForCollect.details.timeForCollect"));
    final LocalDate date = LocalDate.now();
    body.append(" " + date.plusDays(ApplicationConstants.DAYS_FOR_COLLECT_BOOK_AFTER_RESERVATION));
    body.append("</p>");

    body.append("<p>" + messageService
        .getMessageForCurrentLocale("email.reservations.readyForCollect.summary") + "</p>");
    body.append(
        "<b>" + messageService.getMessageForCurrentLocale("email.reservations.readyForCollect.end")
            + "</b>");

    body.append("</div>");
    body.append("</body>");
    body.append("</html>");

    return body.toString();
  }

  protected void sendEmail(final String recipent, final String subject, final String body) {
    MimeMessagePreparator preparator = mimeMessage -> {

      mimeMessage.setRecipient(Message.RecipientType.TO,
          new InternetAddress(recipent));
      mimeMessage.setSubject(subject);
      mimeMessage
          .setContent(body, "text/html; charset=utf-8");
    };

    try {
      emailSender.send(preparator);
    } catch (MailException e) {
      LOG.error(e.getMessage());
    }
  }
}
