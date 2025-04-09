package fileLogic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import models.*;

public class XMLWriter {

    /**
     * Сериализует коллекцию StudyGroup в XML и записывает результат в указанный файл.
     *
     * @param studyGroups коллекция объектов StudyGroup
     * @param filePath    путь к файлу для записи XML
     */
    public static void writeStudyGroupCollection(Collection<StudyGroup> studyGroups, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<Study_groups>\n");
            for (StudyGroup group : studyGroups) {
                serializeStudyGroup(group, writer, "    ");
            }
            writer.write("</Study_groups>\n");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении коллекции: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    /**
     * Сериализует один объект StudyGroup в XML.
     *
     * @param group  объект StudyGroup
     * @param writer экземпляр BufferedWriter
     * @param indent строка отступа для форматирования
     * @throws IOException
     */
    private static void serializeStudyGroup(StudyGroup group, BufferedWriter writer, String indent) throws IOException {
        writer.write(indent + "<StudyGroup>\n");
        String innerIndent = indent + "    ";

        writer.write(innerIndent + "<id>" + group.getId() + "</id>\n");
        writer.write(innerIndent + "<name>" + escapeXml(group.getName()) + "</name>\n");

        // Сериализация координат в одну строку
        if (group.getCoordinates() != null) {
            writer.write(innerIndent + "<coordinates>" + 
                group.getCoordinates().getX() + ", " + 
                group.getCoordinates().getY() + 
                "</coordinates>\n");
        }

        writer.write(innerIndent + "<creationDate>" + group.getCreationDate().toString() + "</creationDate>\n");
        writer.write(innerIndent + "<studentsCount>" + group.getStudentsCount() + "</studentsCount>\n");
        writer.write(innerIndent + "<expelledStudents>" + group.getExpelledStudents() + "</expelledStudents>\n");
        writer.write(innerIndent + "<transferredStudents>" + group.getTransferredStudents() + "</transferredStudents>\n");

        if (group.getFormOfEducation() != null) {
            writer.write(innerIndent + "<formOfEducation>" + escapeXml(group.getFormOfEducation().toString()) + "</formOfEducation>\n");
        }

        if (group.getGroupAdmin() != null) {
            writer.write(innerIndent + "<groupAdmin>\n");
            serializePerson(group.getGroupAdmin(), writer, innerIndent + "    ");
            writer.write(innerIndent + "</groupAdmin>\n");
        }

        writer.write(indent + "</StudyGroup>\n");
    }

    /**
     * Сериализует объект Person в XML.
     *
     * @param person объект Person
     * @param writer экземпляр BufferedWriter
     * @param indent строка отступа для форматирования
     * @throws IOException
     */
    private static void serializePerson(Person person, BufferedWriter writer, String indent) throws IOException {
        writer.write(indent + "<name>" + escapeXml(person.getName()) + "</name>\n");
        if (person.getBirthday() != null) {
            writer.write(indent + "<birthday>" + person.getBirthday().toString() + "</birthday>\n");
        }
        if (person.getPassportID() != null) {
            writer.write(indent + "<passportID>" + escapeXml(person.getPassportID()) + "</passportID>\n");
        }
        writer.write(indent + "<eyeColor>" + escapeXml(person.getEyeColor().toString()) + "</eyeColor>\n");
        
        // Сериализация локации в одну строку
        if (person.getLocation() != null) {
            writer.write(indent + "<location>" + 
                person.getLocation().getX() + ", " + 
                person.getLocation().getY() + ", " + 
                person.getLocation().getZ() + 
                "</location>\n");
        }
    }

    /**
     * Экранирует специальные символы XML в переданной строке.
     *
     * @param input исходная строка
     * @return строка с заменёнными специальными символами
     */
    private static String escapeXml(String input) {
        if (input == null) {
            return "";
        }
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }
} 