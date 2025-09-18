package ru.practicum.shareit.booking.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;

    public BookingDtoResponse create(Long userId, BookingDtoRequestCreate booking) {
        User user = userService.getUser(userId);
        Item item = itemService.getItem(booking.itemId());
        if (!item.isAvailable()) throw new ExceptionBookingNotAvailable("Вещь не доступна для бронирования");
        Booking saved = bookingRepository.save(bookingMapper.toBookingDtoRequestCreate(booking, item, user));
        return bookingMapper.toBookingDtoResponse(saved);
    }

    public BookingDtoResponse approve(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ExceptionBookingNotFound("Бронирования с идентификатором = " + bookingId + " не найдено"));
        if (!booking.getItem().getOwner().getId().equals(userId))
            throw new ExceptionBookingNotOwner("Только владелец товара может одобрять бронирование");
        if (booking.getStatus() != Status.WAITING)
            throw new ExceptionInvalidBookingState("Статус бронирования должен быть WAITING (Новое бронирование)");
        if (approved) booking.setStatus(Status.APPROVED);
        else booking.setStatus(Status.REJECTED);
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toBookingDtoResponse(saved);
    }

    public BookingDtoResponse get(Long requesterId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ExceptionBookingNotFound("Бронирования с идентификатором = " + bookingId + " не найдено"));
        boolean isBooker = booking.getBooker().getId().equals(requesterId);
        boolean isOwner = booking.getItem().getOwner().getId().equals(requesterId);
        if (!isBooker && !isOwner)
            throw new ExceptionBookingForbiddenOperation("Отказано в доступе к бронированию");
        return bookingMapper.toBookingDtoResponse(booking);
    }

    public List<BookingDtoResponse> getAllBookingCurrentUser(Long userId, BookingState bookingState) {
        userService.getUser(userId);
        Specification<Booking> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("booker").get("id"), userId));
            predicates.add(statePredicate(root, criteriaBuilder, bookingState, Instant.now()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<Booking> result = bookingRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "start"));
        return bookingMapper.toListBookingDtoResponse(result);
    }

    public List<BookingDtoResponse> getAllBookingAllItemCurrentUser(Long userId, BookingState bookingState) {
        userService.getUser(userId);
        Specification<Booking> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("item").get("owner").get("id"), userId));
            predicates.add(statePredicate(root, criteriaBuilder, bookingState, Instant.now()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<Booking> result = bookingRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "start"));
        return bookingMapper.toListBookingDtoResponse(result);
    }

    private static Predicate statePredicate(Root<Booking> root, CriteriaBuilder criteriaBuilder, BookingState bookingState, Instant now) {
        return switch (bookingState) {
            case CURRENT -> criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("start"), now),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("end"), now)
            );
            case PAST -> criteriaBuilder.lessThan(root.get("end"), now);
            case FUTURE -> criteriaBuilder.greaterThan(root.get("start"), now);
            case WAITING -> criteriaBuilder.equal(root.get("status"), Status.WAITING);
            case REJECTED -> criteriaBuilder.equal(root.get("status"), Status.REJECTED);
            case ALL -> criteriaBuilder.conjunction();
        };
    }

}
