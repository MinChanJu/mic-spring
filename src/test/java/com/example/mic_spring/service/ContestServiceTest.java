package com.example.mic_spring.service;

import java.sql.Connection;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mic_spring.domain.entity.Contest;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ContestServiceTest {

        @Autowired
        private ContestService contestService;
        @Autowired
        private DataSource dataSource;

        @BeforeEach
        @AfterEach
        void resetAutoIncrement() {
                try (Connection conn = dataSource.getConnection();
                                Statement stmt = conn.createStatement()) {
                        stmt.execute("SELECT setval('contests_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM contests), false)");
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        @Test
        void create() {
                Contest contest1 = new Contest(null, "test1", "test11", "test", "test", ZonedDateTime.now(),
                                ZonedDateTime.now().plusMinutes(5),
                                ZonedDateTime.now());
                Contest contest2 = new Contest(null, "test1", "test11", "test", "test", ZonedDateTime.now(),
                                ZonedDateTime.now().plusMinutes(5),
                                ZonedDateTime.now());

                Contest find1 = contestService.createContest(contest1);

                assertThat(find1.getContestName()).isEqualTo(contest1.getContestName());
                assertThatThrownBy(() -> contestService.createContest(contest2))
                                .isInstanceOf(CustomException.class)
                                .hasMessage(ErrorCode.DUPLICATE_CONTEST_NAME.getMessage());
        }

        @Test
        void read() {
                int size1 = contestService.getAllContests().size();

                Contest contest1 = new Contest(null, "test1", "test11", "test", "test", ZonedDateTime.now().plusDays(5),
                                ZonedDateTime.now().plusDays(5).plusMinutes(5),
                                ZonedDateTime.now());
                Contest contest2 = new Contest(null, "test1", "test22", "test", "test",
                                ZonedDateTime.now().minusDays(1), ZonedDateTime.now().minusDays(1).plusMinutes(5),
                                ZonedDateTime.now());
                Contest contest3 = new Contest(null, "test1", "test33", "test", "test",
                                ZonedDateTime.now().minusHours(12), ZonedDateTime.now().minusHours(12).plusMinutes(5),
                                ZonedDateTime.now());
                Contest contest4 = new Contest(null, "test1", "test44", "test", "test",
                                ZonedDateTime.now().plusHours(2), ZonedDateTime.now().plusHours(2).plusMinutes(5),
                                ZonedDateTime.now());
                Contest contest5 = new Contest(null, "test1", "test55", "test", "test",
                                ZonedDateTime.now().plusMinutes(10), ZonedDateTime.now().plusMinutes(10).plusMinutes(5),
                                ZonedDateTime.now());

                contestService.createContest(contest1);
                contestService.createContest(contest2);
                contestService.createContest(contest3);
                contestService.createContest(contest4);
                contestService.createContest(contest5);

                Contest find1 = contestService.getContestById(contest1.getId());
                List<Contest> find2 = contestService.getAllContests();

                assertThat(find1.getContestName()).isEqualTo(contest1.getContestName());
                assertThat(find2.size() - size1).isEqualTo(5);
                assertThatThrownBy(() -> contestService.getContestById(null))
                                .isInstanceOf(CustomException.class)
                                .hasMessage(ErrorCode.CONTEST_NOT_FOUND.getMessage());
        }

        @Test
        void update() {
                Contest contest1 = new Contest(null, "test1", "test11", "test", "test", ZonedDateTime.now().plusDays(5),
                                ZonedDateTime.now().plusDays(5).plusMinutes(5),
                                ZonedDateTime.now());
                Contest contest2 = new Contest(null, "test1", "test22", "test", "test",
                                ZonedDateTime.now().minusDays(1), ZonedDateTime.now().minusDays(1).plusMinutes(5),
                                ZonedDateTime.now());

                contestService.createContest(contest1);
                contestService.createContest(contest2);

                contest1.setContestDescription("sf");
                contest1.setStartTime(ZonedDateTime.now());
                contest1.setEndTime(ZonedDateTime.now().plusMinutes(140));

                Contest noExistContest2 = new Contest(null, "test1", "test22", "sdf", "asfa",
                                ZonedDateTime.now().minusDays(1),
                                ZonedDateTime.now().minusDays(1).plusMinutes(5),
                                ZonedDateTime.now());

                Contest update1 = contestService.updateContest(contest1);

                assertThat(update1.getContestDescription()).isEqualTo(contest1.getContestDescription());
                assertThatThrownBy(() -> contestService.updateContest(noExistContest2))
                                .isInstanceOf(CustomException.class)
                                .hasMessage(ErrorCode.CONTEST_NOT_FOUND.getMessage());
        }

        @Test
        void delete() {
                Contest contest1 = new Contest(null, "test1", "test11", "test", "test", ZonedDateTime.now().plusDays(5),
                                ZonedDateTime.now().plusDays(5).plusMinutes(5),
                                ZonedDateTime.now());
                Contest contest2 = new Contest(null, "test1", "test22", "test", "test",
                                ZonedDateTime.now().minusDays(1),
                                ZonedDateTime.now().minusDays(1).plusMinutes(5),
                                ZonedDateTime.now());

                contestService.createContest(contest1);
                contestService.createContest(contest2);

                contestService.deleteContest(contest1.getId());
                assertThatThrownBy(() -> contestService.deleteContest(null))
                                .isInstanceOf(CustomException.class)
                                .hasMessage(ErrorCode.CONTEST_NOT_FOUND.getMessage());
        }
}