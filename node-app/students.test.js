const { pool, addStudent, getStudent, averageGrade } = require("./students");

beforeEach(async () => {
  await pool.query("DELETE FROM students");
});

afterAll(async () => {
  await pool.end();
});

test("ajouter puis lire un étudiant", async () => {
  const id = await addStudent("Amel", 15.5);
  expect(await getStudent(id)).toEqual({ name: "Amel", grade: 15.5 });
});

test("étudiant inconnu", async () => {
  expect(await getStudent(9999)).toBeNull();
});

test("moyenne des notes", async () => {
  await addStudent("Amel", 10);
  await addStudent("Karim", 14);
  expect(await averageGrade()).toBe(12);
});
