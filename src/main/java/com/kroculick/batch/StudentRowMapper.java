package com.kroculick.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<StudentDto> {
    @Override
    public StudentDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        StudentDto student = new StudentDto();
        student.setEmailAddress(resultSet.getString("email_address"));
        student.setName(resultSet.getString("name"));
        student.setPurchasePackage(resultSet.getString("purchase_package"));
         //System.out.println(resultSet.getString("email_address"));
        //resultSet.close();
        return student;
    }

}
