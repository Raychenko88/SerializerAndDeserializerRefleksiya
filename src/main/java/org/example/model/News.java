package org.example.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private Integer id;
    private String image;
    private List<String> listPhotos;
    private List<String> listVideo;
    private String heading;
    private String text;
    private String author;
}
