package alicjawozniak.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString(exclude = "id")
public class ImportedFont {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer number;

    @NotNull
    private String fontName;
}
