package ru.arkhipov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Request;

@Service
public interface ModifyRequestService {
    Request modify(Request request);
}
