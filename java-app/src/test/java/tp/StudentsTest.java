package tp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentsTest {

    private Connection conn;

    @BeforeEach
    void setUp() throws SQLException {
        conn = Students.connect();
        try (Statement st = conn.createStatement()) {
            st.execute("DELETE FROM students");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    void ajouterEtLire() throws SQLException {
        int id = Students.add(conn, "Amel", 15.5);
        assertEquals(Optional.of(new Students.Student("Amel", 15.5)), Students.get(conn, id));
    }

    @Test
    void etudiantInconnu() throws SQLException {
        assertTrue(Students.get(conn, 9999).isEmpty());
    }

    @Test
    void moyenne() throws SQLException {
        Students.add(conn, "Amel", 10);
        Students.add(conn, "Karim", 14);
        assertEquals(12.0, Students.average(conn).getAsDouble(), 0.001);
    }
}
