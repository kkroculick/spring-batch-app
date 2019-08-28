package com.kroculick.batch;


import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@Import({DatabaseConfig.class, BatchScheduler.class})
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("dataSource1")
    private DataSource dataSource;

    @Autowired
    private DataSource dataSource1;


    private static final String QUERY_FIND_STUDENTS =
            "SELECT email_address, name, purchase_package FROM STUDENTS ORDER BY email_address ASC";

    @Autowired
    StudentTasklet taskletStep;





    @Bean(destroyMethod="")
    public ItemReader<StudentDto> itemReader() throws Exception {

      /*  JdbcCursorItemReader<StudentDto> dbReader = new JdbcCursorItemReader<>();
        dbReader.setDataSource(dataSource);
        dbReader.setSql(QUERY_FIND_STUDENTS);
        //dbReader.setRowMapper(new BeanPropertyRowMapper<>(StudentDto.class));
        dbReader.setRowMapper(new StudentRowMapper());
        int counter = 0;
        ExecutionContext executionContext = new ExecutionContext();
        dbReader.open(executionContext);

        Object studentDto  = new Object();
        while(studentDto  != null){
            studentDto  = dbReader.read();
            counter++;
        }
        dbReader.close();
        return dbReader;*/


    return new JdbcCursorItemReaderBuilder<StudentDto>()
                .dataSource(dataSource)
                .name("itemReader")
                .sql(QUERY_FIND_STUDENTS)
                .rowMapper(new StudentRowMapper())
                .build();
    }


    @Bean
    public ItemProcessor<StudentDto, StudentDto> itemProcessor() {

        return new StudentDtoProcessor();
    }




    @Bean
    public ItemWriter<StudentDto> itemWriter() {
        /*return new FlatFileItemWriterBuilder<StudentDto>()
                .name("itemWriter")
                .resource(new FileSystemResource("target/test-outputs/output.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();*/

        FlatFileItemWriter<StudentDto> csvFileWriter = new FlatFileItemWriter<>();

        //String exportFileHeader = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_HEADER);
        String exportFileHeader ="NAME:EMAIL;PUCHASE";
        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);
        csvFileWriter.setHeaderCallback(headerWriter);

        //String exportFilePath = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_PATH);
        csvFileWriter.setResource(new FileSystemResource("target/test-outputs/output.txt"));

        LineAggregator<StudentDto> lineAggregator = createStudentLineAggregator();
        csvFileWriter.setLineAggregator(lineAggregator);

        return csvFileWriter;


    }

    private LineAggregator<StudentDto> createStudentLineAggregator() {
        DelimitedLineAggregator<StudentDto> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");

        FieldExtractor<StudentDto> fieldExtractor = createStudentFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;
    }

    private FieldExtractor<StudentDto> createStudentFieldExtractor() {
        BeanWrapperFieldExtractor<StudentDto> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"emailAddress","name", "purchasePackage"});
        return extractor;
    }


    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step")
                .<StudentDto, StudentDto>chunk(10)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Step step2() throws Exception {

        return stepBuilderFactory.get("step2")
                .tasklet(taskletStep)
                .build();
    }


    @Bean
    public Job readJdbcTableJob(JobCompletionNotificationListener listener, Step step1) throws Exception {
        return jobBuilderFactory
                .get("readJdbcTableJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1).next(step2())
                .end()
                .build();
    }

   /* @Scheduled(cron = "0 0 8 1/1 * ?")
    public void startJob() throws Exception
    {
        System.out.println(" Job Started at :"+ new Date());
        //obParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();


        Map<String,JobParameter> jobParametersMap=new HashMap<String,JobParameter>();
        jobParametersMap.put("listener",listener);
        jobParametersMap.put("step1",new JobParameter();


        JobExecution execution = jobLauncher.run(readJdbcTableJob(), param);
        System.out.println("Job finished with status :" + execution.getStatus());
    }*/


}
