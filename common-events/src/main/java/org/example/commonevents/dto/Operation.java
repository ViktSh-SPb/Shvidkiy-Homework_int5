package org.example.commonevents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Operation {
    CREATE("Аккаунт создан", "Здравствуйте. Ваш аккаунт на сайте ваш сайт был успешно создан."),
    DELETE("Аккаунт удален", "Здравствуйте. Ваш аккаунт был удален.");

    private final String subject;
    private final String text;
}
