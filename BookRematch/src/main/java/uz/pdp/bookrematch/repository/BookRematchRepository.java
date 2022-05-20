package uz.pdp.bookrematch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.bookrematch.entity.BookRematch;

import java.util.List;

public interface BookRematchRepository extends JpaRepository<BookRematch,Integer> {
       List<BookRematch> findByReceiverId(Integer receiverId);
    List<BookRematch> findBySenderId(Integer senderId);
}
