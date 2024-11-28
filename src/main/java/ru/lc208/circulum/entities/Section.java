package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Section generated by hbm2java
 */
@Entity
@Table(name="section"
    ,schema="public"
)
public class Section  implements java.io.Serializable {


     private int id;
     private Theme theme;
     private String name;
     private String content;

    public Section() {
    }

    public Section(int id, Theme theme, String name, String content) {
       this.id = id;
       this.theme = theme;
       this.name = name;
       this.content = content;
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
    @JoinColumn(name="theme_id", nullable=false)
    public Theme getTheme() {
        return this.theme;
    }
    
    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    
    @Column(name="name", nullable=false)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name="content", nullable=false)
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }




}

