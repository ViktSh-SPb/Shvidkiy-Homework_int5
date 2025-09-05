package org.example.commonevents.dto;

public enum Operation {
    CREATE("Аккаунт создан", "Здравствуйте. Ваш аккаунт на сайте ваш сайт был успешно создан."),
    DELETE("Аккаунт удален", "Здравствуйте. Ваш аккаунт был удален.");

    private final String subject;
    private final String text;

    Operation(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }
}
