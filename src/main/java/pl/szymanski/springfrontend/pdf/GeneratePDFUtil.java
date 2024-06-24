package pl.szymanski.springfrontend.pdf;

import java.io.ByteArrayInputStream;
import java.util.Locale;
import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.dtos.UserDTO;

public interface GeneratePDFUtil {

  ByteArrayInputStream generateBookLabel(final BookEntryDTO bookEntry, Locale locale);

  ByteArrayInputStream generateUserLabel(final UserDTO userDTO, Locale locale);
}
