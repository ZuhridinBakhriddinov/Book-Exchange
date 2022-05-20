package uz.pdp.bookrematch.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bookrematch.entity.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookRematch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer senderId;
    private Integer receiverId;
    private Integer bookId;
    private LocalDateTime localDateTime=LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private Status status = Status.PROGRESS;



}
