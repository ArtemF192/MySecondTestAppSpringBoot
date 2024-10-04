package ru.arkhipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MySecondTestAppSpringBoot.model.*;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.SendToSecondInstanceService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ValidationService;
import ru.arkhipov.MySecondTestAppSpringBoot.util.DateTimeUtil;


import java.util.Date;


@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService1;
    private final ModifyResponseService modifyResponseService2;
    private final ModifyRequestService modifyRequestService1;
    private final ModifyRequestService modifyRequestService2;
    private final SendToSecondInstanceService sendToSecondInstanceService;
    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService1,
                        @Qualifier("ModifyOperationUidResponseService") ModifyResponseService modifyResponseService2,
                        @Qualifier("ModifySystemNameRequestService") ModifyRequestService modifyRequestService1,
                        @Qualifier("ModifySourceRequestService") ModifyRequestService modifyRequestService2,
                        @Qualifier("SendToSecondInstanceService") SendToSecondInstanceService sendToSecondInstanceService) {
        this.validationService = validationService;
        this.modifyResponseService1 = modifyResponseService1;
        this.modifyResponseService2 = modifyResponseService2;
        this.modifyRequestService1 = modifyRequestService1;
        this.modifyRequestService2 = modifyRequestService2;
        this.sendToSecondInstanceService = sendToSecondInstanceService;
    }

    @RequestMapping(value = "/feedback", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        log.info("request: {}", request);

        Date requestDateTime = new Date();

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("response: {}", response);
        try {
            validationService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.error("response: {}", response);
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.error("response: {}", response);
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.error("response: {}", response);
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response = modifyResponseService1.modify(response);
        response = modifyResponseService2.modify(response);
        log.info("response: {}", response);


        request = modifyRequestService1.modify(request);
        request = modifyRequestService2.modify(request);
        request.setSystemTime(DateTimeUtil.getCustomFormat().format(requestDateTime));

        sendToSecondInstanceService.send(request);
        log.info("request sent to second service, request: {}", request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
