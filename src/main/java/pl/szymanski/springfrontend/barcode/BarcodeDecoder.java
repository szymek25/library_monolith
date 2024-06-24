package pl.szymanski.springfrontend.barcode;

import org.apache.commons.lang3.math.NumberUtils;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.exceptions.BarcodeDecodingException;

public class BarcodeDecoder {

  public static Integer decodeUserBarCode(final String barcode) throws BarcodeDecodingException {
    validateBarcode(barcode, ApplicationConstants.USER_BAR_CODE_PREFIX);

    return Integer.parseInt(barcode.substring(4, 12));
  }

  public static Integer decodeBookBarCode(final String barcode) throws BarcodeDecodingException {
    validateBarcode(barcode, ApplicationConstants.BOOK_BAR_CODE_PREFIX);

    return Integer.parseInt(barcode.substring(4, 12));
  }

  private static void validateBarcode(final String barcode, final String barcodePrefix)
      throws BarcodeDecodingException {
    if (!NumberUtils.isNumber(barcode)) {
      throw new BarcodeDecodingException("Barcode isn`t a number");
    } else if (barcode.length() != 13) {
      throw new BarcodeDecodingException("Barcode hasn`t proper length, must have 13 digits");
    } else if (!hasValidPrefix(barcode, barcodePrefix)) {
      throw new BarcodeDecodingException("Barcode hasn`t proper application prefix");
    }
  }

  private static boolean hasValidPrefix(final String barcode, String barcodePrefix) {
    final String prefix = barcode.substring(0, 4);
    return barcodePrefix
        .equals(prefix);
  }
}
