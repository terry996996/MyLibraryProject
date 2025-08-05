package library.dto;

// 借書資料的DTO類別(借書資料傳給前端的關鍵欄位在於，誰借的(userId)、哪筆對應的庫存資料該做異動(inventoryId))
public class BorrowDTO {
    private Long userId;
    private Long inventoryId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

}
