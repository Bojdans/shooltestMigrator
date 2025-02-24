package org.example.shooltest.Migration;

import org.example.shooltest.Entities.NewRubric;
import org.example.shooltest.Entities.OldEntityPost;
import org.example.shooltest.Entities.OldRubric;
import org.example.shooltest.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostMigrator implements ApplicationListener<ApplicationReadyEvent> {
    private static final Map<String, String> rubricMapping = new HashMap<>();
    private static final Map<OldRubric, NewRubric> newRubricMapping = new HashMap<>();
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ShooltestTestRepository shooltestTestRepository;
    @Autowired
    private OldRubricRepository oldRubricRepository;
    @Autowired
    private NewRubricRepository newRubricRepository;
    @Autowired
    private OldEntityPostRepository oldEntityPostRepository;

    public PostMigrator(AnswerRepository answerRepository, QuestionRepository questionRepository, ShooltestTestRepository shooltestTestRepository, OldRubricRepository oldRubricRepository, NewRubricRepository newRubricRepository, OldEntityPostRepository oldEntityPostRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.shooltestTestRepository = shooltestTestRepository;
        this.oldRubricRepository = oldRubricRepository;
        this.newRubricRepository = newRubricRepository;
        this.oldEntityPostRepository = oldEntityPostRepository;
    }

    static {
        rubricMapping.put("Литература", "Литература");
        rubricMapping.put("История", "История");
        rubricMapping.put("Биология", "Биология");
        rubricMapping.put("Английский язык", "Английский");
        rubricMapping.put("Экология", "Экология");
        rubricMapping.put("География", "География, экономическая география");
        rubricMapping.put("Русский язык", "Русский язык культура речи");
        rubricMapping.put("Политология", "Политология");
        rubricMapping.put("Информатика", "Информационные технологии");
        rubricMapping.put("Право", "Основы права");
        rubricMapping.put("Экономика", "Экономика предприятия");
        rubricMapping.put("Социология", "Социология");
        rubricMapping.put("Искусство", "Культурология");
        rubricMapping.put("Философия", "Философия");
        rubricMapping.put("Физика", "Физика");
        rubricMapping.put("Химия", "Химия");
        rubricMapping.put("Медицина", "Медицина, здравоохранение");
        rubricMapping.put("Геометрия", "Математика");
        rubricMapping.put("Алгебра", "Математика");
        rubricMapping.put("Математика", "Математика");
        rubricMapping.put("Физическая культура", "Физическая культура, спорт");
        rubricMapping.put("Менеджмент", "Менеджмент");
        rubricMapping.put("Финансы", "Финансовый менеджмент, финансовая математика");
        rubricMapping.put("Бухгалтерия", "Бухучет, управленч.учет");
        rubricMapping.put("Банковское дело", "Банковское дело");
        rubricMapping.put("Психология", "Психология");
        rubricMapping.put("Право", "Основы права");
        rubricMapping.put("Административное право", "Административное право");
        rubricMapping.put("Гражданское право", "Гражданское право");
        rubricMapping.put("Трудовое право", "Трудовое право");
        rubricMapping.put("Налоговое право", "Налоговое право");
        rubricMapping.put("Уголовное право", "Уголовное право");
        rubricMapping.put("Экономика", "Экономика предприятия");
        rubricMapping.put("Финансы", "Финансовый менеджмент, финансовая математика");
        rubricMapping.put("Банковское дело", "Банковское дело");
        rubricMapping.put("Менеджмент", "Менеджмент");
        rubricMapping.put("Охрана труда", "Техносферная безопасность");
        rubricMapping.put("Информатика", "Информационные технологии");
        rubricMapping.put("Станки", "Технология машиностроения");
        rubricMapping.put("Чертежи", "Начертательная геометрия");
        rubricMapping.put("Обществознание", "Политология");
        rubricMapping.put("Окружающий мир", "Экология");
        rubricMapping.put("Другое", "Другое");
        rubricMapping.put("Разное", "Разное");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<OldEntityPost> oldTests = oldEntityPostRepository.findAllByPostTitle("Тест");
        oldTests.addAll(oldEntityPostRepository.findAllByPostTitle("тест"));
        System.out.println("==============тесты===============");
        System.out.println(oldTests);
        System.out.println("==================================");
        oldRubricRepository.findAll().forEach(oldRubric -> System.out.println(getNewRubric(oldRubric)));
    }

    public NewRubric getNewRubric(OldRubric oldRubric) {
        System.out.println(oldRubric);
        return newRubricRepository.findByNameContaining(rubricMapping.getOrDefault(oldRubric.getName(),"Другое"));
    }
}