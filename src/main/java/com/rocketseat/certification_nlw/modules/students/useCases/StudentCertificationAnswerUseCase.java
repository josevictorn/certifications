package com.rocketseat.certification_nlw.modules.students.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.rocketseat.certification_nlw.modules.questions.repositories.StudentRepository;
import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.entities.AnswersCertificationsEntity;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.entities.StudentEntity;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;

@Service
public class StudentCertificationAnswerUseCase {

  @Autowired
  private StudentRepository studentRepository;
  
  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private CertificationStudentRepository certificationStudentRepository;

  @Autowired
  private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

  public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {

    var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

    if(hasCertification) {
      throw new Exception("Você já tirou sua certificação!");
    }

    List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());
    List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

    AtomicInteger correctAnswers = new AtomicInteger(0);

    dto.getQuestionsAnswer().stream().forEach(questionAnswer -> {
      var question = questionsEntity.stream().filter(q -> q.getId().equals(questionAnswer.getQuestionId()))
        .findFirst().get();
      
        var findCorrectAlternative = question.getAlternatives().stream().filter(alternative -> alternative.isCorrect()).findFirst().get();

        if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeId())) {
          questionAnswer.setCorrect(true);
          correctAnswers.incrementAndGet();
        } else {
          questionAnswer.setCorrect(false);
        }

        var answersCertificationsEntity = AnswersCertificationsEntity.builder()
          .answerId(questionAnswer.getAlternativeId())
          .questionId(questionAnswer.getQuestionId())
          .isCorrect(questionAnswer.isCorrect()).build();
        
        answersCertifications.add(answersCertificationsEntity); 
    });

    var student = studentRepository.findByEmail(dto.getEmail());

    UUID studentId;

    if(student.isEmpty()) {
      var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
      studentCreated = studentRepository.save(studentCreated);
      studentId = studentCreated.getId();
    } else {
      studentId = student.get().getId();
    }

    CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
      .technology(dto.getTechnology())
      .studentId(studentId)
      .grade(correctAnswers.get())
      .build();
    
    var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

    answersCertifications.stream().forEach(answersCertification -> {
      answersCertification.setCertificationId(certificationStudentEntity.getId());
      answersCertification.setCertificationStudentEntity(certificationStudentEntity);
    });

    certificationStudentEntity.setAnswersCertificationsEntity(answersCertifications);
    certificationStudentRepository.save(certificationStudentEntity);

    return certificationStudentCreated;
  }
}
