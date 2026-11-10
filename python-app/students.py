
import psycopg2


def add_student(conn, name, grade):
    cursor = conn.cursor()

    cursor.execute(
        "INSERT INTO students (name, grade) VALUES (%s, %s) RETURNING id",
        (name, grade)
    )

    student_id = cursor.fetchone()[0]
    conn.commit()
    cursor.close()

    return student_id


def get_student(conn, student_id):
    cursor = conn.cursor()

    cursor.execute(
        "SELECT id, name, grade FROM students WHERE id = %s",
        (student_id,)
    )

    student = cursor.fetchone()
    cursor.close()

    if student is None:
        return None

    return {
        "id": student[0],
        "name": student[1],
        "grade": float(student[2])
    }


def average_grade(conn):
    cursor = conn.cursor()

    cursor.execute("SELECT AVG(grade) FROM students")

    result = cursor.fetchone()[0]

    cursor.close()

    return float(result) if result is not None else 0.0
