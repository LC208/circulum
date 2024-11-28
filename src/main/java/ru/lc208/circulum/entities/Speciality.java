package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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


     private int id;
     private Faculty faculty;
     private Direction direction;
     private String specName;
     private Set studyPlans = new HashSet(0);
     private Set studyPrograms = new HashSet(0);

    public Speciality() {
    }

    public Speciality(int id, Faculty faculty, Direction direction, String specName, Set studyPlans, Set studyPrograms) {
       this.id = id;
       this.faculty = faculty;
       this.direction = direction;
       this.specName = specName;
       this.studyPlans = studyPlans;
       this.studyPrograms = studyPrograms;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false)
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
    public Set getStudyPlans() {
        return this.studyPlans;
    }
    
    public void setStudyPlans(Set studyPlans) {
        this.studyPlans = studyPlans;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="speciality")
    public Set getStudyPrograms() {
        return this.studyPrograms;
    }
    
    public void setStudyPrograms(Set studyPrograms) {
        this.studyPrograms = studyPrograms;
    }




}

