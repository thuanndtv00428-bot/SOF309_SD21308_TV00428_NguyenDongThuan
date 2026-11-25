package com.poly.sof309.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * =========================================================================
 * [Y4] PAGE OBJECT MODEL – TRANG ĐĂNG KÝ
 * Quản lý Locator và hành động trên trang Register.
 * =========================================================================
 */
public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By txtUsername = By.id("username");
    private final By txtEmail = By.id("email");
    private final By txtPassword = By.id("password");
    private final By txtConfirmPassword = By.id("confirmPassword");
    private final By btnRegister = By.id("btn-register");
    private final By lblErrorMessage = By.id("error-message");

    public RegisterPage(WebDriver driver) {
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

    public void enterEmail(String email) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtEmail));
        el.clear();
        el.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtPassword));
        el.clear();
        el.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtConfirmPassword));
        el.clear();
        el.sendKeys(confirmPassword);
    }

    public void clickRegister() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnRegister));
        btn.click();
    }

    /** Hành động nghiệp vụ: đăng ký đầy đủ */
    public void register(String username, String email, String password, String confirmPassword) {
        enterUsername(username);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickRegister();
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
}
