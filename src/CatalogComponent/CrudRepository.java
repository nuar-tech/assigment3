package CatalogComponent;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    Optional<T> findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;

    Book save(Book book) throws SQLException;

    boolean delete(ID id) throws SQLException;
    int count() throws SQLException;
}
