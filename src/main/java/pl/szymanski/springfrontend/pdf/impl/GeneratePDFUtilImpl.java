package pl.szymanski.springfrontend.pdf.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dtos.BookDTO;
import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.pdf.GeneratePDFUtil;

@Component
public class GeneratePDFUtilImpl implements GeneratePDFUtil {

  private static final Logger logger = LoggerFactory.getLogger(GeneratePDFUtilImpl.class);
  private static final List<Integer> EAN13_CHECKSUMS = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  @Autowired
  private MessageSource messageSource;

  public ByteArrayInputStream generateBookLabel(final BookEntryDTO bookEntry,
      final Locale locale) {

    Document document = new Document();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {

      final PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
      document.open();

      buildBookInfoSection(bookEntry, locale, document);

      document.add(createBarcode(pdfWriter, bookEntry.getId(), ApplicationConstants.BOOK_BAR_CODE_PREFIX));

      document.close();
    } catch (DocumentException | IOException ex) {

      logger.error("Error occurred: {0}", ex);
    }

    return new ByteArrayInputStream(out.toByteArray());
  }

  @Override
  public ByteArrayInputStream generateUserLabel(UserDTO userDTO, Locale locale) {
    Document document = new Document();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {

      final PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
      document.open();

      buildUserInfoSection(userDTO, locale, document);

      document.add(createBarcode(pdfWriter, userDTO.getId(), ApplicationConstants.USER_BAR_CODE_PREFIX));

      document.close();
    } catch (DocumentException | IOException ex) {

      logger.error("Error occurred: {0}", ex);
    }

    return new ByteArrayInputStream(out.toByteArray());
  }

  protected void buildBookInfoSection(final BookEntryDTO bookEntry, final Locale locale,
      final Document document)
      throws DocumentException, IOException {
    Paragraph bookInfo = new Paragraph();
    Font font = getFont(13);

    final BookDTO book = bookEntry.getBook();
    final String bookTitle = messageSource
        .getMessage("book.entry.pdf.bookTitle", new Object[]{"Title"}, locale);
    bookInfo.add(new Paragraph(
        bookTitle + ": " + book.getTitle(),
        font));
    final String author = messageSource
        .getMessage("book.entry.pdf.author", new Object[]{"Author"}, locale);
    bookInfo.add(new Paragraph(author + ": " + book.getAuthor(), font));
    final String signature = messageSource
        .getMessage("book.entry.pdf.signature", new Object[]{"Signature"}, locale);
    bookInfo.add(new Paragraph(signature + ": " + bookEntry.getSignature(), font));
    final String inventoryNumber = messageSource
        .getMessage("book.entry.pdf.inventoryNumber", new Object[]{"Inventory Number"}, locale);
    bookInfo.add(new Paragraph(inventoryNumber + ": " + bookEntry.getInventoryNumber(), font));

    document.add(bookInfo);
  }

  protected void buildUserInfoSection(final UserDTO userDTO, final Locale locale,
      final Document document)
      throws DocumentException, IOException {
    Paragraph userInfo = new Paragraph();
    Font font = getFont(13);

    final String userName = messageSource
        .getMessage("users.edit.pdf.name", new Object[]{"Name"}, locale);
    userInfo.add(new Paragraph(
        userName + ": " + userDTO.getName(),
        font));
    final String userLastName = messageSource
        .getMessage("users.edit.pdf.lastName", new Object[]{"Last Name"}, locale);
    userInfo.add(new Paragraph(userLastName + ": " + userDTO.getLastName(), font));
    final String email = messageSource
        .getMessage("users.edit.pdf.email", new Object[]{"Email"}, locale);
    userInfo.add(new Paragraph(email + ": " + userDTO.getEmail(), font));

    document.add(userInfo);
  }

  protected Font getFont(final int size) throws DocumentException, IOException {
    //Get unicode font
    BaseFont bf = BaseFont.createFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    return new Font(bf, size);
  }

  protected Image createBarcode(final PdfWriter writer, final int code, final String barcodePrefix) {
    BarcodeEAN barcode = new BarcodeEAN();
    barcode.setCodeType(Barcode.EAN13);

    final String ean13Code = barcodePrefix + String.format("%08d", code);
    barcode.setCode(ean13Code + ean13CheckSum(ean13Code));
    return barcode
        .createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY);
  }

  protected int ean13CheckSum(final String input) {

    int numberToGenerateCheckSum = 0;
    int checkSum = 0;
    if (NumberUtils.isNumber(input) && input.length() == 12) {
      for (int i = 0; i <= 11; i = ++i) {
        final char c = input.charAt(i);
        if (((i + 1) % 2) == 0) {
          numberToGenerateCheckSum += Character.getNumericValue(c) * 3;
        } else {
          numberToGenerateCheckSum += Character.getNumericValue(c) * 1;

        }
      }

      final Iterator<Integer> iterator = EAN13_CHECKSUMS.iterator();
      while (iterator.hasNext()) {
        int candidate = iterator.next();
        if ((numberToGenerateCheckSum + candidate) % 10 == 0) {
          checkSum = candidate;
        }
      }
    }
    return checkSum;
  }
}
