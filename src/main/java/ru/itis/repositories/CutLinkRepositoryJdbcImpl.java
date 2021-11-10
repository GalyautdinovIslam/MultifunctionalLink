package ru.itis.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.models.Account;
import ru.itis.models.CutLink;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.util.*;

public class CutLinkRepositoryJdbcImpl implements CutLinkRepository {

    //language=SQL
    private final String SQL_CREATE_CUT = "insert into cutlink (owner_id, cut, link) values (?, ?, ?)";

    //language=SQL
    private final String SQL_DELETE_CUT_BY_ID = "delete from cutlink where id = ?";

    //language=SQL
    private final String SQL_DELETE_ACCOUNT_CUTS = "delete from cutlink where owner_id = ?";

    //language=SQL
    private final String SQL_UPDATE_CLICKS = "update cutlink set clicks = ? where id = ?";

    //language=SQL
    private final String SQL_FIND_BY_ID = "select c.id as c_id, * from cutlink c left join account a on a.id = c.owner_id where c.id = ?";

    //language=SQL
    private final String SQL_FIND_BY_CUT = "select c.id as c_id, * from cutlink c left join account a on a.id = c.owner_id where c.cut = ?";

    //language=SQL
    private final String SQL_FIND_BY_ACCOUNT = "select c.id as c_id, * from cutlink c left join account a on a.id = c.owner_id where a.id = ?";

    //language=SQL
    private final String SQL_FIND_ALL = "select c.id as c_id, * from cutlink c left join account a on a.id = c.owner_id";

    private final ResultSetExtractor<List<CutLink>> rse = resultSet -> {
        List<CutLink> cutLinks = new ArrayList<>();
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

            CutLink cutLink;
            try {
                cutLink = CutLink.builder()
                        .id(resultSet.getLong("c_id"))
                        .owner(account)
                        .cut(resultSet.getString("cut"))
                        .clicks(resultSet.getInt("clicks"))
                        .addedAt(resultSet.getDate("added_at"))
                        .build();

                if(resultSet.getString("link") != null) {
                    cutLink.setLink(new URI(resultSet.getString("link")));
                }

                if(cutLink.getCut() != null) cutLinks.add(cutLink);
            } catch (URISyntaxException e) {
                throw new IllegalStateException(e);
            }
        }

        return cutLinks;
    };

    private final JdbcTemplate jdbcTemplate;

    public CutLinkRepositoryJdbcImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createCut(CutLink cutLink) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_CUT, new String[]{"id", "clicks", "added_at"});

            preparedStatement.setLong(1, cutLink.getOwner().getId());
            preparedStatement.setString(2, cutLink.getCut());
            preparedStatement.setString(3, cutLink.getLink().toString());

            return preparedStatement;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();

        Long id = (Long) keys.get("id");
        Integer clicks = (Integer) keys.get("clicks");
        Date addedAt = (Date) keys.get("added_at");

        cutLink.setId(id);
        cutLink.setClicks(clicks);
        cutLink.setAddedAt(addedAt);
    }

    @Override
    public void deleteCut(CutLink cutLink) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_CUT_BY_ID);

            preparedStatement.setLong(1, cutLink.getId());

            return preparedStatement;
        });
    }

    @Override
    public void deleteAllCutByAccount(Account account) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ACCOUNT_CUTS);

            preparedStatement.setLong(1, account.getId());

            return preparedStatement;
        });
    }

    @Override
    public void updateClicks(CutLink cutLink) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_CLICKS);

            preparedStatement.setInt(1, cutLink.getClicks() + 1);
            preparedStatement.setLong(2, cutLink.getId());

            return preparedStatement;
        });
    }

    @Override
    public Optional<CutLink> findById(Long id) {
        List<CutLink> cutLinks = jdbcTemplate.query(SQL_FIND_BY_ID, rse, id);
        if (cutLinks.size() > 1) throw new IllegalStateException();
        else if (cutLinks.size() == 1) return Optional.of(cutLinks.get(0));
        else return Optional.empty();
    }

    @Override
    public Optional<CutLink> findByCut(String cut) {
        List<CutLink> cutLinks = jdbcTemplate.query(SQL_FIND_BY_CUT, rse, cut);
        if (cutLinks.size() > 1) throw new IllegalStateException();
        else if (cutLinks.size() == 1) return Optional.of(cutLinks.get(0));
        else return Optional.empty();
    }

    @Override
    public List<CutLink> findByAccount(Account account) {
        return jdbcTemplate.query(SQL_FIND_BY_ACCOUNT, rse, account.getId());
    }

    @Override
    public List<CutLink> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rse);
    }
}
