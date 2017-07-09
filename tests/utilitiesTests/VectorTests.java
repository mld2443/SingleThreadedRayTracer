package utilitiesTests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.Vector;

public class VectorTests {
	private static Vector a, b, normal;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		a = new Vector(1,2,3);
		b = new Vector(0,-1,-1);
		normal = new Vector(0,0,1);
	}

	@Test
	public void testVectorArithmatic() {
		assertEquals("Negation", b.invert(), new Vector(0,1,1));
		assertEquals("Addition", Vector.add(a, b), new Vector(1,1,2));
		assertEquals("Subtraction", Vector.sub(a, b), new Vector(1,3,4));
		assertEquals("Scaling", a.scale(2.0f), new Vector(2,4,6));
		assertEquals("Scaling", a.divide(2.0f), new Vector(0.5f,1.0f,1.5f));
		assertEquals("Inline Product", Vector.inlineMultiply(a, b), new Vector(0,-2,-3));
		assertEquals("Dot Product", Vector.dot(a, b), -5.0f, 8);
		assertEquals("Cross Product", Vector.cross(a, b), new Vector(1,-1,-1));
	}

	@Test
	public void testNormalization() {
		assertEquals("Normalization", b.normalize().magnitude(), 1.0f, 6);
	}
	
	@Test
	public void testEquality() {
		assertEquals("Equality", a.normalize(), new Vector(2,4,6).normalize());
	}
	
	@Test
	public void testReflection() {
		Vector reflection = Vector.reflect(b.normalize(), normal);
		Vector expected = new Vector(0,-1,1).normalize();
		
		System.out.println(reflection + ", " + expected);
		
		assertEquals("Reflection", reflection, expected);
	}
	
	@Test
	public void testRefraction() {
		Vector refraction = Vector.refract(b.normalize(), normal, 1.33f);
		Vector expected = new Vector(0,-1,-1).normalize();
		
		fail("Not Implemented");
	}

}
