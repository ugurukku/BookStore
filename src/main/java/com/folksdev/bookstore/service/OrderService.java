package com.folksdev.bookstore.service;

import com.folksdev.bookstore.models.Book;
import com.folksdev.bookstore.models.Order;
import com.folksdev.bookstore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private final BookService bookService;


    public OrderService(OrderRepository orderRepository, BookService bookService) {
        this.orderRepository = orderRepository;
        this.bookService = bookService;
    }

    public Order putAnOrder(List<Integer> bookIdList, String username) {

        List<Optional<Book>> bookList = bookIdList.stream().map(bookService::findById).collect(Collectors.toList());

        Double totalPrice = bookList.stream()
                .map(optionalBook -> optionalBook.map(Book::getPrice).orElse(0.0))
                .reduce(0.0, Double::sum);

        Order order = Order.builder()
                .bookList(bookIdList)
                .totalPrice(totalPrice)
                .userName(username)
                .build();

        return orderRepository.save(order);
    }
}
