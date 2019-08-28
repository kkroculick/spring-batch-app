package com.kroculick.batch;

import org.springframework.batch.item.ItemProcessor;

public class StudentDtoProcessor implements ItemProcessor<StudentDto, StudentDto> {
    @Override
    public StudentDto process(StudentDto studentDto) throws Exception {

        String emailAddress = studentDto.getEmailAddress();
        String name = studentDto.getName();
        String purchasePackage = studentDto.getPurchasePackage();

        studentDto.setEmailAddress(emailAddress);
        studentDto.setName(name);
        studentDto.setPurchasePackage(purchasePackage);


        return studentDto;
    }
}
