package uz.pdp.bookrematch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRematchEmailDto {
    private String receiverEmail;
    private String authorName;
    private String subject;
    private String bookTitle;
    private String reviewBody;
    private String acceptUrl;
    private String rejectUrl;

}
