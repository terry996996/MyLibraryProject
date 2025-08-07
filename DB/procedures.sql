-- 第三步需要執行的sql
-- 建立預存程序 Stored Procedure

-- USE LibrarySystem; *******這行要先框起來執行*******

---------- 使用者註冊的預存程序 ----------
CREATE OR ALTER PROCEDURE registerUser
    @phoneNumber VARCHAR(20),
    @passwordHash VARCHAR(200),
    @passwordSalt VARCHAR(50),
    @userName NVARCHAR(200)
AS
BEGIN
    IF EXISTS (SELECT 1 FROM users WHERE phone_number = @phoneNumber)
    BEGIN
        RAISERROR(N'此組手機號碼已被註冊。', 16, 1); -- 如果使用者輸入的手機號碼已經被註冊過
        RETURN;
    END

    INSERT INTO users (phone_number, password_hash, password_salt, user_name)
    VALUES (@phoneNumber, @passwordHash, @passwordSalt, @userName);
END;
GO
---------- 使用者註冊的預存程序 ----------



---------- 使用者登入的預存程序 ---------- 
CREATE OR ALTER PROCEDURE loginUser
    @phoneNumber VARCHAR(20)
AS
BEGIN
    SELECT TOP 1 user_id, phone_number, password_hash, password_salt, user_name
    FROM users
    WHERE phone_number = @phoneNumber;
END;
GO
---------- 使用者登入的預存程序 ---------- 



---------- 更新登入時間的預存程序 ---------- 
CREATE OR ALTER PROCEDURE updateLastLogin
    @userId BIGINT
AS
BEGIN
    UPDATE users SET last_login_time = GETDATE() WHERE user_id = @userId;
END;
GO
---------- 更新登入時間的預存程序 ---------- 



---------- 借書的預存程序 ---------- 
CREATE OR ALTER PROCEDURE borrowBook
    @userId BIGINT,
    @inventoryId BIGINT
AS
BEGIN
    SET XACT_ABORT ON;
    BEGIN TRANSACTION;

    DECLARE @isbn NVARCHAR(20);

    -- 先取得 inventory 對應的 ISBN
    SELECT @isbn = isbn FROM inventory WHERE inventory_id = @inventoryId;

    -- 檢查此使用者是否已借過相同書籍但尚未歸還
    IF EXISTS (
        SELECT 1
        FROM borrowing_record br
        JOIN inventory i ON br.inventory_id = i.inventory_id
        WHERE br.user_id = @userId AND i.isbn = @isbn AND br.return_time IS NULL
    )
    BEGIN
        ROLLBACK TRANSACTION;
        RAISERROR(N'您已借閱此書籍，請先歸還後再借。', 16, 1);
        RETURN;
    END

    -- 檢查此 inventory 是否可借
    IF EXISTS (
        SELECT 1 FROM inventory 
        WHERE inventory_id = @inventoryId AND status = N'在庫'
    )
    BEGIN
        UPDATE inventory SET status = N'出借中' WHERE inventory_id = @inventoryId;
        INSERT INTO borrowing_record (user_id, inventory_id)
        VALUES (@userId, @inventoryId);
    END
    ELSE
    BEGIN
        ROLLBACK TRANSACTION;
        RAISERROR(N'此書不可借閱（可能已被借出）。', 16, 1);
        RETURN;
    END

    COMMIT TRANSACTION;
END;
GO
---------- 借書的預存程序 ---------- 



---------- 還書的預存程序 ----------
CREATE OR ALTER PROCEDURE returnBook
    @userId BIGINT,
    @inventoryId BIGINT
AS
BEGIN
    SET XACT_ABORT ON;
	-- 開啟明顯交易模式，滿足ACID(單元、一致、隔離、永久)
    BEGIN TRANSACTION;

    IF EXISTS (
        SELECT 1 FROM borrowing_record
		-- 設定可以還書的條件(對應的還書者、庫存紀錄)以及當前還書時間為NULL(因為還沒還)
        WHERE user_id = @userId AND inventory_id = @inventoryId AND return_time IS NULL
    )
    BEGIN
		-- 更新狀態為在庫
        UPDATE inventory SET status = N'在庫' WHERE inventory_id = @inventoryId;

		-- 更動借閱紀錄
        UPDATE borrowing_record
        SET return_time = GETDATE()
        WHERE user_id = @userId AND inventory_id = @inventoryId AND return_time IS NULL;
    END
    ELSE
    BEGIN
		-- 交易回滾
        ROLLBACK TRANSACTION;
        RAISERROR(N'查無可還書紀錄。', 16, 1); -- 不能還
        RETURN;
    END

    COMMIT TRANSACTION; -- 手動commit這組交易
END;
GO
---------- 還書的預存程序 ----------



---------- 查詢使用者尚未還歸還書籍的預存程序 ----------
CREATE OR ALTER PROCEDURE getUnreturnedBooksByUserId
    @userId BIGINT
AS
BEGIN
    SELECT 
        br.record_id,
        br.borrowing_time,   
        b.name AS book_name,
        i.inventory_id
    FROM borrowing_record br
    JOIN inventory i ON br.inventory_id = i.inventory_id
    JOIN book b ON i.isbn = b.isbn
    WHERE br.user_id = @userId AND br.return_time IS NULL;
END;
GO
---------- 查詢使用者尚未還歸還書籍的預存程序 ----------



---------- 取得所有書籍資料(包含他的庫存資訊)的預存程序 ----------
CREATE OR ALTER PROCEDURE getAllBooksWithInventories
AS
BEGIN
    SELECT 
        b.ISBN,
        b.name AS book_name,
        b.author,
        b.introduction,
        i.inventory_id,
		i.status,
        i.store_time
    FROM book b
    LEFT JOIN inventory i ON b.ISBN = i.ISBN -- 設定只要書有資料，即使庫存沒資料(考慮到未來可能擴充更多不同的書籍狀態，如:即將上架等等)也會顯示
    ORDER BY b.ISBN, i.inventory_id;
END;
GO
---------- 取得所有書籍資料(包含他的庫存資訊)的預存程序 ----------
