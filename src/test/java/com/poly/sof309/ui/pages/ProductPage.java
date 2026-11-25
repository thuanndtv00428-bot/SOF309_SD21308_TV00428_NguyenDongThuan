package com.poly.sof309.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * =========================================================================
 * [Y4] PAGE OBJECT MODEL – TRANG SẢN PHẨM (LIST + FORM)
 * Quản lý Locator danh sách, form thêm/sửa và các hành động CRUD.
 * =========================================================================
 */
public class ProductPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Trang danh sách
    private final By btnAdd = By.id("btn-add");
    private final By productTable = By.id("product-table");
    private final By successMessage = By.id("success-message");
    private final By errorMessage = By.id("error-message");
    private final By productNames = By.cssSelector("#product-table .p-name");

    // Form thêm / sửa
    private final By txtName = By.id("name");
    private final By txtPrice = By.id("price");
    private final By txtQuantity = By.id("quantity");
    private final By btnSave = By.id("btn-save");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openList(String productsUrl) {
        driver.get(productsUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTable));
    }

    public void clickAddProduct() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnAdd));
        btn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtName));
    }

    public void enterName(String name) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtName));
        el.clear();
        el.sendKeys(name);
    }

    public void enterPrice(String price) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtPrice));
        el.clear();
        el.sendKeys(price);
    }

    public void enterQuantity(String quantity) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtQuantity));
        el.clear();
        el.sendKeys(quantity);
    }

    public void clickSave() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnSave));
        btn.click();
    }

    /** Hành động nghiệp vụ: thêm sản phẩm mới */
    public void addProduct(String name, String price, String quantity) {
        clickAddProduct();
        enterName(name);
        enterPrice(price);
        enterQuantity(quantity);
        clickSave();
    }

    public String getSuccessMessage() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        return el.getText();
    }

    public boolean isSuccessDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return el.getText();
    }

    public boolean isProductInTable(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTable));
        List<WebElement> names = driver.findElements(productNames);
        return names.stream().anyMatch(el -> el.getText().trim().equals(productName));
    }

    public int getProductCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTable));
        return driver.findElements(productNames).size();
    }
}
