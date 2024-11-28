package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * StudyPlan generated by hbm2java
 */
@Entity
@Table(name="study_plan"
    ,schema="public"
)
public class StudyPlan  implements java.io.Serializable {


     private int id;
     private Subject subject;
     private Speciality speciality;
     private Department department;
     private int hours;
     private Set workTypes = new HashSet(0);

    public StudyPlan() {
    }

    public StudyPlan(int id, Subject subject, Speciality speciality, Department department, int hours, Set workTypes) {
       this.id = id;
       this.subject = subject;
       this.speciality = speciality;
       this.department = department;
       this.hours = hours;
       this.workTypes = workTypes;
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
    @JoinColumn(name="sub_id", nullable=false)
    public Subject getSubject() {
        return this.subject;
    }
    
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="spec_id", nullable=false)
    public Speciality getSpeciality() {
        return this.speciality;
    }
    
    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="dep_id", nullable=false)
    public Department getDepartment() {
        return this.department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }

    
    @Column(name="hours", nullable=false)
    public int getHours() {
        return this.hours;
    }
    
    public void setHours(int hours) {
        this.hours = hours;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="plan_wtype", schema="public", joinColumns = { 
        @JoinColumn(name="plan_id", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="wtype_id", nullable=false, updatable=false) })
    public Set getWorkTypes() {
        return this.workTypes;
    }
    
    public void setWorkTypes(Set workTypes) {
        this.workTypes = workTypes;
    }




}


