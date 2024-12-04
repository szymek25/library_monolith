package pl.szymanski.springfrontend.constants;

public class ApplicationConstants {

  public static final String USER_ROLE_NAME = "ROLE_USER";
  public static final String MANAGER_ROLE_NAME = "ROLE_MANAGER";
  public static final String EMPLOYEE_ROLE_NAME = "ROLE_EMPLOYEE";
  public static final Integer DEFAULT_PAGE_SIZE = 10;
  public static final String BOOK_BAR_CODE_PREFIX = "7869";
  public static final String USER_BAR_CODE_PREFIX = "5298";
  public static final Integer DAYS_FOR_COLLECT_BOOK_AFTER_RESERVATION = 7;
  public static final Integer DAYS_FOR_COLLECT_BOOK_AFTER_ORDER = 3;

  public class KeyCloak {
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String ADDRESS_LINE_1 = "addressLine1";
    public static final String PHONE = "phone";
    public static final String BIRTHDATE = "dayOfBirth";
    public static final String TOWN = "town";
    public static final String POSTAL_CODE = "postalCode";
    public static final String UUID = "sub";
  }

  public class UserService {
    public static final String LIBRARY_CUSTOMERS_ENDPOINT = "/users/customers";
    public static final String LIBRARY_EMPLOYEES_ENDPOINT = "/users/employees";
    public static final String ALL_USERS_ENDPOINT = "/users";
    public static final String CURRENT_PAGE_PARAM = "currentPage";
    public static final String PAGE_SIZE_PARAM = "pageSize";
    public static final String ADD_USER_ENDPOINT = "/users/add";
  }
}
