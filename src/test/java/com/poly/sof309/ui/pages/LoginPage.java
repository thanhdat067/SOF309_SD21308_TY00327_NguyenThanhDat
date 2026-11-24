package com.poly.sof309.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * =========================================================================
 * [Y4 - P1] PAGE OBJECT MODEL (POM) CHO TRANG ĐĂNG NHẬP (/dangnhap)
 * Khớp chuẩn cấu trúc HTML & DOM của dự án HNDD Bida
 * =========================================================================
 */
public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Định nghĩa các Locator chuẩn
    private By txtUsername = By.name("username");
    private By txtPassword = By.name("password");
    private By btnLogin = By.xpath("//button[@type='submit']");
    private By lblErrorMessage = By.xpath("//form//i");
    private By linkRegister = By.xpath("//a[contains(@href, 'dangky')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void enterUsername(String username) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(txtUsername));
        element.clear();
        if (username != null && !username.isEmpty()) {
            element.sendKeys(username);
        }
    }

    public void enterPassword(String password) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(txtPassword));
        element.clear();
        if (password != null && !password.isEmpty()) {
            element.sendKeys(password);
        }
    }

    public void clickLogin() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnLogin));
        element.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getErrorMessage() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(lblErrorMessage));
            return element.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void goToRegister() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(linkRegister));
        element.click();
    }
}
