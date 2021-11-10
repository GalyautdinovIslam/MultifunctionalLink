package ru.itis.repositories;

import ru.itis.exceptions.AlreadySubscribedException;
import ru.itis.exceptions.AlreadyUnsubscribedException;
import ru.itis.models.Account;
import ru.itis.models.CutLink;
import ru.itis.models.MultiLink;
import ru.itis.repositories.jdbcTemplate.JdbcTemplate;
import ru.itis.repositories.jdbcTemplate.ResultSetExtractor;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class AccountRepositoryJdbcImpl implements AccountRepository {

    //language=SQL
    private final String SQL_CREATE_ACCOUNT = "insert into account(email, password, nickname) values (?, ?, ?)";

    //language=SQL
    private final String SQL_UPDATE_PASSWORD = "update account set password = ? where id = ?";

    //language=SQL
    private final String SQL_UPDATE_AGE = "update account set age = ? where id = ?";

    //language=SQL
    private final String SQL_UPDATE_STATUS = "update account set activated = true where id = ?";

    //language=SQL
    private final String SQL_SUBSCRIBE = "insert into subscription(who_id, sub_to_id) values (?, ?)";

    //language=SQL
    private final String SQL_UNSUBSCRIBE = "delete from subscription where who_id = ? and sub_to_id = ?";

    //language=SQL
    private final String SQL_DELETE_ACCOUNT = "delete from account where id = ?";

    //language=SQL
    private final String SQL_DELETE_SIGN_UP = "delete from sign_up where account_id = ?";

    //language=SQL
    private final String SQL_DELETE_RECOVERY = "delete from recovery where account_id = ?";

    //language=SQL
    private final String SQL_DELETE_SUBS = "delete from subscription where who_id = ? or sub_to_id = ?";

    //language=SQL
    private final String SQL_FIND_BY_EMAIL = "select c.id as c_id, c.owner_id as c_owner_id, c.link as c_link, " +
            "c.clicks as c_clicks, c.added_at as c_added_at, m.id as m_id, m.owner_id as m_owner_id, " +
            "m.link as m_link, m.clicks as m_clicks, m.added_at as m_added_at, s.who_id as s_who_id, " +
            "s.sub_to_id as s_sub_to_id, aa.id as aa_id, aa.email as aa_email, aa.password as aa_password, " +
            "aa.nickname as aa_nickname, aa.age as aa_age, aa.created_at as aa_created_at, " +
            "aa.activated as aa_activated, ss.who_id as ss_who_id, ss.sub_to_id as ss_sub_to_id, " +
            "aaa.id as aaa_id, aaa.email as aaa_email, aaa.password as aaa_password, aaa.nickname as aaa_nickname, " +
            "aaa.age as aaa_age, aaa.created_at as aaa_created_at, aaa.activated as aaa_activated, * from account as a " +
            "left join cutlink as c on a.id = c.owner_id " +
            "left join multilink as m on a.id = m.owner_id " +
            "left join subscription as s on a.id = s.who_id " +
            "left join account as aa on s.sub_to_id = aa.id " +
            "left join subscription as ss on a.id = ss.sub_to_id " +
            "left join account as aaa on ss.who_id = aaa.id " +
            "where a.email = ?";

    //language=SQL
    private final String SQL_FIND_BY_NICKNAME = "select c.id as c_id, c.owner_id as c_owner_id, c.link as c_link, " +
            "c.clicks as c_clicks, c.added_at as c_added_at, m.id as m_id, m.owner_id as m_owner_id, " +
            "m.link as m_link, m.clicks as m_clicks, m.added_at as m_added_at, s.who_id as s_who_id, " +
            "s.sub_to_id as s_sub_to_id, aa.id as aa_id, aa.email as aa_email, aa.password as aa_password, " +
            "aa.nickname as aa_nickname, aa.age as aa_age, aa.created_at as aa_created_at, " +
            "aa.activated as aa_activated, ss.who_id as ss_who_id, ss.sub_to_id as ss_sub_to_id, " +
            "aaa.id as aaa_id, aaa.email as aaa_email, aaa.password as aaa_password, aaa.nickname as aaa_nickname, " +
            "aaa.age as aaa_age, aaa.created_at as aaa_created_at, aaa.activated as aaa_activated, * from account as a " +
            "left join cutlink as c on a.id = c.owner_id " +
            "left join multilink as m on a.id = m.owner_id " +
            "left join subscription as s on a.id = s.who_id " +
            "left join account as aa on s.sub_to_id = aa.id " +
            "left join subscription as ss on a.id = ss.sub_to_id " +
            "left join account as aaa on ss.who_id = aaa.id " +
            "where a.nickname = ?";

    //language=SQL
    private final String SQL_FIND_BY_ID = "select c.id as c_id, c.owner_id as c_owner_id, c.link as c_link, " +
            "c.clicks as c_clicks, c.added_at as c_added_at, m.id as m_id, m.owner_id as m_owner_id, " +
            "m.link as m_link, m.clicks as m_clicks, m.added_at as m_added_at, s.who_id as s_who_id, " +
            "s.sub_to_id as s_sub_to_id, aa.id as aa_id, aa.email as aa_email, aa.password as aa_password, " +
            "aa.nickname as aa_nickname, aa.age as aa_age, aa.created_at as aa_created_at, " +
            "aa.activated as aa_activated, ss.who_id as ss_who_id, ss.sub_to_id as ss_sub_to_id, " +
            "aaa.id as aaa_id, aaa.email as aaa_email, aaa.password as aaa_password, aaa.nickname as aaa_nickname, " +
            "aaa.age as aaa_age, aaa.created_at as aaa_created_at, aaa.activated as aaa_activated, * from account as a " +
            "left join cutlink as c on a.id = c.owner_id " +
            "left join multilink as m on a.id = m.owner_id " +
            "left join subscription as s on a.id = s.who_id " +
            "left join account as aa on s.sub_to_id = aa.id " +
            "left join subscription as ss on a.id = ss.sub_to_id " +
            "left join account as aaa on ss.who_id = aaa.id " +
            "where a.id = ?";

    //language=SQL
    private final String SQL_FIND_ALL = "select c.id as c_id, c.owner_id as c_owner_id, c.link as c_link, " +
            "c.clicks as c_clicks, c.added_at as c_added_at, m.id as m_id, m.owner_id as m_owner_id, " +
            "m.link as m_link, m.clicks as m_clicks, m.added_at as m_added_at, s.who_id as s_who_id, " +
            "s.sub_to_id as s_sub_to_id, aa.id as aa_id, aa.email as aa_email, aa.password as aa_password, " +
            "aa.nickname as aa_nickname, aa.age as aa_age, aa.created_at as aa_created_at, " +
            "aa.activated as aa_activated, ss.who_id as ss_who_id, ss.sub_to_id as ss_sub_to_id, " +
            "aaa.id as aaa_id, aaa.email as aaa_email, aaa.password as aaa_password, aaa.nickname as aaa_nickname, " +
            "aaa.age as aaa_age, aaa.created_at as aaa_created_at, aaa.activated as aaa_activated, * from account as a " +
            "left join cutlink as c on a.id = c.owner_id " +
            "left join multilink as m on a.id = m.owner_id " +
            "left join subscription as s on a.id = s.who_id " +
            "left join account as aa on s.sub_to_id = aa.id " +
            "left join subscription as ss on a.id = ss.sub_to_id " +
            "left join account as aaa on ss.who_id = aaa.id";

    private final ResultSetExtractor<List<Account>> rse = resultSet -> {
        Map<Long, Account> accounts = new HashMap<>();
        Map<Account, Set<Long>> accountsIdSubscription = new HashMap<>();
        Map<Account, Set<Long>> accountsIdSubscribers = new HashMap<>();
        Map<Long, CutLink> cutLinks = new HashMap<>();
        Map<Long, MultiLink> multiLinks = new HashMap<>();
        Map<Long, Account> secondlyAccounts = new HashMap<>();

        while (resultSet.next()) {
            Account account;
            Set<Long> idsSubscription;
            Set<Long> idsSubscribers;
            CutLink cutLink;
            MultiLink multiLink;
            Account secondlyAccount;
            Account thirdlyAccount;

            if (accounts.containsKey(resultSet.getLong("id"))) {
                account = accounts.get(resultSet.getLong("id"));
            } else {
                account = Account.builder()
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
                        .build();
                accounts.put(account.getId(), account);
            }

            if (!accountsIdSubscription.containsKey(account)) {
                accountsIdSubscription.put(account, new HashSet<>());
            }
            idsSubscription = accountsIdSubscription.get(account);

            if (!accountsIdSubscribers.containsKey(account)) {
                accountsIdSubscribers.put(account, new HashSet<>());
            }
            idsSubscribers = accountsIdSubscribers.get(account);

            if (cutLinks.containsKey(resultSet.getLong("c_id"))) {
                cutLink = cutLinks.get(resultSet.getLong("c_id"));
            } else {
                try {
                    cutLink = CutLink.builder()
                            .id(resultSet.getLong("c_id"))
                            .cut(resultSet.getString("cut"))
                            .clicks(resultSet.getInt("c_clicks"))
                            .addedAt(resultSet.getDate("c_added_at"))
                            .build();

                    if (resultSet.getString("c_link") != null) {
                        String link = resultSet.getString("c_link");
                        cutLink.setLink(new URI(link));
                    }

                    if (cutLink.getCut() != null) cutLinks.put(cutLink.getId(), cutLink);
                } catch (URISyntaxException e) {
                    throw new IllegalStateException(e);
                }
            }

            if (multiLinks.containsKey(resultSet.getLong("m_id"))) {
                multiLink = multiLinks.get(resultSet.getLong("m_id"));
            } else {
                try {
                    multiLink = MultiLink.builder()
                            .id(resultSet.getLong("m_id"))
                            .name(resultSet.getString("multi_name"))
                            .clicks(resultSet.getInt("m_clicks"))
                            .addedAt(resultSet.getDate("m_added_at"))
                            .build();

                    if (resultSet.getString("m_link") != null) {
                        multiLink.setLink(new URI(resultSet.getString("m_link")));
                    }
                    if (multiLink.getName() != null) multiLinks.put(multiLink.getId(), multiLink);
                } catch (URISyntaxException e) {
                    throw new IllegalStateException(e);
                }

            }

            if (!secondlyAccounts.containsKey(resultSet.getLong("aa_id"))) {
                secondlyAccount = Account.builder()
                        .id(resultSet.getLong("aa_id"))
                        .email(resultSet.getString("aa_email"))
                        .password(resultSet.getString("aa_password"))
                        .nickname(resultSet.getString("aa_nickname"))
                        .age(resultSet.getInt("aa_age"))
                        .createdAt(resultSet.getDate("aa_created_at"))
                        .multiLinks(new HashSet<>())
                        .cutLinks(new HashSet<>())
                        .subscribers(new HashSet<>())
                        .subscriptions(new HashSet<>())
                        .build();
                if (secondlyAccount.getEmail() != null) secondlyAccounts.put(secondlyAccount.getId(), secondlyAccount);
            }

            if (!secondlyAccounts.containsKey(resultSet.getLong("aaa_id"))) {
                thirdlyAccount = Account.builder()
                        .id(resultSet.getLong("aaa_id"))
                        .email(resultSet.getString("aaa_email"))
                        .password(resultSet.getString("aaa_password"))
                        .nickname(resultSet.getString("aaa_nickname"))
                        .age(resultSet.getInt("aaa_age"))
                        .createdAt(resultSet.getDate("aaa_created_at"))
                        .multiLinks(new HashSet<>())
                        .cutLinks(new HashSet<>())
                        .subscribers(new HashSet<>())
                        .subscriptions(new HashSet<>())
                        .build();
                if (thirdlyAccount.getEmail() != null) secondlyAccounts.put(thirdlyAccount.getId(), thirdlyAccount);
            }

            Long secondId = resultSet.getLong("s_sub_to_id");
            Long thirdId = resultSet.getLong("ss_who_id");
            if (resultSet.getString("s_sub_to_id") != null) idsSubscription.add(secondId);
            if (resultSet.getString("ss_who_id") != null) idsSubscribers.add(thirdId);
            cutLink.setOwner(account);
            if (cutLink.getCut() != null) account.getCutLinks().add(cutLink);
            multiLink.setOwner(account);
            if (multiLink.getName() != null) account.getMultiLinks().add(multiLink);
        }

        List<Account> accountList = new ArrayList<>(accounts.values());
        for (Account account : accountList) {
            Set<Long> idsSubscription = accountsIdSubscription.get(account);
            Set<Long> idsSubscribers = accountsIdSubscribers.get(account);
            for (Long id : idsSubscription) {
                if (accounts.containsKey(id)) {
                    accounts.get(id).getSubscribers().add(account);
                    account.getSubscriptions().add(accounts.get(id));
                } else {
                    secondlyAccounts.get(id).getSubscribers().add(account);
                    account.getSubscriptions().add(secondlyAccounts.get(id));
                }
            }
            for (Long id : idsSubscribers) {
                if (accounts.containsKey(id)) {
                    accounts.get(id).getSubscriptions().add(account);
                    account.getSubscribers().add(accounts.get(id));
                } else {
                    secondlyAccounts.get(id).getSubscriptions().add(account);
                    account.getSubscribers().add(secondlyAccounts.get(id));
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
    public Account createAccount(Account account) {
        jdbcTemplate.update(SQL_CREATE_ACCOUNT, account.getEmail(), account.getPassword(), account.getNickname());
        return findByEmail(account.getEmail()).get();
    }

    @Override
    public void updatePassword(Account account) {
        jdbcTemplate.update(SQL_UPDATE_PASSWORD, account.getPassword(), account.getId());
    }

    @Override
    public void updateAge(Account account) {
        jdbcTemplate.update(SQL_UPDATE_AGE, account.getAge(), account.getId());
    }

    @Override
    public void updateStatus(Long id) {
        jdbcTemplate.update(SQL_UPDATE_STATUS, id);
    }

    @Override
    public void subscribe(Account who, Account subTo) throws AlreadySubscribedException {
        int i = jdbcTemplate.update(SQL_SUBSCRIBE, who.getId(), subTo.getId());;

        if (i == 0) throw new AlreadySubscribedException();

        who.getSubscriptions().add(subTo);
        subTo.getSubscribers().add(who);
    }

    @Override
    public void unsubscribe(Account who, Account subTo) throws AlreadyUnsubscribedException {
        int i = jdbcTemplate.update(SQL_UNSUBSCRIBE, who.getId(), subTo.getId());

        if (i == 0) throw new AlreadyUnsubscribedException();

        for (Account s : who.getSubscriptions()) {
            if (s.getId().equals(subTo.getId())) {
                who.getSubscriptions().remove(s);
                break;
            }
        }
        for (Account s : subTo.getSubscribers()) {
            if (s.getId().equals(who.getId())) {
                subTo.getSubscribers().remove(s);
                break;
            }
        }
    }

    @Override
    public void deleteAccount(Account account) {
        jdbcTemplate.update(SQL_DELETE_SUBS, account.getId(), account.getId());
        jdbcTemplate.update(SQL_DELETE_SIGN_UP, account.getId());
        jdbcTemplate.update(SQL_DELETE_RECOVERY, account.getId());
        jdbcTemplate.update(SQL_DELETE_ACCOUNT, account.getId());
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        List<Account> accounts = jdbcTemplate.query(SQL_FIND_BY_EMAIL, rse, email);
        if (accounts != null) {
            if (accounts.size() > 1) throw new IllegalStateException();
            else if (accounts.size() == 1) return Optional.of(accounts.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByNickname(String nickname) {
        List<Account> accounts = jdbcTemplate.query(SQL_FIND_BY_NICKNAME, rse, nickname);
        if (accounts != null) {
            if (accounts.size() > 1) throw new IllegalStateException();
            else if (accounts.size() == 1) return Optional.of(accounts.get(0));
        }
        return Optional.empty();
    }

    @Override
    public void save(Account account) {
        this.createAccount(account);
    }

    @Override
    public void update(Account account) {
        this.updatePassword(account);
        this.updateAge(account);

        jdbcTemplate.update(SQL_DELETE_SUBS, account.getId(), account.getId());

        Set<Account> subscribers = account.getSubscribers();
        Set<Account> subscriptions = account.getSubscriptions();

        try {
            for (Account a : subscribers) {
                this.subscribe(a, account);
            }
            for (Account a : subscriptions) {
                this.subscribe(account, a);
            }
        } catch (AlreadySubscribedException ignored) {
        }
    }

    @Override
    public void delete(Account account) {
        this.deleteAccount(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        List<Account> accounts = jdbcTemplate.query(SQL_FIND_BY_ID, rse, id);
        if (accounts != null) {
            if (accounts.size() > 1) throw new IllegalStateException();
            else if (accounts.size() == 1) return Optional.of(accounts.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rse);
    }
}
