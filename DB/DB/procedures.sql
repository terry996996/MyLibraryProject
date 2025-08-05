-- 建立預存程序 Stored Procedure

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
    SELECT TOP 1 user_id, password_hash, password_salt, user_name
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
	-- 開啟明顯交易模式，滿足ACID(單元、一致、隔離、永久)
    BEGIN TRANSACTION;

    IF EXISTS (
		-- 能借的狀態
        SELECT 1 FROM inventory 
        WHERE inventory_id = @inventoryId AND status = N'在庫'
    )
    BEGIN
		-- 在庫 -> 出借中
        UPDATE inventory SET status = N'出借中' WHERE inventory_id = @inventoryId;

		-- 新增書的出借紀錄資料
        INSERT INTO borrowing_record (user_id, inventory_id)
        VALUES (@userId, @inventoryId);
    END
    ELSE
    BEGIN
		-- 交易回滾
        ROLLBACK TRANSACTION;
        RAISERROR(N'此書不可借閱（可能已被借出）。', 16, 1); -- 不能借
        RETURN;
    END

    COMMIT TRANSACTION; -- 手動commit這組交易
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