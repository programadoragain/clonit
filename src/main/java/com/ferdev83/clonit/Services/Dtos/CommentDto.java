package com.ferdev83.clonit.Services.Dtos;

import lombok.*;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private Instant createdDate;
    private String text;
    private String username;
}
