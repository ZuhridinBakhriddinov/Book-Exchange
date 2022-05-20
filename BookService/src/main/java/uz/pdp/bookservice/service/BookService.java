package uz.pdp.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.pdp.bookservice.common.ApiResponse;
import uz.pdp.bookservice.entity.Book;
import uz.pdp.bookservice.repository.AuthorRepository;
import uz.pdp.bookservice.repository.BookRepository;
import uz.pdp.clients.user.UserClient;


import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;


    private final UserClient userClient;


    public List<Map<String, Object>> getAllBooks(Integer page, Integer size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        List<Book> allBooks = bookRepository.getAllBooks(pageable, search);

        List<Map<String, Object>> bookList = new ArrayList<>();
        for (Book book : allBooks) {
            Map<String, Object> bookMap = new HashMap<>();
            bookMap.put("bookId", book.getId());
            bookMap.put("bookTitle", book.getTitle());
            bookMap.put("attachmentId", book.getAttachmentId());
            bookMap.put("userId",book.getUserId());

//            String continentUrl = "http://BOOK-REVIEW-SERVICE/api/book-review-service/average-rating/";
//            Double averageRating = restTemplate.getForObject(continentUrl +  book.getId(), Double.class);

            bookList.add(bookMap);
        }

        return bookList;
    }

    public ApiResponse  getBookById(Integer bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) return new ApiResponse("Book not found", false);
        Book book = optionalBook.get();

        Map<String, Object> bookMap = new HashMap<>();
        bookMap.put("bookId", book.getId());
        bookMap.put("bookTitle", book.getTitle());
        bookMap.put("bookDescription", book.getDescription());
        bookMap.put("attachmentId", book.getAttachmentId());
        bookMap.put("createdBy", book.getUserId());
        bookMap.put("authors", book.getAuthors());



        return new ApiResponse("Ok", true, bookMap);
    }



}
