package Server.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Common.models.Coordinates;
import Common.models.FormOfEducation;
import Common.models.Location;
import Common.models.Person;
import Common.models.StudyGroup;

public class StudyGroupDAO {
    private final Connection connection;

    public StudyGroupDAO(Connection connection) {
        this.connection = connection;
        try {
            createDefaultUserIfNotExists();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create default user", e);
        }
    }

    private void createDefaultUserIfNotExists() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, "admin"); // In a real app, this should be a proper hash
                    pstmt.executeUpdate();
                }
            }
        }
    }

    public void add(StudyGroup group, int userId) throws SQLException {
        // First ensure the user exists
        String checkUserSql = "SELECT id FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkUserSql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("User with ID " + userId + " does not exist");
                }
            }
        }

        String sql = "INSERT INTO study_groups (name, coordinates_x, coordinates_y, creation_date, " +
                    "students_count, expelled_students, transferred_students, form_of_education, " +
                    "group_admin_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, group.getName());
            stmt.setLong(2, group.getCoordinates().getX());
            stmt.setLong(3, group.getCoordinates().getY());
            stmt.setDate(4, Date.valueOf(group.getCreationDate().toLocalDate()));
            stmt.setLong(5, group.getStudentsCount());
            stmt.setInt(6, group.getExpelledStudents());
            stmt.setLong(7, group.getTransferredStudents());
            stmt.setObject(8, group.getFormOfEducation().name(), java.sql.Types.OTHER);
            
            // First insert the group admin (Person)
            int adminId = insertPerson(group.getGroupAdmin());
            stmt.setInt(9, adminId);
            stmt.setInt(10, userId);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    group.setId(rs.getInt(1));
                }
            }
        }
    }

    private int insertPerson(Person person) throws SQLException {
        // Сначала проверяем, существует ли человек с таким паспортом
        String checkSql = "SELECT id FROM persons WHERE passport_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, person.getPassportID());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Возвращаем ID существующего человека
                }
            }
        }

        // Если человек не найден, создаем нового
        String sql = "INSERT INTO persons (name, birthday, passport_id, eye_color, location_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, person.getName());
            stmt.setDate(2, person.getBirthday() != null ? Date.valueOf(person.getBirthday()) : null);
            stmt.setString(3, person.getPassportID());
            stmt.setObject(4, person.getEyeColor().name(), java.sql.Types.OTHER);
            
            // Insert location first
            int locationId = insertLocation(person.getLocation());
            stmt.setInt(5, locationId);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Failed to get generated person ID");
            }
        }
    }

    private int insertLocation(Location location) throws SQLException {
        String sql = "INSERT INTO locations (x, y, z) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setFloat(1, location.getX());
            stmt.setFloat(2, location.getY());
            stmt.setFloat(3, location.getZ());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Failed to get generated location ID");
            }
        }
    }

    public boolean removeById(long id) throws SQLException {
        String sql = "DELETE FROM study_groups WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateById(long id, StudyGroup newGroup) throws SQLException {
        String sql = "UPDATE study_groups SET name = ?, coordinates_x = ?, coordinates_y = ?, " +
                    "students_count = ?, expelled_students = ?, transferred_students = ?, " +
                    "form_of_education = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newGroup.getName());
            stmt.setLong(2, newGroup.getCoordinates().getX());
            stmt.setLong(3, newGroup.getCoordinates().getY());
            stmt.setLong(4, newGroup.getStudentsCount());
            stmt.setInt(5, newGroup.getExpelledStudents());
            stmt.setLong(6, newGroup.getTransferredStudents());
            stmt.setObject(7, newGroup.getFormOfEducation().name(), java.sql.Types.OTHER);
            stmt.setLong(8, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public void clear() throws SQLException {
        String sql = "DELETE FROM study_groups";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public StudyGroup getById(long id) throws SQLException {
        String sql = "SELECT * FROM study_groups WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudyGroup(rs);
                }
            }
        }
        return null;
    }

    public List<StudyGroup> getAll() throws SQLException {
        List<StudyGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM study_groups ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                try {
                    StudyGroup group = mapResultSetToStudyGroup(rs);
                    if (group != null) {
                        groups.add(group);
                    }
                } catch (SQLException e) {
                    System.err.println("Error mapping study group: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return groups;
    }

    private StudyGroup mapResultSetToStudyGroup(ResultSet rs) throws SQLException {
        StudyGroup group = new StudyGroup();
        try {
            group.setId(rs.getInt("id"));
            group.setName(rs.getString("name"));
            
            Coordinates coords = new Coordinates(rs.getLong("coordinates_x"), rs.getLong("coordinates_y"));
            group.setCoordinates(coords);
            
            // Convert Date to ZonedDateTime
            Date creationDate = rs.getDate("creation_date");
            if (creationDate != null) {
                try {
                    java.lang.reflect.Field field = group.getClass().getDeclaredField("creationDate");
                    field.setAccessible(true);
                    field.set(group, creationDate.toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault()));
                } catch (Exception e) {
                    throw new SQLException("Failed to set creationDate", e);
                }
            }
            
            group.setStudentsCount(rs.getLong("students_count"));
            group.setExpelledStudents(rs.getInt("expelled_students"));
            group.setTransferredStudents(rs.getInt("transferred_students"));
            group.setFormOfEducation(FormOfEducation.valueOf(rs.getString("form_of_education")));
            
            // Load group admin
            int adminId = rs.getInt("group_admin_id");
            Person admin = loadPerson(adminId);
            if (admin == null) {
                throw new SQLException("Failed to load group admin with ID: " + adminId);
            }
            group.setGroupAdmin(admin);
            
            return group;
        } catch (SQLException e) {
            System.err.println("Error mapping study group: " + e.getMessage());
            throw e;
        }
    }

    private Person loadPerson(int personId) throws SQLException {
        String sql = "SELECT * FROM persons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, personId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Person person = new Person();
                    person.setName(rs.getString("name"));
                    Date birthday = rs.getDate("birthday");
                    if (birthday != null) {
                        person.setBirthday(birthday.toLocalDate());
                    }
                    person.setPassportID(rs.getString("passport_id"));
                    person.setEyeColor(Common.models.Color.valueOf(rs.getString("eye_color")));
                    
                    // Load location
                    int locationId = rs.getInt("location_id");
                    person.setLocation(loadLocation(locationId));
                    
                    return person;
                }
            }
        }
        return null;
    }

    private Location loadLocation(int locationId) throws SQLException {
        String sql = "SELECT * FROM locations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Location(rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"));
                }
            }
        }
        return null;
    }
} 