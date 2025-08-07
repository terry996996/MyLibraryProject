package library.dto;

// 還書資料的DTO類別(還書資料傳給前端的關鍵欄位在於，誰還的(userId)、哪筆對應的庫存資料該做異動(inventoryId))
// 其實大致仿照借書DTO部份的邏輯
public class ReturnDTO {
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
