package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Teacher generated by hbm2java
 */
@Entity
@Table(name="teacher"
    ,schema="public"
)
public class Teacher  implements java.io.Serializable {

    public boolean equals(Object obj) {
        if(obj instanceof Teacher)
        {
            return this.id == ((Teacher)obj).id;
        }
        return false;
    }
     private int id;
     private Department department;
     private Set<Subject> subjects = new HashSet<>(0);

    public Teacher() {
    }

    public Teacher(int id, Department department, Set<Subject> subjects) {
       this.id = id;
       this.department = department;
       this.subjects = subjects;
    }

    @Id
    @Column(name="id", unique=true, nullable=false)
    @SequenceGenerator(name = "teacher", sequenceName = "teacher_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher")
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="dep_id", nullable=false)
    public Department getDepartment() {
        return this.department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="teacher")
    public Set<Subject> getSubjects() {
        return this.subjects;
    }
    
    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return String.valueOf(id) + " - " + department;
    }
}


