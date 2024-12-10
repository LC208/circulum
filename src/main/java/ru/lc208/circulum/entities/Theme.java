package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Theme generated by hbm2java
 */
@Entity
@Table(name="theme"
    ,schema="public"
)
public class Theme  implements java.io.Serializable {

    public boolean equals(Object obj) {
        if(obj instanceof Theme)
        {
            return this.id == ((Theme)obj).id;
        }
        return false;
    }
     private int id;
     private String name;
     private Set<Section> sections = new HashSet<>(0);
     private Set<StudyProgram> studyPrograms = new HashSet<>(0);

    public Theme() {
    }

    public Theme(int id, String name, Set<Section> sections, Set<StudyProgram> studyPrograms) {
       this.id = id;
       this.name = name;
       this.sections = sections;
       this.studyPrograms = studyPrograms;
    }

    @Id
    @Column(name="id", unique=true, nullable=false)
    @SequenceGenerator(name = "theme", sequenceName = "theme_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "theme")
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

@OneToMany(fetch=FetchType.LAZY, mappedBy="theme")
    public Set<Section> getSections() {
        return this.sections;
    }
    
    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="program_theme", schema="public", joinColumns = { 
        @JoinColumn(name="theme_id", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="program_id", nullable=false, updatable=false) })
    public Set<StudyProgram> getStudyPrograms() {
        return this.studyPrograms;
    }
    
    public void setStudyPrograms(Set<StudyProgram> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }


    @Override
    public String toString() {
        return name; // Показывать название департамента в ComboBox
    }

}


