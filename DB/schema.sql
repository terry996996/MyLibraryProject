-- 第一步需要執行的sql
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'LibrarySystem')
BEGIN
    CREATE DATABASE LibrarySystem; -- 建資料庫
END;
GO

USE LibrarySystem;
GO



----------users 使用者 table----------
IF OBJECT_ID('dbo.users', 'U') IS NULL
BEGIN
	CREATE TABLE [users] (
		user_id BIGINT IDENTITY(1,1) PRIMARY KEY,
		phone_number VARCHAR(20) UNIQUE NOT NULL,
		password_hash VARCHAR(200) NOT NULL, -- 存 SHA-256 加密後結果的欄位
		password_salt VARCHAR(50) NOT NULL, -- 存salt的欄位
		user_name NVARCHAR(200) NOT NULL,
		registration_time DATETIME NOT NULL DEFAULT GETDATE(),
		last_login_time DATETIME
	);
END;
GO
----------users 使用者 table----------



----------book 書籍 table----------
IF OBJECT_ID('dbo.book', 'U') IS NULL
BEGIN
	CREATE TABLE [book] (
		ISBN VARCHAR(13) PRIMARY KEY, -- 查過ISBN最多13碼，在最省儲存空間的考慮情況下，先設定存13字元
		name NVARCHAR(200) NOT NULL,
		author NVARCHAR(200) NOT NULL,
		introduction NVARCHAR(MAX)
	);
END;
GO
----------book 書籍 table----------



----------inventory 庫存 table----------
IF OBJECT_ID('dbo.inventory', 'U') IS NULL
BEGIN
	CREATE TABLE [inventory] (
		inventory_id BIGINT IDENTITY(1,1) PRIMARY KEY,
		ISBN VARCHAR(13) NOT NULL,
		store_time DATETIME NOT NULL DEFAULT GETDATE(),
		status NVARCHAR(10) CHECK (status IN ('在庫', '出借中', '整理中', '遺失', '損毀', '廢棄')) NOT NULL DEFAULT '在庫',
		FOREIGN KEY (ISBN) REFERENCES book(ISBN) ON DELETE CASCADE
	);
END;
GO
----------inventory 庫存 table----------



----------borrowing_record 借閱紀錄 table----------
IF OBJECT_ID('dbo.borrowing_record', 'U') IS NULL
BEGIN
	CREATE TABLE [borrowing_record] (
		record_id BIGINT IDENTITY(1,1) PRIMARY KEY,
		user_id BIGINT NOT NULL,
		inventory_id BIGINT NOT NULL,
		borrowing_time DATETIME NOT NULL DEFAULT GETDATE(),
		return_time DATETIME,
		FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
		FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id) ON DELETE CASCADE
	);
END;
GO
----------borrowing_record 借閱紀錄 table----------