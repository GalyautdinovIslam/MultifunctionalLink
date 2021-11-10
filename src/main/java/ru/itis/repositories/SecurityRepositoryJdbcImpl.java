package ru.itis.repositories;

import ru.itis.models.Account;
import ru.itis.repositories.jdbcTemplate.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Optional;

public class SecurityRepositoryJdbcImpl implements SecurityRepository {

    //language=SQL
    private final String SQL_FIND_BY_RECOVERY_CODE = "select * from recovery r " +
            "left join account a on r.account_id = a.id where r.recovery_code = ?";

    //language=SQL
    private final String SQL_CREATE_RECOVERY_CODE = "insert into recovery(account_id, recovery_code) values (?, ?)";

    //language=SQL
    private final String SQL_DELETE_RECOVERY_CODE = "delete from recovery where account_id = ?";

    //language=SQL
    private final String SQL_FIND_BY_SIGN_UP_CODE = "select * from sign_up s " +
            "left join account a on s.account_id = a.id where s.code = ?";

    //language=SQL
    private final String SQL_CREATE_SIGN_UP_CODE = "insert into sign_up(account_id, code) values (?, ?)";

    //language=SQL
    private final String SQL_DELETE_SIGN_UP_CODE = "delete from sign_up where account_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public SecurityRepositoryJdbcImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private Optional<Account> getAccountByCode(String sql, String code) {
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                return Optional.of(Account.builder()
                        .id(resultSet.getLong("id"))
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .nickname(resultSet.getString("nickname"))
                        .age(resultSet.getInt("age"))
                        .createdAt(resultSet.getDate("created_at"))
                        .multiLinks(new HashSet<>())
                        .cutLinks(new HashSet<>())
                        .subscribers(new HashSet<>())
                        .subscriptions(new HashSet<>())
                        .build());
            } else {
                return Optional.empty();
            }
        }, code);
    }

    @Override
    public Optional<Account> findByRecoveryCode(String recoveryCode) {
        return getAccountByCode(SQL_FIND_BY_RECOVERY_CODE, recoveryCode);
    }

    @Override
    public void createRecoveryCode(Account account, String recoveryCode) {
        jdbcTemplate.update(SQL_CREATE_RECOVERY_CODE, account.getId(), recoveryCode);
    }

    @Override
    public void deleteRecoveryCode(Long id) {
        jdbcTemplate.update(SQL_DELETE_RECOVERY_CODE, id);
    }

    @Override
    public Optional<Account> findBySignUpCode(String code) {
        return getAccountByCode(SQL_FIND_BY_SIGN_UP_CODE, code);
    }

    @Override
    public void createSignUpCode(Account account, String code) {
        jdbcTemplate.update(SQL_CREATE_SIGN_UP_CODE, account.getId(), code);
    }

    @Override
    public void deleteSignUpCode(Long id) {
        jdbcTemplate.update(SQL_DELETE_SIGN_UP_CODE, id);
    }
}
