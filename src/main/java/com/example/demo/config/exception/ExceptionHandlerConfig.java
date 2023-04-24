package com.example.demo.config.exception;

import com.example.demo.constants.CommonConstant;
import com.example.demo.web.base.RestData;
import com.example.demo.web.base.VsResponseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    private static final Log LOG = LogFactory.getLog(ExceptionHandlerConfig.class);

    private final MessageSource messageSource;

    public ExceptionHandlerConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = {InvalidException.class})
    protected ResponseEntity<RestData<?>> handleVsException(InvalidException ex) {
        LOG.error(ex.getMessage(), ex);

        String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message, ex.getDevMessage());
    }

    @ExceptionHandler(value = {VsException.class})
    protected ResponseEntity<RestData<?>> handleVsException(VsException ex) {
        LOG.error(ex.getMessage(), ex);

        String message = messageSource.getMessage(ex.getMessage(), ex.getObjects(), LocaleContextHolder.getLocale());
        return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getDevMessage());
    }

    @ExceptionHandler(value = {ProcessingException.class})
    protected ResponseEntity<RestData<?>> handleProcessingException(ProcessingException ex) {
        LOG.error(ex.getMessage(), ex);

        String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        return VsResponseUtil.error(HttpStatus.OK, message, ex.getDevMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<RestData<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        LOG.error(ex.getMessage(), ex);

        List<String> lsMessage = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String message;
            try {
                message = messageSource.getMessage(violation.getMessage(), null, LocaleContextHolder.getLocale());
            } catch (Exception e) {
                message = ex.getMessage();
            }
            lsMessage.add(message);
        }

        return VsResponseUtil.error(HttpStatus.BAD_REQUEST, String.join(CommonConstant.SPACE, lsMessage), ex.getCause() != null ?
                ex.getCause().getMessage() : ex.getMessage());
    }

    @ExceptionHandler(value = {BindException.class})
    protected ResponseEntity<RestData<?>> handleBindException(BindException ex) {
        LOG.error(ex.getMessage(), ex);

        List<String> lsMessage = new ArrayList<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            String message;
            try {
                message = messageSource.getMessage(Objects.requireNonNull(fieldError.getDefaultMessage()), null,
                        LocaleContextHolder.getLocale());
            } catch (Exception e) {
                message = ex.getMessage();
            }
            lsMessage.add(message);
        }

        return VsResponseUtil.error(HttpStatus.BAD_REQUEST, String.join(CommonConstant.SPACE, lsMessage), ex.getCause() != null ?
                ex.getCause().getMessage() : ex.getMessage());
    }

//  @ExceptionHandler(value = {AccessDeniedException.class})
//  protected ResponseEntity<RestData<?>> handleAccessDeniedException(Exception ex) {
//    LOG.error(ex.getMessage(), ex);
//
//    String message = messageSource.getMessage("err.exception.general", null, LocaleContextHolder.getLocale());
//    return VsResponseUtil.error(HttpStatus.FORBIDDEN, message, ex.getCause() != null ?
//        ex.getCause().getMessage() : ex.getMessage());
//  }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<RestData<?>> handleException(Exception ex) {
        LOG.error(ex.getMessage(), ex);

        if (ex.getCause() instanceof VsException || (ex.getCause().getCause() != null && ex.getCause().getCause() instanceof VsException)) {
            VsException vsException = (VsException) (ex.getCause().getCause() == null ? ex.getCause() : ex.getCause().getCause());
            String message = messageSource.getMessage(vsException.getUserMessage(), vsException.getObjects(), LocaleContextHolder.getLocale());
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, message, vsException.getDevMessage());
        }
        if (ex.getCause() instanceof ProcessingException || (ex.getCause().getCause() != null && ex.getCause().getCause() instanceof ProcessingException)) {
            ProcessingException processingException = (ProcessingException) (ex.getCause().getCause() == null ? ex.getCause() : ex.getCause().getCause());
            String message = messageSource.getMessage(processingException.getUserMessage(), null, LocaleContextHolder.getLocale());
            return VsResponseUtil.processing(HttpStatus.OK, message, processingException.getDevMessage());
        }
        if (ex.getCause() instanceof InvalidException || (ex.getCause().getCause() != null && ex.getCause().getCause() instanceof InvalidException)) {
            InvalidException invalidException = (InvalidException) (ex.getCause().getCause() == null ? ex.getCause() : ex.getCause().getCause());
            String message = messageSource.getMessage(invalidException.getUserMessage(), invalidException.getObjects(),
                    LocaleContextHolder.getLocale()
            );
            return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message, invalidException.getDevMessage());
        }
        String message = messageSource.getMessage("err.exception.general", null, LocaleContextHolder.getLocale());
        return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getCause() != null ?
                ex.getCause().getMessage() : ex.getMessage());
    }

}
