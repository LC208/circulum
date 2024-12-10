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
    }

    public static String translate(String key) {
        return translations.getOrDefault(key, key); // Если ключ не найден, вернуть сам ключ.
    }
}
