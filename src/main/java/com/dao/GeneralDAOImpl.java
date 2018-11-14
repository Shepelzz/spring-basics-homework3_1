package com.dao;

import com.exception.InternalServerError;

import java.sql.*;

public abstract class GeneralDAOImpl{
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.ce5xbsungqgk.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "11111111";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    long getNewEntityId(String sql) throws InternalServerError, SQLException{
        try(Connection conn = getConnection(); Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
                return rs.getLong(1);
            throw new InternalServerError(getClass().getName()+"-getNewEntityId. Get ID fail");
        }catch (SQLException e){
            throw e;
        }
    }

    void closeConnection(Connection connection) throws SQLException{
        if(connection != null)
            connection.close();
    }
}
