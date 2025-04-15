CREATE DATABASE HANNI_STORE2
USE HANNI_STORE2

CREATE TABLE TaiKhoan (
    MaTK NVARCHAR(10) PRIMARY KEY,
    MatKhau NVARCHAR(20) NOT NULL,
    HoTen NVARCHAR(50) NOT NULL,
    DiaChi NVARCHAR(100) NULL,
    SDT NCHAR(20) NULL,
    Email NVARCHAR(50) NULL,
    VaiTro BIT NOT NULL
);

CREATE TABLE KhachHang (
    MAKH NVARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(50) NOT NULL,
    DiaChi NVARCHAR(100) NULL,
    SDT NVARCHAR(20) NULL,
    Email NVARCHAR(50) NULL
);

CREATE TABLE PhanLoaiSP (
    MaPL NVARCHAR(10) PRIMARY KEY,
    TenPL NVARCHAR(50) NOT NULL,
    ThongTin NVARCHAR(256) NULL
);

CREATE TABLE SanPham (
    MaSP NVARCHAR(10) PRIMARY KEY,
    TenSP NVARCHAR(50) NOT NULL,
    Gia FLOAT NOT NULL,
    Size FLOAT NULL,
    Soluong INTEGER NOT NULL,
    HinhAnh NVARCHAR(100) NULL,
    ThongTin NVARCHAR(256) NULL,
    MaPL NVARCHAR(10) NOT NULL FOREIGN KEY REFERENCES PhanLoaiSP(MaPL)
);

CREATE TABLE DonHang (
    MaDH NVARCHAR(10) PRIMARY KEY,
    MaKH NVARCHAR(10) NOT NULL FOREIGN KEY REFERENCES KhachHang(MAKH),
    NgayLap DATETIME NOT NULL,
    TongTien DECIMAL(10,2) NULL,
    TrangThai BIT NOT NULL
);

CREATE TABLE DonHangChiTiet (
    MaDHCT NVARCHAR(10) PRIMARY KEY,
    MASP NVARCHAR(10) NOT NULL FOREIGN KEY REFERENCES SanPham(MaSP),
    MaDH NVARCHAR(10) NOT NULL FOREIGN KEY REFERENCES DonHang(MaDH),
    DonGia FLOAT NOT NULL,
    Soluong INTEGER NOT NULL
);

CREATE TABLE CSKH (
    MaCSKH NVARCHAR(10) PRIMARY KEY,
    NoiDung NVARCHAR(256) NULL,
    MaTK NVARCHAR(10) NOT NULL FOREIGN KEY REFERENCES TaiKhoan(MaTK),
    MaKH NVARCHAR(10) NOT NULL FOREIGN KEY REFERENCES KhachHang(MAKH),
    Ngay DATETIME
);


CREATE TRIGGER UpdateDonHangTongTienTrigger
ON DonHangChiTiet
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE DonHang
    SET TongTien = (
        SELECT SUM(DonGia * SoLuong)
        FROM DonHangChiTiet
        WHERE DonHangChiTiet.MaDH = DonHang.MaDH
    )
    WHERE DonHang.MaDH IN (SELECT MaDH FROM inserted UNION SELECT MaDH FROM deleted);
END;

CREATE TRIGGER XoaDonHangInsteadOf
ON DonHang
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;

    -- Xóa don hàng chi ti?t tru?c
    DELETE FROM DonHangChiTiet
    WHERE MaDH IN (SELECT MaDH FROM deleted);

    -- Ti?p theo, xóa don hàng
    DELETE FROM DonHang
    WHERE MaDH IN (SELECT MaDH FROM deleted);
END;

CREATE PROCEDURE ThongKeDonHang
AS
BEGIN
    SELECT
        DH.MaDH,
        DH.NgayLap,
        KH.HoTen AS TenKhachHang,
        DH.TongTien,
        DH.TrangThai,
        SP.TenSP AS TenSanPham,
        DHCT.DonGia,
        DHCT.SoLuong
    FROM
        DonHang DH
    JOIN KhachHang KH ON DH.MaKH = KH.MAKH
    JOIN DonHangChiTiet DHCT ON DH.MaDH = DHCT.MaDH
    JOIN SanPham SP ON DHCT.MaSP = SP.MaSP;
END;

-- S? d?ng l?nh EXEC
EXEC ThongKeDonHang;


CREATE PROCEDURE HienThiDoanhThu
    @Ngay DATE = NULL,
    @Thang INT = NULL,
    @Nam INT = NULL
AS
BEGIN
    SELECT
        DH.NgayLap,
        KH.HoTen AS TenKhachHang,
        DH.TongTien,
        SP.TenSP AS TenSanPham,
        DHCT.DonGia,
        DHCT.SoLuong
    FROM
        DonHang DH
    JOIN KhachHang KH ON DH.MaKH = KH.MAKH
    JOIN DonHangChiTiet DHCT ON DH.MaDH = DHCT.MaDH
    JOIN SanPham SP ON DHCT.MaSP = SP.MaSP
    WHERE
        (@Ngay IS NULL OR DAY(DH.NgayLap) = DAY(@Ngay))
        AND (@Thang IS NULL OR MONTH(DH.NgayLap) = @Thang)
        AND (@Nam IS NULL OR YEAR(DH.NgayLap) = @Nam);
END;


