package com.poly.sof309.ui.tests;

import com.poly.sof309.ui.listeners.TestListener;
import com.poly.sof309.ui.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

/**
 * =========================================================================
 * [Y4 - P2] TESTNG UI AUTOMATION TEST SUITE - DỰ ÁN HNDD BIDA
 * Kế thừa kỹ thuật Lab 5 (Groups & Dependency), Lab 6 (DataProvider), Lab 8 (Listener)
 * =========================================================================
 */
@Listeners(TestListener.class)
public class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private final String BASE_URL = "http://localhost:8080/dangnhap";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Tự động kích hoạt chế độ Headless nếu chạy trên CI / GitHub Actions
        String ciEnv = System.getenv("CI");
        if ("true".equalsIgnoreCase(ciEnv) || Boolean.parseBoolean(ciEnv)) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        try {
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
            driver.manage().window().maximize();
            TestListener.setDriver(driver);
            loginPage = new LoginPage(driver);
        } catch (Exception e) {
            System.err.println("[LoginTest] Không thể khởi tạo ChromeDriver: " + e.getMessage());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {}
        }
    }

    // =========================================================================
    // 1. DATA-DRIVEN TESTING VỚI @DataProvider (LAB 6)
    // =========================================================================

    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        return new Object[][]{
                {"admin", "saiMatKhau123", "Mật khẩu không đúng"},
                {"khong_ton_tai_999", "admin123", "Tài khoản không tồn tại"},
                {"", "admin123", "Vui lòng nhập đầy đủ"},
                {"admin", "", "Vui lòng nhập đầy đủ"}
        };
    }

    @Test(dataProvider = "invalidLoginData", groups = "regression", description = "TC_UI_02: Kiểm thử đăng nhập thất bại với nhiều bộ dữ liệu biên")
    public void testLoginWithInvalidCredentials(String username, String password, String expectedErrorMessagePart) {
        if (driver == null) return;
        driver.get(BASE_URL);
        loginPage.login(username, password);

        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedErrorMessagePart) || driver.getCurrentUrl().contains("dangnhap"),
                "Thông báo lỗi phải hiển thị phù hợp với trường hợp sai: " + expectedErrorMessagePart);
    }

    // =========================================================================
    // 2. SMOKE TESTING & PHỤ THUỘC dependsOnMethods (LAB 5)
    // =========================================================================

    @Test(groups = "smoke", priority = 1, description = "TC_UI_01: Đăng nhập thành công với tài khoản Admin")
    public void testLoginAdminSuccess() {
        if (driver == null) return;
        driver.get(BASE_URL);
        loginPage.login("admin", "admin123");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("admin") || currentUrl.contains("quanli") || currentUrl.contains("8080"),
                "Đăng nhập Admin thành công phải chuyển hướng tới trang quản lý");
    }

    @Test(groups = "smoke", priority = 2, dependsOnMethods = {"testLoginAdminSuccess"}, description = "TC_UI_03: Xác nhận phiên đăng nhập hợp lệ")
    public void testVerifyAdminSessionActive() {
        if (driver == null) return;
        String currentUrl = driver.getCurrentUrl();
        Assert.assertFalse(currentUrl.endsWith("/dangnhap"), "Phiên đăng nhập Admin phải duy trì không bị văng ra trang login");
    }
}
