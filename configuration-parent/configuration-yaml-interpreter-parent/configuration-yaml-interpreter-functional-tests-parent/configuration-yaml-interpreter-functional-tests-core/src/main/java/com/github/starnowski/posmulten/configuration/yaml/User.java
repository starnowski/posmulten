package com.github.starnowski.posmulten.configuration.yaml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    private Long id;
    private String name;
    private String tenantId;
}
