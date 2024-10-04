package ru.arkhipov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.swing.text.Position;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @NotBlank(message = "UID не может быть пустым")
    private String uid; // Уникальный идентификатор сообщение


    private String operationUid; // Уникальный идентификатор операции

    private Systems systemName; // Имя системы отправителя
    private String systemTime; // Время создания сообщения

    private String source; // Наименование ресурса
    private Positions position; // Должность
    private Double salary; // Зарплата
    private Double bonus; // Бонус
    private Integer workDays; // Кол-во рабочих дней


    private Integer communicationId; // Уникальный идентификатор коммуникации

    private Integer templateId; // Уникальный идентификатор шаблона
    private Integer productCode; // Код продукта
    private Integer smsCode; // Смс код

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';

    }
}
