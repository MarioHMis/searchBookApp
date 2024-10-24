package com.bookapp.practice.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record AutorData(
        @JsonAlias("name") String name,
        @JsonAlias("birth_year") String birthYear
) {
}
