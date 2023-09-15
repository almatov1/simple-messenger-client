package com.smessenger.client.shared.dao;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class MainResponse {
    private Timestamp timestamp;
    private String path;
    private int status;
    private String error;
    private Object body;
}
