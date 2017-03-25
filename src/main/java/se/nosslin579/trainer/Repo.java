package se.nosslin579.trainer;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import se.nosslin579.aardvark.Config;

public class Repo {

    private final SimpleJdbcInsertOperations insertConfig;
    private JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:./db/test;IFEXISTS=TRUE;AUTO_SERVER=TRUE", "sa", "");
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(pool);
    private final RowMapper<Config> configRowMapper = new BeanPropertyRowMapper<>(Config.class);


    public Repo() {
        this.insertConfig = new SimpleJdbcInsert(pool)
                .withTableName("CONFIG")
                .usingGeneratedKeyColumns("ID")
                .usingColumns("RATING");
    }

    public Config getConfig(int id) {
        String sql = "SELECT * FROM CONFIG WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, configRowMapper);
    }

    public Config addConfig(Config config) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(config);
        Number number = insertConfig.executeAndReturnKey(parameterSource);
        config.setId(number.intValue());
        return config;
    }

    public static void main(String[] a) throws Exception {
        Repo repo = new Repo();
        Config configOld = repo.addConfig(new Config());
        Config configStored = repo.getConfig(configOld.getId());
        System.out.println(configStored);
    }

/*
CREATE TABLE CONFIG
(
    ID INTEGER AUTO_INCREMENT PRIMARY KEY,
    TEST INTEGER
)

CREATE TABLE MATCH
(
    ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
    WINNER INTEGER NOT NULL,
    LOSER INTEGER NOT NULL
)
*/

}