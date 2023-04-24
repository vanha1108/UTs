package com.example.demo.web.controller;

import com.example.demo.response.Output;
import com.example.demo.response.PagingMeta;
import com.example.demo.response.ResponseHeader;
import com.example.demo.web.base.RestData;
import com.example.demo.web.base.VsResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public abstract class BaseController {

    protected ResponseHeader responseHeader;

    /**
     * get response
     *
     * @param output Output
     * @return PagingMeta
     */
    protected ResponseEntity<RestData<?>> vsResponse(Output output, PagingMeta pagingMeta) {

        // データが存在しない場合は404エラー
        if (Objects.isNull(output)) {
            return notFoundResponse();
        }

        // レスポンス出力
        return successResponse(output, pagingMeta);
    }

    /**
     * get response
     *
     * @param output Output
     * @return ResponseEntity<RestData < ?>>
     */
    protected ResponseEntity<RestData<?>> vsResponse(Output output) {

        // データが存在しない場合は404エラー
        if (Objects.isNull(output)) {
            return notFoundResponse();
        }

        // レスポンス出力
        return successResponse(output);
    }

    /**
     * get success response
     *
     * @param output Output
     * @return ResponseEntity<RestData < ?>>
     */
    protected ResponseEntity<RestData<?>> successResponse(Output output) {
        return VsResponseUtil.ok( output);
    }

    /**
     * get success response
     *
     * @param output     Output
     * @param pagingMeta PagingMeta
     * @return ResponseEntity<RestData < ?>>
     */
    protected ResponseEntity<RestData<?>> successResponse(Output output, PagingMeta pagingMeta) {
        return VsResponseUtil.ok(this.responseHeader.getHeader(), HttpStatus.OK, output, pagingMeta);
    }

    /**
     * get success response
     *
     * @return ResponseEntity<RestData < ?>>
     */
    protected ResponseEntity<RestData<?>> successResponse() {
        return VsResponseUtil.ok( HttpStatus.OK);
    }

    /**
     * get not found response
     *
     * @return ResponseEntity<RestData < ?>>
     */
    private ResponseEntity<RestData<?>> notFoundResponse() {
        String notFoundMsg = "Not Found!";
        return VsResponseUtil.notFound(this.responseHeader.getHeader(), notFoundMsg);
    }

    /**
     * get response
     *
     * @param output Output
     * @return PagingMeta
     */
    protected ResponseEntity<RestData<?>> vsResponse(Output output, PagingMeta pagingMeta, Output outputUrl) {

        // データが存在しない場合は404エラー
        if (Objects.isNull(output)) {
            return notFoundResponse();
        }

        // レスポンス出力
        return successResponse(output, pagingMeta, outputUrl);
    }

    /**
     * get success response
     *
     * @param output     Output
     * @param pagingMeta PagingMeta
     * @return ResponseEntity<RestData < ?>>
     */
    protected ResponseEntity<RestData<?>> successResponse(Output output, PagingMeta pagingMeta, Output outputUrl) {
        return VsResponseUtil.ok(this.responseHeader.getHeader(), HttpStatus.OK, output, pagingMeta, outputUrl);
    }
}
