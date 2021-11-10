package ru.itis.repositories;

import ru.itis.models.Account;
import ru.itis.models.CutLink;
import ru.itis.repositories.jdbcTemplate.JdbcTemplate;
import ru.itis.repositories.jdbcTemplate.ResultSetExtractor;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
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

                if (resultSet.getString("link") != null) {
                    cutLink.setLink(new URI(resultSet.getString("link")));
                }

                if (cutLink.getCut() != null) cutLinks.add(cutLink);
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
    public CutLink createCut(CutLink cutLink) {
        jdbcTemplate.update(SQL_CREATE_CUT, cutLink.getOwner().getId(), cutLink.getCut(), cutLink.getLink().toString());
        return findByCut(cutLink.getCut()).get();
    }

    @Override
    public void deleteCut(CutLink cutLink) {
        jdbcTemplate.update(SQL_DELETE_CUT_BY_ID, cutLink.getId());
    }

    @Override
    public void deleteAllCutByAccount(Account account) {
        jdbcTemplate.update(SQL_DELETE_ACCOUNT_CUTS, account.getId());
    }

    @Override
    public void updateClicks(CutLink cutLink) {
        jdbcTemplate.update(SQL_UPDATE_CLICKS, cutLink.getClicks() + 1, cutLink.getId());
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
