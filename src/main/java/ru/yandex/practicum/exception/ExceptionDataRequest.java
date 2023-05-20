package ru.yandex.practicum.exception;

public class ExceptionDataRequest extends RuntimeException {

    public String getNameExcept() {
        return nameExcept;
    }

    private final String nameExcept;

    public ExceptionDataRequest(String nameExcept, String message) {
        super(message);
        this.nameExcept = nameExcept;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
