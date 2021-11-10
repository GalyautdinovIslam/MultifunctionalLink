package ru.itis.repositories.jdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractor<T> {
    T extractData(ResultSet row) throws SQLException;
}
