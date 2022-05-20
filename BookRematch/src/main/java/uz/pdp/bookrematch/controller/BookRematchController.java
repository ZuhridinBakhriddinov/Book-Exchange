package uz.pdp.bookrematch.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.bookrematch.common.ApiResponse;
import uz.pdp.bookrematch.entity.BookRematch;
import uz.pdp.bookrematch.service.BookRematchService;

@RestController
@RequestMapping("/api/book-rematch-service")
public class BookRematchController {
    @Autowired
    BookRematchService bookRematchService;

    @PostMapping
    public ResponseEntity<?> saveBookRematch(@RequestBody BookRematch bookRematch) {
        ApiResponse apiResponse = bookRematchService.saveBookRematch(bookRematch);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse.getObject());
    }

    @GetMapping("/send_rematch/{userId}")
    public ResponseEntity<?> sendRematch(@PathVariable Integer userId) {
        ApiResponse apiResponse = bookRematchService.sendRematch(userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse.getObject());
    }

    @GetMapping("/receive_rematch/{userId}")
    public ResponseEntity<?> receiveRematch(@PathVariable Integer userId) {
        ApiResponse apiResponse = bookRematchService.receiveRematch(userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse.getObject());
    }

    @GetMapping("/rematch")
    public ResponseEntity<?> setReviewStatus(@RequestParam(name = "isAccepted") boolean isAccepted,
                                             @RequestParam(name = "rematchId") Integer rematchId) {
        ApiResponse apiResponse = bookRematchService.setBookrematchStatus(isAccepted, rematchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }
}
