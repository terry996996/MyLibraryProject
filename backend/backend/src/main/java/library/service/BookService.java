package library.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import library.dto.BookInventoryDTO;
import library.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository; // 依賴注入

    @PersistenceContext
    private EntityManager entityManager;

    // 查出全部的書籍，查到後存成List(當中指明存BookDTO中設定的欄位)
    // public List<BookDTO> findAllBooks() {
    // return bookRepository.findAll().stream()
    // .map(book -> new BookDTO( // lambda語法
    // book.getIsbn(),
    // book.getName(),
    // book.getAuthor(),
    // book.getIntroduction()))
    // .collect(Collectors.toList());
    // }

    // 透過書的ISBN去查找出這一本書籍的資料(單筆查詢)，然後資料裡存成BookDTO中設定的欄位
    // public BookDTO findBookByIsbn(String isbn) {
    // BookBean book = bookRepository.findById(isbn)
    // .orElseThrow(() -> new IllegalArgumentException("找不到該書籍")); // lambda語法
    // return new BookDTO(book.getIsbn(), book.getName(), book.getAuthor(),
    // book.getIntroduction()); //
    // 透過建構子new物件，前端到時候會去用get呼叫後端API，然後對應的Controller當中會去調這個方法，這邊就會將DTO資料傳過去給前端
    // }

    public List<BookInventoryDTO> getBooksWithInventory() {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("getAllBooksWithInventories");

        List<Object[]> results = query.getResultList();
        List<BookInventoryDTO> books = new ArrayList<>();

        for (Object[] row : results) {
            BookInventoryDTO bookInventory = new BookInventoryDTO();
            bookInventory.setIsbn((String) row[0]);
            bookInventory.setBookName((String) row[1]);
            bookInventory.setAuthor((String) row[2]);
            bookInventory.setIntroduction((String) row[3]);
            bookInventory.setInventoryId(((Number) row[4]).longValue());
            bookInventory.setStatus((String) row[5]);
            bookInventory.setStoreTime(row[6] != null ? ((Timestamp) row[6]).toLocalDateTime() : null);
            books.add(bookInventory);
        }

        return books;
    }
}
