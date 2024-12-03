package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Subject generated by hbm2java
 */
@Entity
@Table(name="subject"
    ,schema="public"
)
public class Subject  implements java.io.Serializable {


     private int id;
     private Teacher teacher;
     private String subName;
     private Set<StudyProgram>  studyPrograms = new HashSet<>(0);
     private Set<StudyPlan>  studyPlans = new HashSet<>(0);

    public Subject() {
    }

    public Subject(int id, Teacher teacher, String subName, Set<StudyProgram> studyPrograms, Set<StudyPlan>  studyPlans) {
       this.id = id;
       this.teacher = teacher;
       this.subName = subName;
       this.studyPrograms = studyPrograms;
       this.studyPlans = studyPlans;
    }

    @Id
    @Column(name="id", unique=true, nullable=false)
    @SequenceGenerator(name = "subject", sequenceName = "subject_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject")
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="teacher_id", nullable=false)
    public Teacher getTeacher() {
        return this.teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    
    @Column(name="sub_name", nullable=false)
    public String getSubName() {
        return this.subName;
    }
    
    public void setSubName(String subName) {
        this.subName = subName;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="subject")
    public Set<StudyProgram> getStudyPrograms() {
        return this.studyPrograms;
    }
    
    public void setStudyPrograms(Set<StudyProgram> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="subject")
    public Set<StudyPlan> getStudyPlans() {
        return this.studyPlans;
    }
    
    public void setStudyPlans(Set<StudyPlan>  studyPlans) {
        this.studyPlans = studyPlans;
    }

    @Override
    public String toString() {
        return subName; // Показывать название департамента в ComboBox
    }


}


