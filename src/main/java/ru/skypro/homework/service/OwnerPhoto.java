package ru.skypro.homework.service;

/**
 * Интерфейс реализации хозяина фото
 */
public interface OwnerPhoto {

    /**
     * Получение идентификатора хозяина фото
     * @return - идентификатор
     */
    Integer getId();

    /**
     * Получение типа фото {@code Avatar} или {@code Picture}
     * @return - тип фото
     */
    String getTypePhoto();
}
