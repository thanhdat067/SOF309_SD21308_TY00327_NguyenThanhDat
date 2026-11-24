-- ============================================================
-- SCRIPT THIẾT LẬP DATABASE CHO DỰ ÁN QUẢN LÝ BIDA (QLBida5)
-- Hướng dẫn chạy trên SSMS:
-- 1. Mở SSMS, kết nối vào Server Name: localhost (chọn Windows Authentication)
-- 2. Nhấn nút "New Query", dán toàn bộ script này và nhấn "Execute" (F5)
-- ============================================================

-- BƯỚC 1: Bật Mixed Mode Authentication (đăng nhập bằng sa)
EXEC xp_instance_regwrite 
    N'HKEY_LOCAL_MACHINE', 
    N'Software\Microsoft\MSSQLServer\MSSQLServer', 
    N'LoginMode', 
    REG_DWORD, 
    2;
GO

-- BƯỚC 2: Bật tài khoản sa và đặt mật khẩu là 123456
ALTER LOGIN sa WITH PASSWORD = N'123456', CHECK_POLICY = OFF;
ALTER LOGIN sa ENABLE;
GO

-- BƯỚC 3: Tạo cơ sở dữ liệu QLBida5
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'QLBida5')
BEGIN
    CREATE DATABASE QLBida5;
    PRINT N'Đã tạo database QLBida5 thành công!';
END
ELSE
    PRINT N'Database QLBida5 đã tồn tại.';
GO

USE QLBida5;
GO

-- BƯỚC 4: Tạo các bảng dữ liệu
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Account')
BEGIN
    CREATE TABLE Account (
        id INT IDENTITY(1,1) PRIMARY KEY,
        username NVARCHAR(100) NOT NULL UNIQUE,
        password NVARCHAR(255) NOT NULL,
        role NVARCHAR(50) NOT NULL DEFAULT N'Khách hàng',
        name NVARCHAR(150) NULL,
        phone NVARCHAR(20) NULL,
        email NVARCHAR(150) NULL,
        is_deleted BIT NOT NULL DEFAULT 0,
        points INT NOT NULL DEFAULT 0,
        discount_rate FLOAT NOT NULL DEFAULT 0.0,
        member_rank NVARCHAR(100) NULL DEFAULT N'Nghiệp dư',
        playing_style NVARCHAR(250) NULL DEFAULT N'Chưa cập nhật'
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Category')
BEGIN
    CREATE TABLE Category (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(100) NOT NULL UNIQUE,
        is_deleted BIT NOT NULL DEFAULT 0
    );
    INSERT INTO Category(name) VALUES (N'Nước uống'), (N'Đồ uống có cồn'), (N'Đồ ăn'), (N'Thuốc lá'), (N'Khác');
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'TableType')
BEGIN
    CREATE TABLE TableType (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(100) NOT NULL,
        price DECIMAL(18,2) NOT NULL DEFAULT 0,
        is_deleted BIT NOT NULL DEFAULT 0
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'TableBida')
BEGIN
    CREATE TABLE TableBida (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(100) NOT NULL,
        type_id INT NULL REFERENCES TableType(id),
        status NVARCHAR(50) NOT NULL DEFAULT N'Trống',
        is_deleted BIT NOT NULL DEFAULT 0,
        camera_ip NVARCHAR(100) NULL,
        camera_port NVARCHAR(10) NULL,
        camera_username NVARCHAR(100) NULL,
        camera_password NVARCHAR(255) NULL
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Service')
BEGIN
    CREATE TABLE Service (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(150) NOT NULL,
        price DECIMAL(18,2) NOT NULL DEFAULT 0,
        category NVARCHAR(100) NOT NULL DEFAULT N'Khác',
        is_deleted BIT NOT NULL DEFAULT 0
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Cue')
BEGIN
    CREATE TABLE Cue (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(150) NOT NULL,
        brand NVARCHAR(100) NULL,
        price DECIMAL(18,2) NOT NULL DEFAULT 0,
        status NVARCHAR(50) NOT NULL DEFAULT N'Có sẵn',
        is_deleted BIT NOT NULL DEFAULT 0
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Booking')
BEGIN
    CREATE TABLE Booking (
        id INT IDENTITY(1,1) PRIMARY KEY,
        account_id INT NULL REFERENCES Account(id),
        table_id INT NULL REFERENCES TableBida(id),
        start_time DATETIME NOT NULL,
        end_time DATETIME NULL,
        status NVARCHAR(50) NOT NULL DEFAULT N'Chờ duyệt',
        note NVARCHAR(500) NULL,
        is_deleted BIT NOT NULL DEFAULT 0,
        created_at DATETIME NOT NULL DEFAULT GETDATE()
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Bill')
BEGIN
    CREATE TABLE Bill (
        id INT IDENTITY(1,1) PRIMARY KEY,
        account_id INT NULL REFERENCES Account(id),
        table_id INT NULL REFERENCES TableBida(id),
        total DECIMAL(18,2) NOT NULL DEFAULT 0,
        status NVARCHAR(50) NOT NULL DEFAULT N'Chờ thanh toán',
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        member_id INT NULL REFERENCES Account(id),
        discount_amount DECIMAL(18, 2) NOT NULL DEFAULT 0,
        is_deleted BIT NOT NULL DEFAULT 0
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Matchmaking')
BEGIN
    CREATE TABLE Matchmaking (
        id INT IDENTITY(1,1) PRIMARY KEY,
        account_id INT NOT NULL REFERENCES Account(id),
        play_type NVARCHAR(100) NOT NULL,
        play_time DATETIME NOT NULL,
        note NVARCHAR(500) NULL,
        status NVARCHAR(50) NOT NULL DEFAULT N'Waiting',
        partner_id INT NULL REFERENCES Account(id),
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        format NVARCHAR(50) NOT NULL DEFAULT N'Giao lưu',
        rank_requirement NVARCHAR(50) NOT NULL DEFAULT N'Không giới hạn'
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'MatchmakingProfile')
BEGIN
    CREATE TABLE MatchmakingProfile (
        id INT IDENTITY(1,1) PRIMARY KEY,
        account_id INT NOT NULL UNIQUE REFERENCES Account(id),
        full_name NVARCHAR(150) NOT NULL,
        age INT NOT NULL,
        player_rank NVARCHAR(100) NOT NULL,
        zalo_phone NVARCHAR(30) NOT NULL,
        created_at DATETIME NOT NULL DEFAULT GETDATE()
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Locker')
BEGIN
    CREATE TABLE Locker (
        id INT IDENTITY(1,1) PRIMARY KEY,
        locker_number NVARCHAR(100) NOT NULL UNIQUE,
        status NVARCHAR(50) NOT NULL DEFAULT N'Available',
        member_id INT NULL REFERENCES Account(id),
        start_date DATETIME NULL,
        end_date DATETIME NULL,
        price DECIMAL(18,2) NOT NULL DEFAULT 0.0,
        note NVARCHAR(500) NULL,
        is_deleted BIT NOT NULL DEFAULT 0,
        customer_name NVARCHAR(200) NULL
    );
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'StaffCall')
BEGIN
    CREATE TABLE StaffCall (
        id INT IDENTITY(1,1) PRIMARY KEY,
        table_id INT NOT NULL REFERENCES TableBida(id),
        call_time DATETIME NOT NULL DEFAULT GETDATE(),
        status NVARCHAR(50) NOT NULL DEFAULT N'Pending',
        note NVARCHAR(500) NULL
    );
END
GO

-- BƯỚC 5: Tạo tài khoản admin mặc định nếu chưa có (Mật khẩu: admin123)
-- Hash BCrypt chuẩn của 'admin123'
IF NOT EXISTS (SELECT * FROM Account WHERE username = 'admin')
BEGIN
    INSERT INTO Account(username, password, role, name, email, is_deleted)
    VALUES ('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewYpfQ8U2gY8Fj4a', 'admin', N'Quản trị viên', 'admin@hndd.com', 0);
    PRINT N'Đã tạo tài khoản admin (mật khẩu: admin123)';
END
ELSE
BEGIN
    UPDATE Account 
    SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewYpfQ8U2gY8Fj4a', 
        is_deleted = 0,
        role = 'admin'
    WHERE username = 'admin';
    PRINT N'Đã cập nhật tài khoản admin.';
END
GO

PRINT N'======================================================';
PRINT N'HOÀN TẤT CẤU HÌNH DATABASE QLBida5 VÀ TÀI KHOẢN SA!';
PRINT N'LƯU Ý: Vui lòng Restart SQL Server service để Mixed Mode có hiệu lực.';
PRINT N'======================================================';
GO
