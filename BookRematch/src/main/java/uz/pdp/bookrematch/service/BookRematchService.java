package uz.pdp.bookrematch.service;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.pdp.bookrematch.common.ApiResponse;
import uz.pdp.bookrematch.dto.BookRematchEmailDto;
import uz.pdp.bookrematch.entity.BookRematch;
import uz.pdp.bookrematch.entity.enums.Status;
import uz.pdp.bookrematch.repository.BookRematchRepository;
import uz.pdp.clients.book.BookClient;
import uz.pdp.clients.user.UserClient;
import uz.pdp.clients.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class BookRematchService {
    @Autowired
    BookRematchRepository bookRematchRepository;
    @Autowired
    BookClient bookClient;

    @Autowired
    UserClient userClient;


    private final RabbitTemplate rabbitTemplate;


    @Value("${spring.rabbitmq.exchange}")
    String exchange;
    @Value("${spring.rabbitmq.routingkey}")
    String routingKey;

    public ApiResponse saveBookRematch(BookRematch bookRematch) {
        bookRematch.setBookId(bookRematch.getBookId());
        bookRematch.setLocalDateTime(LocalDateTime.now());
        bookRematch.setStatus(Status.PROGRESS);
        bookRematch.setReceiverId(1);
        bookRematch.setSenderId(2);
        BookRematch savedBookRematch = bookRematchRepository.save(bookRematch);

        Map<String, Object> bookMap = bookClient.getBookInfo(savedBookRematch.getBookId());
        UserDto sender = userClient.getUser(savedBookRematch.getSenderId());
        UserDto receiver = userClient.getUser(savedBookRematch.getReceiverId());

        BookRematchEmailDto bookRematchEmailDto = new BookRematchEmailDto();
        bookRematchEmailDto.setBookTitle((String) bookMap.get("bookTitle"));
        bookRematchEmailDto.setReviewBody(savedBookRematch.getLocalDateTime().toString());
        bookRematchEmailDto.setAuthorName(sender.getFirstName()+" "+sender.getLastName());
        bookRematchEmailDto.setReceiverEmail("leozukich@gmail.com");
        bookRematchEmailDto.setAcceptUrl("http://localhost:8090/api/book-rematch-service/rematch?isAccepted=true&rematchId=" + savedBookRematch.getId());
        bookRematchEmailDto.setRejectUrl("http://localhost:8090/api/book-rematch-service/rematch?isAccepted=false&rematchId=" + savedBookRematch.getId());
        bookRematchEmailDto.setSubject("New book rematch for your Book");
        sendEmailToBookCreator(bookRematchEmailDto);


        return new ApiResponse("Rematch successfully saved", true, bookRematch);
    }

    public void sendEmailToBookCreator(BookRematchEmailDto bookRematchEmailDto){
        rabbitTemplate.convertAndSend(exchange, routingKey, bookRematchEmailDto);
    }

    public ApiResponse sendRematch(Integer userId) {
        List<BookRematch> bookRematchList = bookRematchRepository.findBySenderId(userId);
        return new ApiResponse("Rematch successfully saved", true, bookRematchList);

    }

    public ApiResponse receiveRematch(Integer userId) {
        List<BookRematch> bookRematchList = bookRematchRepository.findByReceiverId(userId);
        return new ApiResponse("Rematch successfully saved", true, bookRematchList);


    }


    public ApiResponse setBookrematchStatus(boolean isAccepted, Integer rematchId) {
        Optional<BookRematch> bookRematchOptional = bookRematchRepository.findById(rematchId);
        if (!bookRematchOptional.isPresent()) return new ApiResponse("Not found", false);
        BookRematch bookRematch = bookRematchOptional.get();

        if (isAccepted){
            bookRematch.setStatus(Status.ACCEPTED);
        } else {
            bookRematch.setStatus(Status.IGNORED);
        }

        bookRematchRepository.save(bookRematch);
        return new ApiResponse(isAccepted ? "Matching is Accepted" : "Matching is rejected", true);

    }
}
