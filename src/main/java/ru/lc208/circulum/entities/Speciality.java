package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Speciality generated by hbm2java
 */
@Entity
@Table(name="speciality"
    ,schema="public"
)
public class Speciality  implements java.io.Serializable {

    public boolean equals(Object obj) {
        if(obj instanceof Speciality)
        {
            return this.id == ((Speciality)obj).id;
        }
        return false;
    }
     private int id;
     private Faculty faculty;
     private Direction direction;
     private String specName;
     private Set<StudyPlan> studyPlans = new HashSet<>(0);
     private Set<StudyProgram> studyPrograms = new HashSet<>(0);

    public Speciality() {
    }

    public Speciality(int id, Faculty faculty, Direction direction, String specName, Set<StudyPlan> studyPlans, Set<StudyProgram> studyPrograms) {
       this.id = id;
       this.faculty = faculty;
       this.direction = direction;
       this.specName = specName;
       this.studyPlans = studyPlans;
       this.studyPrograms = studyPrograms;
    }

    @Id
    @Column(name="id", unique=true, nullable=false)
    @SequenceGenerator(name = "speciality", sequenceName = "speciality_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "speciality")
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fac_id", nullable=false)
    public Faculty getFaculty() {
        return this.faculty;
    }
    
    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="dir_id", nullable=false)
    public Direction getDirection() {
        return this.direction;
    }
    
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    
    @Column(name="spec_name", nullable=false)
    public String getSpecName() {
        return this.specName;
    }
    
    public void setSpecName(String specName) {
        this.specName = specName;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="speciality")
    public Set<StudyPlan> getStudyPlans() {
        return this.studyPlans;
    }
    
    public void setStudyPlans(Set<StudyPlan> studyPlans) {
        this.studyPlans = studyPlans;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="speciality")
    public Set<StudyProgram> getStudyPrograms() {
        return this.studyPrograms;
    }
    
    public void setStudyPrograms(Set<StudyProgram> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }

    @Override
    public String toString() {
        return specName; // Показывать название департамента в ComboBox
    }


}


