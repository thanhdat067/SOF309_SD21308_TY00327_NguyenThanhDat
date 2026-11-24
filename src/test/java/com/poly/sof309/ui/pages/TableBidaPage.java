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
 * [Y4 - P1] PAGE OBJECT MODEL (POM) CHO TRANG QUẢN LÝ BÀN BIDA (/admin/quanliTableBida.jsp)
 * =========================================================================
 */
public class TableBidaPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locator cho trang quản lý bàn bida
    private By btnAddNewTable = By.xpath("//a[contains(@href, 'themTableBida') or contains(text(), 'Thêm bàn')]");
    private By txtSearch = By.name("search");
    private By tableRows = By.xpath("//table//tbody//tr");
    private By lblPageTitle = By.xpath("//h1 | //h2[contains(text(), 'Quản Lý Bàn')]");

    public TableBidaPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean isPageLoaded() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(lblPageTitle));
            return title.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddNew() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnAddNewTable));
        btn.click();
    }

    public int getTableCount() {
        try {
            List<WebElement> rows = driver.findElements(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void searchTable(String keyword) {
        try {
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(txtSearch));
            searchBox.clear();
            searchBox.sendKeys(keyword);
        } catch (Exception ignored) {}
    }
}
