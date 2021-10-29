package ru.itis.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.exceptions.AlreadySubscribedException;
import ru.itis.exceptions.AlreadyUnsubscribedException;
import ru.itis.models.Account;
import ru.itis.models.CutLink;
import ru.itis.models.MultiLink;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class AccountRepositoryJdbcImpl implements AccountRepository {

    //language=SQL
    private final String SQL_CREATE_ACCOUNT = "insert into account(email, password, nickname) values (?, ?, ?)";

    //language=SQL
    private final String SQL_UPDATE_PASSWORD = "update account set password = ? where id = ?";

    //language=SQL
    private final String SQL_UPDATE_AGE = "update account set age = ? where id = ?";

    //language=SQL
    private final String SQL_CHECK_SUB = "select * from subscription where who_id = ? and sub_to_id = ?";

    //language=SQL
    private final String SQL_SUBSCRIBE = "insert into subscription(who_id, sub_to_id) values (?, ?)";

    //language=SQL
    private final String SQL_UNSUBSCRIBE = "delete from subscription where who_id = ? and sub_to_id = ?";

    //language=SQL
    private final String SQL_DELETE_ACCOUNT = "delete from account where id = ?";

    //language=SQL
    private final String SQL_DELETE_SUBS = "delete from subscription where who_id = ? or sub_to_id = ?";

    //language=SQL
    private final String SQL_FIND_BY_EMAIL = "select * from account a " +
            "left join cutlink c on a.id = c.owner_id " +
            "left join multilink m on a.id = m.owner_id " +
            "left join subscription s on a.id = s.who_id " +
            "left join account aa on s.sub_to_id = aa.id " +
            "where a.email = ?";

    //language=SQL
    private final String SQL_FIND_BY_NICKNAME = "select * from account a " +
            "left join cutlink c on a.id = c.owner_id " +
            "left join multilink m on a.id = m.owner_id " +
            "left join subscription s on a.id = s.who_id " +
            "left join account aa on s.sub_to_id = aa.id " +
            "where a.nickname = ?";

    //language=SQL
    private final String SQL_FIND_BY_ID = "select * from account a " +
            "left join cutlink c on a.id = c.owner_id " +
            "left join multilink m on a.id = m.owner_id " +
            "left join subscription s on a.id = s.who_id " +
            "left join account aa on s.sub_to_id = aa.id " +
            "where a.id = ?";

    //language=SQL
    private final String SQL_FIND_ALL = "select * from account a " +
            "left join cutlink c on a.id = c.owner_id " +
            "left join multilink m on a.id = m.owner_id " +
            "left join subscription s on a.id = s.who_id " +
            "left join account aa on s.sub_to_id = aa.id ";

    private final ResultSetExtractor<List<Account>> rse = resultSet -> {
        Map<Long, Account> accounts = new HashMap<>();
        Map<Account, Set<Long>> accountsId = new HashMap<>();
        Map<Long, CutLink> cutLinks = new HashMap<>();
        Map<Long, MultiLink> multiLinks = new HashMap<>();
        Map<Long, Account> secondlyAccounts = new HashMap<>();

        while (resultSet.next()) {
            Account account;
            Set<Long> ids;
            CutLink cutLink;
            MultiLink multiLink;
            Account secondlyAccount;

            if (accounts.containsKey(resultSet.getLong("a.id"))) {
                account = accounts.get(resultSet.getLong("a.id"));
            } else {
                account = Account.builder()
                        .id(resultSet.getLong("a.id"))
                        .email(resultSet.getString("a.email"))
                        .password(resultSet.getString("a.password"))
                        .nickname(resultSet.getString("a.nickname"))
                        .age(resultSet.getInt("a.age"))
                        .createdAt(resultSet.getDate("a.created_at"))
                        .editedAt(resultSet.getDate("a.edited_at"))
                        .build();
                accounts.put(account.getId(), account);
            }

            if (!accountsId.containsKey(account)) {
                accountsId.put(account, new HashSet<>());
            }
            ids = accountsId.get(account);

            if (cutLinks.containsKey(resultSet.getLong("c.id"))) {
                cutLink = cutLinks.get(resultSet.getLong("c.id"));
            } else {
                cutLink = CutLink.builder()
                        .id(resultSet.getLong("c.id"))
                        .cut(resultSet.getString("c.cut"))
                        .link(resultSet.getString("c.link"))
                        .clicks(resultSet.getInt("c.clicks"))
                        .addedAt(resultSet.getDate("c.added_at"))
                        .build();
                cutLinks.put(cutLink.getId(), cutLink);
            }

            if (multiLinks.containsKey(resultSet.getLong("m.id"))) {
                multiLink = multiLinks.get(resultSet.getLong("m.id"));
            } else {
                multiLink = MultiLink.builder()
                        .id(resultSet.getLong("m.id"))
                        .link(resultSet.getString("m.link"))
                        .clicks(resultSet.getInt("m.clicks"))
                        .addedAt(resultSet.getDate("m.added_at"))
                        .build();
                multiLinks.put(multiLink.getId(), multiLink);
            }

            if (secondlyAccounts.containsKey(resultSet.getLong("aa.id"))) {
                secondlyAccount = secondlyAccounts.get(resultSet.getLong("aa.id"));
            } else {
                secondlyAccount = Account.builder()
                        .id(resultSet.getLong("aa.id"))
                        .email(resultSet.getString("aa.email"))
                        .password(resultSet.getString("aa.password"))
                        .nickname(resultSet.getString("aa.nickname"))
                        .age(resultSet.getInt("aa.age"))
                        .createdAt(resultSet.getDate("aa.created_at"))
                        .editedAt(resultSet.getDate("aa.edited_at"))
                        .build();
                secondlyAccounts.put(secondlyAccount.getId(), secondlyAccount);
            }

            ids.add(resultSet.getLong("s.sub_to_id"));
            cutLink.setOwner(account);
            multiLink.setOwner(account);
        }

        List<Account> accountList = new ArrayList<>(accounts.values());
        for (Account account : accountList) {
            Set<Long> ids = accountsId.get(account);
            for (Long id : ids) {
                if (accounts.containsKey(id)) {
                    accounts.get(id).getSubscribers().add(account);
                    account.getSubscriptions().add(accounts.get(id));
                } else {
                    secondlyAccounts.get(id).getSubscribers().add(account);
                    account.getSubscriptions().add(secondlyAccounts.get(id));
                }
            }
        }

        return accountList;
    };

    private final JdbcTemplate jdbcTemplate;

    public AccountRepositoryJdbcImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createAccount(Account account) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_ACCOUNT, new String[]{"id", "created_at", "edited_at"});

            preparedStatement.setString(1, account.getEmail());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setString(3, account.getNickname());

            return preparedStatement;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();

        Long id = (Long) keys.get("id");
        Date createdAt = (Date) keys.get("created_at");
        Date editedAt = (Date) keys.get("edited_at");

        account.setId(id);
        account.setCreatedAt(createdAt);
        account.setEditedAt(editedAt);
    }

    @Override
    public void updatePassword(Account account) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PASSWORD);

            preparedStatement.setString(1, account.getPassword());
            preparedStatement.setLong(2, account.getId());

            return preparedStatement;
        });
    }

    @Override
    public void updateAge(Account account) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_AGE);

            preparedStatement.setInt(1, account.getAge());
            preparedStatement.setLong(2, account.getId());

            return preparedStatement;
        });
    }

    @Override
    public void subscribe(Account who, Account subTo) throws AlreadySubscribedException {
        if (checkSubscription(who, subTo)) throw new AlreadySubscribedException();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SUBSCRIBE);

            preparedStatement.setLong(1, who.getId());
            preparedStatement.setLong(2, subTo.getId());

            return preparedStatement;
        });

        who.getSubscriptions().add(subTo);
        subTo.getSubscribers().add(who);
    }

    @Override
    public void unsubscribe(Account who, Account subTo) throws AlreadyUnsubscribedException {
        if (!checkSubscription(who, subTo)) throw new AlreadyUnsubscribedException();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UNSUBSCRIBE);

            preparedStatement.setLong(1, who.getId());
            preparedStatement.setLong(2, subTo.getId());

            return preparedStatement;
        });

        who.getSubscriptions().remove(subTo);
        subTo.getSubscribers().remove(who);
    }

    @Override
    public void deleteAccount(Account account) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_SUBS);

            preparedStatement.setLong(1, account.getId());
            preparedStatement.setLong(2, account.getId());

            return preparedStatement;
        });

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ACCOUNT);

            preparedStatement.setLong(1, account.getId());

            return preparedStatement;
        });
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        List<Account> accounts = jdbcTemplate.query(SQL_FIND_BY_EMAIL, rse, email);
        if (accounts.size() > 1) throw new IllegalStateException();
        else if (accounts.size() == 1) return Optional.of(accounts.get(0));
        else return Optional.empty();
    }

    @Override
    public Optional<Account> findByNickname(String nickname) {
        List<Account> accounts = jdbcTemplate.query(SQL_FIND_BY_NICKNAME, rse, nickname);
        if (accounts.size() > 1) throw new IllegalStateException();
        else if (accounts.size() == 1) return Optional.of(accounts.get(0));
        else return Optional.empty();
    }

    @Override
    public Optional<Account> findById(Long id) {
        List<Account> accounts = jdbcTemplate.query(SQL_FIND_BY_NICKNAME, rse, id);
        if (accounts.size() > 1) throw new IllegalStateException();
        else if (accounts.size() == 1) return Optional.of(accounts.get(0));
        else return Optional.empty();
    }

    @Override
    public List<Account> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rse);
    }

    private boolean checkSubscription(Account who, Account subTo) {
        return jdbcTemplate.query(SQL_CHECK_SUB, ResultSet::next, who.getId(), subTo.getId());
    }
}
