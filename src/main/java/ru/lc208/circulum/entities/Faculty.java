package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Faculty generated by hbm2java
 */
@Entity
@Table(name="faculty"
    ,schema="public"
)
public class Faculty  implements java.io.Serializable {


     private int id;
     private String name;
     private Set departments = new HashSet(0);
     private Set specialities = new HashSet(0);

    public Faculty() {
    }

    public Faculty(int id, String name, Set departments, Set specialities) {
       this.id = id;
       this.name = name;
       this.departments = departments;
       this.specialities = specialities;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    @Column(name="name", nullable=false)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="faculty")
    public Set getDepartments() {
        return this.departments;
    }
    
    public void setDepartments(Set departments) {
        this.departments = departments;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="faculty")
    public Set getSpecialities() {
        return this.specialities;
    }
    
    public void setSpecialities(Set specialities) {
        this.specialities = specialities;
    }




}

