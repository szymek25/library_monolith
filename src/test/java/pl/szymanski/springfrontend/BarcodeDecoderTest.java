package pl.szymanski.springfrontend;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.szymanski.springfrontend.barcode.BarcodeDecoder;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.exceptions.BarcodeDecodingException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class BarcodeDecoderTest {

  @Test
  public void testDecodingUserBarcode() throws BarcodeDecodingException {
    final Integer value = BarcodeDecoder.decodeUserBarCode(ApplicationConstants.USER_BAR_CODE_PREFIX + "000000124");
    Assert.assertEquals(12,value.intValue());
  }

  @Test
  public void testDecodingBookEntryBarcode() throws BarcodeDecodingException{
    final Integer value = BarcodeDecoder.decodeBookBarCode(ApplicationConstants.BOOK_BAR_CODE_PREFIX + "000000024");
    Assert.assertEquals(2,value.intValue());
  }

  @Test(expected = BarcodeDecodingException.class)
  public void testValidationPrefix() throws BarcodeDecodingException {
    BarcodeDecoder.decodeUserBarCode(ApplicationConstants.BOOK_BAR_CODE_PREFIX + "000000124");
  }

  @Test(expected = BarcodeDecodingException.class)
  public void verifyBarcodeLength() throws BarcodeDecodingException {
    BarcodeDecoder.decodeBookBarCode(ApplicationConstants.BOOK_BAR_CODE_PREFIX + "0000024");
  }

  @Test(expected = BarcodeDecodingException.class)
  public void verifyIfBarcodeIsNumber() throws BarcodeDecodingException {
    BarcodeDecoder.decodeBookBarCode("test");
  }


}
