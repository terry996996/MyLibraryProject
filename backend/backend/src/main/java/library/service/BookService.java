// package library.service;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import library.domain.BookBean;
// import library.dto.BookDTO;
// import library.repository.BookRepository;

// @Service
// public class BookService {

// @Autowired
// private BookRepository bookRepository; // 依賴注入

// // 查出全部的書籍，查到後存成List(當中指明存BookDTO中設定的欄位)
// public List<BookDTO> findAllBooks() {
// return bookRepository.findAll().stream()
// .map(book -> new BookDTO( // lambda語法
// book.getIsbn(),
// book.getName(),
// book.getAuthor(),
// book.getIntroduction()))
// .collect(Collectors.toList());
// }

// // 透過書的ISBN去查找出這一本書籍的資料(單筆查詢)，然後資料裡存成BookDTO中設定的欄位
// public BookDTO findBookByIsbn(String isbn) {
// BookBean book = bookRepository.findById(isbn)
// .orElseThrow(() -> new IllegalArgumentException("找不到該書籍")); // lambda語法
// return new BookDTO(book.getIsbn(), book.getName(), book.getAuthor(),
// book.getIntroduction()); //
// 透過建構子new物件，前端到時候會去用get呼叫後端API，然後對應的Controller當中會去調這個方法，這邊就會將DTO資料傳過去給前端
// }
// }
