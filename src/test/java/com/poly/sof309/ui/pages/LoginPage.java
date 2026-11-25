package com.poly.sof309.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * =========================================================================
 * [Y4] PAGE OBJECT MODEL – TRANG ĐĂNG NHẬP
 * Chỉ quản lý Locator và hành động trên trang Login.
 * Sử dụng WebDriverWait, không dùng Thread.sleep().
 * =========================================================================
 */
public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By txtUsername = By.id("username");
    private final By txtPassword = By.id("password");
    private final By btnLogin = By.id("btn-login");
    private final By lblErrorMessage = By.id("error-message");
    private final By lblSuccessMessage = By.id("success-message");
    private final By linkRegister = By.id("link-register");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtUsername));
    }

    public void enterUsername(String username) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtUsername));
        el.clear();
        el.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtPassword));
        el.clear();
        el.sendKeys(password);
    }

    public void clickLogin() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnLogin));
        btn.click();
    }

    /** Hành động nghiệp vụ: đăng nhập đầy đủ */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getErrorMessage() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(lblErrorMessage));
        return el.getText();
    }

    public boolean isErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(lblErrorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void goToRegister() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(linkRegister));
        link.click();
    }
}
