package alicjawozniak.repository;

import alicjawozniak.model.ImportedFont;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by alicja on 29.10.16.
 */
@Repository
public interface ImportedFontRepository extends JpaRepository<ImportedFont, Long> {
    ImportedFont findOneByNumber(int number);
}
