package p76.data.etl.history;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import p76.data.etl.JobCompletionNotificationListener;
import p76.data.etl.history.entity.Monarch;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class HistoryBatchConfiguration {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    public HistoryBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public FlatFileItemReader<Monarch> monarchReader() {
        return new FlatFileItemReaderBuilder<Monarch>()
                .name("monarchsItemReader")
                .resource(new ClassPathResource("monarchs.csv"))
                .delimited()
                .delimiter(";")
                .names(new String[]{"reignStart", "reignEnd", "name",
                        "dateOfBirth", "dateOfDeath", "dynasty", "country" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Monarch>() {{
                    setTargetType(Monarch.class);
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Monarch> monarchWriter(EntityManagerFactory emf) {
        JpaItemWriter<Monarch> writer = new JpaItemWriter<Monarch>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public Job importMonarchsJob(JobCompletionNotificationListener listener, Step importMonarchs) {
        return jobBuilderFactory.get("importMonarchsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importMonarchs)
                .end()
                .build();
    }

    @Bean
    public Step importMonarchs(JpaItemWriter<Monarch>  writer) {
        return stepBuilderFactory.get("importMonarchsStep")
                .<Monarch, Monarch> chunk(10)
                .reader(monarchReader())
                .writer(writer)
                .build();
    }
}
