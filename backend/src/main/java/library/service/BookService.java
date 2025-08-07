package library.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import library.dto.BookInventoryDTO;

@Service
public class BookService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<BookInventoryDTO> getBooksWithInventory() {
        // 呼叫procedures.sql中定義的預存程序'getAllBooksWithInventories'
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
