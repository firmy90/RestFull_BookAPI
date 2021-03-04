package pl.firmy90.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Book extends RepresentationModel<Book> {
    private Integer id;
    private String isbn;
    private String title;
    private String author;
    private String type;
}
