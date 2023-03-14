package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, PagingAndSortingRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long booker, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long booker, LocalDateTime dateTime, LocalDateTime dateTime2, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long booker, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long booker, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusIsOrderByStartDesc(Long booker, Status status, Pageable pageable);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long owner, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long owner, LocalDateTime dateTime, LocalDateTime dateTime2, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long owner, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long owner, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatusIsOrderByStartDesc(Long owner, Status status, Pageable pageable);

    Booking findTop1ByItemIdAndEndIsBeforeAndStatusIsOrderByStartDesc(Long itemId, LocalDateTime dateTime, Status status);

    Booking findTop1ByItemIdAndStartIsAfterAndStatusIsOrderByStartAsc(Long itemId, LocalDateTime dateTime, Status status);

    Booking findTop1ByBookerIdAndItemIdAndEndIsBefore(Long booker, Long itemId, LocalDateTime time);
}
