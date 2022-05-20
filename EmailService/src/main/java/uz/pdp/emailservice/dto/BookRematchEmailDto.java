package uz.pdp.emailservice.dto;

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
    private String Body;
    private String acceptUrl;
    private String rejectUrl;

}
