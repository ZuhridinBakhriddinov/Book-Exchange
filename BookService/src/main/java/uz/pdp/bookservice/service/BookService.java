package uz.pdp.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.pdp.bookservice.common.ApiResponse;
import uz.pdp.bookservice.entity.Book;
import uz.pdp.bookservice.entity.BookCollection;
import uz.pdp.bookservice.repository.AuthorRepository;
import uz.pdp.bookservice.repository.BookCollectionRepository;
import uz.pdp.bookservice.repository.BookRepository;
import uz.pdp.clients.bookReview.BookReviewClient;
import uz.pdp.clients.bookReview.BookReviewDto;
import uz.pdp.clients.user.UserClient;
import uz.pdp.clients.user.UserDto;


import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final AuthorRepository authorRepository;
    private final BookCollectionRepository bookCollectionRepository;
    private final BookReviewClient bookReviewClient;
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

            Double averageRating = bookReviewClient.getBookAverageRating(book.getId());
            bookMap.put("averageRating", averageRating);
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

//        String continentUrl = "http://BOOK-REVIEW-SERVICE/api/book-review-service/" + book.getId();
//        BookReviewDto[] bookReviews = restTemplate.getForObject(continentUrl, BookReviewDto[].class);

      /*  List<BookReviewDto> bookReviews = bookReviewClient.getBookReviews(bookId);
        bookMap.put("reviews", bookReviews);*/

        return new ApiResponse("Ok", true, bookMap);
    }


    public ApiResponse getUserBookCollection(Integer userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
//        List<Book> userBookCollection = bookCollectionRepository.getUserBookCollection(userId, size,page * 10);
        List<BookCollection> bookCollections = bookCollectionRepository.findAllByUserId(pageable, userId);

        List<Map<String, Object>> bookList = new ArrayList<>();
        for (BookCollection book : bookCollections) {
            Map<String, Object> bookMap = new HashMap<>();
            bookMap.put("bookId", book.getBook().getId());
            bookMap.put("bookTitle", book.getBook().getTitle());
            bookMap.put("attachmentId", book.getBook().getAttachmentId());
            bookList.add(bookMap);
        }

        return new ApiResponse("Ok", true, bookList);
    }

    public ApiResponse addBookToCollection(Integer bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
       // UserDto user = userClient.getUser(1);


        if (!optionalBook.isPresent()) return new ApiResponse("Book not found", false);
        Book book = optionalBook.get();


        boolean existsInCollection = bookCollectionRepository.existsByUserIdAndBookId(1, bookId);
        if (existsInCollection) return new ApiResponse("This book is already exists in your collection", false);

        BookCollection bookCollection = new BookCollection(null, 1, book);

        bookCollectionRepository.save(bookCollection);
        return new ApiResponse("Successfully added to book Collection", true);
    }
}
