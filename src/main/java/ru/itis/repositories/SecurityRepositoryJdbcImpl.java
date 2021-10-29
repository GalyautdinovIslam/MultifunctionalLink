package ru.itis.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.itis.models.Account;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Optional;

public class SecurityRepositoryJdbcImpl implements SecurityRepository {

    //language=SQL
    private final String SQL_FIND_BY_RECOVERY_CODE = "select * from security s " +
            "left join account a on s.account_id = a.id where recovery_code = ?";

    //language=SQL
    private final String SQL_CREATE_RECOVERY_CODE = "insert into security(account_id, recovery_code) values (?, ?)";

    //language=SQL
    private final String SQL_DELETE_RECOVERY_CODE = "delete from security where recovery_code = ?";

    private final JdbcTemplate jdbcTemplate;

    public SecurityRepositoryJdbcImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Account> findByRecoveryCode(String recoveryCode) {
        return jdbcTemplate.query(SQL_FIND_BY_RECOVERY_CODE, resultSet -> {
            if (resultSet.next()) {
                return Optional.of(Account.builder()
                        .id(resultSet.getLong("a.id"))
                        .email(resultSet.getString("a.email"))
                        .password(resultSet.getString("a.password"))
                        .nickname(resultSet.getString("a.nickname"))
                        .age(resultSet.getInt("a.age"))
                        .createdAt(resultSet.getDate("a.created_at"))
                        .editedAt(resultSet.getDate("a.edited_at"))
                        .build());
            } else {
                return Optional.empty();
            }
        }, recoveryCode);
    }

    @Override
    public void createRecoveryCode(Account account, String recoveryCode) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_RECOVERY_CODE);

            preparedStatement.setLong(1, account.getId());
            preparedStatement.setString(2, recoveryCode);

            return preparedStatement;
        });
    }

    @Override
    public void deleteRecoveryCode(String recoveryCode) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_RECOVERY_CODE);

            preparedStatement.setString(1, recoveryCode);

            return preparedStatement;
        });
    }
}
