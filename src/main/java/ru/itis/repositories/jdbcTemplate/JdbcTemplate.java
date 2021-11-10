package ru.itis.repositories.jdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return rse.extractData(resultSet);
            } catch (SQLException ex) {
                throw new IllegalStateException(ex);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public int update(String sql, Object... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }

            return statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
