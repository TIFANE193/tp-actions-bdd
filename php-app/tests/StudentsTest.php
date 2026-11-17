<?php
use App\Students;
use PHPUnit\Framework\TestCase;

class StudentsTest extends TestCase
{
    private PDO $pdo;

    protected function setUp(): void
    {
        $this->pdo = Students::connect();
        $this->pdo->exec("DELETE FROM students");
    }

    public function testAjouterEtLire(): void
    {
        $id = Students::add($this->pdo, "Amel", 15.5);
        $this->assertSame(["name" => "Amel", "grade" => 15.5], Students::get($this->pdo, $id));
    }

    public function testEtudiantInconnu(): void
    {
        $this->assertNull(Students::get($this->pdo, 9999));
    }

    public function testMoyenne(): void
    {
        Students::add($this->pdo, "Amel", 10);
        Students::add($this->pdo, "Karim", 14);
        $this->assertSame(12.0, Students::average($this->pdo));
    }
}
