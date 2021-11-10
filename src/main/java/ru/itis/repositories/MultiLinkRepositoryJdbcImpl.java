package ru.itis.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.models.Account;
import ru.itis.models.MultiLink;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.util.*;

public class MultiLinkRepositoryJdbcImpl implements MultiLinkRepository {

    //language=SQL
    private final String SQL_CREATE_MULTI = "insert into multilink (owner_id, multi_name,link) values (?, ?, ?)";

    //language=SQL
    private final String SQL_DELETE_MULTI_BY_ID = "delete from multilink where id = ?";

    //language=SQL
    private final String SQL_DELETE_ACCOUNT_MULTI = "delete from multilink where owner_id = ?";

    //language=SQL
    private final String SQL_UPDATE_CLICKS = "update multilink set clicks = ? where id = ?";

    //language=SQL
    private final String SQL_FIND_BY_ACCOUNT_AND_NAME = "select m.id as m_id, * from multilink m left join account a on a.id = m.owner_id where a.id = ? and m.multi_name = ?";

    //language=SQL
    private final String SQL_FIND_BY_ID = "select m.id as m_id, * from multilink m left join account a on a.id = m.owner_id where m.id = ?";

    //language=SQL
    private final String SQL_FIND_BY_ACCOUNT = "select m.id as m_id, * from multilink m left join account a on a.id = m.owner_id where a.id = ?";

    //language=SQL
    private final String SQL_FIND_ALL = "select m.id as m_id, * from multilink m left join account a on a.id = m.owner_id";

    private final ResultSetExtractor<List<MultiLink>> rse = resultSet -> {
        List<MultiLink> multiLinks = new ArrayList<>();
        Map<Long, Account> accounts = new HashMap<>();

        while (resultSet.next()) {
            Account account;
            if (accounts.containsKey(resultSet.getLong("id"))) {
                account = accounts.get(resultSet.getLong("id"));
            } else {
                account = Account.builder()
                        .id(resultSet.getLong("owner_id"))
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .nickname(resultSet.getString("nickname"))
                        .age(resultSet.getInt("age"))
                        .createdAt(resultSet.getDate("created_at"))
                        .multiLinks(new HashSet<>())
                        .cutLinks(new HashSet<>())
                        .subscribers(new HashSet<>())
                        .subscriptions(new HashSet<>())
                        .build();
                accounts.put(account.getId(), account);
            }

            MultiLink multiLink;
            try {
                multiLink = MultiLink.builder()
                        .id(resultSet.getLong("m_id"))
                        .owner(account)
                        .name(resultSet.getString("multi_name"))
                        .clicks(resultSet.getInt("clicks"))
                        .addedAt(resultSet.getDate("added_at"))
                        .build();

                if(resultSet.getString("link") != null) {
                    multiLink.setLink(new URI(resultSet.getString("link")));
                }

                if(multiLink.getName() != null) multiLinks.add(multiLink);
            } catch (URISyntaxException e) {
                throw new IllegalStateException(e);
            }
        }

        return multiLinks;
    };

    private final JdbcTemplate jdbcTemplate;

    public MultiLinkRepositoryJdbcImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createMulti(MultiLink multiLink) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_MULTI, new String[]{"id", "clicks", "added_at"});

            preparedStatement.setLong(1, multiLink.getOwner().getId());
            preparedStatement.setString(2, multiLink.getName());
            preparedStatement.setString(3, multiLink.getLink().toString());

            return preparedStatement;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();

        Long id = (Long) keys.get("id");
        Integer clicks = (Integer) keys.get("clicks");
        Date addedAt = (Date) keys.get("added_at");

        multiLink.setId(id);
        multiLink.setClicks(clicks);
        multiLink.setAddedAt(addedAt);
    }

    @Override
    public void deleteMulti(MultiLink multiLink) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_MULTI_BY_ID);

            preparedStatement.setLong(1, multiLink.getId());

            return preparedStatement;
        });
    }

    @Override
    public void deleteAllMultiByAccount(Account account) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ACCOUNT_MULTI);

            preparedStatement.setLong(1, account.getId());

            return preparedStatement;
        });
    }

    @Override
    public void updateClicks(MultiLink multiLink) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_CLICKS);

            preparedStatement.setInt(1, multiLink.getClicks() + 1);
            preparedStatement.setLong(2, multiLink.getId());

            return preparedStatement;
        });
    }

    @Override
    public Optional<MultiLink> findByAccountAndName(Account account, String name) {
        List<MultiLink> multiLinks = jdbcTemplate.query(SQL_FIND_BY_ACCOUNT_AND_NAME, rse, account.getId(), name);
        if (multiLinks.size() > 1) throw new IllegalStateException();
        else if (multiLinks.size() == 1) return Optional.of(multiLinks.get(0));
        else return Optional.empty();
    }

    @Override
    public Optional<MultiLink> findById(Long id) {
        List<MultiLink> multiLinks = jdbcTemplate.query(SQL_FIND_BY_ID, rse, id);
        if (multiLinks.size() > 1) throw new IllegalStateException();
        else if (multiLinks.size() == 1) return Optional.of(multiLinks.get(0));
        else return Optional.empty();
    }

    @Override
    public List<MultiLink> findByAccount(Account account) {
        return jdbcTemplate.query(SQL_FIND_BY_ACCOUNT, rse, account.getId());
    }

    @Override
    public List<MultiLink> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rse);
    }
}
