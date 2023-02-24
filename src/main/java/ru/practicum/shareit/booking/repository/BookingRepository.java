package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long booker);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long booker, LocalDateTime dateTime, LocalDateTime dateTime2);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long booker, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long booker, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStatusIsOrderByStartDesc(Long booker, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long owner);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long owner, LocalDateTime dateTime, LocalDateTime dateTime2);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long owner, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long owner, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStatusIsOrderByStartDesc(Long owner, Status status);

    Booking findTop1ByItemIdAndEndIsBeforeOrderByStartDesc(Long itemId, LocalDateTime dateTime);

    Booking findTop1ByItemIdAndStartIsAfterOrderByStartDesc(Long itemId, LocalDateTime dateTime);

    Booking findByBookerIdAndItemIdAndEndIsBefore(Long booker, Long itemId, LocalDateTime time);
}
