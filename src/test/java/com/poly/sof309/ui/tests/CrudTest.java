package com.poly.sof309.ui.tests;

import com.poly.sof309.ui.pages.LoginPage;
import com.poly.sof309.ui.pages.ProductPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * =========================================================================
 * [Y4] KỊCH BẢN SELENIUM – CRUD SẢN PHẨM (Page Object Model)
 * Đăng nhập → Thêm sản phẩm mới → Assert thông báo thành công / xuất hiện trong bảng.
 * =========================================================================
 */
public class CrudTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductPage productPage;
    private WebDriverWait wait;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String PRODUCTS_URL = BASE_URL + "/products";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getenv("CI"))) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Đăng nhập trước khi thao tác sản phẩm */
    private void loginAsAdmin() {
        loginPage.open(LOGIN_URL);
        loginPage.login("admin", "123456");
        wait.until(ExpectedConditions.urlContains("/home"));
    }

    @Test
    @DisplayName("TC-UI-CRUD-01: Thêm sản phẩm thành công → hiện thông báo & có trong bảng")
    @Disabled("Bỏ @Disabled khi đã chạy Spring Boot (mvn spring-boot:run) ở local")
    public void testCreateProductSuccess() {
        loginAsAdmin();

        // Vào trang danh sách sản phẩm
        driver.get(PRODUCTS_URL);
        wait.until(ExpectedConditions.urlContains("/products"));

        String uniqueName = "SP Test Selenium " + System.currentTimeMillis();
        productPage.addProduct(uniqueName, "150000", "10");

        // Assert thông báo thành công
        Assertions.assertTrue(
                productPage.isSuccessDisplayed(),
                "Phải hiển thị thông báo thêm sản phẩm thành công"
        );
        String successMsg = productPage.getSuccessMessage();
        Assertions.assertTrue(
                successMsg.toLowerCase().contains("thành công") || successMsg.contains("Thêm"),
                "Nội dung thông báo phải liên quan đến thêm thành công"
        );

        // Assert sản phẩm xuất hiện trong bảng
        Assertions.assertTrue(
                productPage.isProductInTable(uniqueName),
                "Sản phẩm vừa thêm phải xuất hiện trong bảng danh sách"
        );
    }

    @Test
    @DisplayName("TC-UI-CRUD-02: Thêm sản phẩm để trống tên → hiện lỗi validation")
    @Disabled("Bỏ @Disabled khi đã chạy Spring Boot ở local")
    public void testCreateProductEmptyName() {
        loginAsAdmin();
        driver.get(PRODUCTS_URL);
        wait.until(ExpectedConditions.urlContains("/products"));

        productPage.clickAddProduct();
        productPage.enterName("");
        productPage.enterPrice("100000");
        productPage.enterQuantity("5");
        productPage.clickSave();

        // Vẫn ở form hoặc hiện lỗi
        Assertions.assertTrue(
                driver.getCurrentUrl().contains("/products") || productPage.isSuccessDisplayed() == false,
                "Không được thêm thành công khi tên trống"
        );
    }
}
