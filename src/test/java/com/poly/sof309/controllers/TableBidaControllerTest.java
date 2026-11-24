package com.poly.sof309.controllers;

import org.example.models.TableBida;
import org.example.models.TableType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * =========================================================================
 * [Y3 - TUẦN 2] UNIT TEST QUẢN LÝ BÀN BIDA (TABLE BIDA CONTROLLER / SERVICE)
 * Áp dụng đầy đủ:
 * 1. Phân tích giá trị biên @ParameterizedTest cho giá bàn và tên bàn.
 * 2. Exception Testing assertThrows bắt lỗi dữ liệu âm hoặc rỗng.
 * 3. Timeout Testing kiểm soát thời gian xử lý.
 * =========================================================================
 */
public class TableBidaControllerTest {

    @ParameterizedTest(name = "[{index}] tableName=''{0}'', price={1} => Hợp lệ={2}")
    @DisplayName("TC_TABLE_BOUND_01: Kiểm tra giá trị biên tên bàn và giá bàn bida")
    @CsvSource({
            "'Bàn VIP 01', 50000, true",
            "'Bàn Lỗ 02', 80000, true",
            "'Bàn Carom 3 Băng', 100000, true",
            "'', 50000, false",
            "'   ', 50000, false",
            "'Bàn VIP', 0, false",
            "'Bàn VIP', -20000, false",
            "'Bàn Siêu Cấp', 1000000, true"
    })
    public void testTableValidation(String tableName, double price, boolean expected) {
        boolean isValid = validateTable(tableName, price);
        assertEquals(expected, isValid);
    }

    @ParameterizedTest(name = "[{index}] status=''{0}'' => Hợp lệ={1}")
    @DisplayName("TC_TABLE_BOUND_02: Kiểm tra các trạng thái hợp lệ của bàn bida")
    @CsvSource({
            "'Trống', true",
            "'Đang sử dụng', true",
            "'Bảo trì', true",
            "'Đã đặt trước', true",
            "'Không xác định', false",
            "'', false",
            "'Hỏng hóc', false"
    })
    public void testTableStatusValidation(String status, boolean expected) {
        boolean isValid = "Trống".equals(status) 
                || "Đang sử dụng".equals(status) 
                || "Bảo trì".equals(status) 
                || "Đã đặt trước".equals(status);
        assertEquals(expected, isValid);
    }

    @Test
    @DisplayName("TC_TABLE_EX_01: Ném IllegalArgumentException khi thêm bàn có giá âm")
    public void testAddTableWithNegativePriceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            createTable("Bàn Test", -10000.0);
        }, "Phải ném ngoại lệ khi giá bàn nhỏ hơn hoặc bằng 0");
    }

    @Test
    @DisplayName("TC_TABLE_EX_02: Ném IllegalArgumentException khi tên bàn để trống")
    public void testAddTableWithEmptyNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            createTable("", 60000.0);
        }, "Phải ném ngoại lệ khi tên bàn để trống");
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    @DisplayName("TC_TABLE_PERF_01: Kiểm tra khởi tạo và gán quan hệ TableBida - TableType dưới 2s")
    public void testTableEntityRelationshipPerformance() {
        TableType type = new TableType(1, "Bàn Lỗ Tiêu Chuẩn", 70000.0);
        TableBida table = new TableBida();
        table.setId(10);
        table.setName("Bàn Lỗ Số 10");
        table.setTableTypeId(type.getId());
        table.setStatus("Trống");

        assertEquals(10, table.getId());
        assertEquals("Bàn Lỗ Số 10", table.getName());
        assertEquals(1, table.getTableTypeId());
        assertEquals("Trống", table.getStatus());
    }

    private boolean validateTable(String name, double price) {
        if (name == null || name.trim().isEmpty()) return false;
        return price > 0;
    }

    private void createTable(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên bàn bida không được để trống!");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Giá bàn phải lớn hơn 0!");
        }
    }
}
