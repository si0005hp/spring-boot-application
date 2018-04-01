package com.example.controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.struct.Emp;

@RestController
@RequestMapping(value = "/db")
@Configuration
public class DBController {
    
    Random rnd = new Random();
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DriverManagerDataSource dataSource;
     
    @RequestMapping(value = "/emp", method = RequestMethod.GET)
    public String emp() {
        List<Emp> dtos = jdbcTemplate.query("SELECT * FROM emp", (rs, n) -> {
            return new Emp(rs.getInt("emp_id"), rs.getString("name"), rs.getInt("project_id"), rs.getInt("grade"));
        });
        return dtos.stream().map(Emp::toString).collect(Collectors.toList()).toString();
    }
    
    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public String project() {
        List<String> rows = jdbcTemplate.query("SELECT * FROM project", (rs, n) -> {
            return String.join(",",
                    String.valueOf(rs.getInt("project_id")),
                    rs.getString("name"),
                    String.valueOf(rs.getBigDecimal("alw_ratio")));
        });
        return rows.toString();
    }
    
    @RequestMapping(value = "/basicSalary", method = RequestMethod.GET)
    public String basicSalary() {
        List<String> rows = jdbcTemplate.query("SELECT * FROM basic_salary", (rs, n) -> {
            return String.join(",",
                    String.valueOf(rs.getInt("grade")),
                    String.valueOf(rs.getBigDecimal("value")));
        });
        return rows.toString();
    }
    
    @RequestMapping(value = "/salary", method = RequestMethod.GET)
    public String salary() {
        List<String> rows = jdbcTemplate.query("SELECT * FROM salary", (rs, n) -> {
            return String.join(",",
                    String.valueOf(rs.getInt("emp_id")),
                    String.valueOf(rs.getBigDecimal("value")));
        });
        return rows.toString();
    }
    
    @RequestMapping(value = "/registerEmp/{cnt}", method = RequestMethod.GET)
    public String registerEmp(@PathVariable String cnt) {
        long start = System.currentTimeMillis();
        registerEmp(Integer.parseInt(cnt));
        long stop = System.currentTimeMillis();
        return "Time taken: " + (stop-start) + " ms" + System.lineSeparator();
    }
    
    private void registerEmp(int cnt) {
        String sql = "INSERT INTO emp (name, project_id, grade) values (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, UUID.randomUUID().toString());
                ps.setInt(2, getProjectId());
                ps.setInt(3, getGrade());
            }
            @Override
            public int getBatchSize() {
                return cnt;
            }
        });
        System.out.println("Successfully registerEmp " + cnt);
    }
    
    @RequestMapping(value = "/registerEmp2/{cnt}", method = RequestMethod.GET)
    public String registerEmp2(@PathVariable String cnt) throws SQLException {
        long start = System.currentTimeMillis();
        registerEmp2(Integer.parseInt(cnt));
        long stop = System.currentTimeMillis();
        return "Time taken: " + (stop-start) + " ms" + System.lineSeparator();
    }
    
    private void registerEmp2(int cnt) throws SQLException {
        String sql = "INSERT INTO emp (name, project_id, grade) values (?, ?, ?)";
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            for (int i = 1; i <= cnt; i++) {
                ps.setString(1, UUID.randomUUID().toString());
                ps.setInt(2, getProjectId());
                ps.setInt(3, getGrade());
                ps.addBatch();
                
                if (i % 1000 == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
            con.commit();
        }
        System.out.println("Successfully registerEmp2 " + cnt);
    }
    
    @RequestMapping(value = "/registerEmp3/{cnt}", method = RequestMethod.GET)
    public String registerEmp3(@PathVariable String cnt) throws SQLException {
        long start = System.currentTimeMillis();
        registerEmp3(Integer.parseInt(cnt));
        long stop = System.currentTimeMillis();
        return "Time taken: " + (stop-start) + " ms" + System.lineSeparator();
    }
    
    private void registerEmp3(int cnt) throws SQLException {
        String sql = "INSERT INTO emp (name, project_id, grade) values (?, ?, ?)";
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            for (int i = 1; i <= cnt; i++) {
                ps.setString(1, UUID.randomUUID().toString());
                ps.setInt(2, getProjectId());
                ps.setInt(3, getGrade());
                ps.addBatch();
            }
            ps.executeBatch();
            con.commit();
        }
        System.out.println("Successfully registerEmp3 " + cnt);
    }
    
    @RequestMapping(value = "/batchcalc", method = RequestMethod.GET)
    public void batchcalc() {
    }
    
    private int getProjectId() {
        return rnd.nextInt(3) + 1;
    }
    
    private int getGrade() {
        return rnd.nextInt(4) + 1;
    }
    
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(String.format("jdbc:postgresql://%s:5432/postgres", getDbHost()));
        ds.setUsername("postgres");
        ds.setPassword("postgres");
        return ds;
    }
    
    @Bean
    public DataSourceTransactionManager transactionManager() {
      return new DataSourceTransactionManager(dataSource());
    }
     
    @Bean
    public JdbcTemplate jdbcTemplate() {
      return new JdbcTemplate(dataSource());
    }
    
    public static String getDbHost() {
        String dbhost = System.getProperty("dbhost");
        if (dbhost == null || "".equals(dbhost)) {
            dbhost = "localhost";
        }
        System.out.println("dbhost: " + dbhost);
        return dbhost;
    }
}
