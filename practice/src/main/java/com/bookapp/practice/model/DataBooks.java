package com.bookapp.practice.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public record DataBooks(
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<AutorData> authors,
        @JsonAlias("languages") List<String> languagesList,
        @JsonAlias("download_count") Double numberOfDownloads
) {
}
