const { Pool } = require("pg");

const pool = new Pool({
  host: process.env.DB_HOST || "localhost",
  port: Number(process.env.DB_PORT || 5432),
  database: process.env.DB_NAME || "tp",
  user: process.env.DB_USER || "postgres",
  password: process.env.DB_PASSWORD || "postgres",
});

// Ajoute un étudiant et retourne son id
async function addStudent(name, grade) {
  const r = await pool.query(
    "INSERT INTO students (name, grade) VALUES ($1, $2) RETURNING id",
    [name, grade]
  );
  return r.rows[0].id;
}

// Retourne { name, grade }, ou null si l'étudiant n'existe pas
async function getStudent(id) {
  const r = await pool.query("SELECT name, grade FROM students WHERE id = $1", [id]);
  if (r.rows.length === 0) return null;
  return { name: r.rows[0].name, grade: Number(r.rows[0].grade) };
}

// Retourne la moyenne des notes, ou null s'il n'y a aucun étudiant
async function averageGrade() {
  const r = await pool.query("SELECT AVG(grade) AS moyenne FROM students");
  const value = r.rows[0].moyenne;
  return value === null ? null : Number(value);
}

module.exports = { pool, addStudent, getStudent, averageGrade };
