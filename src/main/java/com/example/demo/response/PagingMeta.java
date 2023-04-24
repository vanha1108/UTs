package com.example.demo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingMeta {
    private int total;

    /**
     * Current page number
     */
    private int pageNum;

    /**
     * Number of records on a page
     */
    private int pageSize;

    /**
     * Index of first record on current page
     */
    @JsonIgnore
    private int from;

    /**
     * Index of last record on current page
     */
    @JsonIgnore
    private int to;
    /**
     * Sort of type by ascending or descending (ASC/DESC)
     */

    @JsonIgnore
    private String sortType;

    /**
     * Sort by column name
     */
    @JsonIgnore
    private String sortBy;


    @JsonIgnore
    public boolean isSort() {
        return this.sortType != null && this.sortBy != null && !this.sortType.isEmpty() && !this.sortBy
                .isEmpty();
    }

}
