package ru.arkhipov.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Response {
    private String uid; // Уникальный идентификатор сообщение
    private String operationUid; // Уникальный идентификатор операции
    private String systemTime; // Время создания сообщения
    private Codes code; // Код успешности выполнения
    private Double annualBonus; // Годовая премия
    private ErrorCodes errorCode; // Код ошибки
    private ErrorMessages errorMessage; // Сообщение ошибки
}
