package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrowdinParams {
    private Long projectId;
    private String token;
    private String wildcard;
}
