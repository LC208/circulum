package ru.lc208.circulum.entities;// default package
// Generated 28 нояб. 2024 г., 21:40:16 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * StudyProgramViewId generated by hbm2java
 */
@Embeddable
public class StudyProgramViewId  implements java.io.Serializable {


     private String content;
     private String departmentName;
     private String discPlace;
     private String facultyName;
     private String name;
     private Integer programId;
     private String requirments;
     private String specialityName;
     private String subjectName;
     private String tasksTargets;
     private Integer teacherDepartmentId;
     private Integer teacherId;
     private String themeName;

    public StudyProgramViewId() {
    }

    public StudyProgramViewId(String content, String departmentName, String discPlace, String facultyName, String name, Integer programId, String requirments, String specialityName, String subjectName, String tasksTargets, Integer teacherDepartmentId, Integer teacherId, String themeName) {
       this.content = content;
       this.departmentName = departmentName;
       this.discPlace = discPlace;
       this.facultyName = facultyName;
       this.name = name;
       this.programId = programId;
       this.requirments = requirments;
       this.specialityName = specialityName;
       this.subjectName = subjectName;
       this.tasksTargets = tasksTargets;
       this.teacherDepartmentId = teacherDepartmentId;
       this.teacherId = teacherId;
       this.themeName = themeName;
    }
   


    @Column(name="content")
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }


    @Column(name="department_name")
    public String getDepartmentName() {
        return this.departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


    @Column(name="disc_place")
    public String getDiscPlace() {
        return this.discPlace;
    }
    
    public void setDiscPlace(String discPlace) {
        this.discPlace = discPlace;
    }


    @Column(name="faculty_name")
    public String getFacultyName() {
        return this.facultyName;
    }
    
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }


    @Column(name="name")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }


    @Column(name="program_id")
    public Integer getProgramId() {
        return this.programId;
    }
    
    public void setProgramId(Integer programId) {
        this.programId = programId;
    }


    @Column(name="requirments")
    public String getRequirments() {
        return this.requirments;
    }
    
    public void setRequirments(String requirments) {
        this.requirments = requirments;
    }


    @Column(name="speciality_name")
    public String getSpecialityName() {
        return this.specialityName;
    }
    
    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }


    @Column(name="subject_name")
    public String getSubjectName() {
        return this.subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }


    @Column(name="tasks_targets")
    public String getTasksTargets() {
        return this.tasksTargets;
    }
    
    public void setTasksTargets(String tasksTargets) {
        this.tasksTargets = tasksTargets;
    }


    @Column(name="teacher_department_id")
    public Integer getTeacherDepartmentId() {
        return this.teacherDepartmentId;
    }
    
    public void setTeacherDepartmentId(Integer teacherDepartmentId) {
        this.teacherDepartmentId = teacherDepartmentId;
    }


    @Column(name="teacher_id")
    public Integer getTeacherId() {
        return this.teacherId;
    }
    
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }


    @Column(name="theme_name")
    public String getThemeName() {
        return this.themeName;
    }
    
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof StudyProgramViewId) ) return false;
		 StudyProgramViewId castOther = ( StudyProgramViewId ) other; 
         
		 return ( (this.getContent()==castOther.getContent()) || ( this.getContent()!=null && castOther.getContent()!=null && this.getContent().equals(castOther.getContent()) ) )
 && ( (this.getDepartmentName()==castOther.getDepartmentName()) || ( this.getDepartmentName()!=null && castOther.getDepartmentName()!=null && this.getDepartmentName().equals(castOther.getDepartmentName()) ) )
 && ( (this.getDiscPlace()==castOther.getDiscPlace()) || ( this.getDiscPlace()!=null && castOther.getDiscPlace()!=null && this.getDiscPlace().equals(castOther.getDiscPlace()) ) )
 && ( (this.getFacultyName()==castOther.getFacultyName()) || ( this.getFacultyName()!=null && castOther.getFacultyName()!=null && this.getFacultyName().equals(castOther.getFacultyName()) ) )
 && ( (this.getName()==castOther.getName()) || ( this.getName()!=null && castOther.getName()!=null && this.getName().equals(castOther.getName()) ) )
 && ( (this.getProgramId()==castOther.getProgramId()) || ( this.getProgramId()!=null && castOther.getProgramId()!=null && this.getProgramId().equals(castOther.getProgramId()) ) )
 && ( (this.getRequirments()==castOther.getRequirments()) || ( this.getRequirments()!=null && castOther.getRequirments()!=null && this.getRequirments().equals(castOther.getRequirments()) ) )
 && ( (this.getSpecialityName()==castOther.getSpecialityName()) || ( this.getSpecialityName()!=null && castOther.getSpecialityName()!=null && this.getSpecialityName().equals(castOther.getSpecialityName()) ) )
 && ( (this.getSubjectName()==castOther.getSubjectName()) || ( this.getSubjectName()!=null && castOther.getSubjectName()!=null && this.getSubjectName().equals(castOther.getSubjectName()) ) )
 && ( (this.getTasksTargets()==castOther.getTasksTargets()) || ( this.getTasksTargets()!=null && castOther.getTasksTargets()!=null && this.getTasksTargets().equals(castOther.getTasksTargets()) ) )
 && ( (this.getTeacherDepartmentId()==castOther.getTeacherDepartmentId()) || ( this.getTeacherDepartmentId()!=null && castOther.getTeacherDepartmentId()!=null && this.getTeacherDepartmentId().equals(castOther.getTeacherDepartmentId()) ) )
 && ( (this.getTeacherId()==castOther.getTeacherId()) || ( this.getTeacherId()!=null && castOther.getTeacherId()!=null && this.getTeacherId().equals(castOther.getTeacherId()) ) )
 && ( (this.getThemeName()==castOther.getThemeName()) || ( this.getThemeName()!=null && castOther.getThemeName()!=null && this.getThemeName().equals(castOther.getThemeName()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getContent() == null ? 0 : this.getContent().hashCode() );
         result = 37 * result + ( getDepartmentName() == null ? 0 : this.getDepartmentName().hashCode() );
         result = 37 * result + ( getDiscPlace() == null ? 0 : this.getDiscPlace().hashCode() );
         result = 37 * result + ( getFacultyName() == null ? 0 : this.getFacultyName().hashCode() );
         result = 37 * result + ( getName() == null ? 0 : this.getName().hashCode() );
         result = 37 * result + ( getProgramId() == null ? 0 : this.getProgramId().hashCode() );
         result = 37 * result + ( getRequirments() == null ? 0 : this.getRequirments().hashCode() );
         result = 37 * result + ( getSpecialityName() == null ? 0 : this.getSpecialityName().hashCode() );
         result = 37 * result + ( getSubjectName() == null ? 0 : this.getSubjectName().hashCode() );
         result = 37 * result + ( getTasksTargets() == null ? 0 : this.getTasksTargets().hashCode() );
         result = 37 * result + ( getTeacherDepartmentId() == null ? 0 : this.getTeacherDepartmentId().hashCode() );
         result = 37 * result + ( getTeacherId() == null ? 0 : this.getTeacherId().hashCode() );
         result = 37 * result + ( getThemeName() == null ? 0 : this.getThemeName().hashCode() );
         return result;
   }   


}


