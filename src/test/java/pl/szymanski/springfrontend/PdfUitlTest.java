package pl.szymanski.springfrontend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.szymanski.springfrontend.pdf.impl.GeneratePDFUtilImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PdfUitlTest {

  @InjectMocks
  private GeneratePDFUtilImpl generatePDFUtil;

  @Test
  public void testGenerationCheckSum()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = GeneratePDFUtilImpl.class.getDeclaredMethod("ean13CheckSum", String.class);
    method.setAccessible(true);
    final int result = (int) method.invoke(generatePDFUtil, "529800000006");

    Assert.assertEquals(8,result);
  }
}
