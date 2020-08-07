package com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos;

import lombok.*;
import lombok.experimental.Accessors;

@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String tenantId;
}