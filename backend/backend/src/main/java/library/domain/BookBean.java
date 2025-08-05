package library.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "book")
public class BookBean {
    @Id
    // 查過ISBN最多13碼，在最省儲存空間的考慮情況下，先設定存13字元
    @Column(name = "ISBN", length = 13)
    private String isbn;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "introduction")
    private String introduction;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference // 加這個Annotation防止傳資料的時候序列化遞迴循環
    private Set<InventoryBean> inventories;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Set<InventoryBean> getInventories() {
        return inventories;
    }

    public void setInventories(Set<InventoryBean> inventories) {
        this.inventories = inventories;
    }

    @Override
    public String toString() {
        return "BookBean [isbn=" + isbn + ", name=" + name + ", author=" + author + ", introduction=" + introduction
                + ", inventories=" + inventories + "]";
    }

}
