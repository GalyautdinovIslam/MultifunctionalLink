package ru.itis.listeners;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.helpers.*;
import ru.itis.repositories.*;
import ru.itis.services.*;

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
            throw new IllegalStateException(ex);
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
        SecurityRepository securityRepository = new SecurityRepositoryJdbcImpl(hikariDataSource);

        CodeGenerator codeGenerator = new CodeGeneratorImpl();
        EncryptHelper encryptHelper = new EncryptHelperImpl();
        ValidateHelper validator = new ValidateHelperImpl();

        AccountService accountService = new AccountServiceImpl(encryptHelper, validator, codeGenerator, securityRepository, accountRepository);
        CutLinkService cutLinkService = new CutLinkServiceImpl(validator, codeGenerator, cutLinkRepository);
        MultiLinkService multiLinkService = new MultiLinkServiceImpl(multiLinkRepository, validator);
        SecurityService securityService = new SecurityServiceImpl(encryptHelper, validator, accountRepository);
        MailService mailService = new MailServiceImpl();

        servletContext.setAttribute("accountService", accountService);
        servletContext.setAttribute("cutLinkService", cutLinkService);
        servletContext.setAttribute("multiLinkService", multiLinkService);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("mailService", mailService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
