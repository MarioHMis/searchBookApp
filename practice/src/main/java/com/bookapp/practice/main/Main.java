package com.bookapp.practice.main;

import com.bookapp.practice.model.Data;
import com.bookapp.practice.model.DataBooks;
import com.bookapp.practice.service.ApiConsume;
import com.bookapp.practice.service.DataConverter;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ApiConsume apiConsume = new ApiConsume();
    private DataConverter dataConverter = new DataConverter();
    private Scanner scanner = new Scanner(System.in);

    public void showTheMenu() {
       var json = ApiConsume.getData(URL_BASE);
        System.out.println(json);
        var data = dataConverter.getData(json, Data.class);
        System.out.println(data);

        //Top 10 most downloaded books
        System.out.println("Top 10 most downloaded books");
        data.results().stream()
                .sorted(Comparator.comparing(DataBooks::numberOfDownloads).reversed())
                .limit(10)
                .map(l -> l.title().toUpperCase())
                .forEach(System.out::println);

        //Search book by name
        System.out.println("Enter the book title of you want to search");
        var bookTitle = scanner.nextLine();
        json = ApiConsume.getData(URL_BASE+"?search="+bookTitle.replace(" ", "+"));
        var searchData = dataConverter.getData(json, Data.class);
        Optional<DataBooks> searchBook = searchData.results().stream()
                .filter(l -> l.title().toUpperCase().contains(bookTitle.toUpperCase()))
                .findFirst();
        if (searchBook.isPresent()) {
            System.out.println(searchBook.get());
        } else {
            System.out.println("Book not found");
        }

        //Working wit stats
        DoubleSummaryStatistics stats = data.results().stream()
                .filter(d-> d.numberOfDownloads() > 0)
                .collect(Collectors.summarizingDouble(DataBooks::numberOfDownloads));
        System.out.println("Number of downloaded books: " + stats.getAverage());
        System.out.println("Maximum number of downloads: " + stats.getMax());
        System.out.println("Minimum number of downloads: " + stats.getMin());
        System.out.println("Number of records evaluated to calculate statistics: " + stats.getCount());
    }

}
