package p76.data.etl.teryt;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import p76.data.etl.JobCompletionNotificationListener;
import p76.data.etl.teryt.entity.Simc;
import p76.data.etl.teryt.entity.Terc;
import p76.data.etl.teryt.entity.Teryt;
import p76.data.etl.teryt.entity.Ulic;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

@Configuration
@EnableBatchProcessing
public class TerytBatchConfiguration {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    public TerytBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public FlatFileItemReader<Ulic> ulicReader() {
        return new FlatFileItemReaderBuilder<Ulic>()
                .name("monarchsItemReader")
                .resource(new ClassPathResource("teryt/ULIC_Adresowy_2020-05-16.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names(new String[]{"woj", "pow", "gmi", "rodzGmi", "sym", "symUl", "cecha",
                        "nazwa1", "nazwa2", "stanNa" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Ulic>() {{
                    setTargetType(Ulic.class);
                    setCustomEditors(Collections.singletonMap(String.class,
                            new StringTrimmerEditor(true)));
                }})
                .build();
    }

    @Bean
    public FlatFileItemReader<Terc> tercReader() {
        return new FlatFileItemReaderBuilder<Terc>()
                .name("tercItemReader")
                .resource(new ClassPathResource("teryt/TERC_Adresowy_2020-05-16.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names(new String[]{"woj", "pow", "gmi", "rodz", "nazwa", "nazwaDod", "stanNa" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Terc>() {{
                    setTargetType(Terc.class);
                    setCustomEditors(Collections.singletonMap(String.class,
                            new StringTrimmerEditor(true)));
                }})
                .build();
    }

    @Bean
    public FlatFileItemReader<Simc> simcReader() {
        return new FlatFileItemReaderBuilder<Simc>()
                .name("simcReader")
                .resource(new ClassPathResource("teryt/SIMC_Adresowy_2020-05-16.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names(new String[]{"woj", "pow", "gmi", "rodzGmi", "rm", "mz", "nazwa", "sym", "sympod", "stanNa" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Simc>() {{
                    setTargetType(Simc.class);
                    setCustomEditors(Collections.singletonMap(String.class,
                            new StringTrimmerEditor(true)));
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Teryt> terytWriter(EntityManagerFactory emf) {
        JpaItemWriter writer = new JpaItemWriter<Teryt>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public Job importTerytJob(JobCompletionNotificationListener listener,
                              Step importUlic,
                              Step importTerc,
                              Step importSimc) {
        return jobBuilderFactory.get("importTerytJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importUlic).next(importSimc).next(importTerc)
                .end()
                .build();
    }

    @Bean
    public Step importUlic(JpaItemWriter<Teryt>  terytWriter) {
        return stepBuilderFactory.get("importUlicStep")
                .<Teryt, Teryt> chunk(10000)
                .reader(ulicReader())
                .faultTolerant()
                .skip(IncorrectTokenCountException.class).skip(FlatFileParseException.class).skipLimit(10)
                .writer(terytWriter)
                .build();
    }

    @Bean
    public Step importTerc(JpaItemWriter<Teryt>  terytWriter) {
        return stepBuilderFactory.get("importTercStep")
                .<Terc, Terc> chunk(10000)
                .reader(tercReader())
                //.processor(new TercItemProcessor())
                .faultTolerant()
                .skip(IncorrectTokenCountException.class).skip(FlatFileParseException.class).skipLimit(10)
                .writer(terytWriter)
                .build();
    }

    @Bean
    public Step importSimc(JpaItemWriter<Teryt>  terytWriter) {
        return stepBuilderFactory.get("importSimcStep")
                .<Teryt, Teryt> chunk(10000)
                .reader(simcReader())
                .faultTolerant()
                .skip(IncorrectTokenCountException.class).skip(FlatFileParseException.class).skipLimit(10)
                .writer(terytWriter)
                .build();
    }
}
