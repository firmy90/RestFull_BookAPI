package pl.firmy90.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
public class Lending extends RepresentationModel<Lending> {
    private String lenderUsername;
    private Book book;

}
