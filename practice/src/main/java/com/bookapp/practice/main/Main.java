package com.bookapp.practice.main;

import com.bookapp.practice.model.Data;
import com.bookapp.practice.model.DataBooks;
import com.bookapp.practice.service.ApiConsume;
import com.bookapp.practice.service.DataConverter;

import java.util.Comparator;

public class Main {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ApiConsume apiConsume = new ApiConsume();
    private DataConverter dataConverter = new DataConverter();

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
                .forEach(System.out::println)
                ;

    }
}
