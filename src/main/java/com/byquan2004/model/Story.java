package com.byquan2004.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Story {
    private String title;
    private String url;
    private int points;
    private String author;
    private String time;
    private int commentCount;
    private int rank;
} 