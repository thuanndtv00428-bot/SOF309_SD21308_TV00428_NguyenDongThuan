package com.poly.sof309.ui.tests;

import com.poly.sof309.ui.pages.LoginPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * =========================================================================
 * [Y4] KỊCH BẢN SELENIUM – ĐĂNG NHẬP (Page Object Model)
 * Hỗ trợ chế độ có giao diện (Local) và Headless (CI/CD).
 * =========================================================================
 */
public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private WebDriverWait wait;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String LOGIN_URL = BASE_URL + "/login";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // CI/CD (GitHub Actions) → headless
        if (Boolean.parseBoolean(System.getenv("CI"))) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // dùng explicit wait
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("TC-UI-01: Đăng nhập thành công với admin/123456 → chuyển /home")
    @Disabled("Bỏ @Disabled khi đã chạy Spring Boot (mvn spring-boot:run) ở local")
    public void testLoginSuccessUI() {
        loginPage.open(LOGIN_URL);
        loginPage.login("admin", "123456");

        wait.until(ExpectedConditions.urlContains("/home"));
        Assertions.assertTrue(
                driver.getCurrentUrl().contains("/home"),
                "Sau đăng nhập thành công phải chuyển về /home"
        );
    }

    @Test
    @DisplayName("TC-UI-02: Đăng nhập sai mật khẩu → hiện thông báo lỗi")
    @Disabled("Bỏ @Disabled khi đã chạy Spring Boot ở local")
    public void testLoginFailWrongPassword() {
        loginPage.open(LOGIN_URL);
        loginPage.login("admin", "wrongpass");

        Assertions.assertTrue(
                loginPage.isErrorDisplayed(),
                "Phải hiển thị thông báo lỗi khi sai mật khẩu"
        );
        String msg = loginPage.getErrorMessage();
        Assertions.assertTrue(
                msg.contains("Sai") || msg.toLowerCase().contains("mật khẩu"),
                "Nội dung lỗi phải liên quan đến sai thông tin đăng nhập"
        );
    }

    @Test
    @DisplayName("TC-UI-03: Đăng nhập để trống username → hiện lỗi")
    @Disabled("Bỏ @Disabled khi đã chạy Spring Boot ở local")
    public void testLoginEmptyUsername() {
        loginPage.open(LOGIN_URL);
        loginPage.login("", "123456");

        Assertions.assertTrue(loginPage.isErrorDisplayed());
    }
}
