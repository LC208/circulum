package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Gear generated by hbm2java
 */
@Entity
@Table(name="gear"
    ,schema="public"
)
public class Gear  implements java.io.Serializable {


     private int id;
     private String type;
     private String description;
     private Set<StudyProgram> studyPrograms = new HashSet<>(0);

    public Gear() {
    }

    public Gear(int id, String type, String description, Set<StudyProgram> studyPrograms) {
       this.id = id;
       this.type = type;
       this.description = description;
       this.studyPrograms = studyPrograms;
    }

    @Id
    @Column(name="id", unique=true, nullable=false)
    @SequenceGenerator(name = "gear", sequenceName = "gear_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gear")
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    @Column(name="type", nullable=false)
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    
    @Column(name="description", nullable=false)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="program_gear", schema="public", joinColumns = { 
        @JoinColumn(name="gear_id", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="program_id", nullable=false, updatable=false) })
    public Set<StudyProgram> getStudyPrograms() {
        return this.studyPrograms;
    }
    
    public void setStudyPrograms(Set<StudyProgram> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }


    @Override
    public String toString() {
        return type; // Показывать название департамента в ComboBox
    }

}


