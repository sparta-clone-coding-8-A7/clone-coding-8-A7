package hanghaeclone8a7.twotead.service;

import hanghaeclone8a7.twotead.domain.JobPost;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JobPostServiceTest {


    @Test
    void 채용공고상세조회(){

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");

        LocalDate localDateTime = LocalDate.now();
        Date date = java.sql.Date.valueOf(localDateTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        String a = sdfYMD.format(cal.getTime());
        System.out.println(a);
        Assertions.assertThat(a).isEqualTo("2022-08-22");

    }
}