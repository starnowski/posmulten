package com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos;

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
public class UserGroup {
    private Long userId;
    private String groupUuid;
    private String tenantId;
}
