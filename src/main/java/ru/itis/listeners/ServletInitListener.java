package ru.itis.listeners;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.exceptions.UnableToLoadPropertiesException;
import ru.itis.repositories.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@WebListener
public class ServletInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        Properties properties = new Properties();
        try {
            properties.load(servletContext.getResourceAsStream("/WEB-INF/properties/application.properties"));
        } catch (IOException ex) {
            throw new UnableToLoadPropertiesException();
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(properties.getProperty("db.driver"));
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.max-pool-size")));

        DataSource hikariDataSource = new HikariDataSource(hikariConfig);

        AccountRepository accountRepository = new AccountRepositoryJdbcImpl(hikariDataSource);
        CutLinkRepository cutLinkRepository = new CutLinkRepositoryJdbcImpl(hikariDataSource);
        MultiLinkRepository multiLinkRepository = new MultiLinkRepositoryJdbcImpl(hikariDataSource);


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
