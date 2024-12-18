package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * StudyProgramView generated by hbm2java
 */
@Entity
@Table(name="study_program_view"
    ,schema="public"
)
public class StudyProgramView  implements java.io.Serializable {


     private StudyProgramViewId id;

    public StudyProgramView() {
    }

    public StudyProgramView(StudyProgramViewId id) {
       this.id = id;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="content", column=@Column(name="content") ), 
        @AttributeOverride(name="departmentName", column=@Column(name="department_name") ), 
        @AttributeOverride(name="discPlace", column=@Column(name="disc_place") ), 
        @AttributeOverride(name="facultyName", column=@Column(name="faculty_name") ), 
        @AttributeOverride(name="name", column=@Column(name="name") ), 
        @AttributeOverride(name="programId", column=@Column(name="program_id") ), 
        @AttributeOverride(name="requirments", column=@Column(name="requirments") ), 
        @AttributeOverride(name="specialityName", column=@Column(name="speciality_name") ), 
        @AttributeOverride(name="subjectName", column=@Column(name="subject_name") ), 
        @AttributeOverride(name="tasksTargets", column=@Column(name="tasks_targets") ), 
        @AttributeOverride(name="teacherDepartmentId", column=@Column(name="teacher_department_id") ), 
        @AttributeOverride(name="teacherId", column=@Column(name="teacher_id") ), 
        @AttributeOverride(name="themeName", column=@Column(name="theme_name") ) } )
    public StudyProgramViewId getId() {
        return this.id;
    }
    
    public void setId(StudyProgramViewId id) {
        this.id = id;
    }




}


