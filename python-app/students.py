def average_grade(conn):
    cursor = conn.cursor()

    cursor.execute("SELECT AVG(grade) FROM students")

    result = cursor.fetchone()[0]

    cursor.close()

    return float(result) if result is not None else 0.0