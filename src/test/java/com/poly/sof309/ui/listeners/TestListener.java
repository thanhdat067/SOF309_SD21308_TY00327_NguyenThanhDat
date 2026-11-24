package com.poly.sof309.ui.listeners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * =========================================================================
 * [Y4 - P2] TESTNG LISTENER - TỰ ĐỘNG CHỤP ẢNH MÀN HÌNH KHI TEST THẤT BẠI
 * Kế thừa kỹ thuật Lab 8 môn Kiểm thử nâng cao
 * =========================================================================
 */
public class TestListener implements ITestListener {

    private static WebDriver activeDriver;

    public static void setDriver(WebDriver driver) {
        activeDriver = driver;
    }

    public static WebDriver getDriver() {
        return activeDriver;
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[TestNG] Bắt đầu thực thi test: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[TestNG] Test THÀNH CÔNG: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.err.println("[TestNG] Test THẤT BẠI: " + result.getName() + " -> Đang chụp ảnh màn hình...");
        if (activeDriver != null) {
            try {
                TakesScreenshot ts = (TakesScreenshot) activeDriver;
                File srcFile = ts.getScreenshotAs(OutputType.FILE);
                
                // Tạo thư mục screenshots nếu chưa tồn tại
                File dir = new File("screenshots");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "screenshots/" + result.getName() + "_" + timestamp + ".png";
                File destFile = new File(fileName);
                
                FileUtils.copyFile(srcFile, destFile);
                System.out.println("[TestNG] Đã lưu ảnh chụp lỗi tại: " + destFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("[TestNG] Lỗi khi lưu ảnh chụp màn hình: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[TestNG] Test bị BỎ QUA: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("=================================================");
        System.out.println("[TestNG Suite Start]: " + context.getName());
        System.out.println("=================================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("=================================================");
        System.out.println("[TestNG Suite Finish]: Passed=" + context.getPassedTests().size() 
                + ", Failed=" + context.getFailedTests().size());
        System.out.println("=================================================");
    }
}
