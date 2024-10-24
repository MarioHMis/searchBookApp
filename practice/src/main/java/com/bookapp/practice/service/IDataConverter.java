package com.bookapp.practice.service;

public interface IDataConverter {

        <T> T getData(String json, Class<T> clazz);


}
