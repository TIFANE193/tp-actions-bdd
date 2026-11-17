package tp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.OptionalDouble;

public class Students {

    public record Student(String name, double grade) {}

    public static Connection connect() throws SQLException {
        String url = "jdbc:postgresql://" + env("DB_HOST", "localhost")
                + ":" + env("DB_PORT", "5432") + "/" + env("DB_NAME", "tp");
        return DriverManager.getConnection(url,
                env("DB_USER", "postgres"), env("DB_PASSWORD", "postgres"));
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null ? defaultValue : value;
    }

    /** Ajoute un étudiant et retourne son id. */
    public static int add(Connection conn, String name, double grade) throws SQLException {
        String sql = "INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setBigDecimal(2, BigDecimal.valueOf(grade));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    /** Retourne l'étudiant, ou Optional.empty() s'il n'existe pas. */
    public static Optional<Student> get(Connection conn, int id) throws SQLException {
        String sql = "SELECT name, grade FROM students WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Student(rs.getString("name"), rs.getDouble("grade")));
            }
        }
    }

    /** Retourne la moyenne des notes, ou OptionalDouble.empty() s'il n'y a aucun étudiant. */
    public static OptionalDouble average(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT AVG(grade) FROM students");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            double value = rs.getDouble(1);
            return rs.wasNull() ? OptionalDouble.empty() : OptionalDouble.of(value);
        }
    }
}
