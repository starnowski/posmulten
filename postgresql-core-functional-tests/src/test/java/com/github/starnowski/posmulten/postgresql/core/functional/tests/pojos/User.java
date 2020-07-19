package com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String tenantId;
}
