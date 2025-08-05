// package library.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import library.dto.BookDTO;
// import library.service.BookService;

// @RestController // @Controller + @ResponseBody
// @RequestMapping("/api/books") // 根源路徑
// public class BookController {

// @Autowired
// private BookService bookService; // 依賴注入

// // 供前端來呼叫後端中查找全部書籍的API
// // 前端會去用get呼叫後端的API，然後在這個對應的Conteoller當中，會去調bookService當中查全部書籍的方法，
// // 最後轉成json資料回傳給前端
// @GetMapping
// public List<BookDTO> getAllBooks() {
// return bookService.findAllBooks();
// }

// // 供前端來呼叫後端中透過ISBN查找單筆書籍的API
// // 前端會去用get呼叫後端的API，然後在這個對應的Conteoller當中，會去調bookService當中查單筆書籍的方法，
// // 最後轉成json資料回傳給前端
// @GetMapping("/{isbn}")
// public BookDTO getBookByIsbn(@PathVariable String isbn) {
// return bookService.findBookByIsbn(isbn);
// }
// }