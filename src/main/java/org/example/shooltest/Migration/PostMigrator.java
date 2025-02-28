package org.example.shooltest.Migration;

import org.example.shooltest.Entities.*;
import org.example.shooltest.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PostMigrator implements ApplicationListener<ApplicationReadyEvent> {
    private static final Map<String, String> rubricMapping = new HashMap<>();
    private static final Map<OldRubric, NewRubric> newRubricMapping = new HashMap<>();
    private int counter = 0;
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
        rubricMapping.put("Разное", "Другое");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<OldEntityPost> oldTests = oldEntityPostRepository.findByPostTitleContainingIgnoreCase("Тест");
        System.out.println(oldTests.size());
        List<ShooltestTest> newTests = new ArrayList<>();


        oldTests.forEach(oldTest -> {
            ShooltestTest newTest = getShooltestTest(oldTest);
            newTests.add(newTest);
            newTest.getQuestions().forEach(question -> System.out.println(question.getAnswers()));
        });
        printAllTests(newTests);
        System.out.println(newTests);
        System.out.println("Не выполнены преобразования: ");
        newTests.stream()
                .filter(newTest -> newTest.getQuestions().isEmpty())
                .map(newTest -> newTest.getOldEntityPost().getPostContent())
                .toList()
                .forEach(newTest -> {
                    System.out.println("----------------------Тест------------------------");
                    System.out.println(newTest);
                });
        System.out.println(newTests.stream().filter(newTest -> !newTest.getQuestions().isEmpty()).toList().size() + "/" + oldTests.size());
        shooltestTestRepository.saveAll(newTests);
        System.out.println("Сохранено!");
    }

    public NewRubric getNewRubric(OldRubric oldRubric) {
        if (oldRubric == null) return newRubricRepository.findByName("Другое");
        NewRubric newRubric = newRubricRepository.findByName(rubricMapping.getOrDefault(oldRubric.getName(), "Другое"));
        return newRubric != null ? newRubric : newRubricRepository.findByName("Другое");
    }

    public ShooltestTest getShooltestTest(OldEntityPost oldEntityPost) {
        ShooltestTest shooltestTest = new ShooltestTest();


        shooltestTest.setName(oldEntityPost.getPostTitle());


        shooltestTest.setRubric(getNewRubric(oldEntityPost.getTerm()));


        List<Question> questions = parseTest(oldEntityPost, shooltestTest);
        shooltestTest.setQuestions(questions);


        shooltestTest.setRedirectUrl(oldEntityPost.getGuid());

        shooltestTest.setOldEntityPost(oldEntityPost);

        return shooltestTest;
    }

    public List<Question> parseTest(OldEntityPost oldEntityPost, ShooltestTest shooltestTest) {

        String normalized = oldEntityPost.getPostContent();
        normalized = normalized.replace('\u00A0', ' ');
        normalized = normalized.replaceAll("[\u200B\uFEFF\u200E\u200F]", "");

        normalized = normalized.replaceAll("[\\p{Cntrl}&&[^\\r\\n\\t]]", "");


        String[] lines = normalized.split("\\r?\\n");


        Pattern questionPattern = Pattern.compile("^\\s*(\\d+)(?:[\\)\\.]?)\\s+(.+)$");


        Pattern answerPattern = Pattern.compile("^\\s*([A-Za-zА-Яа-я0-9]+)(?:[\\)\\.]?)\\s+(.+)$");


        List<Question> result = new ArrayList<>();


        Question currentQuestion = null;
        boolean inQuestion = false;
        boolean questionDiscarded = false;


        NewRubric rubric = getNewRubric(oldEntityPost.getTerm());

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {

                continue;
            }

            Matcher qMatcher = questionPattern.matcher(line);
            Matcher aMatcher = answerPattern.matcher(line);

            if (qMatcher.matches()) {


                if (inQuestion && currentQuestion != null && !questionDiscarded) {
                    finalizeAndAdd(currentQuestion, result);
                }


                currentQuestion = new Question();
                currentQuestion.setQuestionText(qMatcher.group(2).trim());
                currentQuestion.setRubric(rubric);
                currentQuestion.setTest(shooltestTest);
                currentQuestion.setAnswers(new ArrayList<>());

                inQuestion = true;
                questionDiscarded = false;

            } else if (aMatcher.matches() && inQuestion && !questionDiscarded) {

                String answerText = aMatcher.group(2).trim();
                boolean isCorrect = false;


                if (!answerText.isEmpty() && answerText.charAt(answerText.length() - 1) == '+') {
                    isCorrect = true;

                    answerText = answerText.substring(0, answerText.length() - 1).trim();
                }


                Answer answer = new Answer();
                answer.setAnswerText(answerText);
                answer.setCorrect(isCorrect);
                answer.setQuestion(currentQuestion);

                currentQuestion.getAnswers().add(answer);

            } else {

                if (inQuestion && !questionDiscarded) {
                    System.out.println("Отклоняю текущий вопрос из-за некорректной строки: " + line);
                }
                questionDiscarded = true;
                inQuestion = false;
            }
        }


        if (inQuestion && currentQuestion != null && !questionDiscarded) {
            finalizeAndAdd(currentQuestion, result);
        }

        return result;
    }

    /**
     * Если у вопроса есть хотя бы один ответ и хотя бы один правильный - добавляем в result.
     * Иначе - логируем и отбрасываем.
     */
    private void finalizeAndAdd(Question question, List<Question> result) {
        if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
            System.out.println("Вопрос без ответов, пропускаем: " + question.getQuestionText());
            return;
        }
        boolean hasCorrect = question.getAnswers().stream().anyMatch(Answer::isCorrect);
        if (!hasCorrect) {
            System.out.println("Вопрос без правильных ответов, пропускаем: " + question.getQuestionText());
            return;
        }

        result.add(question);
    }


    private NewRubric getNewRubric(String term) {

        return new NewRubric();
    }


    public void printAllTests(List<ShooltestTest> tests) {
        for (int i = 0; i < tests.size(); i++) {
            ShooltestTest test = tests.get(i);
            System.out.println("=============================================");
            System.out.println("Тест #" + (i + 1) + ": " + test.getName());
            System.out.println("Рубрика: " + (test.getRubric() != null ? test.getRubric().getName() : "Не указана"));
            System.out.println("=============================================\n");

            List<Question> questions = test.getQuestions();
            if (questions.isEmpty()) {
                System.out.println("Нет вопросов в этом тесте.\n");
                continue;
            }

            for (int j = 0; j < questions.size(); j++) {
                Question q = questions.get(j);
                System.out.println((j + 1) + ". " + q.getQuestionText());

                for (int k = 0; k < q.getAnswers().size(); k++) {
                    Answer a = q.getAnswers().get(k);
                    String correctMark = a.isCorrect() ? "[✔]" : "[ ]";
                    System.out.println("   " + (char) ('а' + k) + ") " + correctMark + " " + a.getAnswerText());
                }
                System.out.println();
            }
            counter++;
        }
    }


}