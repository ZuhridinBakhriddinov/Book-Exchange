package uz.pdp.bookservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bookservice.entity.enums.Status;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    private Integer attachmentId;
    private boolean isRead;
    private Integer userId;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToMany
    private List<Author> authors;


}