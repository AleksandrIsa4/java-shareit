package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.State;
import ru.practicum.shareit.booking.model.enumeration.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validator.Validator;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    @MockBean
    BookingService bookingService;
    @MockBean
    Validator<BookingMessageDto> bookingDtoValidator;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    BookingResponseDto bookingResponseDto1;
    BookingMessageDto bookingMessageDto1;
    BookingResponseDto bookingResponseDto2;
    BookingMessageDto bookingMessageDto2;
    ItemResponseDto itemResponseDto2;
    ItemResponseDto itemResponseDto1;
    Item item1;
    Item item2;
    User user1;
    User user2;
    Booking booking1;
    Booking booking2;

    @BeforeEach
    void setUp() {
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, null);
        itemResponseDto2 = new ItemResponseDto("item2", "description Item2", false, null, null, null, null);
        itemResponseDto1.setId(1L);
        itemResponseDto2.setId(2L);
        user1 = new User("NameTest", "test@test.ru");
        user1.setId(1L);
        user2 = new User("NameTest2", "test@test2.ru");
        user2.setId(2L);
        item1 = new Item("item1", "description Item1", true, user1, null);
        item2 = new Item("item2", "description Item2", false, user1, null);
        item1.setId(1L);
        item2.setId(2L);
        booking1 = new Booking(item1, user2, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), Status.APPROVED);
        booking1.setId(1L);
        booking2 = new Booking(item2, user2, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), Status.APPROVED);
        booking2.setId(2L);
        bookingResponseDto1 = new BookingResponseDto(itemResponseDto1, user2, LocalDateTime.now().plusSeconds(5), LocalDateTime.now().plusSeconds(6), Status.APPROVED, 2L);
        bookingResponseDto2 = new BookingResponseDto(itemResponseDto2, user2, LocalDateTime.now().plusSeconds(5), LocalDateTime.now().plusSeconds(6), Status.APPROVED, 2L);
        bookingMessageDto1 = new BookingMessageDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2));
        bookingMessageDto2 = new BookingMessageDto(2L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2));
    }

    @Test
    void saveTest() throws Exception {
        when(bookingService.save(Mockito.any(Booking.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(booking1);
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingMessageDto1))
                        .header(HEADER_REQUEST, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookerId", is(bookingResponseDto1.getBookerId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .save(Mockito.any(Booking.class), Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void patchTest() throws Exception {
        when(bookingService.patch(Mockito.anyBoolean(), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(booking2);
        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header(HEADER_REQUEST, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookerId", is(bookingResponseDto2.getBookerId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto2.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .patch(Mockito.anyBoolean(), Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void getBookingTest() throws Exception {
        when(bookingService.get(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(booking1);
        mockMvc.perform(get("/bookings/1")
                        .header(HEADER_REQUEST, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookerId", is(bookingResponseDto1.getBookerId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .get(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void findAllByStateBookerTest() throws Exception {
        when(bookingService.getAllBooker(Mockito.anyLong(), Mockito.any(State.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(booking1, booking2));
        mockMvc.perform(get("/bookings/?state=PAST")
                        .header(HEADER_REQUEST, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllBooker(Mockito.anyLong(), Mockito.any(State.class), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void findAllByStateOwnerTest() throws Exception {
        when(bookingService.getAllOwner(Mockito.anyLong(), Mockito.any(State.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(booking1, booking2));
        mockMvc.perform(get("/bookings/owner/?state=FUTURE")
                        .header(HEADER_REQUEST, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllOwner(Mockito.anyLong(), Mockito.any(State.class), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void findAllByStateOwnerTestStatusError() throws Exception {
        when(bookingService.getAllOwner(Mockito.anyLong(), Mockito.any(State.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(booking1, booking2));
        mockMvc.perform(get("/bookings/owner/?state=FUTU")
                        .header(HEADER_REQUEST, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}