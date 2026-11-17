<?php
namespace App;

use PDO;

class Students
{
    public static function connect(): PDO
    {
        $dsn = sprintf(
            "pgsql:host=%s;port=%s;dbname=%s",
            getenv("DB_HOST") ?: "localhost",
            getenv("DB_PORT") ?: "5432",
            getenv("DB_NAME") ?: "tp"
        );
        return new PDO(
            $dsn,
            getenv("DB_USER") ?: "postgres",
            getenv("DB_PASSWORD") ?: "postgres",
            [PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]
        );
    }

    // Ajoute un étudiant et retourne son id
    public static function add(PDO $pdo, string $name, float $grade): int
    {
        $st = $pdo->prepare("INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id");
        $st->execute([$name, $grade]);
        return (int) $st->fetchColumn();
    }

    // Retourne ["name" => ..., "grade" => ...], ou null si l'étudiant n'existe pas
    public static function get(PDO $pdo, int $id): ?array
    {
        $st = $pdo->prepare("SELECT name, grade FROM students WHERE id = ?");
        $st->execute([$id]);
        $row = $st->fetch(PDO::FETCH_ASSOC);
        if ($row === false) {
            return null;
        }
        return ["name" => $row["name"], "grade" => (float) $row["grade"]];
    }

    // Retourne la moyenne des notes, ou null s'il n'y a aucun étudiant
    public static function average(PDO $pdo): ?float
    {
        $value = $pdo->query("SELECT AVG(grade) FROM students")->fetchColumn();
        return $value === null ? null : (float) $value;
    }
}
