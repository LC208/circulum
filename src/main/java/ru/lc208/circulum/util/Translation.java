package ru.lc208.circulum.util;

import java.util.HashMap;
import java.util.Map;

public class Translation {
    private static final Map<String, String> translations = new HashMap<>();

    static {
        translations.put("title", "Название");
        translations.put("save", "Сохранить");
        translations.put("cancel", "Отменить");
        translations.put("name", "Имя");
        translations.put("description", "Описание");
        translations.put("Competition", "Компетенции");
        translations.put("Department", "Отдел");
        translations.put("Direction", "Направление");
        translations.put("Faculty", "Факультет");
        translations.put("Gear", "Оборудование");
        translations.put("Section", "Раздел");
        translations.put("Speciality", "Специальность");
        translations.put("StudyPlan", "Учебный план");
        translations.put("StudyProgram", "Учебная программа");
        translations.put("Subject", "Предмет");
        translations.put("Teacher", "Преподаватель");
        translations.put("Theme", "Тема");
        translations.put("WorkType", "Тип работы");
        translations.put("id", "Идентификатор");
        translations.put("studyPrograms", "Учебные программы");
        translations.put("faculty", "Факультет");
        translations.put("depName", "Имя отдела");
        translations.put("deanId", "Идентификатор декана");
        translations.put("teachers", "Учителя");
        translations.put("studyPlans", "Учебные планы");
        translations.put("dirName", "Имя направления");
        translations.put("specialities", "Специальности");
        translations.put("departments", "Отделы");
        translations.put("type", "Тип");
        translations.put("theme", "Тема");
        translations.put("content", "Содержание");
        translations.put("direction", "Направление");
        translations.put("specName", "Имя специальности");
        translations.put("subject", "Предмет");
        translations.put("speciality", "Специальность");
        translations.put("tasksTargets", "Цели и задачи");
        translations.put("requirments", "Требования");
        translations.put("discPlace", "Место дисциплины");
        translations.put("gears", "Оборудование");
        translations.put("competitions", "Компетенции");
        translations.put("themes", "Темы");
        translations.put("teacher", "Преподаватель");
        translations.put("subName", "Имя предмета");
        translations.put("department", "Отдел");
        translations.put("subjects", "Предметы");
        translations.put("sections", "Разделы");
        translations.put("hours", "Часы");
        translations.put("workTypes", "Типы работы");
        translations.put("workType", "Тип работы");
    }

    public static String translate(String key) {
        return translations.getOrDefault(key, key); // Если ключ не найден, вернуть сам ключ.
    }
}
