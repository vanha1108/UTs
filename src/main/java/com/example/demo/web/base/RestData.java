package com.example.demo.web.base;

import com.example.demo.response.PagingMeta;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonFilter("restDataFilter")
public class RestData<T> {

    private RestStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T userMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String devMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PagingMeta meta;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T breadcrumb;

    public RestData() {
    }

    public RestData(T data) {
        this.status = RestStatus.SUCCESS;
        this.data = data;
    }

    public RestData(RestStatus status, T userMessage, T data) {
        this.status = status;
        this.userMessage = userMessage;
        this.data = data;
    }

    public RestData(RestStatus status, T userMessage, String devMessage, T data) {
        this.status = status;
        this.userMessage = userMessage;
        this.devMessage = devMessage;
        this.data = data;
    }

    public RestData(T data, PagingMeta meta) {
        this.status = RestStatus.SUCCESS;
        this.data = data;
        this.meta = meta;
    }

    public RestData(T data, PagingMeta meta, T breadcrumb) {
        this.status = RestStatus.SUCCESS;
        this.data = data;
        this.meta = meta;
        this.breadcrumb = breadcrumb;
    }

    public static RestData<?> error(Object userMessage, String devMessage) {
        return new RestData<>(RestStatus.ERROR, userMessage, devMessage, null);
    }

    public static RestData<?> processing(Object userMessage, String devMessage) {
        return new RestData<>(RestStatus.PROCESSING, userMessage, devMessage, null);
    }

    public static RestData<?> error(Object userMessage) {
        return new RestData<>(RestStatus.ERROR, userMessage, null);
    }

    public RestStatus getStatus() {
        return status;
    }

    public void setStatus(RestStatus status) {
        this.status = status;
    }

    public T getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(T userMessage) {
        this.userMessage = userMessage;
    }

    public String getDevMessage() {
        return devMessage;
    }

    public void setDevMessage(String devMessage) {
        this.devMessage = devMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public PagingMeta getMeta() {
        return meta;
    }

    public void setMeta(PagingMeta meta) {
        this.meta = meta;
    }

    public T getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(T breadcrumb) {
        this.breadcrumb = breadcrumb;
    }
}
