package com.poly.sof309.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * =========================================================================
 * [Y4 - P1] PAGE OBJECT MODEL (POM) CHO TRANG ĐĂNG KÝ (/dangky)
 * =========================================================================
 */
public class RegisterPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locator các trường trên form đăng ký
    private By txtFullName = By.name("name");
    private By txtUsername = By.name("username");
    private By txtEmail = By.name("email");
    private By txtPhone = By.name("phone");
    private By txtPassword = By.name("password");
    private By txtConfirmPassword = By.name("confirm_password");
    private By btnRegister = By.xpath("//button[@type='submit']");
    private By lblErrorMessage = By.xpath("//form//i");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void enterFullName(String name) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtFullName));
        el.clear();
        el.sendKeys(name);
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

    public void enterPhone(String phone) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(txtPhone));
        el.clear();
        el.sendKeys(phone);
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
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(btnRegister));
        el.click();
    }

    public void register(String name, String username, String email, String phone, String password, String confirmPassword) {
        enterFullName(name);
        enterUsername(username);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickRegister();
    }

    public String getErrorMessage() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(lblErrorMessage));
            return el.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
