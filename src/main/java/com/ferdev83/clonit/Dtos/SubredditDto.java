package com.ferdev83.clonit.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class SubredditDto {
        private Long id;
        private String name;
        private String description;
        private Integer numberOfPost;
}
