package mn.edu.must.sqat;

// ЛАБ 11 — B1: сул assertion-уудыг яг утгын assertEquals болгосон.
// ЛАБ 11 — B2: зааг утгын тестүүд, оролтын хязгаар, бүх үсгэн дүн нэмсэн.

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScholarshipCalculatorTest {

    ScholarshipCalculator calc;

    @BeforeEach
    void setUp() { calc = new ScholarshipCalculator(); }

    // ================= B1: яг утгын шалгалт =================

    @Test
    void letterGrade_returnsExactGradeForAllRanges() {
        assertEquals("A", calc.letterGrade(95));
        assertEquals("B", calc.letterGrade(85));
        assertEquals("C", calc.letterGrade(75));
        assertEquals("D", calc.letterGrade(65));
        assertEquals("F", calc.letterGrade(30));
    }

    @Test
    void letterGrade_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));
    }

    @Test
    void gpa_returnsExactValues() {
        assertEquals(3.5, calc.gpa(new double[]{95, 85}), 1e-9);
        assertEquals(1.5, calc.gpa(new double[]{75, 65}), 1e-9);
        assertEquals(0.0, calc.gpa(new double[]{40}), 1e-9);
    }

    @Test
    void gpa_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{}));
    }

    @Test
    void scholarship_returnsExactAmounts() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
        assertEquals(70, calc.scholarship(3.5, 13, false));
        assertEquals(50, calc.scholarship(3.1, 5, false));
        assertEquals(0, calc.scholarship(2.0, 1, false));
        assertEquals(0, calc.scholarship(4.0, 20, true));
    }

    @Test
    void scholarship_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.1, 10, false));
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.0, -1, false));
    }

    // ================= B2: зааг утгууд =================

    // letterGrade: 90/89.99, 80/79.99, 70/69.99, 60/59.99, 0, 100
    @Test
    void letterGrade_boundaries() {
        assertEquals("A", calc.letterGrade(100));
        assertEquals("A", calc.letterGrade(90));
        assertEquals("B", calc.letterGrade(89.99));
        assertEquals("B", calc.letterGrade(80));
        assertEquals("C", calc.letterGrade(79.99));
        assertEquals("C", calc.letterGrade(70));
        assertEquals("D", calc.letterGrade(69.99));
        assertEquals("D", calc.letterGrade(60));
        assertEquals("F", calc.letterGrade(59.99));
        assertEquals("F", calc.letterGrade(0));
    }

    @Test
    void letterGrade_justOutsideRange_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-0.01));
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(100.01));
    }

    // gpa: switch-ийн бүх үсгэн дүн тус бүрийг ганцаарчлан
    @Test
    void gpa_eachLetterGradeContributesExactPoints() {
        assertEquals(4.0, calc.gpa(new double[]{95}), 1e-9);
        assertEquals(3.0, calc.gpa(new double[]{85}), 1e-9);
        assertEquals(2.0, calc.gpa(new double[]{75}), 1e-9);
        assertEquals(1.0, calc.gpa(new double[]{65}), 1e-9);
        assertEquals(0.0, calc.gpa(new double[]{40}), 1e-9);
    }

    @Test
    void gpa_mixedGradesAverage() {
        assertEquals(2.0, calc.gpa(new double[]{95, 85, 75, 65, 40}), 1e-9);
    }

    @Test
    void gpa_gradeBoundaries() {
        assertEquals(4.0, calc.gpa(new double[]{90}), 1e-9);
        assertEquals(3.0, calc.gpa(new double[]{89.99}), 1e-9);
        assertEquals(1.0, calc.gpa(new double[]{60}), 1e-9);
        assertEquals(0.0, calc.gpa(new double[]{59.99}), 1e-9);
    }

    // scholarship: 3.8/15 заагууд
    @Test
    void scholarship_fullTierBoundaries() {
        assertEquals(100, calc.scholarship(3.8, 15, false));
        assertEquals(70, calc.scholarship(3.79, 15, false));
        assertEquals(70, calc.scholarship(3.8, 14, false));
        assertEquals(100, calc.scholarship(4.0, 15, false));
    }

    // scholarship: 3.4/12 заагууд
    @Test
    void scholarship_partialTierBoundaries() {
        assertEquals(70, calc.scholarship(3.4, 12, false));
        assertEquals(50, calc.scholarship(3.39, 12, false));
        assertEquals(50, calc.scholarship(3.4, 11, false));
    }

    // scholarship: 3.0 заагууд
    @Test
    void scholarship_basicTierBoundaries() {
        assertEquals(50, calc.scholarship(3.0, 0, false));
        assertEquals(0, calc.scholarship(2.99, 20, false));
    }

    // scholarship: оролтын хязгаар (gpa 0 ба 4, credits 0)
    @Test
    void scholarship_inputLimitsAreValid() {
        assertEquals(0, calc.scholarship(0.0, 0, false));
        assertEquals(50, calc.scholarship(4.0, 0, false));
    }

    @Test
    void scholarship_justOutsideLimits_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-0.01, 10, false));
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.01, 10, false));
    }

    // scholarship: сахилгын зөрчил бусад бүх нөхцлийг давна
    @Test
    void scholarship_violationAlwaysZero() {
        assertEquals(0, calc.scholarship(3.0, 0, true));
        assertEquals(0, calc.scholarship(3.9, 16, true));
    }
}
